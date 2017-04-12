package freedom.jdfs.protocol;
/**
	* receive package info
	*/
	public class TrackerHeader
	{
		public byte[] body;
		public byte errno;
		public byte cmd;
		
		public TrackerHeader(byte errno,byte[] body)
		{
			this.body = body;
			this.errno = errno;
			
		}
		public TrackerHeader(byte[] body,byte cmd,byte errno)
		{
			this.body = body;
			this.cmd  = cmd;   
			this.errno = errno;
			
		}
	}