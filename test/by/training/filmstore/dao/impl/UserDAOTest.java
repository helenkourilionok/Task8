package by.training.filmstore.dao.impl;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.entity.Role;
import by.training.filmstore.entity.User;

public class UserDAOTest {
	
	private static UserDAOImpl userDAO;
	private static User user;
	
	@BeforeClass
	public static void initUserDAO()
	{
		userDAO = new UserDAOImpl();
		user = new User("email","password",Role.ROLE_USER,"Anna","Kedric",
				       "Andreevna","+375291235678",
				       new BigDecimal(130000),(byte)0);
	}
		
	@Test
	public void crudUserTest() throws FilmStoreDAOException
	{
		boolean expected = true;
		boolean actual1 = userDAO.create(user);
		user.setPhone("+375298235678");
		user.setBalance(new BigDecimal(120000));
		user.setLastName("Milka");
		boolean actual2 = userDAO.update(user);
		boolean actual3 = userDAO.delete(user.getEmail());
		assertEquals("Can't create user fields",expected, actual1);
		assertEquals("Error updating user",expected, actual2);
		assertEquals("Can't delete user by id",expected,actual3);
	}	
	
	@Test
	public void makeDiscountTest()
	{
		byte sizeOfDiscount = 12;
		int year = 2016;
		int month = 5;
		byte countOrders = 1;
		boolean expected = true;
		boolean actual = true;
		try {
			userDAO.makeDiscount(sizeOfDiscount,year,month,countOrders);
		} catch (FilmStoreDAOException e) {
			actual = false;
		}finally{
			assertEquals("Can't make discount",expected, actual);
		}
	}
	
	@Test
	public void takeAwayDiscount()
	{
		int year = 2016;
		int month = 5;
		boolean expected = true;
		boolean actual = true;
		try {
			userDAO.takeAwayDiscount(year, month);
		} catch (FilmStoreDAOException e) {
			actual = false;
		}
		finally{
			assertEquals("Can't take away discount",expected, actual);
		}
	}
	
	@Test
	public void findByIDTest() throws FilmStoreDAOException
	{
		String correctId = "sonya2016@yandex.ru";
		String incorrectId = "incorrectId";
		User userCorrect = userDAO.find(correctId);
		User userInCorrect = userDAO.find(incorrectId);
		assertNotNull( "Can't find existing user",userCorrect);
		assertNull("Wrong action.User with that ID doesn't exist.", userInCorrect);;
	}
	
	@Test 
	public void findAllUserTest() throws FilmStoreDAOException
	{
		List<User> listUsers = userDAO.findAll();
		assertNotNull("Operation failed. No user was found",listUsers);
		assertEquals("Operation failed. No user was found",false,listUsers.isEmpty());
	}
		
	@Test
	public void findUserByEmailPassword() throws FilmStoreDAOException
	{
		User user = userDAO.find("sonya2016@",  "sonyasonya");
		assertNull("Users with that email and password doesn't exist",user);
		user = userDAO.find("sonya2016@yandex.ru","incorrectpasw");
		assertNull("Users with that email and password doesn't exist",user);
		user = userDAO.find("sonya2016@", "incorrectpasw");
		assertNull("Users with that email and password doesn't exist",user);
		user = userDAO.find("sonya2016@yandex.ru","sonyasonya");
		assertNotNull("Users with that email and password doesn't exist",user);
	}
	
	@Test
	public void changePassword() throws FilmStoreDAOException
	{
		User updateUser = userDAO.find("email");
		updateUser.setPassword("changedPasword");
		boolean actual = userDAO.changePassword(updateUser);
		User userFind = userDAO.find(updateUser.getEmail(), updateUser.getPassword());
		boolean expected = true;
		assertEquals("Password wasn't changed",expected, actual);
		assertNotNull("Can't find user by email and changed password",userFind);
	}
}
