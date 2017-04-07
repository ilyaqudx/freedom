package freedom.jdfs.storage;

public class FDFSStorageReservedSpace {

	public byte flag;
	public RSUnion rs;

	public class RSUnion{
		public int mb;
		public double ratio;
	}
}

