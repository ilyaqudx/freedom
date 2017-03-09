package com.iquizoo.web.po.course;

/**
 * 课程常量
 * */
public class CourseConst {

	/**
	 * 年级
	 * */
	public static enum Grade {

		ONE(1, "一年级"), TWO(2, "二年级"), THREE(3, "三年级"), FOUR(4, "四年级"), FIVE(5,
				"五年级"), SIX(6, "六年级"), SERVEN(7, "七年级"), EIGHT(8, "八年级"), NINE(
				9, "九年级");

		public int code;
		public String name;

		private Grade(int code, String name) {
			this.name = name;
		}
	}
	
	/**
	 * 学期
	 * */
	public static enum Semester{
		
		ONE(1, "上学期"), TWO(2, "下学期");

		public int code;
		public String name;

		private Semester(int code, String name) {
			this.code = code;
			this.name = name;
		}
	}
}
