package freedom.nio.codec;

import freedom.nio.IoSession;
import freedom.nio.codec.ProtocolCodecFilter.ProtocolEncoderOutput;

public interface Encoder {

	/**编码接口:将编码后的数据放入ByteBuffer,再调用output的write方法写入消息队列里面.<br>
	 * 注意:写入消息队列前,必须调用ByteBuffer的flip方法
	 * @param session	会话上下文
	 * @param msg		消息对象
	 * @param output	将消息对象编码后,输出对象
	 */
	public void encode(IoSession session,Object msg,ProtocolEncoderOutput output);
}
