package com.iquizoo.web.po.course;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 课程表
 * */
public class TimeTable { 

	private int id;
	private String name;
	
	//教材版本
	private int textBookVersionId;
	//训练学科
	private int TrainingSubjectId;
	//年级
	private int grade;
	//学期
	private int semester;
	//具体的排课表
	private List<Integer> lessons = new ArrayList<Integer>();
	private Date createTime;
}
