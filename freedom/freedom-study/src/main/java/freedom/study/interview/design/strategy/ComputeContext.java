package freedom.study.interview.design.strategy;

import java.util.List;

/**
 * 麻将番数计算上下文(?为什么需要上下文处理,不直接采用操作类,不明白)
 * */
public class ComputeContext {

	public static final int compute(ComputeFan computer,List<String> cards)
	{
		return computer.compute(cards);
	}
}
