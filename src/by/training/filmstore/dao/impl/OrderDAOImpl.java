package by.training.filmstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.filmstore.dao.OrderDAO;
import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.dao.pool.PoolConnection;
import by.training.filmstore.dao.pool.PoolConnectionException;
import by.training.filmstore.entity.KindOfDelivery;
import by.training.filmstore.entity.KindOfPayment;
import by.training.filmstore.entity.Order;
import by.training.filmstore.entity.Status;

public class OrderDAOImpl implements OrderDAO {

	private static final Logger logger = LogManager.getLogger(OrderDAOImpl.class);
	private static final String SQL_INSERT = "insert into `order`(`order`.ord_email_user,`order`.ord_common_price,`order`.ord_status,"
			+ "`order`.ord_kind_of_delivery,`order`.ord_kind_of_payment,`order`.ord_date_of_order,"
			+ "`order`.ord_date_of_delivery,`order`.ord_address) values(?,?,?,?,?,?,?,?)";
	private static final String SQL_UPDATE = 
			"update `order`  SET `order`.ord_email_user = ?,`order`.ord_common_price = ?,"
			+ "`order`.ord_status = ?,`order`.ord_kind_of_delivery = ?,"
			+ "`order`.ord_kind_of_payment = ?,`order`.ord_date_of_order = ?,"
			+ "`order`.ord_date_of_delivery = ?,`order`.ord_address = ? where `order`.ord_uid = ?";
	private static final String SQL_DELETE = "DELETE FROM `order` WHERE `order`.ord_uid = ?";
	private static final String SQL_FIND_BY_ID = "select `order`.ord_email_user,`order`.ord_common_price,`order`.ord_status,"
			+ "`order`.ord_kind_of_delivery,`order`.ord_kind_of_payment,`order`.ord_date_of_order,"
			+ "`order`.ord_date_of_delivery,`order`.ord_address from `order` where `order`.ord_uid = ?";
	private static final String SQL_FIND_ALL = "select `order`.ord_email_user,`order`.ord_common_price,`order`.ord_status,"
			+ "`order`.ord_kind_of_delivery,`order`.ord_kind_of_payment,`order`.ord_date_of_order,"
			+ "`order`.ord_date_of_delivery,`order`.ord_address from `order`";
	private static final String SQL_FIND_BY_STATUS = "select `order`.ord_email_user,`order`.ord_common_price,`order`.ord_status,"
			+ "`order`.ord_kind_of_delivery,`order`.ord_kind_of_payment,`order`.ord_date_of_order,"
			+ "`order`.ord_date_of_delivery,`order`.ord_address from `order` where `order`.ord_status = ?";
	private static final String SQL_FIND_EMAIL_USER = "select `order`.ord_email_user,`order`.ord_common_price,`order`.ord_status,"
			+ "`order`.ord_kind_of_delivery,`order`.ord_kind_of_payment,`order`.ord_date_of_order,"
			+ "`order`.ord_date_of_delivery,`order`.ord_address from `order` where `order`.ord_email_user = ?";

	@Override
	public boolean create(Order entity) throws FilmStoreDAOException {
		return updateByCriteria(CommandDAO.INSERT, entity);
	}

	@Override
	public boolean update(Order entity) throws FilmStoreDAOException {
		return updateByCriteria(CommandDAO.UPDATE, entity);
	}

	@Override
	public boolean delete(Integer id) throws FilmStoreDAOException {
	 return updateByCriteria(CommandDAO.DELETE, id);
	}
	
	@Override
	public Order find(Integer id) throws FilmStoreDAOException {
		List<Order> listOrder = findOrderByCriteria(id, FindOrderCriteria.FIND_BY_ID);
		if (listOrder.isEmpty()) {
			return null;
		}
		return listOrder.get(0);
	}

	@Override
	public List<Order> findAll() throws FilmStoreDAOException {
		return findOrderByCriteria(null, FindOrderCriteria.FIND_ALL);
	}

	@Override
	public List<Order> findOrderByStatus(Status status) throws FilmStoreDAOException {
		return findOrderByCriteria(status.getNameStatus(), FindOrderCriteria.FIND_BY_STATUS);
	}

	@Override
	public List<Order> findOrderByUserEmail(String userEmail) throws FilmStoreDAOException {
		return findOrderByCriteria(userEmail, FindOrderCriteria.FIND_BY_USER_EMAIL);
	}

	private <T> boolean updateByCriteria(CommandDAO commandDAO, T parametr) throws FilmStoreDAOException {
		Connection connection = null;
		PreparedStatement prepStatement = null;
		PoolConnection poolConnection = null;
		boolean success = false;
		try {
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();

			prepStatement = createPrepStatementByCommandCriteria(connection, parametr, commandDAO);

			int affectedRows = prepStatement.executeUpdate();
			
			if (affectedRows != 0) {
				success = true;
			}
			
			fillGeneratedIdIfInsert(commandDAO, prepStatement, (Order)parametr);
		} catch (SQLException | PoolConnectionException e) {
			logger.error("Error creating of PreparedStatement.Operation failed ("+commandDAO.name()+")", e);
			throw new FilmStoreDAOException(e);
		} finally {
			try {
				poolConnection.putbackConnection(connection);
				prepStatement.close();
			} catch (SQLException e) {
				logger.error("Error closing of PreparedStatement or Connection", e);
			}
		}
		return success;
	}
	
	private <T> PreparedStatement createPrepStatementByCommandCriteria(Connection connection, T parametr,
			CommandDAO commandDAO) throws SQLException {
		PreparedStatement prepStatement = null;
		boolean insert = true;
		switch (commandDAO) {
		case INSERT: {
			prepStatement = connection.prepareStatement(SQL_INSERT,PreparedStatement.RETURN_GENERATED_KEYS);
			fillPreparedStatementForOrder(prepStatement,(Order) parametr, insert);
		}
			break;
		case UPDATE: {
			insert = false;
			prepStatement = connection.prepareStatement(SQL_UPDATE);
			fillPreparedStatementForOrder(prepStatement, (Order) parametr, insert);
		}
			break;
		case DELETE: {
			prepStatement = connection.prepareStatement(SQL_DELETE);
			prepStatement.setInt(1, (Integer) parametr);
		}
			break;
		}
		return prepStatement;
	}
	
	private void fillPreparedStatementForOrder(PreparedStatement preparedStatement,Order entity,boolean insert) throws SQLException{
		preparedStatement.setString(1, entity.getUserEmail());
		preparedStatement.setBigDecimal(2, entity.getCommonPrice());
		preparedStatement.setString(3, entity.getStatus().getNameStatus());
		preparedStatement.setString(4, entity.getKindOfDelivery().getNameKindOfDelivery());
		preparedStatement.setString(5, entity.getKindOfPayment().getNameKindOfPAyment());
		preparedStatement.setDate(6, entity.getDateOfOrder());
		preparedStatement.setDate(7, entity.getDateOfDelivery());
		preparedStatement.setString(8, entity.getAddress());
		if(!insert){
			preparedStatement.setInt(9, entity.getId());
		}
	}
	
	private void fillGeneratedIdIfInsert(CommandDAO commandDAO,PreparedStatement prepStatement,Order order) throws SQLException{
		if(commandDAO == CommandDAO.INSERT){
			ResultSet resultset = prepStatement.getGeneratedKeys();
			if (resultset != null && resultset.next()) {
				order.setId(resultset.getInt(1));
			}
		}
	}
	
	private <T> List<Order> findOrderByCriteria(T parametr, FindOrderCriteria criteria) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		Order order = null;
		List<Order> listOrder = new ArrayList<Order>();
		try {
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();

			prepStatement = createPrepStatementByOrderCriteria(connection, criteria, parametr);

			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				order = new Order();
				fillOrder(order, resultSet);
				listOrder.add(order);
			}
		} catch (PoolConnectionException | SQLException e) {
			logger.error("Error creating of PreparedStatement.Can't find order(" + criteria.name() + ")", e);
			throw new FilmStoreDAOException(e);
		} finally {
			try {
				poolConnection.putbackConnection(connection);
				prepStatement.close();
			} catch (SQLException e) {
				logger.error("Error closing of PreparedStatement or Connection", e);
			}
		}
		return listOrder;
	}

	private <T> PreparedStatement createPrepStatementByOrderCriteria(Connection connection, FindOrderCriteria criteria,
			T parametr) throws SQLException {
		PreparedStatement prepStatement = null;
		switch (criteria) {
		case FIND_BY_ID: {
			prepStatement = connection.prepareStatement(SQL_FIND_BY_ID);
			prepStatement.setInt(1, (Integer) parametr);
		}
			break;
		case FIND_ALL: {
			prepStatement = connection.prepareStatement(SQL_FIND_ALL);
		}
			break;
		case FIND_BY_STATUS: {
			prepStatement = connection.prepareStatement(SQL_FIND_BY_STATUS);
			prepStatement.setString(1, (String) parametr);
		}
			break;
		case FIND_BY_USER_EMAIL: {
			prepStatement = connection.prepareStatement(SQL_FIND_EMAIL_USER);
			prepStatement.setString(1, (String) parametr);
		}
			break;
		}
		return prepStatement;
	}

	private void fillOrder(Order order, ResultSet resultSet) throws SQLException {
		order.setId(resultSet.getInt(DatabaseColumnName.TABLE_ORDER_UID));
		order.setUserEmail(resultSet.getString(DatabaseColumnName.TABLE_ORDER_EMAIL_USER));
		order.setCommonPrice(resultSet.getBigDecimal(DatabaseColumnName.TABLE_ORDER_COMMON_PRICE));
		order.setStatus(Status.getStatusByName(resultSet.getString(DatabaseColumnName.TABLE_ORDER_STATUS)));
		order.setKindOfDelivery(KindOfDelivery
				.getKindOfDeliveryByName(resultSet.getString(DatabaseColumnName.TABLE_ORDER_KIND_OF_DELIVERY)));
		order.setKindOfPayment(KindOfPayment
				.getKindOfPaymentByName(resultSet.getString(DatabaseColumnName.TABLE_ORDER_KIND_OF_PAYMENT)));
		order.setDateOfOrder(resultSet.getDate(DatabaseColumnName.TABLE_ORDER_DATE_OF_ORDER));
		order.setDateOfDelivery(resultSet.getDate(DatabaseColumnName.TABLE_ORDER_DATE_OF_DELIVERY));
		order.setAddress(resultSet.getString(DatabaseColumnName.TABLE_ORDER_ADDRESS));
	}

	private enum FindOrderCriteria {
		FIND_BY_ID, FIND_BY_USER_EMAIL, FIND_BY_STATUS, FIND_ALL
	}

}
