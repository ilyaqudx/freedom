package freedom.nio.codec;


public class JSONCodec implements Codec {

	private Encoder encoder;
	private Decoder decoder;
	public JSONCodec()
	{
		this.encoder = new JSONEncoder();
		this.decoder = new JSONDecoder();
	}
	
	@Override
	public Encoder getEncoder()
	{
		return this.encoder;
	}

	@Override
	public Decoder getDecoder()
	{
		return this.decoder;
	}

}
