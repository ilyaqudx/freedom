package freedom.study.interview.design.fixed.icpay.policy;

import freedom.study.interview.design.fixed.icpay.po.ICCard;
import freedom.study.interview.design.fixed.icpay.po.Product;

public interface PayPolicy {

	/**
	 * 付款
	 * */
	public void pay(ICCard card,Product product);
}
