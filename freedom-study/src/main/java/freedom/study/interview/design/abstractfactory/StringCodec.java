package freedom.study.interview.design.abstractfactory;

public class StringCodec implements Codec {

	@Override
	public Decoder getDecoder()
	{
		return new StringDecoder();
	}

	@Override
	public Encoder getEncoder()
	{
		return new StringEncoder();
	}

}
