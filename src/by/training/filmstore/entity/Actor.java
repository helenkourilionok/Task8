package by.training.filmstore.entity;

import java.io.Serializable;
import java.util.List;

public class Actor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6102227750875275056L;
	private short id;
	private String fio;
	private List<Film> films;
	public short getId() {
		return id;
	}
	public void setId(short id) {
		this.id = id;
	}
	public String getFio() {
		return fio;
	}
	public void setFio(String fio) {
		this.fio = fio;
	}
	public List<Film> getFilms() {
		return films;
	}
	public void setFilms(List<Film> films) {
		this.films = films;
	}
	
	public Actor() {
		super();
	}
	
	public Actor(short id, String fio) {
		super();
		this.id = id;
		this.fio = fio;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fio == null) ? 0 : fio.hashCode());
		result = prime * result + id;
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
		Actor other = (Actor) obj;
		if (fio == null) {
			if (other.fio != null)
				return false;
		} else if (!fio.equals(other.fio))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Actor [id=" + id + ", fio=" + fio + ", films=" + films + "]";
	}
	
}
