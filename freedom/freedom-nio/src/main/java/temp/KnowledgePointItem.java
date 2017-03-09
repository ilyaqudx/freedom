package com.iquizoo.web.po.course;

import java.util.ArrayList;
import java.util.List;

/**
 * 课对应的知识点选项
 * */
public class KnowledgePointItem {

	private int id;
	//具体属于哪一课
	private int lessonId;
	//知识点
	private int pointId;
	//该知识点选择的题目数量
	private int count;
	//开始等级(基础能力适用)
	private int startLevel;
	//完成时间(学科能力适用)-限制在多少时间内完成
	private int limitTime;
	//知识点对应的题列表(可以考虑使用JSON)
	private List<Integer> subjects = new ArrayList<Integer>();
	public int getLessonId() {
		return lessonId;
	}
	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}
	public List<Integer> getSubjects() {
		return subjects;
	}
	public void setSubjects(List<Integer> subjects) {
		this.subjects = subjects;
	}
	public int getLimitTime() {
		return limitTime;
	}
	public void setLimitTime(int limitTime) {
		this.limitTime = limitTime;
	}
	public int getStartLevel() {
		return startLevel;
	}
	public void setStartLevel(int startLevel) {
		this.startLevel = startLevel;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPointId() {
		return pointId;
	}
	public void setPointId(int pointId) {
		this.pointId = pointId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "KnowledgePointItem [id=" + id + ", pointId=" + pointId
				+ ", count=" + count + "]";
	}
}
