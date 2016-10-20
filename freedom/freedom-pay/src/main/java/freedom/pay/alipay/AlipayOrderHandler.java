package freedom.pay.alipay;

import freedom.common.kit.HttpKit;
import freedom.common.kit.StrKit;
import freedom.pay.Utils;

public class AlipayOrderHandler{

	public static final AlipayOrderHandler I = new AlipayOrderHandler();
	
	private AlipayOrderHandler()
	{
		
	}
	

	/**验证支付宝回调的参数列表</br>
	 * <B>支付宝回调时订单状态有两种:WAIT_BUYER_PAY / TRADE_SUCCESS</B></BR>
	 * <B>只有状态为TRADE_SUCCESS时才会进行参数验证,验证通过返回true,失败抛出异常</B>
	 * @param config
	 * @param response
	 * @return				成功返回true
	 * @throws Exception	验证失败时抛出异常
	 */
	public boolean validateNotifyResult(AlipayConfig config,AlipayPayResult response) throws Exception
	{
		if(!AlipayConsts.TRADE_SUCCESS.equalsIgnoreCase(response.getTrade_status()))
			return false;
		if(!verify(config,response))
			throw new Exception("签名验证失败");
		return true;
	}
	
	private boolean verify(AlipayConfig config,AlipayPayResult result)
	{
		return verifyResult(config,result.getNotify_id()) ? verifySign(config,result) : false;
	}

	private boolean verifyResult(AlipayConfig config,String notify_id)
	{
		if(StrKit.isNotBlank(notify_id))
		{
			String verifyUrl = String.format("%s&partner=%s&notify_id=%s",config.getVerifyUrl(),config.getPartner(),notify_id);
			String result    = HttpKit.get(verifyUrl);
			boolean ret = "TRUE\n".equalsIgnoreCase(result);
			return ret;
		}
		return true;
	}
	
	private boolean verifySign(AlipayConfig config,AlipayPayResult result)
	{
		String remoteSign  = result.getSign();
		if(StrKit.isNotBlank(remoteSign))
		{
			String signType    = result.getSign_type();
			String waitSignStr = Utils.getAlipaySignString(result);
			
			return "RSA".equalsIgnoreCase(signType) 
					? RSA.verify(waitSignStr, remoteSign, config.getPublicKey(), config.getCharset())
					: MD5.verify(waitSignStr, remoteSign, config.getPrivateKey(), config.getCharset());
		}
		return false;
	}

}
