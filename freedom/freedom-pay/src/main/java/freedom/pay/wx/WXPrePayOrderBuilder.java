package freedom.pay.wx;

public class WXPrePayOrderBuilder {

	private String appid, attach, body, mch_id, nonce_str, notify_url,
			out_trade_no, spbill_create_ip, total_fee, trade_type, sign,openid;

	public String getOpenid() 
	{
		return openid;
	}

	public WXPrePayOrderBuilder setOpenid(String openid)
	{
		this.openid = openid;
		return this;
	}

	public String getAppid() 
	{
		return appid;
	}

	public WXPrePayOrderBuilder setAppid(String appid) 
	{
		this.appid = appid;
		return this;
	}

	public String getAttach() 
	{
		return attach;
	}

	public WXPrePayOrderBuilder setAttach(String attach)
	{
		this.attach = attach;
		return this;
	}

	public String getBody()
	{
		return body;
	}

	public WXPrePayOrderBuilder setBody(String body)
	{
		this.body = body;
		return this;
	}

	public String getMch_id()
	{
		return mch_id;
	}

	public WXPrePayOrderBuilder setMch_id(String mch_id)
	{
		this.mch_id = mch_id;
		return this;
	}

	public String getNonce_str()
	{
		return nonce_str;
	}

	public WXPrePayOrderBuilder setNonce_str(String nonce_str)
	{
		this.nonce_str = nonce_str;
		return this;
	}

	public String getNotify_url() 
	{
		return notify_url;
	}

	public WXPrePayOrderBuilder setNotify_url(String notify_url) 
	{
		this.notify_url = notify_url;
		return this;
	}

	public String getOut_trade_no()
	{
		return out_trade_no;
	}

	public WXPrePayOrderBuilder setOut_trade_no(String out_trade_no)
	{
		this.out_trade_no = out_trade_no;
		return this;
	}

	public String getSpbill_create_ip()
	{
		return spbill_create_ip;
	}

	public WXPrePayOrderBuilder setSpbill_create_ip(String spbill_create_ip)
	{
		this.spbill_create_ip = spbill_create_ip;
		return this;
	}

	public String getTotal_fee()
	{
		return total_fee;
	}

	public WXPrePayOrderBuilder setTotal_fee(String total_fee)
	{
		this.total_fee = total_fee;
		return this;
	}

	public String getTrade_type()
	{
		return trade_type;
	}

	public WXPrePayOrderBuilder setTrade_type(String trade_type)
	{
		this.trade_type = trade_type;
		return this;
	}

	public String getSign()
	{
		return sign;
	}

	public WXPrePayOrderBuilder setSign(String sign)
	{
		this.sign = sign;
		return this;
	}
	
	public WXPrePayOrder build()
	{
		WXPrePayOrder order = new WXPrePayOrder();
		order.setAppid(appid);
		order.setAttach(attach);
		order.setBody(body);
		order.setMch_id(mch_id);
		order.setNonce_str(nonce_str);
		order.setNotify_url(notify_url);
		order.setOpenid(openid);
		order.setOut_trade_no(out_trade_no);
		order.setSign(sign);
		order.setSpbill_create_ip(spbill_create_ip);
		order.setTotal_fee(total_fee);
		order.setTrade_type(trade_type);
		return order;
	}

}
