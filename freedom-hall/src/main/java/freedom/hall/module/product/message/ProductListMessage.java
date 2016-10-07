package freedom.hall.module.product.message;

import java.util.ArrayList;
import java.util.List;

import freedom.hall.module.product.entity.Product;
import freedom.hall.module.product.message.ProductListMessage.In;
import freedom.hall.module.product.message.ProductListMessage.Out;
import freedom.socket.command.CommandMessage;

public class ProductListMessage extends CommandMessage<In,Out> {

	public static class In{
		private int productType;

		public int getProductType() {
			return productType;
		}

		public void setProductType(int productType) {
			this.productType = productType;
		}
	}
	
	public static class Out{
		private List<Product> productList = new ArrayList<Product>();

		public List<Product> getProductList() {
			return productList;
		}

		public void setProductList(List<Product> productList) {
			this.productList = productList;
		}
		
	}
}
