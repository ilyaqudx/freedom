package freedom.study.interview.design.strategy;

import java.util.List;

/**
 * 番型计算接口
 * */
public interface ComputeFan {

	/**
	 * 计算
	 * */
	public int compute(List<String> cards);
}
