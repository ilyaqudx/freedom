
package freedom.socket.server.serializable;

public interface Serializable {

	public byte[] encode(Object msg)throws Exception;
	
	public Object decode(byte[] msg)throws Exception;
}
