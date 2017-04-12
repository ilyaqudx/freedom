package freedom.jdfs.storage;

import freedom.jdfs.protocol.ProtoCommon;

public class FDHTKeyInfo {

	public int namespace_len;
	public int obj_id_len;
	public int key_len;
	public byte[] szNameSpace = new byte[ProtoCommon.FDHT_MAX_NAMESPACE_LEN + 1];
	public byte[] szObjectId = new byte[ProtoCommon.FDHT_MAX_OBJECT_ID_LEN + 1];
	public byte[] szKey = new byte[ProtoCommon.FDHT_MAX_SUB_KEY_LEN + 1];
}
