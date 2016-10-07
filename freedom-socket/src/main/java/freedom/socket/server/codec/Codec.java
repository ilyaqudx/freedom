
package freedom.socket.server.codec;


public interface Codec {

	public Decoder getDecoder();
	
	public Encoder getEncoder();
}
