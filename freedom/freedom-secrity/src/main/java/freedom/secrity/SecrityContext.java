package freedom.secrity;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SecrityContext {

	
	
	/**
	 * 获取默认证书受信管理器
	 * */
	private static X509TrustManager getDefaultTrustManager()
	{
		return new DefaultTrustManager();
	}
	
	
	/**
	 * 获取默认SSL SOCKET FACTORY
	 * */
	public static SSLSocketFactory getDefaultSSLSocketFactory() 
	{
		try 
		{
			TrustManager[] tm = { getDefaultTrustManager() };
			SSLContext sslContext = SSLContext.getInstance("TLS"); 
			sslContext.init(null, tm, new java.security.SecureRandom());
			return sslContext.getSocketFactory();
		} 
		catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * HTTPS 域名校验
	 */
	public static class TrustAnyHostnameVerifier implements HostnameVerifier
	{
		public boolean verify(String hostname, SSLSession session)
		{
			return true;
		}
	}
	
	
	/**
	 * 默认证书信任管理器
	 * */
	public static final class DefaultTrustManager implements X509TrustManager
	{

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException 
		{
			//no thing
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
		{
			//no thing
		}

		@Override
		public X509Certificate[] getAcceptedIssuers()
		{
			//trust all
			return null;
		}
		
	}
	
}
