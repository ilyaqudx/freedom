package freedom.cache;

public interface CommandParser {

	public Command parse(byte[] msg);
}
