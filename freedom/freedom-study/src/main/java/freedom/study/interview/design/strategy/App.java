package freedom.study.interview.design.strategy;

public class App {

	public static void main(String[] args) {
		
		
		int guangDongFan = ComputeContext.compute(new GuangDongMaJiangComputeFan(), null);
		
		System.out.println(guangDongFan);
		
		System.out.println(new SiChuanMajiangComputeFan().compute(null));
		
		System.out.println(CommonCompute.compute("湖南", null));
	}
}
