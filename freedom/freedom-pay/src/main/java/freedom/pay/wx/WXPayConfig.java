package freedom.pay.wx;


public class WXPayConfig {

	private String appId,mchId,nonceStr,tradeType,apiKey,prePayUrl = WXPayConsts.PRE_PAY_URL,callbackUrl;
	
	public WXPayConfig(String appId, String mchId, String nonceStr, String tradeType, String apiKey,String callbackUrl) 
	{
		this.appId = appId;
		this.mchId = mchId;
		this.nonceStr = nonceStr;
		this.tradeType = tradeType;
		this.apiKey = apiKey;
		this.callbackUrl = callbackUrl;
	}

	public WXPayConfig(String appId, String mchId, String nonceStr, String tradeType, String apiKey, String callbackUrl,String prePayUrl) 
	{
		this.appId = appId;
		this.mchId = mchId;
		this.nonceStr = nonceStr;
		this.tradeType = tradeType;
		this.apiKey = apiKey;
		this.prePayUrl = prePayUrl;
		this.callbackUrl = callbackUrl;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getPrePayUrl() {
		return prePayUrl;
	}

	public void setPrePayUrl(String prePayUrl) {
		this.prePayUrl = prePayUrl;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	@Override
	public String toString() {
		return "WXPayConfig [appId=" + appId + ", mchId=" + mchId + ", nonceStr=" + nonceStr + ", tradeType="
				+ tradeType + ", apiKey=" + apiKey + ", prePayUrl=" + prePayUrl + ", callbackUrl=" + callbackUrl + "]";
	}
}
