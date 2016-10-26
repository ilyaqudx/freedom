package freedom.project.po;

public class Model {

	protected long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Model [id=" + id + "]";
	}
}
