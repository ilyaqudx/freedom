package freedom.study.interview.design.fixed.icpay.policy;

import freedom.study.interview.design.fixed.icpay.po.ICCard;
import freedom.study.interview.design.fixed.icpay.po.Product;

public class PayContext {

	private PayPolicy policy;
	
	public PayContext(PayPolicy policy)
	{
		this.policy = policy;
	}
	
	public void operator(ICCard card,Product product)
	{
		this.policy.pay(card, product);
	}
}
