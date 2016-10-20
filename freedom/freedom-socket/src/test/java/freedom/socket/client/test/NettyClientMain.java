
package freedom.socket.client.test;

import freedom.socket.client.NettySocketClient;
import freedom.socket.server.message.Request;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Random;

public class NettyClientMain {

	public static void main(String[] args) {
		
		for (int i = 0; i < 1; i++) {
			Channel channel = new NettySocketClient(new InetSocketAddress("127.0.0.1",9966)).connect();
			Request request = new Request();
			request.setCmd((short) 3001);
			request.setMid(999999);
			request.setOneWay((byte) 0);
			request.setBody("i want to enter to server world!");
			request.setUid(956555 + (new Random().nextInt(123) * 4) );
			channel.writeAndFlush(request);
		}
	}
}
