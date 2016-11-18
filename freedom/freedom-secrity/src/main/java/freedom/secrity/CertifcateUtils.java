package freedom.secrity;  
  
import java.io.ByteArrayInputStream;  
import java.io.FileInputStream;  
import java.io.InputStream;  
import java.security.KeyStore;  
import java.security.PrivateKey;  
import java.security.PublicKey;  
import java.security.cert.CertificateFactory;  
/** 
 * Created by kingj on 2014/8/13. 
 */  
public class CertifcateUtils {  
    public static byte[] readCertifacates() throws Exception{  
        CertificateFactory factory=CertificateFactory.getInstance("X.509");  
        InputStream in=new FileInputStream("f:/secrity/java-ssl.crt");  
        java.security.cert.Certificate cate=factory.generateCertificate(in);  
        return cate.getEncoded();  
    }  
  
    public static byte[] readPrivateKey() throws  Exception{  
        KeyStore store=KeyStore.getInstance("JKS");  
        InputStream in=new FileInputStream("f:/secrity/java-ssl.keystore");  
        store.load(in,"java-ssl".toCharArray());  
        PrivateKey pk=(PrivateKey)store.getKey("java-ssl","java-ssl".toCharArray());  
        return pk.getEncoded();  
    }  
  
    public static PrivateKey readPrivateKeys() throws  Exception{  
        KeyStore store=KeyStore.getInstance("JKS");  
        InputStream in=new FileInputStream("f:/secrity/java-ssl.keystore");  
        store.load(in,"java-ssl".toCharArray());  
        PrivateKey pk=(PrivateKey)store.getKey("java-ssl","java-ssl".toCharArray());  
        return pk;  
    }  
  
    public static PublicKey readPublicKeys() throws  Exception{  
        CertificateFactory factory=CertificateFactory.getInstance("X.509");  
        InputStream in=new FileInputStream("f:/secrity/java-ssl.crt");  
        java.security.cert.Certificate cate=factory.generateCertificate(in);  
        return cate.getPublicKey();  
    }  
  
    public static  java.security.cert.Certificate createCertiface(byte b[]) throws Exception{  
        CertificateFactory factory=CertificateFactory.getInstance("X.509");  
        InputStream in=new ByteArrayInputStream(b);  
        java.security.cert.Certificate cate=factory.generateCertificate(in);  
        return cate;  
    }  
  
    public static String byte2hex(byte[] b) {  
        String hs = "";  
        String stmp = "";  
        for (int n = 0; n < b.length; n++) {  
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));  
            if (stmp.length() == 1) {  
                hs = hs + "0" + stmp;  
            } else {  
                hs = hs + stmp;  
            }  
        }  
        return hs.toUpperCase();  
    }  
}  