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
import by.training.filmstore.dao.poolexception.PoolConnectionException;
import by.training.filmstore.entity.Role;
import by.training.filmstore.entity.User;

public class UserDAOImpl implements UserDAO{

	private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);
	private static final String SQL_FIND_BY_ID =
			"SELECT user.us_email,user.us_password,user.us_role,"+
		    "user.us_last_name,user.us_first_name,user.us_patronymic,"+
            "user.us_phone,user.us_balance,user.us_discount FROM user where user.us_email = ?";
    private static final String SQL_FIND_BY_EMAIL_AND_PASSWORD =
		        "SELECT user.us_email,user.us_password,user.us_role,"+
		"user.us_last_name,user.us_first_name,user.us_patronymic," +
        "user.us_phone,user.us_balance,user.us_discount FROM user where user.us_email = ? and user.us_password = md5(?)";
    private static final String SQL_LIST_USER_BY_ID =
		        "SELECT user.us_email,user.us_password,user.us_role," +
				"user.us_last_name,user.us_first_name,user.us_patronymic,"+
		        "user.us_phone,user.us_balance,user.us_discount FROM user ORDER BY user.us_email";
	private static final String SQL_INSERT =
		        "INSERT INTO user (us_email,us_password, us_role,us_last_name,"+
					"us_first_name,us_patronymic,us_phone,us_balance,us_discount)"+ 
                    "VALUES (?, MD5(?), ?, ?, ?,?,?,?,?)";
	private static final String SQL_UPDATE =
			     "UPDATE user SET user.us_last_name = ?,	user.us_balance = ?,"+
		        "user.us_phone = ? WHERE user.us_email = ?";
	private static final String SQL_DELETE =
		        "DELETE FROM user WHERE user.us_email = ?";
	private static final String SQL_CHANGE_PASSWORD =
		        "UPDATE user SET user.us_password = MD5(?) WHERE user.us_email = ?";
	
	private static final String SQL_MAKE_DISCOUNT = 
					"UPDATE user SET user.us_discount = ? where user.us_email in (SELECT `order`.ord_email_user "+
					" FROM `order` WHERE  `order`.ord_status = 'оплачено' AND "+
					" YEAR(`order`.ord_date_of_order) = ? AND MONTH(`order`.ord_date_of_order) = ?"+
					" GROUP BY `order`.ord_email_user HAVING COUNT(`order`.ord_uid) >= ?)";
	private static final String SQL_FIND_USER_FOR_DISCOUNT = 
			"SELECT user.us_email FROM user WHERE user.us_email NOT IN "+ 
			"(SELECT `order`.ord_email_user FROM `order` WHERE YEAR(`order`.ord_date_of_order) = ? "+
			"AND MONTH(`order`.ord_date_of_order) = ?) AND user.us_discount <> 0";
	
	private static final String SQL_TAKE_AWAY_DISCOUNT = 
			"update user  SET user.us_discount = 0 where user.us_email = ? ";
	
	@Override
 	public boolean create(User entity) throws FilmStoreDAOException {
		Connection connection = null;
		PreparedStatement prepStatement = null;
		PoolConnection poolConnection = null;
	    boolean success = false;
        try {
        	poolConnection = PoolConnection.getInstance();
            connection = poolConnection.takeConnection();
            prepStatement = connection.prepareStatement(SQL_INSERT);
            prepStatement.setString(1, entity.getEmail());
            prepStatement.setString(2, entity.getPassword());
            prepStatement.setString(3, entity.getRole().name());
            prepStatement.setString(4, entity.getLastName());
            prepStatement.setString(5, entity.getFirstName());
            prepStatement.setString(6, entity.getPatronimic());
            prepStatement.setString(7, entity.getPhone());
            prepStatement.setBigDecimal(8, (BigDecimal)entity.getBalance());
            prepStatement.setByte(9, (byte)entity.getDiscount());
            int affectedRows = prepStatement.executeUpdate();
            if (affectedRows != 0) {
                success = true;
            }
        } catch (SQLException | PoolConnectionException e) {
        	logger.error("Error creating of PreparedStatement.Can't create user");
            throw new FilmStoreDAOException(e);
        }
        finally {
        	try {
				prepStatement.close();
	            poolConnection.putbackConnection(connection);
			} catch (SQLException e) {
				logger.error("Error closing of PreparedStatement or Connection");
				throw new FilmStoreDAOException(e);
			}
		}
		return success;
	}

	@Override
	public boolean update(User entity) throws FilmStoreDAOException {
		Connection connection = null;
		PreparedStatement prepStatement = null;
		PoolConnection poolConnection = null;
		boolean success = false;
		try{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_UPDATE);
			prepStatement.setString(1, entity.getLastName());
			prepStatement.setBigDecimal(2, entity.getBalance());
			prepStatement.setString(3, entity.getPhone());
			prepStatement.setString(4, entity.getEmail());
			int affectedRows = prepStatement.executeUpdate();
			if(affectedRows != 0)
			{
				success = true;
			}
		}
		catch(PoolConnectionException|SQLException e)
		{
			logger.error("Error of creating PreparedStatement.Can't update user fields");
			throw new FilmStoreDAOException(e);
		}
        finally {
        	try {
				prepStatement.close();
				poolConnection.putbackConnection(connection);
			} catch (SQLException e) {
				logger.error("Error closing of PreparedStatement or Connection");
				throw new FilmStoreDAOException(e);
			}
		}
		return success;
	}

	@Override
	public boolean delete(String id) throws FilmStoreDAOException {
		Connection connection = null;
		PoolConnection poolConnection = null;
		PreparedStatement prepStatement = null;
		boolean success = false;
		try {
				poolConnection = PoolConnection.getInstance();
				connection = poolConnection.takeConnection();
				prepStatement = connection.prepareStatement(SQL_DELETE);
				prepStatement.setString(1,id);
				int affectedRows = prepStatement.executeUpdate();
			     if (affectedRows != 0) {
			       success = true;
			     }
		} catch (PoolConnectionException|SQLException e) {
			logger.error("Error of creating PreparedStatement");
		   throw new FilmStoreDAOException(e);
		}
        finally {
			try {
				prepStatement.close();
				poolConnection.putbackConnection(connection);
			} catch (SQLException e) {
				logger.error("Error closing of PreparedStatement or Connection");
				throw new FilmStoreDAOException(e);
			}
		}
		return success;
	}
	
	@Override
	public boolean makeDiscount(byte sizeOfDiscount,int year,int month,byte countOrders) throws FilmStoreDAOException
	{
		boolean success = false;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		PoolConnection poolConnection = null;
		try{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_MAKE_DISCOUNT);
			prepStatement.setByte(1,sizeOfDiscount);
			prepStatement.setInt(2, year);
			prepStatement.setInt(3, month);
			prepStatement.setByte(4,countOrders);
			int affectedRows = prepStatement.executeUpdate();
			if(affectedRows != 0)
			{
				success = true;
			}
		}
		catch(PoolConnectionException | SQLException e)
		{
			logger.error("Error creating of PreparedStatement.Can't update user discount.");
			throw new FilmStoreDAOException(e);
		}
        finally {
        	try {
				prepStatement.close();
				poolConnection.putbackConnection(connection);
			} catch (SQLException e) {
				logger.error("Error closing of PreparedStatement or Connection");
				throw new FilmStoreDAOException(e);
			}
		}
		return success;
	}
	
	@Override
	public boolean takeAwayDiscount(int year,int month) throws FilmStoreDAOException 
	{
		PoolConnection poolConnection = null;
		boolean success = false;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		List<String> emails = null;
		try
		{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_FIND_USER_FOR_DISCOUNT);
			emails = findEmailsOfUser(prepStatement, year, month);
			
			connection.setAutoCommit(false);
			prepStatement = connection.prepareStatement(SQL_TAKE_AWAY_DISCOUNT);
			int affectedRows = updateDiscountByEmails(prepStatement, emails);
			connection.commit();
			if(affectedRows == emails.size())
			{
				success = true;
			}
		}
		catch(PoolConnectionException | SQLException ex)
		{
			logger.error("Can't update user discount(SQLException)");
			throw new FilmStoreDAOException(ex);
		}
        finally {
        	try {
				prepStatement.close();
				poolConnection.putbackConnection(connection);
			} catch (SQLException e) {
				logger.error("Error closing of ResultSet or PreparedStatement");
				throw new FilmStoreDAOException(e);
			}
		}
		return success;
	}
	
	@Override
	public User find(String id) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		User user = null;
		try {
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_FIND_BY_ID);
			prepStatement.setString(1,id);
			resultSet = prepStatement.executeQuery();
			if(resultSet.next())
			{
				user = new User();
				fillUser(user,resultSet);
			}
		} catch (PoolConnectionException|SQLException e) {
			logger.error("Error executing query. Can't find user by ID");
			new FilmStoreDAOException(e);
		}
        finally {
        	try {
        		resultSet.close();
				prepStatement.close();
				poolConnection.putbackConnection(connection);
			} catch (SQLException e) {
				logger.error("Error closing of ResultSet or PreparedStatement");
				throw new FilmStoreDAOException(e);
			}
        }
		return user;
	}

	@Override
	public List<User> findAll() throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		User user = null;
		List<User> listUser = new ArrayList<User>();
        try {
             poolConnection = PoolConnection.getInstance();
             connection = poolConnection.takeConnection();
             prepStatement = connection.prepareStatement(SQL_LIST_USER_BY_ID);
             resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
            	user = new User();
            	fillUser(user, resultSet);
                listUser.add(user);
            }
        } catch (PoolConnectionException | SQLException e) {
        	logger.error("Error executing query for database.Can't find user by ID");
            throw new FilmStoreDAOException(e);
        }
        finally {
			try {
				resultSet.close();
				prepStatement.close();
				poolConnection.putbackConnection(connection);
			} catch (SQLException e) {
				logger.error("Error closing of ResultSet or PreparedStatement");			
				throw new FilmStoreDAOException(e);
			}
		}
		return listUser;
	}
	
	public User find(String email, String password) throws FilmStoreDAOException
	{
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		User user = null;
		try {
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_FIND_BY_EMAIL_AND_PASSWORD);
			prepStatement.setString(1,email);
			prepStatement.setString(2, password);
			resultSet = prepStatement.executeQuery();
			if(resultSet.next())
			{
				user = new User();
				fillUser(user, resultSet);
			}
		} catch (PoolConnectionException | SQLException e) {
			logger.error("Error executing query for database. Can't find user by email and password");
			new FilmStoreDAOException(e);
		}
        finally {
			try {
				resultSet.close();
				prepStatement.close();
				poolConnection.putbackConnection(connection);
			} catch (SQLException e) {
				logger.error("Error closing of ResultSet or PreparedStatement");
				new FilmStoreDAOException(e);
			}
		}
		return user;
	}
	
    public boolean changePassword(User user) throws FilmStoreDAOException
    {
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
        	 logger.error("Error updating of user information");
             throw new FilmStoreDAOException(e);
         }
         finally {
			try {
				prepStatement.close();
				poolConnection.putbackConnection(connection);
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement");
				throw new FilmStoreDAOException(e);
			}
		}
        return success;
    }
           
    private List<String> findEmailsOfUser(PreparedStatement prepStatement,int year,int month) throws SQLException
    {
		prepStatement.setInt(1, year);
		prepStatement.setInt(2, month);
		ResultSet resultSet = prepStatement.executeQuery();
    	List<String> emails = new ArrayList<String>();
		while(resultSet.next())
		{
			emails.add(resultSet.getString(1));
		}
		resultSet.close();
		return emails;
    }
    
    private int updateDiscountByEmails(PreparedStatement prepStatement,List<String> emails) throws SQLException
    {
		int affectedRows = 0;
		for(int i = 0;i<emails.size();i++)
		{
			prepStatement.setString(1, emails.get(i));
			affectedRows += prepStatement.executeUpdate();
		}
		return affectedRows;
    }
    
	private void fillUser(User user,ResultSet resultSet) throws SQLException
	{
		user.setEmail(resultSet.getString("us_email"));
		user.setPassword(resultSet.getString("us_password"));
		user.setRole(Role.valueOf(resultSet.getString("us_role")));
		user.setLastName(resultSet.getString("us_last_name"));
		user.setFirstName(resultSet.getString("us_first_name"));
		user.setPatronimic(resultSet.getString("us_patronymic"));
		user.setPhone(resultSet.getString("us_phone"));
		user.setBalance(resultSet.getBigDecimal("us_balance"));
		user.setDiscount(resultSet.getByte("us_discount"));
	}
}
