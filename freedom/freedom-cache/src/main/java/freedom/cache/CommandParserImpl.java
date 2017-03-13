package freedom.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParserImpl implements CommandParser {

	@Override
	public Command parse(byte[] msg)
	{
		List<byte[]> commandBytes = parse0(msg);
		if(isSet(commandBytes.get(0))){
			if(commandBytes.size() != 3)
				throw new IllegalArgumentException("cmd is invalid : " + new String(commandBytes.get(0)));
			return CommandFactory.buildSet(new String(commandBytes.get(1)), new String(commandBytes.get(2)));
		}
		else if(isGet(commandBytes.get(0))){
			if(commandBytes.size() != 2)
				throw new IllegalArgumentException("cmd is invalid : " + new String(commandBytes.get(0)));
			return CommandFactory.buildGet(new String(commandBytes.get(1)));
		}
		else if(isDel(commandBytes.get(0))){
			if(commandBytes.size() != 2)
				throw new IllegalArgumentException("cmd is invalid : " + new String(commandBytes.get(0)));
			return CommandFactory.buildDel(new String(commandBytes.get(1)));
		}else if(isInner(commandBytes.get(0))){
			
		}
		throw new IllegalArgumentException("cmd is invalid : " + new String(commandBytes.get(0)));
	}

	private List<byte[]> parse0(byte[] msg)
	{
		int valPos = 0 , offset = 0;
		int arrLen = msg.length;
		List<byte[]> commandBytes = new ArrayList<byte[]>();
		while(offset < arrLen)
		{
			//去除空格
			while(offset < arrLen && msg[offset] == Const.SPACE){
				offset++;
			}
			valPos = offset;
			//获取命令后的key
			while(offset < arrLen && msg[offset] != Const.SPACE){
				offset++;
			}
			
			if(valPos < offset)
				commandBytes.add(Arrays.copyOfRange(msg, valPos,offset));
		}
		return commandBytes;
	}

	public static final boolean isSet(byte[] cmd)
	{
		return is(cmd, Const.SET);
	}
	public static final boolean isGet(byte[] cmd)
	{
		return is(cmd, Const.GET);
	}
	public static final boolean isDel(byte[] cmd)
	{
		return is(cmd, Const.DEL);
	}
	public static final boolean isInner(byte[] cmd)
	{
		return is(cmd,Const.INNER);
	}
	public static final boolean is(byte[] cmd,byte[] target)
	{
		if(cmd.length != target.length)
			return false;
		for (int i = 0; i < target.length; i++) {
			if(cmd[i] != target[i])
				return false;
		}
		return true;
	}
}
