
package freedom.hall.module.product.command;

import org.springframework.stereotype.Service;

import freedom.common.dao.DBFactory;
import freedom.hall.Cmd;
import freedom.hall.module.product.dao.ProductDAO;
import freedom.hall.module.product.entity.Product;
import freedom.hall.module.product.message.ProductListMessage;
import freedom.hall.module.product.message.ProductListMessage.Out;
import freedom.socket.command.AbstractCommand;

@Service(Cmd.Req.PRODUCT_LIST)
public class ProductListCommand extends AbstractCommand<ProductListMessage> {

	private ProductDAO productDAO = DBFactory.getDAO(ProductDAO.class);
	
	@Override
	public ProductListMessage execute(ProductListMessage msg) throws Exception 
	{
		int productType = msg.getIn().getProductType();
		if(productType == Product.TYPE_GOLD)
		{
			Out out = new Out();
			out.setProductList(productDAO.listByType(productType));
			msg.setOut(out).setResCmd(Cmd.Res.PRODUCT_LIST);
		}
		return msg;
	}

}
