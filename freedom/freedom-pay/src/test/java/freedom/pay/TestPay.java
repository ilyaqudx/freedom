package freedom.pay;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import freedom.common.kit.HttpKit;
import freedom.pay.alipay.AlipayConfig;
import freedom.pay.alipay.AlipayOrderHandler;
import freedom.pay.alipay.AlipayPayResult;
import freedom.pay.wx.WXOrderHandler;
import freedom.pay.wx.WXPayConsts;
import freedom.pay.wx.WXPrePayOrderBuilder;
import freedom.pay.wx.WXPrePayOrderRes;

public class TestPay {

	public static void main(String[] args) throws Exception 
	{
		testAlipay();
		//testCallback();
		//testWx();
	}
	
	public static final void testCallback()
	{
		String str = HttpKit.get("http://localhost:7000/AZYWebServer/order/alipay/callback?buyer_id=2088302945517484&trade_no=2016102021001004480209574411&body=1个月会员限时折扣&use_coupon=N&notify_time=2016-10-20 14:17:06&subject=1个月会员限时折扣&sign_type=RSA&is_total_fee_adjust=Y&notify_type=trade_status_sync&out_trade_no=16102014163810000&trade_status=WAIT_BUYER_PAY&discount=0.00&sign=CegC6DHUOadJFqc5W5s71fvNmowEkOk3E0iqdD1vcWIXeiK1cAOHlnIOryJSp+F+nGQy5GPjvjWLuVW0hA1VOIxAcoKRo4mcNpdzfDfvqtpjt5J7MODMoazySiljl1Bnz/Ck8xRGYVaofqKA0mEXf50dsFFz8UgmIMExqeZQogM=&gmt_create=2016-10-20 14:17:06&buyer_email=81154883@qq.com&price=0.01&total_fee=0.01&seller_id=2088121006702322&quantity=1&seller_email=cdb@iquizoo.com&notify_id=98db542b52f5d3d5b5f6e67abf768d0jpe&payment_type=1");
		System.out.println(str);
	}
	
	private static void testAlipay()throws Exception
	{
		AlipayPayResult response = createAlipayGoldPayResult();//
		/*AlipayOrderHandler.I.validateNotifyResult(new AlipayConfig("2088121006702322","ge8o3rwev3o8zdvkyxoqf1yn583z6ber",
				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB"),response);
		
		System.out.println("支付宝回调参数验证成功");*/
		Map queryParas = JSONObject.parseObject(JSON.toJSONString(response), Map.class);
		String r = HttpKit.get("http://localhost:7000/AZYWebServer/order/alipay/callback", queryParas);
		System.out.println(r);
		
	}

	private static AlipayPayResult createAlipayVipPayResult() {
		AlipayPayResult response = new AlipayPayResult();
		response.setBody("1个月会员限时折扣");
		response.setBuyer_email("81154883@qq.com");
		response.setBuyer_id("2088302945517484");
		response.setDiscount("0.00");
		response.setGmt_payment("2016-11-01 13:57:13");
		response.setGmt_create("2016-11-01 13:57:11");
		response.setIs_total_fee_adjust("N");
		response.setNotify_id("3a689793edce07d7fd5ca5f6f8a59f1jpe");
		response.setNotify_time("2016-11-01 13:57:13");
		response.setNotify_type("trade_status_sync");
		response.setOut_trade_no("16110113540810001");
		response.setPayment_type("1");
		response.setPrice("0.01");
		response.setQuantity("1");
		response.setSeller_email("cdb@iquizoo.com");
		response.setSeller_id("2088121006702322");
		response.setSign("cvv2cxDMyxxOQSxYacjKBjl1EB0fRLVD61ma57nGqbApS1Nel4x5QnbsOptE1yRk47tjcDK1nsHM3EJv+5pka3LHvfY3Rq17jOGisnCj+chDCn4pQ1tvPOrUfc23XH3SCSA1g5FiSyATHBL4Remuzjyku5sDpavMaCUDn9ey/jQ=");
		response.setSign_type("RSA");
		response.setSubject("1个月会员限时折扣");
		response.setTotal_fee("0.01");
		response.setTrade_no("2016110121001004480227582005");
		response.setTrade_status("TRADE_SUCCESS");
		response.setUse_coupon("N");
		return response;
	}
	private static AlipayPayResult createAlipayGoldPayResult() {
		AlipayPayResult response = new AlipayPayResult();
		response.setBody("10个栗子");
		response.setBuyer_email("81154883@qq.com");
		response.setBuyer_id("2088302945517484");
		response.setDiscount("0.00");
		response.setGmt_payment("2016-11-01 15:26:46");
		response.setGmt_create("2016-11-01 15:26:44");
		response.setIs_total_fee_adjust("N");
		response.setNotify_id("99148ebf6326f462fe8e486e9ee3823jpe");
		response.setNotify_time("2016-11-01 15:26:46");
		response.setNotify_type("trade_status_sync");
		response.setOut_trade_no("16110115265610003");
		response.setPayment_type("1");
		response.setPrice("0.01");
		response.setQuantity("1");
		response.setSeller_email("cdb@iquizoo.com");
		response.setSeller_id("2088121006702322");
		response.setSign("V78m2y5rkHmzVzWW/B+c4fzGazfnj48ynGl87F3s7YocYzZzUtRUU/tMsyyWu818BWRxNmwzLCOJZHBRIYhehzEirHGeRHcqOMrH/S3XcpCZPTIdIv0OBqiSnPdP4ier5tpV3LPNPPnKUrY0CCTBo3e9QxblFizqA/KCw0jd/1s=");
		response.setSign_type("RSA");
		response.setSubject("10个栗子");
		response.setTotal_fee("0.01");
		response.setTrade_no("2016110121001004480227693072");
		response.setTrade_status("TRADE_SUCCESS");
		response.setUse_coupon("N");
		return response;
	}

	/**
	 *  开放平台APP ID
		public static final String APP_ID = "wx0265dcd516aab527";
		开放平台商户ID
		public static final String MCH_ID = "1400664502";
		公众平台 APP ID
		public static final String PUBLIC_APP_ID = "wxf3f70cd90c7414c8";
		公众平台 商户 ID
		public static final String PUBLIC_MCH_ID = "1395242802";
		预支付订单随机串
		public static final String NONCE_STR = "www.iquizoo.com";
	 * */
	private static void testWx() throws Exception {
		WXPrePayOrderBuilder builder =  new WXPrePayOrderBuilder()
				.setAppid("wxf3f70cd90c7414c8")
				.setAttach("test")
				.setBody("gggg")
				.setMch_id("1395242802")
				.setNonce_str("www.iquizoo.com")
				.setNotify_url("http://test.api.web.iquizoo.com/order/wxpay/callback")
				.setOut_trade_no("lfksdjfkljdl")
				.setSpbill_create_ip("127.0.0.1")
				.setOpenid("oFuiIv2T0rrB9XAlNSqTuhSYgmn0")
				.setTotal_fee(String.valueOf((int)(1)))
				.setTrade_type(WXPayConsts.TRADE_TYPE_JS);
		
		
		WXPrePayOrderRes res = WXOrderHandler.I.generateOrder("20F7080A798C4A18D71652184C2AB0AD",builder);
		
		System.out.println(res);
	}
}
