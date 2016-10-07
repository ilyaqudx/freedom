package freedom.study.interview.design.strategy;

import java.util.List;

public class GuangDongMaJiangComputeFan implements ComputeFan {

	@Override
	public int compute(List<String> cards) 
	{
		return 21;
	}

}
