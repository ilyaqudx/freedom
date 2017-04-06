package freedom.jdfs.storage.trunk;

public class FDFSTrunkPathInfo {
	//无符号的CHAR 对应 c中
	short store_path_index;   //store which path as Mxx
	short sub_path_high;      //high sub dir index, front part of HH/HH
	short sub_path_low;
}
