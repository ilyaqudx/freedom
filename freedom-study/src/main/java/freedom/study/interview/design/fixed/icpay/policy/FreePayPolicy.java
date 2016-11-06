package freedom.study.interview.design.fixed.icpay.policy;

import freedom.study.interview.design.fixed.icpay.po.ICCard;
import freedom.study.interview.design.fixed.icpay.po.Product;

public class FreePayPolicy implements PayPolicy {

	@Override
	public void pay(ICCard card, Product product)
	{
		System.out.println("个人帐户扣款策略 : " + product.getPrice());
	}

}
