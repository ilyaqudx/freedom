package freedom.study.interview.design.abstractfactory;

public class ProtobufCodec implements Codec {

	@Override
	public Decoder getDecoder()
	{
		return new ProtobufDecoder();
	}

	@Override
	public Encoder getEncoder() 
	{
		return new ProtobufEncoder();
	}

}
