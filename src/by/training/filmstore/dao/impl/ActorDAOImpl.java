package by.training.filmstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.filmstore.dao.ActorDAO;
import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.dao.pool.PoolConnection;
import by.training.filmstore.dao.pool.PoolConnectionException;
import by.training.filmstore.entity.Actor;

public class ActorDAOImpl implements ActorDAO {

	private static final Logger logger = LogManager.getLogger(OrderDAOImpl.class);

	private static final String SQL_INSERT = 
			"insert into actor(actor.act_fio) VALUES(?)";
	private static final String SQL_DELETE = 
			"delete from actor where actor.act_uid = ?";
	private static final String SQL_UPDATE = 
			"update actor set  actor.act_fio = ? where actor.act_uid = ?";
	private static final String SQL_FIND_BY_ID = 
			"select actor.act_uid,actor.act_fio from actor where actor.act_uid = ?";
	private static final String SQL_FIND_BY_FIO = 
			"select actor.act_uid,actor.act_fio from actor where actor.act_fio = ?";
	private static final String SQL_FIND_ALL = 
			"select actor.act_uid,actor.act_fio from actor";

	@Override
	public boolean create(Actor entity) throws FilmStoreDAOException {
		return updateByCriteria(CommandDAO.INSERT, entity);
	}

	@Override
	public boolean update(Actor entity) throws FilmStoreDAOException {
		return updateByCriteria(CommandDAO.UPDATE, entity);
	}

	@Override
	public boolean delete(Short id) throws FilmStoreDAOException {
		return updateByCriteria(CommandDAO.DELETE, id);
	}

	@Override
	public Actor findByFIO(String fio) throws FilmStoreDAOException {
		List<Actor> listActor = findActorByCriteria(fio, FindActorCriteria.FIND_BY_FIO);
		if(listActor.isEmpty()){
			return null;
		}
		return listActor.get(0);
	}

	@Override
	public Actor find(Short id) throws FilmStoreDAOException {
		List<Actor> listActor = findActorByCriteria(id, FindActorCriteria.FIND_BY_ID);
		if(listActor.isEmpty()){
			return null;
		}
		return listActor.get(0);
	}

	@Override
	public List<Actor> findAll() throws FilmStoreDAOException {
		return findActorByCriteria(null, FindActorCriteria.FIND_ALL);
	}

	private void fillGeneratedIdIfInsert(CommandDAO commandDAO, PreparedStatement prepStatement, Actor actor)
			throws SQLException {
		if (commandDAO == CommandDAO.INSERT) {
			ResultSet resultset = prepStatement.getGeneratedKeys();
			if (resultset != null && resultset.next()) {
				actor.setId(resultset.getShort(1));
			}
		}
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

			fillGeneratedIdIfInsert(commandDAO, prepStatement, (Actor) parametr);
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
			prepStatement.setString(1, ((Actor) parametr).getFio());
		}
			break;
		case UPDATE: {
			prepStatement = connection.prepareStatement(SQL_UPDATE);
			prepStatement.setString(1, ((Actor) parametr).getFio());
			prepStatement.setShort(1, ((Actor) parametr).getId());
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

	private <T> List<Actor> findActorByCriteria(T parametr, FindActorCriteria criteria) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<Actor> listActor = null;
		try {
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();

			prepStatement = createPrepStatementByActorCriteria(connection, criteria, parametr);

			resultSet = prepStatement.executeQuery();

			listActor = fillListActorFromResultSet(resultSet);
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
		return listActor;
	}
	
	private <T> PreparedStatement createPrepStatementByActorCriteria(Connection connection, FindActorCriteria criteria,
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

	private List<Actor> fillListActorFromResultSet(ResultSet resultSet) throws SQLException{
		List<Actor> listActor = new ArrayList<Actor>();
		short id = 0;
		String fio = null;
		while (resultSet.next()) {
		 	id = resultSet.getShort(DatabaseColumnName.TABLE_ACTOR_ACT_UID);
			fio = resultSet.getString(DatabaseColumnName.TABLE_ACTOR_ACT_FIO);
			listActor.add(new Actor(id,fio));
		}
		return listActor;
	}
	
	private enum FindActorCriteria {
		FIND_BY_ID, FIND_BY_FIO, FIND_ALL
	}
}
