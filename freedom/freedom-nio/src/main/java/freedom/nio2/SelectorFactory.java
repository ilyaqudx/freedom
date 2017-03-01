package freedom.nio2;

import java.io.IOException;
import java.nio.channels.Selector;

public class SelectorFactory {

	public static final Selector open()
	{
		try {
			return Selector.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
