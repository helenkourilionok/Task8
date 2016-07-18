package by.training.filmstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.filmstore.dao.GoodOfOrderDAO;
import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.dao.pool.PoolConnection;
import by.training.filmstore.dao.pool.PoolConnectionException;
import by.training.filmstore.entity.GoodOfOrder;
import by.training.filmstore.entity.GoodOfOrderPK;

public class GoodOfOrderDAOImpl implements GoodOfOrderDAO{

	private static final Logger logger = LogManager.getLogger(GoodOfOrderDAOImpl.class);
	private static final String SQL_INSERT = 
			"insert into good_of_order(good_of_order.ord_id,good_of_order.fm_id,"
			+ "good_of_order.gd_count_films) values(?,?,?)";
	private static final String SQL_UPDATE = 
			"update good_of_order set good_of_order.gd_count_films = ? "+
			 "where good_of_order.ord_id = ? and good_of_order.fm_id = ? ";
	private static final String SQL_DELETE = 
			"delete from good_of_order where good_of_order.ord_id = ? and good_of_order.fm_id = ? ";
	private static final String SQL_FIND_ALL = 
			"select good_of_order.ord_id,good_of_order.fm_id,"
			+ "good_of_order.gd_count_films from good_of_order";
	private static final String SQL_FIND_BY_ID = 
			"select good_of_order.ord_id,good_of_order.fm_id,good_of_order.gd_count_films "+
			"from good_of_order where good_of_order.ord_id = ? and good_of_order.fm_id = ?";
	private static final String SQL_FIND_BY_ORDER_ID = 
			"select good_of_order.ord_id,good_of_order.fm_id,good_of_order.gd_count_films "+
			"from good_of_order where good_of_order.ord_id = ?";
	private static final String SQL_FIND_BY_FILM_ID = 
			"select good_of_order.ord_id,good_of_order.fm_id,good_of_order.gd_count_films "+
			"from good_of_order where good_of_order.fm_id = ?";
	
	
	@Override
	public GoodOfOrder find(GoodOfOrderPK id) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		GoodOfOrder goodOfOrder = null;
		try
		{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement =  connection.prepareStatement(SQL_FIND_BY_ID);
			prepStatement.setInt(1, id.getIdOrder());
			prepStatement.setShort(2, id.getIdFilm());
			resultSet = prepStatement.executeQuery();
			goodOfOrder = new GoodOfOrder();
			if(resultSet.next())
			{
				fillGoodOfOrder(goodOfOrder, resultSet);
			}
		}
		catch(PoolConnectionException|SQLException e)
		{
			logger.error("Error creating of PreparedStatement.Can't find good of order",e);
			throw new FilmStoreDAOException(e);
		}
		finally {
			try {
				poolConnection.putbackConnection(connection);
				resultSet.close();
				prepStatement.close();
			} catch (SQLException e) {
				logger.error("Error closing of PreparedStatement or Connection",e);
			}
		}
		return goodOfOrder;
	}

	@Override
	public boolean create(GoodOfOrder entity) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		boolean success = false;
		try{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_INSERT);
			prepStatement.setInt(1, entity.getId().getIdOrder());
			prepStatement.setInt(2, entity.getId().getIdFilm());
			prepStatement.setByte(3, entity.getCountFilms());
			int affectedRows = prepStatement.executeUpdate();
			if (affectedRows != 0) {
                success = true;
            }
		}
		catch(PoolConnectionException|SQLException e){
			logger.error("Error creating of PreparedStatement.Can't create good of order",e);
			throw new FilmStoreDAOException(e);
		}
		finally {
			try {
				prepStatement.close();
				poolConnection.putbackConnection(connection);
			} catch (SQLException e) {
				logger.error("Error closing of PreparedStatement or Connection",e);
			}
		}
		return success;
	}

	@Override
	public boolean update(GoodOfOrder entity) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		boolean success = false;
		try{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_UPDATE);
			prepStatement.setByte(1, entity.getCountFilms());
			int affectedRows = prepStatement.executeUpdate();
            if (affectedRows != 0) {
                success = true;
            }
		}
		catch(PoolConnectionException|SQLException e){
			logger.error("Error creating of PreparedStatement.Can't update good of order fields",e);
			throw new FilmStoreDAOException(e);
		}
		finally {
			try {
				poolConnection.putbackConnection(connection);
				prepStatement.close();
			} catch (SQLException e) {
				logger.error("Error closing of PreparedStatement or Connection",e);
			}
		}
		return success;
	}

	@Override
	public boolean delete(GoodOfOrderPK id) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		boolean success = false;
		try{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_DELETE);
			prepStatement.setInt(1,id.getIdOrder());
			prepStatement.setShort(2, id.getIdFilm());
			int affectedRows = prepStatement.executeUpdate();
            if (affectedRows != 0) {
                success = true;
            }
		}
		catch(PoolConnectionException|SQLException e){
			logger.error("Error creating of PreparedStatement.Can't delete good of order",e);
			throw new FilmStoreDAOException(e);
		}
		finally {
			try {
				poolConnection.putbackConnection(connection);
				prepStatement.close();
			} catch (SQLException e) {
				logger.error("Error closing of PreparedStatement or Connection");
			}
		}
		return success;
	}

	@Override
	public List<GoodOfOrder> findAll() throws FilmStoreDAOException {
		return findGoodOfOrderByCriteria(0, FindGoodOfOrderCriteria.FIND_ALL);
	}
	
	@Override
	public List<GoodOfOrder> findGoodByOrderId(int id) throws FilmStoreDAOException {
		return findGoodOfOrderByCriteria(id, FindGoodOfOrderCriteria.FIND_BY_ORDER_ID);
	}

	@Override
	public List<GoodOfOrder> findGoodByFilmId(short id) throws FilmStoreDAOException {
		return findGoodOfOrderByCriteria(id, FindGoodOfOrderCriteria.FIND_BY_FILM_ID);
	}
	
	private List<GoodOfOrder> findGoodOfOrderByCriteria(int parametr, FindGoodOfOrderCriteria criteria) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		GoodOfOrder goodOfOrder = null;
		List<GoodOfOrder> listGoodOfOrder = new ArrayList<GoodOfOrder>();
		try {
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			switch(criteria)
			{
				case FIND_BY_ORDER_ID:{
					prepStatement = connection.prepareStatement(SQL_FIND_BY_ORDER_ID);
					prepStatement.setInt(1, parametr);
				}break;
				case FIND_BY_FILM_ID:{
					prepStatement = connection.prepareStatement(SQL_FIND_BY_FILM_ID);
					prepStatement.setShort(1, (short)parametr);
				}break;
				case FIND_ALL:{
					prepStatement = connection.prepareStatement(SQL_FIND_ALL);
				}break;
			}
			resultSet = prepStatement.executeQuery();
			while(resultSet.next()){
				goodOfOrder = new GoodOfOrder();
				fillGoodOfOrder(goodOfOrder, resultSet);
				listGoodOfOrder.add(goodOfOrder);
			}
		}
		catch(PoolConnectionException|SQLException e){
			logger.error("Error creating of PreparedStatement.Can't find good of order("+criteria.name()+")",e);
			throw new FilmStoreDAOException(e);
		}
		finally {
			try {
				poolConnection.putbackConnection(connection);
				prepStatement.close();
			} catch (SQLException e) {
				logger.error("Error closing of PreparedStatement or Connection",e);
			}
		}
		return listGoodOfOrder;
	}
	
	private void fillGoodOfOrder(GoodOfOrder goodOfOrder,ResultSet resultSet) throws SQLException{
		GoodOfOrderPK goodOfOrderPK = new GoodOfOrderPK();
		goodOfOrderPK.setIdFilm(resultSet.getShort(DatabaseColumnName.TABLE_GOOD_OF_ORDER_FILM_ID));
		goodOfOrderPK.setIdOrder(resultSet.getInt(DatabaseColumnName.TABLE_GOOD_OF_ORDER_ORDER_ID));
		goodOfOrder.setId(goodOfOrderPK);
		goodOfOrder.setCountFilms(resultSet.getByte(DatabaseColumnName.TABLE_GOOD_OF_ORDER_COUNT_FILMS));
	}
	
	private enum FindGoodOfOrderCriteria {
		FIND_BY_ORDER_ID,FIND_BY_FILM_ID,FIND_ALL
	}

}
