package freedom.study.interview.design.fixed.icpay.service;

import freedom.study.interview.design.fixed.icpay.po.Employee;
import freedom.study.interview.design.fixed.icpay.po.Product;
import freedom.study.interview.design.fixed.icpay.policy.FixedPayPolicy;
import freedom.study.interview.design.fixed.icpay.policy.FreePayPolicy;
import freedom.study.interview.design.fixed.icpay.policy.PayContext;

public class ICCardServiceImpl implements ICCardService {

	@Override
	public void pay(Employee employee, Product product) 
	{
		if(product.getPrice() > 100)
		{
			new PayContext(new FreePayPolicy()).operator(employee.getCard(), product);
		}
		else
		{
			new PayContext(new FixedPayPolicy()).operator(employee.getCard(), product);
		}
	}

}
