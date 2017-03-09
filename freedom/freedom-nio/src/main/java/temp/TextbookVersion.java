package com.iquizoo.web.po.course;

/**
 * 课本版本(人教版)
 * */
public class TextbookVersion {

	private int id;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "TextbookVersion [id=" + id + ", name=" + name + "]";
	}
}
