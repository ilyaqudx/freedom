package freedom.pay.wx;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;

import freedom.common.kit.HttpKit;
import freedom.common.kit.StrKit;
import freedom.pay.Utils;
import freedom.pay.sign.SignUtils;

public class WXOrderHandler
{
	public static final WXOrderHandler I = new WXOrderHandler();
	
	private WXOrderHandler()
	{
		
	}

	/**生成预支付订单,网页支付和APP支付两种类型(JS支付时需要传openid,app时一定不传openid)
	 * @param apiKey
	 * @param builder
	 * @return
	 * @throws Exception
	 */
	public WXPrePayOrderRes generateOrder(String apiKey,WXPrePayOrderBuilder builder) throws Exception
	{
		return generateOrder(WXPayConsts.PRE_PAY_URL, apiKey, builder);
	}
	
	/**生成预支付订单,网页支付和APP支付两种类型(JS支付时需要传openid,app时一定不传openid)
	 * @param prePayUrl
	 * @param apiKey
	 * @param builder
	 * @return
	 * @throws Exception
	 */
	public WXPrePayOrderRes generateOrder(String prePayUrl,String apiKey,WXPrePayOrderBuilder builder) throws Exception
	{
		WXPrePayOrder prePayOrder = builder.build();
		String waitSignStr = addApiKey(Utils.getWXSignString(prePayOrder), apiKey);
		String sign        = SignUtils.md5(waitSignStr);
		prePayOrder.setSign(sign);
		String xml  = Utils.toXml(prePayOrder, "<%s>%s</%s>");
		String response = HttpKit.post(prePayUrl, xml);
		if(StrKit.isBlank(response))
			throw new Exception("生成预付订单失败,请检查必传参数是否已传");
		return parseResult(apiKey,response, WXPrePayOrderRes.class);
	}

	public WXPayResult parseAndValidateNotifyResult(String apiKey,String response) throws Exception 
	{
		return parseResult(apiKey,response, WXPayResult.class);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T parseResult(String apiKey,String result,Class<T> clazz) throws Exception
	{
		 Document document = DocumentHelper.parseText(result);
		 Element root = document.getRootElement();
		 List<Element> elementList = root.elements();
		 Set<String> datas = new TreeSet<String>();
		 JSONObject json = new JSONObject();
		 for (Element e : elementList)
		 {
			 json.put(e.getName(), e.getText());
			 if(!"sign".equals(e.getName()))
			 datas.add(e.getName() + "=" + e.getText());
		 }
		 String waitSignStr =  addApiKey(Joiner.on('&').join(datas.iterator()).toString(),apiKey);
		 String localSign = SignUtils.md5(waitSignStr).toUpperCase();
		 if(WXPayConsts.SUCCESS.equals(json.getString("return_code")))
		 {
			 if(localSign.equals(json.get("sign")))
			 {
				 System.out.println("验证返回参数签名成功");
				 return JSON.parseObject(json.toJSONString(),clazz);
			 }
			 throw new Exception("签名验证失败");
		 }
		 throw new Exception(json.getString("return_msg"));
	}
	
	private final String addApiKey(String params,String apiKey)
	{
		return params + String.format("&key=%s", apiKey);
	}
	
}
