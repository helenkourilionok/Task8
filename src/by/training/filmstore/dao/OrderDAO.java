package by.training.filmstore.dao;

import java.util.List;

import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.entity.Order;
import by.training.filmstore.entity.Status;

public interface OrderDAO extends AbstractDAO<Order, Integer>{
	List<Order> findOrderByStatus(Status status) throws FilmStoreDAOException;
	List<Order> findOrderByUserEmail(String userEmail)throws FilmStoreDAOException;
} 
