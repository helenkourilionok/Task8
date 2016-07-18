package by.training.filmstore.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.filmstore.dao.UserDAO;
import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.dao.pool.PoolConnection;
import by.training.filmstore.dao.pool.PoolConnectionException;
import by.training.filmstore.entity.Role;
import by.training.filmstore.entity.User;

public class UserDAOImpl implements UserDAO {

	private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);
	private static final String SQL_FIND_BY_ID = "SELECT user.us_email,user.us_password,user.us_role,"
			+ "user.us_last_name,user.us_first_name,user.us_patronymic,"
			+ "user.us_phone,user.us_balance,user.us_discount FROM user where user.us_email = ?";
	private static final String SQL_FIND_BY_EMAIL_AND_PASSWORD = "SELECT user.us_email,user.us_password,user.us_role,"
			+ "user.us_last_name,user.us_first_name,user.us_patronymic,"
			+ "user.us_phone,user.us_balance,user.us_discount FROM user where user.us_email = ? and user.us_password = md5(?)";
	private static final String SQL_FIND_ALL = "SELECT user.us_email,user.us_password,user.us_role,"
			+ "user.us_last_name,user.us_first_name,user.us_patronymic,"
			+ "user.us_phone,user.us_balance,user.us_discount FROM user ORDER BY user.us_email";
	private static final String SQL_INSERT = "INSERT INTO user (us_email,us_password, us_role,us_last_name,"
			+ "us_first_name,us_patronymic,us_phone,us_balance,us_discount)" + "VALUES (?, MD5(?), ?, ?, ?,?,?,?,?)";
	private static final String SQL_UPDATE = "UPDATE user SET user.us_password = ?,user.us_role = ?,"
			+ "user.us_last_name = ?,user.us_first_name = ?,"
			+ "user.us_patronymic = ?,user.us_phone = ?,user.us_balance = ?,user.us_discount = ?"
			+ " WHERE user.us_email = ?";
	private static final String SQL_DELETE = "DELETE FROM user WHERE user.us_email = ?";
	private static final String SQL_CHANGE_PASSWORD = "UPDATE user SET user.us_password = MD5(?) WHERE user.us_email = ?";
	private static final String SQL_MAKE_DISCOUNT = "UPDATE user SET user.us_discount = ? where user.us_email in (SELECT `order`.ord_email_user "
			+ " FROM `order` WHERE  `order`.ord_status = 'оплачено' AND "
			+ " YEAR(`order`.ord_date_of_order) = ? AND MONTH(`order`.ord_date_of_order) = ?"
			+ " GROUP BY `order`.ord_email_user HAVING COUNT(`order`.ord_uid) >= ?)";
	private static final String SQL_FIND_USER_FOR_DISCOUNT = "SELECT user.us_email FROM user WHERE user.us_email NOT IN "
			+ "(SELECT `order`.ord_email_user FROM `order` WHERE YEAR(`order`.ord_date_of_order) = ? "
			+ "AND MONTH(`order`.ord_date_of_order) = ?) AND user.us_discount <> 0";

	private static final String SQL_TAKE_AWAY_DISCOUNT = "update user  SET user.us_discount = 0 where user.us_email = ? ";

	@Override
	public boolean create(User entity) throws FilmStoreDAOException {
		return updateByCriteria(CommandDAO.INSERT, entity);
	}

	@Override
	public boolean update(User entity) throws FilmStoreDAOException {
		return updateByCriteria(CommandDAO.UPDATE, entity);
	}

	@Override
	public boolean delete(String id) throws FilmStoreDAOException {
		return updateByCriteria(CommandDAO.DELETE, id);
	}

	@Override
	public boolean makeDiscount(byte sizeOfDiscount, int year, int month, byte countOrders)
			throws FilmStoreDAOException {
		boolean success = false;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		PoolConnection poolConnection = null;
		try {
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_MAKE_DISCOUNT);
			prepStatement.setByte(1, sizeOfDiscount);
			prepStatement.setInt(2, year);
			prepStatement.setInt(3, month);
			prepStatement.setByte(4, countOrders);
			int affectedRows = prepStatement.executeUpdate();
			if (affectedRows != 0) {
				success = true;
			}
		} catch (PoolConnectionException | SQLException e) {
			logger.error("Error creating of PreparedStatement.Can't update user discount.", e);
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

	@Override
	public boolean takeAwayDiscount(int year, int month) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		boolean success = false;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		List<String> emails = null;
		try {
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_FIND_USER_FOR_DISCOUNT);
			emails = findEmailsOfUser(prepStatement, year, month);

			connection.setAutoCommit(false);
			prepStatement = connection.prepareStatement(SQL_TAKE_AWAY_DISCOUNT);
			int affectedRows = updateDiscountByEmails(prepStatement, emails);
			connection.commit();
			if (affectedRows == emails.size()) {
				success = true;
			}
		} catch (PoolConnectionException | SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				logger.error("Can't rollback update operations(SQLException)", e);
			}
			logger.error("Can't update user discount(SQLException)", e);
			throw new FilmStoreDAOException(e);
		} finally {
			try {
				poolConnection.putbackConnection(connection);
				prepStatement.close();
			} catch (SQLException e) {
				logger.error("Error closing of ResultSet or PreparedStatement", e);
			}
		}
		return success;
	}

	public boolean changePassword(User user) throws FilmStoreDAOException {
		Connection connection = null;
		PreparedStatement prepStatement = null;
		PoolConnection poolConnection = null;
		boolean success = false;
		try {
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_CHANGE_PASSWORD);
			prepStatement.setString(1, user.getPassword());
			prepStatement.setString(2, user.getEmail());
			int affectedRows = prepStatement.executeUpdate();
			if (affectedRows != 0) {
				success = true;
			}
		} catch (PoolConnectionException | SQLException e) {
			logger.error("Error updating of user information", e);
			throw new FilmStoreDAOException(e);
		} finally {
			try {
				poolConnection.putbackConnection(connection);
				prepStatement.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement", e);
			}
		}
		return success;
	}

	@Override
	public boolean changeEmail(String oldEmail, String newEmail) throws FilmStoreDAOException {
		Connection connection = null;
		PreparedStatement prepStatementDelete = null;
		PreparedStatement prepStatementInsert = null;
		PoolConnection poolConnection = null;
		User foundedUser = null;
		boolean success = false;

		try {
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatementDelete = connection.prepareStatement(SQL_DELETE);
			prepStatementInsert = connection.prepareStatement(SQL_INSERT);

			foundedUser = find(oldEmail);
			if (foundedUser == null) {
				logger.error("Can't change user email!Incorrect email!");
				throw new FilmStoreDAOException("User wasn't found!Incorrect email!");
			}

			connection.setAutoCommit(false);

			deleteInsertUser(prepStatementDelete, prepStatementInsert, foundedUser, newEmail);

			connection.commit();
			success = true;

		} catch (PoolConnectionException | SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				logger.error("Can't rollback update and delete operation with user table!", e);
			}
			logger.error("Error updating of user information", e);
			throw new FilmStoreDAOException(e);
		} finally {
			try {
				poolConnection.putbackConnection(connection);
				prepStatementDelete.close();
				prepStatementInsert.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement", e);
			}
		}
		return success;
	}

	@Override
	public User find(String id) throws FilmStoreDAOException {
		List<User> listUsers = findUserByCriteria(FindUserCriteria.FIND_BY_ID, id);
		if (listUsers.isEmpty()) {
			return null;
		}
		return listUsers.get(0);
	}

	@Override
	public User find(String email, String password) throws FilmStoreDAOException {
		List<User> listUsers = findUserByCriteria(FindUserCriteria.FIND_BY_EMAIL_AND_PASSWORD, email, password);
		if (listUsers.isEmpty()) {
			return null;
		}
		return listUsers.get(0);
	}

	@Override
	public List<User> findAll() throws FilmStoreDAOException {
		return findUserByCriteria(FindUserCriteria.FIND_ALL);
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
		} catch (SQLException | PoolConnectionException e) {
			logger.error("Error creating of PreparedStatement.Can't "+commandDAO.name()+" user", e);
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
		boolean insertOperation = true;
		switch (commandDAO) {
		case INSERT: {
			prepStatement = connection.prepareStatement(SQL_INSERT);
			fillPreparedStatementForUser(prepStatement, (User) parametr, insertOperation);
		}
			break;
		case UPDATE: {
			insertOperation = false;
			prepStatement = connection.prepareStatement(SQL_UPDATE);
			fillPreparedStatementForUser(prepStatement, (User) parametr, insertOperation);
		}
			break;
		case DELETE: {
			prepStatement = connection.prepareStatement(SQL_DELETE);
			prepStatement.setString(1, (String) parametr);
		}
			break;
		}
		return prepStatement;
	}

	private void deleteInsertUser(PreparedStatement prepStatementDelete,
			PreparedStatement prepStatementInsert,User foundedUser,String newEmail) throws SQLException, FilmStoreDAOException{
		boolean insertOperation = true;
		prepStatementDelete.setString(1, foundedUser.getEmail());
		int affectedRows = prepStatementDelete.executeUpdate();
		if (affectedRows == 0) {
			logger.error("Can't change user email!Incorrect email!");
			throw new FilmStoreDAOException("User wasn't found!Incorrect email!");
		}
		
		foundedUser.setEmail(newEmail);
		fillPreparedStatementForUser(prepStatementInsert, foundedUser, insertOperation);
		affectedRows = prepStatementInsert.executeUpdate();
		if (affectedRows == 0) {
			logger.error("Can't change user email!Incorrect email!");
			throw new FilmStoreDAOException("User wasn't found!Incorrect email!");
		}
	}
	
	private List<String> findEmailsOfUser(PreparedStatement prepStatement, int year, int month) throws SQLException {
		prepStatement.setInt(1, year);
		prepStatement.setInt(2, month);
		ResultSet resultSet = prepStatement.executeQuery();
		List<String> emails = new ArrayList<String>();
		while (resultSet.next()) {
			emails.add(resultSet.getString(1));
		}
		resultSet.close();
		return emails;
	}

	private int updateDiscountByEmails(PreparedStatement prepStatement, List<String> emails) throws SQLException {
		int affectedRows = 0;
		for (int i = 0; i < emails.size(); i++) {
			prepStatement.setString(1, emails.get(i));
			affectedRows += prepStatement.executeUpdate();
		}
		return affectedRows;
	}

	private List<User> findUserByCriteria(FindUserCriteria criteria, String... parametr) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		User user = null;
		List<User> listUser = new ArrayList<User>();
		try {
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();

			prepStatement = createPrepStatementByUserCriteria(connection, criteria, parametr);

			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				user = new User();
				fillUser(user, resultSet);
				listUser.add(user);
			}
		} catch (PoolConnectionException | SQLException e) {
			logger.error("Error creating of PreparedStatement.Can't find user(" + criteria.name() + ")", e);
			throw new FilmStoreDAOException(e);
		} finally {
			try {
				poolConnection.putbackConnection(connection);
				prepStatement.close();
			} catch (SQLException e) {
				logger.error("Error closing of PreparedStatement or Connection", e);
			}
		}
		return listUser;
	}

	private PreparedStatement createPrepStatementByUserCriteria(Connection connection,
			FindUserCriteria findUserCriteria, String... parametr) throws SQLException {
		PreparedStatement prepStatement = null;
		switch (findUserCriteria) {
		case FIND_BY_ID: {
			prepStatement = connection.prepareStatement(SQL_FIND_BY_ID);
			prepStatement.setString(1, parametr[0]);
		}
			break;
		case FIND_BY_EMAIL_AND_PASSWORD: {
			prepStatement = connection.prepareStatement(SQL_FIND_BY_EMAIL_AND_PASSWORD);
			prepStatement.setString(1, parametr[0]);
			prepStatement.setString(2, parametr[1]);
		}
			break;
		case FIND_ALL: {
			prepStatement = connection.prepareStatement(SQL_FIND_ALL);
		}
			break;
		}
		return prepStatement;
	}

	private void fillPreparedStatementForUser(PreparedStatement preparedStatement, User entity, boolean insert)
			throws SQLException {
		int[] indexes = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		if (!insert) {
			indexes = new int[] { 9, 1, 2, 3, 4, 5, 6, 7, 8 };
		}
		preparedStatement.setString(indexes[0], entity.getEmail());
		preparedStatement.setString(indexes[1], entity.getPassword());
		preparedStatement.setString(indexes[2], entity.getRole().name());
		preparedStatement.setString(indexes[3], entity.getLastName());
		preparedStatement.setString(indexes[4], entity.getFirstName());
		preparedStatement.setString(indexes[5], entity.getPatronymic());
		preparedStatement.setString(indexes[6], entity.getPhone());
		preparedStatement.setBigDecimal(indexes[7], (BigDecimal) entity.getBalance());
		preparedStatement.setByte(indexes[8], (byte) entity.getDiscount());
	}

	private void fillUser(User user, ResultSet resultSet) throws SQLException {
		user.setEmail(resultSet.getString(DatabaseColumnName.TABLE_USER_EMAIL));
		user.setPassword(resultSet.getString(DatabaseColumnName.TABLE_USER_PASSWORD));
		user.setRole(Role.valueOf(resultSet.getString(DatabaseColumnName.TABLE_USER_ROLE)));
		user.setLastName(resultSet.getString(DatabaseColumnName.TABLE_USER_LAST_NAME));
		user.setFirstName(resultSet.getString(DatabaseColumnName.TABLE_USER_FIRST_NAME));
		user.setPatronymic(resultSet.getString(DatabaseColumnName.TABLE_USER_PATRONIMIC));
		user.setPhone(resultSet.getString(DatabaseColumnName.TABLE_USER_PHONE));
		user.setBalance(resultSet.getBigDecimal(DatabaseColumnName.TABLE_USER_BALANCE));
		user.setDiscount(resultSet.getByte(DatabaseColumnName.TABLE_USER_DISCOUNT));
	}

	private enum FindUserCriteria {
		FIND_BY_ID, FIND_BY_EMAIL_AND_PASSWORD, FIND_ALL
	}
}
