package freedom.jdfs.protocol;
/**
	* receive package info
	*/
	public class RecvPackageInfo
	{
		public byte[] body;
		public byte errno;
		public byte cmd;
		
		public RecvPackageInfo(byte errno,byte[] body)
		{
			this.body = body;
			this.errno = errno;
			
		}
		public RecvPackageInfo(byte[] body,byte cmd,byte errno)
		{
			this.body = body;
			this.cmd  = cmd;   
			this.errno = errno;
			
		}
	}