package freedom.cache;

import java.util.Arrays;

public class CommandParserImpl implements CommandParser {

	@Override
	public Command parse(byte[] msg)
	{
		int offset = 0 , arrLen = msg.length;
		int cmdPos = 0,keyPos = 0,valPos = 0;
		//去除头部的空格
		while(offset < arrLen && msg[offset] == Const.SPACE){
			offset++;
		}
		cmdPos = offset;
		//获取命令
		while(offset < arrLen && msg[offset] != Const.SPACE){
			offset++;
		}
		byte[] cmd = Arrays.copyOfRange(msg, cmdPos, offset);
		//去除命令后的空格
		while(offset < arrLen && msg[offset] == Const.SPACE){
			offset++;
		}
		keyPos = offset;
		//获取命令后的key
		while(offset < arrLen && msg[offset] != Const.SPACE){
			offset++;
		}
		byte[] key = Arrays.copyOfRange(msg, keyPos, offset);
		//判断具体的命令:get key没有第三个参数.set key value 则有第三个参数
		if(isSet(cmd)){
			//继续获取value
			//去除空格
			while(offset < arrLen && msg[offset] == Const.SPACE){
				offset++;
			}
			valPos = offset;
			//获取命令后的key
			while(offset < arrLen && msg[offset] != Const.SPACE){
				offset++;
			}
			byte[] val = Arrays.copyOfRange(msg, valPos, offset);
			
			System.out.println(String.format("【%s %s %s】", new String(cmd),new String(key),new String(val)));
			
			return CommandFactory.buildSet(new String(key),new String(val));
			
		}else if(isGet(cmd)){
			System.out.println(String.format("【%s %s】", new String(cmd),new String(key)));
			return CommandFactory.buildGet(new String(key));
		}else if(isDel(cmd)){
			System.out.println(String.format("【%s %s】", new String(cmd),new String(key)));
			return CommandFactory.buildDel(new String(key));
		}
		throw new IllegalArgumentException("cmd is invalid : " + new String(cmd));
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
	public static final boolean is(byte[] cmd,byte[] target)
	{
		if(cmd.length != 3)
			return false;
		for (int i = 0; i < 3; i++) {
			if(cmd[i] != target[i])
				return false;
		}
		return true;
	}
}
