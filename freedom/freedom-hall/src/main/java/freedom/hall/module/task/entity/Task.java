package freedom.hall.module.task.entity;

public class Task {

	private long id;
	private String title;
	private String icon;
	private String description;
	private int attachment;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getAttachment() {
		return attachment;
	}
	public void setAttachment(int attachment) {
		this.attachment = attachment;
	}
	@Override
	public String toString() {
		return "Task [id=" + id + ", title=" + title + ", icon=" + icon
				+ ", description=" + description + ", attachment=" + attachment
				+ "]";
	}
}
