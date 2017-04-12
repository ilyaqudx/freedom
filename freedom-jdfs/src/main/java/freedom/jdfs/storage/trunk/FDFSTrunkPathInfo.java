package freedom.jdfs.storage.trunk;

public class FDFSTrunkPathInfo {
	//无符号的CHAR 对应 c中
	public short storePathIndex;   //store which path as Mxx
	public short subPathHigh;      //high sub dir index, front part of HH/HH
	public short subPathLow;
}
