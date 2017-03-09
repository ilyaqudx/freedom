package com.iquizoo.web.po.course;

import java.util.ArrayList;
import java.util.List;

public class ReviewItem {

	//复习题生成规则(1-手动排课,2-程序自动生成)
	private int  generateRule;
	//复习题
	private List<Integer> points = new ArrayList<Integer>();
}
