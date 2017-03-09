package com.iquizoo.web.po.course;

import java.util.ArrayList;
import java.util.List;

/**
 * 具体的课
 * */
public class Lesson {

	private int    id;
	private String name;
	private int    timeTableId;
	private String description;
	
	/**基础*/
	private int 	enableBasic;
	private List<Integer> gamePoints   = new ArrayList<Integer>();
	
	/**学科*/
	private int enableCourse;
	private int courseGenerateRule;
	private int courseCount;
	private int courseLimitTime;
	//对应多个知识点-(KnowledgePointItem)
	private List<Integer> coursePoints = new ArrayList<Integer>();
	
	/**复习*/
	private int enableReview;
	//复习题生成规则(1-手动选择,2-程序自动匹配)
	private int reviewGenerateRule;
	//复习题数量(总数量)
	private int reviewCount;
	//复习题限制完成时间
	private int reviewLimitTime;
	//复习知识点 - ReviewItem
	private List<Integer> reviewPoints = new ArrayList<Integer>();
	
}
