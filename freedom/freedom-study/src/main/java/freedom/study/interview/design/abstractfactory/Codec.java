package freedom.study.interview.design.abstractfactory;

public interface Codec {

	public Decoder getDecoder();
	
	public Encoder getEncoder();
}
