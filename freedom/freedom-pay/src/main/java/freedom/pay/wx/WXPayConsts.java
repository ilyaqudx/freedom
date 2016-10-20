package freedom.pay.wx;

public interface WXPayConsts {

	/**
	 * 成功/失败
	 * */
	public static final String SUCCESS = "SUCCESS" , FAIL = "FAIL";
	/**
	 * 统一下单地址
	 * */
	public static final String PRE_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**
	 * 交易类型(JS,APP)
	 * */
	public static final String TRADE_TYPE_JS = "JSAPI" , TRADE_TYPE_APP = "APP";
	/**
	 * 安全API(每一个APPID都有一个,现在公众平台和开放平台设置的相同)
	 * */
	public static final String API_KEY = "20F7080A798C4A18D71652184C2AB0AD";
}
