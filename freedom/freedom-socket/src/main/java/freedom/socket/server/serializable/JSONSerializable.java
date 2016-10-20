
package freedom.socket.server.serializable;

import com.alibaba.fastjson.JSON;

import freedom.socket.server.message.Request;




public class JSONSerializable implements Serializable {

	public byte[] encode(Object msg) throws Exception 
	{
		return JSON.toJSONString(msg).getBytes();
	}

	public Object decode(byte[] msg) throws Exception 
	{
		return JSON.parseObject(new String(msg), Request.class);
	}

}
