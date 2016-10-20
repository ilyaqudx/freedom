
package freedom.socket.server.codec;

public interface Encoder {

	public byte[] encode(Object msg)throws Exception;
}
