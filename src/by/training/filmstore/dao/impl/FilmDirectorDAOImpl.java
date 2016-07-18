package by.training.filmstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.filmstore.dao.FilmDirectorDAO;
import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.dao.pool.PoolConnection;
import by.training.filmstore.dao.pool.PoolConnectionException;
import by.training.filmstore.entity.FilmDirector;

public class FilmDirectorDAOImpl implements FilmDirectorDAO {

	private static final Logger logger = LogManager.getLogger(OrderDAOImpl.class);
	
	private static final String SQL_INSERT = 
			"insert into film_director(film_director.fd_fio)  VALUES(?)";
	private static final String SQL_UPDATE = 
			"update film_director set film_director.fd_fio = ? where film_director.fd_uid = ?";
	private static final String SQL_DELETE = 
			"delete from film_director where film_director.fd_uid = ?";
	private static final String SQL_FIND_BY_ID = 
			"select film_director.fd_uid,film_director.fd_fio from actor where film_director.fd_uid = ?";
	private static final String SQL_FIND_BY_FIO = 
			"select film_director.fd_uid,film_director.fd_fio from actor where film_director.fd_fio = ?";
	private static final String SQL_FIND_ALL = 
			"select film_director.fd_uid,film_director.fd_fio from film_director";

	@Override
	public boolean create(FilmDirector entity) throws FilmStoreDAOException {
		return updateByCriteria(CommandDAO.INSERT, entity);
	}

	@Override
	public boolean update(FilmDirector entity) throws FilmStoreDAOException {
		return updateByCriteria(CommandDAO.UPDATE, entity);
	}

	@Override
	public boolean delete(Short id) throws FilmStoreDAOException {
		return updateByCriteria(CommandDAO.DELETE, id);
	}

	@Override
	public FilmDirector findByFIO(String fio) throws FilmStoreDAOException {
		List<FilmDirector> lisFilmDirector = findFilmDirByCriteria(fio, FindFilmDirectorCriteria.FIND_BY_FIO);
		if(lisFilmDirector.isEmpty()){
			return null;
		}
		return lisFilmDirector.get(0);
	}
	
	@Override
	public FilmDirector find(Short id) throws FilmStoreDAOException {
		List<FilmDirector> lisFilmDirector = findFilmDirByCriteria(id, FindFilmDirectorCriteria.FIND_BY_ID);
		if(lisFilmDirector.isEmpty()){
			return null;
		}
		return lisFilmDirector.get(0);
	}

	@Override
	public List<FilmDirector> findAll() throws FilmStoreDAOException {
		return findFilmDirByCriteria(null, FindFilmDirectorCriteria.FIND_ALL);
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

			fillGeneratedIdIfInsert(commandDAO, prepStatement, (FilmDirector) parametr);
		} catch (SQLException | PoolConnectionException e) {
			logger.error("Error creating of PreparedStatement.Operation failed (" + commandDAO.name() + ")", e);
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
		switch (commandDAO) {
		case INSERT: {
			prepStatement = connection.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
			prepStatement.setString(1, ((FilmDirector) parametr).getFio());
		}
			break;
		case UPDATE: {
			prepStatement = connection.prepareStatement(SQL_UPDATE);
			prepStatement.setString(1, ((FilmDirector) parametr).getFio());
			prepStatement.setShort(1, ((FilmDirector) parametr).getId());
		}
			break;
		case DELETE: {
			prepStatement = connection.prepareStatement(SQL_DELETE);
			prepStatement.setShort(1, (Short) parametr);
		}
			break;
		}
		return prepStatement;
	}

	private void fillGeneratedIdIfInsert(CommandDAO commandDAO, PreparedStatement prepStatement, FilmDirector filmDirector)
			throws SQLException {
		if (commandDAO == CommandDAO.INSERT) {
			ResultSet resultset = prepStatement.getGeneratedKeys();
			if (resultset != null && resultset.next()) {
				filmDirector.setId(resultset.getShort(1));
			}
		}
	}
	
	private <T> List<FilmDirector> findFilmDirByCriteria(T parametr, FindFilmDirectorCriteria criteria) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<FilmDirector> listFilmDir = null;
		try {
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();

			prepStatement = createPrepStatementByFilmDirCriteria(connection, criteria, parametr);

			resultSet = prepStatement.executeQuery();

			listFilmDir = fillListFilmDirFromResultSet(resultSet);
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
		return listFilmDir;
	}
	
	private List<FilmDirector> fillListFilmDirFromResultSet(ResultSet resultSet) throws SQLException{
		List<FilmDirector> listFilmDirector = new ArrayList<FilmDirector>();
		short id = 0;
		String fio = null;
		while (resultSet.next()) {
		 	id = resultSet.getShort(DatabaseColumnName.TABLE_FILM_DIRECTOR_FD_UID);
			fio = resultSet.getString(DatabaseColumnName.TABLE_FILM_DIRECTOR_FD_FIO);
			listFilmDirector.add(new FilmDirector(id,fio));
		}
		return listFilmDirector;
	}

	private <T> PreparedStatement createPrepStatementByFilmDirCriteria(Connection connection, FindFilmDirectorCriteria criteria,
			T parametr) throws SQLException {
		PreparedStatement prepStatement = null;
		switch (criteria) {
			case FIND_BY_ID: {
				prepStatement = connection.prepareStatement(SQL_FIND_BY_ID);
				prepStatement.setShort(1, (Short) parametr);
			}
				break;
			case FIND_ALL: {
				prepStatement = connection.prepareStatement(SQL_FIND_ALL);
			}
				break;
			case FIND_BY_FIO: {
				prepStatement = connection.prepareStatement(SQL_FIND_BY_FIO);
				prepStatement.setString(1, (String) parametr);
			}
				break;
		}
		return prepStatement;
	}
	
	private enum FindFilmDirectorCriteria {
		FIND_BY_ID, FIND_BY_FIO, FIND_ALL
	}
}
