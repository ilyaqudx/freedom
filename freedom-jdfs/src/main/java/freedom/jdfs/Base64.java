package freedom.jdfs;

import java.nio.CharBuffer;

public class Base64 {

	public static void main(String[] args)
	{
		String str = "-4";
		String jdkEncode = new sun.misc.BASE64Encoder().encode(str.getBytes());
		String encodeStr = encode(str);
		
		System.out.println(jdkEncode);
		System.out.println(encodeStr);
		System.out.println(jdkEncode.equals(encodeStr));
	}
	
	public static final String BASE64_CODE= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";  
	public static final String encode(String str)
	{
		if(str == null)
			return null;
		byte[] bytes = str.getBytes();
		int count = bytes.length / 3;
		int mod   = bytes.length % 3;
		int offset = 0;
		int len = count * 4  + (mod > 0 ? 4 : 0);
		final int newLinePadCharLen = 2;
		final int newLine = len / 76 * newLinePadCharLen;//每76个字符添加\r\n
		int newLineCount = 0;
		CharBuffer charBuffer = CharBuffer.allocate(len + newLine);
		while(offset < count)
		{
			int first  = (bytes[offset * 3] >> 2) & 0x3F;
			int secend = (bytes[offset * 3] << 4 & 0x30) + ((bytes[offset * 3 + 1] >> 4) & 0x0F);
			int third  = (bytes[offset * 3 +1] << 2 & 0x3C) + ((bytes[offset * 3 + 2] >> 6) & 0x03);
			int four   = (bytes[offset * 3 + 2]) & 0x3F;
			offset++;
			charBuffer.put(BASE64_CODE.charAt(first));
			charBuffer.put(BASE64_CODE.charAt(secend));
			charBuffer.put(BASE64_CODE.charAt(third));
			charBuffer.put(BASE64_CODE.charAt(four));
			newLineCount = checkPadNewLine(newLinePadCharLen, newLineCount, charBuffer);
		}
		
		if(mod > 0){
			int pad = 6 - (mod * 8) % 6;//按6位补齐
			if(pad == 4){
				//只有一个字节
				int first  = (bytes[offset * 3] >> 2) & 0x3F;
				int secend = (bytes[offset * 3] << 4 & 0x30);
				charBuffer.put(BASE64_CODE.charAt(first));
				charBuffer.put(BASE64_CODE.charAt(secend));
			}else if(pad == 2){
				//有2个字节
				int first  = (bytes[offset * 3] >> 2) & 0x3F;
				int secend = (bytes[offset * 3] << 4 & 0x30) + ((bytes[offset * 3 + 1] >> 4) & 0x0F); 
				int third  = (bytes[offset * 3 +1] << 2 & 0x3C);
				charBuffer.put(BASE64_CODE.charAt(first));
				charBuffer.put(BASE64_CODE.charAt(secend));
				charBuffer.put(BASE64_CODE.charAt(third));
			}
			for (int i = 0; i < 3 - mod; i++) {
				charBuffer.put('=');
			}
			newLineCount = checkPadNewLine(newLinePadCharLen, newLineCount, charBuffer);
		}
		charBuffer.flip();
		return charBuffer.toString();
	}
	private static int checkPadNewLine(final int newLinePadCharLen, int newLineCount, CharBuffer charBuffer)
	{
		if(charBuffer.position() > 0 && ((charBuffer.position() - newLineCount * newLinePadCharLen) % 76 == 0))
		{
			newLineCount++;
			charBuffer.put('\r');
			charBuffer.put('\n');
		}
		return newLineCount;
	}
}
