package freedom.jdfs.storage.trunk;

public class FDFSTrunkPathInfo {
	//无符号的CHAR 对应 c中
	public short store_path_index;   //store which path as Mxx
	public short sub_path_high;      //high sub dir index, front part of HH/HH
	public short sub_path_low;
}
