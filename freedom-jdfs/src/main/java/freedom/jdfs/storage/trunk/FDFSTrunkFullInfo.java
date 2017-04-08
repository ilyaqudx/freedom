package freedom.jdfs.storage.trunk;

public class FDFSTrunkFullInfo {

	byte status;  //normal or hold
	public FDFSTrunkPathInfo path = new FDFSTrunkPathInfo();
	public FDFSTrunkFileInfo file = new FDFSTrunkFileInfo();
}
