package by.training.filmstore.entity;

import java.io.Serializable;
import java.util.List;

public class FilmDirector implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3807617042515714172L;
	private short id;
	private String fio;
	private List<Film> listFilms;
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
	public List<Film> getListFilms() {
		return listFilms;
	}
	public void setListFilms(List<Film> listFilms) {
		this.listFilms = listFilms;
	}
	
	public FilmDirector() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public FilmDirector(short id, String fio, List<Film> listFilms) {
		super();
		this.id = id;
		this.fio = fio;
		this.listFilms = listFilms;
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
		FilmDirector other = (FilmDirector) obj;
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
		return "FilmDirector [id=" + id + ", fio=" + fio + ", listFilms=" + listFilms + "]";
	}
	
}
