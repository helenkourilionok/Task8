package by.training.filmstore.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Comment implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3951625113074125948L;
	private CommentPK id;
	private String content;
	private Timestamp date;
	public CommentPK getId() {
		return id;
	}
	public void setId(CommentPK id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Comment(CommentPK id, String content, Timestamp date) {
		super();
		this.id = id;
		this.content = content;
		this.date = date;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Comment [id=" + id + ", content=" + content + ", date=" + date + "]";
	}	
}
