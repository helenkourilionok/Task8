package by.training.filmstore.entity;

import java.io.Serializable;

public class GoodOfOrder implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1452709168610741318L;
	private GoodOfOrderPK id;
	private byte countFilms;
	
	public GoodOfOrderPK getId() {
		return id;
	}
	public void setId(GoodOfOrderPK id) {
		this.id = id;
	}
	public byte getCountFilms() {
		return countFilms;
	}
	public void setCountFilms(byte countFilms) {
		this.countFilms = countFilms;
	}
	public GoodOfOrder() {
		super();
	}
	public GoodOfOrder(GoodOfOrderPK id, byte countFilms) {
		super();
		this.id = id;
		this.countFilms = countFilms;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + countFilms;
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
		GoodOfOrder other = (GoodOfOrder) obj;
		if (countFilms != other.countFilms)
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
		return "GoodOfOrder [id=" + id + ", countFilms=" + countFilms + "]";
	}
	
}
