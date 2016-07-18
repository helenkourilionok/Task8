package by.training.filmstore.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class Order implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 375938686621408295L;
	private int id;
	private String userEmail;
	private BigDecimal commonPrice;
	private Status status;
	private KindOfDelivery kindOfDelivery;
	private KindOfPayment kindOfPayment;
	private Date dateOfOrder;
	private Date dateOfDelivery;
	private String address;
	private List<GoodOfOrder> listGoodOfOrder;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public BigDecimal getCommonPrice() {
		return commonPrice;
	}
	public void setCommonPrice(BigDecimal commonPrice) {
		this.commonPrice = commonPrice;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public KindOfDelivery getKindOfDelivery() {
		return kindOfDelivery;
	}
	public void setKindOfDelivery(KindOfDelivery kindOfDelivery) {
		this.kindOfDelivery = kindOfDelivery;
	}
	public KindOfPayment getKindOfPayment() {
		return kindOfPayment;
	}
	public void setKindOfPayment(KindOfPayment kindOfPayment) {
		this.kindOfPayment = kindOfPayment;
	}
	public Date getDateOfOrder() {
		return dateOfOrder;
	}
	public void setDateOfOrder(Date dateOfOrder) {
		this.dateOfOrder = dateOfOrder;
	}
	public Date getDateOfDelivery() {
		return dateOfDelivery;
	}
	public void setDateOfDelivery(Date dateOfDelivery) {
		this.dateOfDelivery = dateOfDelivery;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public GoodOfOrder getListGoodOfOrder(int index) {
		return listGoodOfOrder.get(index);
	}
	public void setListGoodOfOrder(int index,GoodOfOrder goodOfOrder) {
		listGoodOfOrder.add(index, goodOfOrder);
	}
	public List<GoodOfOrder> getListGoodOfOrder() {
		return listGoodOfOrder;
	}
	public void setListGoodOfOrder(List<GoodOfOrder> listGoodOfOrder) {
		this.listGoodOfOrder = listGoodOfOrder;
	}
	
	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Order(int id, String userEmail, BigDecimal commonPrice, Status status, KindOfDelivery kindOfDelivery,
			KindOfPayment kindOfPayment, Date dateOfOrder, Date dateOfDelivery, String address) {
		super();
		this.id = id;
		this.userEmail = userEmail;
		this.commonPrice = commonPrice;
		this.status = status;
		this.kindOfDelivery = kindOfDelivery;
		this.kindOfPayment = kindOfPayment;
		this.dateOfOrder = dateOfOrder;
		this.dateOfDelivery = dateOfDelivery;
		this.address = address;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((commonPrice == null) ? 0 : commonPrice.hashCode());
		result = prime * result + ((dateOfDelivery == null) ? 0 : dateOfDelivery.hashCode());
		result = prime * result + ((dateOfOrder == null) ? 0 : dateOfOrder.hashCode());
		result = prime * result + id;
		result = prime * result + ((kindOfDelivery == null) ? 0 : kindOfDelivery.hashCode());
		result = prime * result + ((kindOfPayment == null) ? 0 : kindOfPayment.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((userEmail == null) ? 0 : userEmail.hashCode());
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
		Order other = (Order) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (commonPrice == null) {
			if (other.commonPrice != null)
				return false;
		} else if (!commonPrice.equals(other.commonPrice))
			return false;
		if (dateOfDelivery == null) {
			if (other.dateOfDelivery != null)
				return false;
		} else if (!dateOfDelivery.equals(other.dateOfDelivery))
			return false;
		if (dateOfOrder == null) {
			if (other.dateOfOrder != null)
				return false;
		} else if (!dateOfOrder.equals(other.dateOfOrder))
			return false;
		if (id != other.id)
			return false;
		if (kindOfDelivery != other.kindOfDelivery)
			return false;
		if (kindOfPayment != other.kindOfPayment)
			return false;
		if (status != other.status)
			return false;
		if (userEmail == null) {
			if (other.userEmail != null)
				return false;
		} else if (!userEmail.equals(other.userEmail))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Order [id=" + id + ", userEmail=" + userEmail + ", commonPrice=" + commonPrice + ", status=" + status
				+ ", kindOfDelivery=" + kindOfDelivery + ", kindOfPayment=" + kindOfPayment + ", dateOfOrder="
				+ dateOfOrder + ", dateOfDelivery=" + dateOfDelivery + ", address=" + address + ", listGoodOfOrder="
				+ listGoodOfOrder + "]";
	}

	
}
