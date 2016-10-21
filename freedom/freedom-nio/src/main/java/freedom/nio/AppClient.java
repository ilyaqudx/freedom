package freedom.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class AppClient {

	public static void main(String[] args) throws IOException, Exception
	{
		Selector sel = Selector.open();
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		channel.connect(new InetSocketAddress("127.0.0.1", 3333));
		channel.register(sel, SelectionKey.OP_CONNECT);
		
		while(true)
		{
			
			int r = sel.select(1000);
			
			if(r > 0)
			{
				Iterator<SelectionKey> it = sel.selectedKeys().iterator();
				while(it.hasNext()){
					SelectionKey key = it.next();
					if(key.isConnectable())
					{
						SocketChannel socket = (SocketChannel) key.channel();
						socket.finishConnect();
						if(socket.isConnected())
						{
							while(true)
							{
								String msg = "篝火升腾，温暖的火光照耀在唐芊儿娇躯上，但却反而令得她感觉到有些凉意，这些凉意，来自牧尘对她描述的那个她曾经向往的灵路世界。"
										+ "这一次的北灵之原修行，这里便将会是我们的营地，你们明日将会从这里出发，前往北灵之原修行，与其中的灵兽做正面的交锋，在此之前，我得提醒你们，"
										+ "低级灵兽约莫灵动境的实力，中级则是灵轮境，高级灵兽更是堪比神魄境的实力，在这北灵之原深处，便是有着高级灵兽存在，所以，你们绝对不能够深入北灵之原！"
    +"莫师神色微微缓和，他望着那些眼睛一下子亮起来的少年少女们，也是一笑，道:所以，为了获得好成绩，跟你的同伴好好合作吧，你们现在的实力，必须要采取一些合作，才能够在这北灵之原外围混得风生水起。"
    +"说到此处，他声音已是有些严厉，北灵之原深处的高级灵兽，就连他对付起来都是极为的麻烦，这些刚刚从学院出来的学员，更是犹如毫无反抗的羔羊。"
    +"我炼化的是高级灵兽金雷狼的精魄，这金雷狼在万兽录上也是有着排名，不过并不靠前，仅仅是地榜三百八十二位，席师炼化的是高级灵兽石龟狮的兽魄，在万兽录地榜之上，名列第三百九十。”莫师见到席师的动作，也是一笑，然后一挥手，身后金色巨狼便是消散而去。"
    +"至于万兽录天榜的那些存在...”莫师声音顿了顿，旋即笑道：“恐怕就算是大千世界中的那些至尊级别的超级大人物，都不敢轻易的对它们出手。"
										+ "灵兽分高中低三级，北灵之原外围，大多都是低级灵兽，但即便如此，你们也必须联手方才能够对付。"
										+ "看来灵路没我想的那么美好，还好我没去，不然恐怕连骨头都不会剩下。”唐芊儿有点心悸的道，她实在无法想象，一个连信任都不存在的地方，究竟该过得如何的提心吊胆。";
								byte[] data = msg.getBytes();

								ByteBuffer buffer = ByteBuffer.allocate(data.length+4);
								buffer.putInt(data.length);
								buffer.put(data);
								buffer.flip();
								socket.write(buffer);
							}
						}
					}
					else if(key.isReadable())
					{
						SocketChannel socket = (SocketChannel) key.channel();
						
						
						
						
						
						
						
						
						
						
						
						
					}
					it.remove();
				}
			}
			Thread.sleep(1000);
		}
	}
}
