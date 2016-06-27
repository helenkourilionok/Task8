package by.training.filmstore.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -662089236680154467L;
	private String email;
	private String password;
	private Role role;
	private String lastName;
	private String firstName;
	private String patronimic;
	private String phone;
	private BigDecimal balance;
	private byte discount;
	private List<Order> orders;
	private List<Comment> comments;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getPatronimic() {
		return patronimic;
	}
	public void setPatronimic(String patronimic) {
		this.patronimic = patronimic;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public byte getDiscount() {
		return discount;
	}
	public void setDiscount(byte discount) {
		this.discount = discount;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(String email, String password, Role role, String lastName, String firstName, String patronimic,
			String phone, BigDecimal balance, byte discount) {
		super();
		this.email = email;
		this.password = password;
		this.role = role;
		this.lastName = lastName;
		this.firstName = firstName;
		this.patronimic = patronimic;
		this.phone = phone;
		this.balance = balance;
		this.discount = discount;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + discount;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((patronimic == null) ? 0 : patronimic.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		User other = (User) obj;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (discount != other.discount)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (patronimic == null) {
			if (other.patronimic != null)
				return false;
		} else if (!patronimic.equals(other.patronimic))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (role != other.role)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + ", role=" + role + ", lastName=" + lastName
				+ ", firstName=" + firstName + ", patronimic=" + patronimic + ", phone=" + phone + ", balance="
				+ balance + ", discount=" + discount + ", orders=" + orders + ", comments=" + comments + "]";
	}
	
}
