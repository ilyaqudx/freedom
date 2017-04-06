package freedom.jdfs.protocol;
/**
	* receive package info
	*/
	public class PacketHeader
	{
		public byte[] body;
		public byte errno;
		public byte cmd;
		
		public PacketHeader(byte errno,byte[] body)
		{
			this.body = body;
			this.errno = errno;
			
		}
		public PacketHeader(byte[] body,byte cmd,byte errno)
		{
			this.body = body;
			this.cmd  = cmd;   
			this.errno = errno;
			
		}
	}