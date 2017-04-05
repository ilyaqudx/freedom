package freedom.jdfs.storage;

public class MD5_CTX {

	int[] state = new int[4];
	int[] count = new int[2];
	byte[] buffer = new byte[64];
}
