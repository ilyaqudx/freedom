package freedom.pay.alipay;

public class AlipayConfig {

    private String partner;
    private String privateKey;
    private String publicKey;
    private String verifyUrl = AlipayConsts.HTTPS_VERIFY_URL;
    private String charset = AlipayConsts.CHARSET;
    
	public AlipayConfig(String partner, String privateKey,String publicKey) 
	{
		this.partner = partner;
		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getVerifyUrl() {
		return verifyUrl;
	}
	public void setVerifyUrl(String verifyUrl) {
		this.verifyUrl = verifyUrl;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
}
