package by.training.filmstore.dao.impl;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.entity.KindOfDelivery;
import by.training.filmstore.entity.KindOfPayment;
import by.training.filmstore.entity.Order;
import by.training.filmstore.entity.Status;

public class OrderDAOTest {
	private static OrderDAOImpl orderDAOImpl;
	private static Order order;
	
	@BeforeClass
	public static void initOrderDAO(){
		order = new Order();
		order.setAddress("address");
		order.setCommonPrice(new BigDecimal(150000));
		order.setDateOfDelivery(Date.valueOf(LocalDate.now()));
		order.setDateOfOrder(Date.valueOf(LocalDate.of(2017, Month.JULY, 9)));
		order.setKindOfDelivery(KindOfDelivery.MAILING);
		order.setKindOfPayment(KindOfPayment.PAYMENT_IN_CASH);
		order.setStatus(Status.PAID);
		order.setUserEmail("smile269@mail.ru");
		orderDAOImpl = new OrderDAOImpl();
	}
	
	
	@Test
	public void crudOrderDAOTest() throws FilmStoreDAOException{
		boolean actual = orderDAOImpl.create(order);
		order.setKindOfDelivery(KindOfDelivery.COURIER);
		boolean actual2 = orderDAOImpl.update(order);
		boolean actual3 = orderDAOImpl.delete(order.getId());
		boolean expected = true;
		assertEquals("Error creating order",expected, actual);
		assertEquals("Error updating order",expected, actual2);
		assertEquals("Operation failed.Can't delete order",expected, actual3);
	}
	
	@Test
	public void findOrderByIdTest() throws FilmStoreDAOException{
		Order order = orderDAOImpl.find(2);
		Order norder = orderDAOImpl.find(90);
		assertNotNull("Can't find order",order);
		assertNull("Order with that ID doesn't exist",norder);
	}
	
	@Test
	public void findAllOrder() throws FilmStoreDAOException
	{
		List<Order> listOrder = orderDAOImpl.findAll(); 
		assertNotNull("Operation failed. No order was found",listOrder);
		assertEquals("Operation failed. No order was found",false,listOrder.isEmpty());
	}
	
	@Test
	public void findOrderByStatusTest() throws FilmStoreDAOException
	{
		List<Order> listOrder = orderDAOImpl.findOrderByStatus(Status.UNPAID); 
		assertNotNull("Operation failed. No orders were found",listOrder);
		assertEquals("Operation failed. No orders were found",false,listOrder.isEmpty());
	}
	
	@Test
	public void findOrderByUserEmailTest() throws FilmStoreDAOException
	{
		List<Order> listOrder = orderDAOImpl.findOrderByUserEmail("smile269@mail.ru"); 
		assertNotNull("Operation failed. No orders were found",listOrder);
		assertEquals("Operation failed. No orders were found",false,listOrder.isEmpty());
	}
}
