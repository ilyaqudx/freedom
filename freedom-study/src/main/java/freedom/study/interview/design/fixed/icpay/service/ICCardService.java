package freedom.study.interview.design.fixed.icpay.service;

import freedom.study.interview.design.fixed.icpay.po.Employee;
import freedom.study.interview.design.fixed.icpay.po.Product;

public interface ICCardService {

	/**
	 * 付款
	 * */
	public void pay(Employee employee,Product product);
}
