package com.iquizoo.web.po.course;

/**
 * 训练学科(语文、数学)
 * */
public class TrainingSubject {

	private int id;
	private String name;
	private String description;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "TrainingSubject [id=" + id + ", name=" + name
				+ ", description=" + description + "]";
	}
}
