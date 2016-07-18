package by.training.filmstore.entity;

import java.io.Serializable;

public class GoodOfOrderPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4368600987569793551L;
	private int idOrder;
	private short idFilm;
	public int getIdOrder() {
		return idOrder;
	}
	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}
	public short getIdFilm() {
		return idFilm;
	}
	public void setIdFilm(short idFilm) {
		this.idFilm = idFilm;
	}
	public GoodOfOrderPK() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idFilm;
		result = prime * result + idOrder;
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
		GoodOfOrderPK other = (GoodOfOrderPK) obj;
		if (idFilm != other.idFilm)
			return false;
		if (idOrder != other.idOrder)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "GoodOfOrderPK [idOrder=" + idOrder + ", idFilm=" + idFilm + "]";
	}
	
}
