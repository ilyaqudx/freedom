package freedom.jdfs.storage;

import java.net.InetSocketAddress;

import freedom.nio2.NioAcceptor;
import freedom.nio2.TextLineProcotolCodec;

public class StorageServer {

	public static void main(String[] args) {
		
		new NioAcceptor(new InetSocketAddress(8888), new StorageHandler() , new TextLineProcotolCodec());
		
	}
}
