package freedom.study.interview.design.strategy;

import java.util.List;


/**
 * 普通计算类
 * */
public class CommonCompute {

	
	/**
	 * 1-通过分支类型来判断采用哪种算法 
	 * 2-如果传入类型错误,返回值无用(并且返回到调用者,调用者还需要处理,不太好)
	 * 3-如果增加一种地方麻将,需要修改compute方法(违反封闭原则)
	 * */
	public static final int compute(String majiangType,List<String> cards)
	{
		if("四川".equals(majiangType))
			return 10;
		else if("广东".equals(majiangType))
			return 21;
		else if("湖南".equals(majiangType))
			return 5;
		return -1;
	}
}
