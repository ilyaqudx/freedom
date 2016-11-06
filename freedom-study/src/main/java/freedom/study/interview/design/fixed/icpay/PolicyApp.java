package freedom.study.interview.design.fixed.icpay;

import freedom.study.interview.design.fixed.icpay.po.Employee;
import freedom.study.interview.design.fixed.icpay.po.ICCard;
import freedom.study.interview.design.fixed.icpay.po.Product;
import freedom.study.interview.design.fixed.icpay.service.ICCardService;
import freedom.study.interview.design.fixed.icpay.service.ICCardServiceImpl;

public class PolicyApp {

	public static void main(String[] args)
	{
		Employee zhangsan = new Employee(1, "张三", new ICCard("abcdef",1,500,200));
		Product  yifu     = new Product("衣服",99);
		
		ICCardService service = new ICCardServiceImpl();
		
		service.pay(zhangsan, yifu);
	}
}
