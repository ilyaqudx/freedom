import freedom.gate.module.login.message.LoginMessage.In;
import freedom.socket.client.NettySocketClient;
import freedom.socket.server.message.Request;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.UUID;


public class GateClientMain {

	public static void main(String[] args) {
		Channel channel = new NettySocketClient(new InetSocketAddress("127.0.0.1",9966)).connect();
		Request request = new Request();
		request.setCmd((short) 3001);
		request.setMid(999999);
		request.setOneWay((byte) 0);
		In body = new In();
		body.setPassword("123456");
		body.setEmail("123@qq.com");
		body.setImei(UUID.randomUUID().toString());
		request.setBody(body);
		request.setUid(956555 + (new Random().nextInt(123) * 4) );
		channel.writeAndFlush(request);
	}
}
