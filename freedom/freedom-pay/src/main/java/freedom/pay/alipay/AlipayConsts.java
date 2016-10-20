package freedom.pay.alipay;

public interface AlipayConsts {

	/**
     * 支付宝消息验证地址
     */
    public static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify";
    /**
     * 编码格式
     * */
    public static String CHARSET = "UTF-8";
    /**
     * 交易成功
     * */
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
}
