/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zch.safelottery.pull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zch.safelottery.R;
import com.zch.safelottery.activity.MainTabActivity;
import com.zch.safelottery.setttings.Settings;
import com.zch.safelottery.util.LogUtil;

/** 
 * Activity for displaying the notification details view.
 *
 */
public class NotificationDetailsActivity extends Activity {

    private TextView notice_title,notice_content,notice_time;
    private View notice_close,buy_lottery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.notice);

        Intent intent = getIntent();
        String notificationId = intent.getStringExtra(PullSettings.NOTIFICATION_ID);
        String notificationApiKey = intent.getStringExtra(PullSettings.NOTIFICATION_API_KEY);
        String notificationTitle = intent.getStringExtra(PullSettings.NOTIFICATION_TITLE);
        String notificationMessage = intent.getStringExtra(PullSettings.NOTIFICATION_MESSAGE);
        String notificationUri = intent.getStringExtra(PullSettings.NOTIFICATION_URI);

        LogUtil.DefalutLog("notificationId=" + notificationId);
        LogUtil.DefalutLog("notificationApiKey=" + notificationApiKey);
        LogUtil.DefalutLog("notificationTitle=" + notificationTitle);
        LogUtil.DefalutLog("notificationMessage=" + notificationMessage);
        LogUtil.DefalutLog("notificationUri=" + notificationUri);
        
        createView(notificationTitle, notificationMessage, notificationUri);
    }

    private void createView(String title, String message, final String uri) {

    	notice_title = (TextView)findViewById(R.id.notice_title);
    	notice_content = (TextView)findViewById(R.id.notice_content);
    	notice_time = (TextView)findViewById(R.id.notice_time);
    	notice_close = (View)findViewById(R.id.notice_close);
    	buy_lottery = (View)findViewById(R.id.notice_to_buy_lottery);
    	
    	notice_title.setText(title);
    	notice_content.setText(message);
    	
    	notice_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NotificationDetailsActivity.this.finish();
            }
        });
    	
    	buy_lottery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
				Intent intent = new Intent(NotificationDetailsActivity.this, MainTabActivity.class);
				intent.putExtra(Settings.TABHOST, 0);
				startActivity(intent);
				NotificationDetailsActivity.this.finish();
            }
        });
    }
}
