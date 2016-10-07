package freedom.hall.module.product.dao;

import java.util.List;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;

import freedom.hall.module.product.entity.Product;

@DB
public interface ProductDAO {

	@SQL("select * from Product where type = :1")
	public List<Product> listByType(int type);
	@SQL("select * from Product")
	public List<Product> list();
}
