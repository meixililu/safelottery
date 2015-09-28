package com.zch.safelottery.bean;

public class UserInfoBean implements SafelotteryType{

	// 登陆接口返回数据与用户信息查看接口公用数据
	private String userCode; // 用户编号
	private String userName; // 用户名
	private String nickname = "";//昵称
	private String mobile;// 手机号
	private String email;// 邮箱
	private String realName;// 真实姓名
	private String cardType;// 证件类型
	private String cardCode;// 身份证号码
	private String loginCount;// 登录次数
	// 用户信息接口额外数据
	private String province;// 银行卡开户省
	private String city;// 银行卡开户城市
	private String bankName;// 银行名称
	private String subBank;// 支行名称
	private String bankCode;// 银行卡号

	// 登陆接口额外数据
	private String rechargeAmount = "0.00";// 充值金余额
	private String bonusAmount = "0.00";   // 奖金余额
	private String presentAmount = "0.00"; // 赠金余额
	private String freezeAmount = "0.00";  // 冻结金余额
	private String drawAmount = "0.00";    // 可提现金额
	private String useAmount = "0.00";     // 可用金额

	//联合登录中用到的字段
	private String passWord;//密码
	
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getUserCode() {
		return userCode;
	}
	
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCardType() {
		return cardType;
	}
	
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getLoginCount() {
		return loginCount;
	}

	public String getUseAmount() {
		return useAmount;
	}

	public void setUseAmount(String useAmount) {
		this.useAmount = useAmount;
	}

	public void setLoginCount(String loginCount) {
		this.loginCount = loginCount;
	}

	public String getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(String rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public String getBonusAmount() {
		return bonusAmount;
	}

	public void setBonusAmount(String bonusAmount) {
		this.bonusAmount = bonusAmount;
	}

	public String getPresentAmount() {
		return presentAmount;
	}

	public void setPresentAmount(String presentAmount) {
		this.presentAmount = presentAmount;
	}

	public String getFreezeAmount() {
		return freezeAmount;
	}

	public void setFreezeAmount(String freezeAmount) {
		this.freezeAmount = freezeAmount;
	}

	public String getDrawAmount() {
		return drawAmount;
	}

	public void setDrawAmount(String drawAmount) {
		this.drawAmount = drawAmount;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getSubBank() {
		return subBank;
	}

	public void setSubBank(String subBank) {
		this.subBank = subBank;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@Override
	public String toString() {
		return "UserInfoBean [userCode=" + userCode + ", userName=" + userName +", nickname=" + nickname + ", mobile=" + mobile + ", email=" + email + ", realName=" + realName + ", cardCode=" + cardCode + ", loginCount="
				+ loginCount + ", rechargeAmount=" + rechargeAmount + ", bonusAmount=" + bonusAmount + ", presentAmount=" + presentAmount + ", freezeAmount=" + freezeAmount + ", drawAmount="
				+ drawAmount + ", province=" + province + ", city=" + city + ", bankName=" + bankName + ", subBank=" + subBank + ", bankCode=" + bankCode + "]";
	}

}
