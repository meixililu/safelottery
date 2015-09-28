package com.zch.safelottery.lazyloadimage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ImageLoader {
    
	private Context context;
	private int image_size;
	private FileCache fileCache;
	
    static MemoryCache memoryCache = new MemoryCache();
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private int stub_id = 0;
    private boolean isRoundPic;
    private int which = 0;
    private Drawable smallPic;
    private Handler updateUIHandler;
    
    public ImageLoader(Context context,boolean roundPic,int size){
    	this.context = context;
    	this.isRoundPic = roundPic;
    	this.image_size = size;
        //Make the background thead low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
    }
    //show before load the image
    
    public void DisplayBigImage(String url, Activity activity, ImageView imageView,Drawable smallPic,
    		Handler updateUIHandler){
    	this.updateUIHandler = updateUIHandler;
    	this.smallPic = smallPic;
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else{
        	if(updateUIHandler != null){
            	Message msg = new Message();
                msg.what = 1;
                updateUIHandler.sendMessage(msg);
            }
            queuePhoto(url, activity, imageView);
            imageView.setImageDrawable(smallPic);
        }    
        which = 1;
    }
    
    public void DisplayImage(String url, Context activity, ImageView imageView){
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else{
            queuePhoto(url, activity, imageView);
            imageView.setImageResource(stub_id);
        }   
    }
    
    public void DisplayImageHasDefault(String url, Context activity, ImageView imageView, int pic){
    	stub_id = pic;
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else{
            queuePhoto(url, activity, imageView);
            imageView.setImageResource(stub_id);
        }   
    }
        
    private void queuePhoto(String url, Context activity, ImageView imageView){
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them. 
        photosQueue.Clean(imageView);
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        synchronized(photosQueue.photosToLoad){
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }
        //start thread if it's not started yet
        if(photoLoaderThread.getState()==Thread.State.NEW)
            photoLoaderThread.start();
    }
    
    
    //Task for the queue
    private class PhotoToLoad{
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url = u; 
            imageView = i;
        }
    }
    
    PhotosQueue photosQueue = new PhotosQueue();
    
    public void stopThread(){
        photoLoaderThread.interrupt();
    }
    
    //stores list of photos to download
    class PhotosQueue{
        private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();
        //removes all instances of this ImageView
        public void Clean(ImageView image){
        	try{
        		Iterator<PhotoToLoad> it = photosToLoad.iterator();
            	while(it.hasNext()){
            		if(it.next().imageView == image){
            			it.remove();
            		}
            	}
        	}catch(Exception e){
         	}
        }
    }
    
    class PhotosLoader extends Thread {
        @Override
		public void run() {
            try {
                while(true)
                {
                    //thread waits until there are any images to load in the queue
                    if(photosQueue.photosToLoad.size()==0)
                        synchronized(photosQueue.photosToLoad){
                            photosQueue.photosToLoad.wait();
                        }
                    if(photosQueue.photosToLoad.size()!=0){
                        PhotoToLoad photoToLoad;
                        synchronized(photosQueue.photosToLoad){
                            photoToLoad = photosQueue.photosToLoad.pop();
                        }
                        Bitmap bmp = getBitmap(photoToLoad.url);
                        if(isRoundPic){
                        	if(bmp != null){
                        		memoryCache.put(photoToLoad.url,getRoundedCornerBitmap(bmp));
                        	}
                        }else{
                        	memoryCache.put(photoToLoad.url, bmp);
                        }
                        String tag = imageViews.get(photoToLoad.imageView);
                        if(tag!=null && tag.equals(photoToLoad.url)){
                            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad.imageView);
                            Activity a = (Activity)photoToLoad.imageView.getContext();
                            a.runOnUiThread(bd);
                        }
                    }
                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
    }
    
    /**
	 * @param url 下载的地址
	 * @return 图片
	 */
	public Bitmap getBitmap(String url){
		fileCache=new FileCache(context);
        try{
        	File f = fileCache.getFile(url);
	        //from SD cache
	        Bitmap b = decodeFile(f);
	        if(b != null)
	            return b;
	        //from web
	        try {
	            Bitmap bitmap = null;
	            URL imageUrl = new URL(url);
	            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
	            conn.setConnectTimeout(20 * 1000);
	            conn.setReadTimeout(20 * 1000);
	            InputStream is = conn.getInputStream();
	            OutputStream os = new FileOutputStream(f);
	            Utils.CopyStream(is, os);
	            os.close();
	            bitmap = decodeFile(f);
	            return bitmap;
	        } catch (MalformedURLException ex){
	           return null;
	        } catch (Exception e) {
	        	return null;
			}
        }catch (Exception e) {
        	return null;
		}finally{
        	fileCache.clear();
        	fileCache = null;
        }
    }

	//decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            
            //Find the correct scale value. It should be the power of 2.
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
			 while(true){
				 if(width_tmp/2 < image_size || height_tmp/2 < image_size)
					 break;
				 width_tmp/=2;
				 height_tmp/=2;
				 scale*=2;
			 }
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        	return null;
        }
    }
    
    PhotosLoader photoLoaderThread = new PhotosLoader();
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        ImageView imageView;
        public BitmapDisplayer(Bitmap b, ImageView i){
        	bitmap=b;
        	imageView=i;
        }
        public void run(){
            if(bitmap!=null)
            	if(isRoundPic){
            		if(bitmap != null){
            			imageView.setImageBitmap(getRoundedCornerBitmap(bitmap));
            		}
            	}else{
            		imageView.setImageBitmap(bitmap);
            	}
            else{
            	if(which == 0){
            		imageView.setImageResource(stub_id);
            	}else{
            		imageView.setImageDrawable(smallPic);
            	}
            }
            if(which == 1 && updateUIHandler != null){
            	Message msg = new Message();
                msg.what=0;
                updateUIHandler.sendMessage(msg);
            }
        }
    }

    public void clearCache() {
        memoryCache.clear();
    }
    
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {    
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Config.ARGB_8888);    
        Canvas canvas = new Canvas(output);    
         
        final int color = 0xff424242;    
        final Paint paint = new Paint();    
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());    
        final RectF rectF = new RectF(rect);    
        final float roundPx = 5;    
         
        paint.setAntiAlias(true);    
        canvas.drawARGB(0, 0, 0, 0);    
        paint.setColor(color);    
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);    
         
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));    
        canvas.drawBitmap(bitmap, rect, rect, paint);    
         
        return output;    
      }   

}
