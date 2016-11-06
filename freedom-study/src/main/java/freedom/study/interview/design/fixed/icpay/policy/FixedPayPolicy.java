package freedom.study.interview.design.fixed.icpay.policy;

import freedom.study.interview.design.fixed.icpay.po.ICCard;
import freedom.study.interview.design.fixed.icpay.po.Product;

public class FixedPayPolicy implements PayPolicy {

	@Override
	public void pay(ICCard card, Product product) 
	{
		System.out.println("混合付款策略 : " + product.getPrice());
	}

}
