
package freedom.socket.server.codec;

public interface Decoder {

	public Object decode(byte[] msg)throws Exception;
}
