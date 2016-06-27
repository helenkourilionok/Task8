package by.training.filmstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.filmstore.dao.CommentDAO;
import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.dao.pool.PoolConnection;
import by.training.filmstore.dao.pool.PoolConnectionException;
import by.training.filmstore.entity.Comment;
import by.training.filmstore.entity.CommentPK;

public class CommentDAOImpl implements CommentDAO{


	private static final Logger logger = LogManager.getLogger(CommentDAOImpl.class);
	private static final String SQL_INSERT = 
			"insert into comment(comment.cm_email_user,comment.cm_film,"+
			 "comment.cm_content,comment.cm_date) values(?,?,?,?)";
	private static final String SQL_UPDATE = 
			"update comment set comment.cm_content = ? "+
			" where comment.cm_film = ? and comment.cm_email_user = ? ";
	private static final String SQL_DELETE = 
			"delete from comment where comment.cm_email_user = ? and comment.cm_film = ?";
	private static final String SQL_FIND_ALL = 
			"select comment.cm_email_user,comment.cm_film,comment.cm_content,comment.cm_date "+ 
			"from comment";
	private static final String SQL_FIND_BY_ID = 
			"select comment.cm_email_user,comment.cm_film,comment.cm_content,comment.cm_date "+
			"from comment where comment.cm_film = ? and comment.cm_email_user = ?";
	private static final String SQL_FIND_BY_EMAIL_USER = 
			"select comment.cm_email_user,comment.cm_film,comment.cm_content,comment.cm_date "+
			"from comment where comment.cm_email_user = ?";
	private static final String SQL_FIND_BY_FILM_ID = 
			"select comment.cm_email_user,comment.cm_film,comment.cm_content,comment.cm_date "+ 
			"from comment where comment.cm_film = ?";
	
	private static final String TABLE_COMMENT_EMAIL_USER = "cm_email_user";
	private static final String TABLE_COMMENT_FILM_ID = "cm_film";
	private static final String TABLE_COMMENT_CONTENT = "cm_content";
	private static final String TABLE_COMMENT_DATE = "cm_date";
	
	
	@Override
 	public Comment find(CommentPK id) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		Comment comment = null;
		try
		{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement =  connection.prepareStatement(SQL_FIND_BY_ID);
			prepStatement.setString(1, id.getUserEmail());
			prepStatement.setShort(2, id.getFilmId());
			resultSet = prepStatement.executeQuery();
			comment = new Comment();
			if(resultSet.next())
			{
				fillComment(comment, resultSet);
			}
		}
		catch(PoolConnectionException|SQLException e)
		{
			logger.error("Error creating of PreparedStatement.Can't find comment",e);
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
		return comment;
	}

	@Override
	public List<Comment> findAll() throws FilmStoreDAOException {
		return findCommentByCriteria(null, FindCommentCriteria.FIND_ALL );
	}

	@Override
	public boolean create(Comment entity) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		boolean success = false;
		try{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_INSERT);
			prepStatement.setString(1, entity.getId().getUserEmail());
			prepStatement.setShort(2, entity.getId().getFilmId());
			prepStatement.setString(3, entity.getContent());
			prepStatement.setTimestamp(4, entity.getDate());
			int affectedRows = prepStatement.executeUpdate();
			if (affectedRows != 0) {
                success = true;
            }
		}
		catch(PoolConnectionException|SQLException e){
			logger.error("Error creating of PreparedStatement.Can't create comment",e);
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
	public boolean update(Comment entity) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		boolean success = false;
		try{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_UPDATE);
			prepStatement.setString(1, entity.getId().getUserEmail());
			prepStatement.setShort(2, entity.getId().getFilmId());
			prepStatement.setString(3, entity.getContent());
			int affectedRows = prepStatement.executeUpdate();
            if (affectedRows != 0) {
                success = true;
            }
		}
		catch(PoolConnectionException|SQLException e){
			logger.error("Error creating of PreparedStatement.Can't update comment fields");
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
	public boolean delete(CommentPK id) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		boolean success = false;
		try{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_DELETE);
			prepStatement.setString(1, id.getUserEmail());
			prepStatement.setShort(2, id.getFilmId());
			int affectedRows = prepStatement.executeUpdate();
            if (affectedRows != 0) {
                success = true;
            }
		}
		catch(PoolConnectionException|SQLException e){
			logger.error("Error creating of PreparedStatement.Can't delete comment",e);
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
	public List<Comment> findCommentByIdFilm(short filmId) throws FilmStoreDAOException {
		return findCommentByCriteria(new Short(filmId), FindCommentCriteria.FIND_BY_FILM_ID);
	}

	@Override
	public List<Comment> findCommentByIdUser(String userEmail) throws FilmStoreDAOException {
		return findCommentByCriteria(userEmail, FindCommentCriteria.FIND_BY_USER_ID);
	}

	private <T> List<Comment> findCommentByCriteria(T parametr,FindCommentCriteria criteria ) throws FilmStoreDAOException{
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		Comment comment = null;
		List<Comment> listComment = new ArrayList<Comment>();
		try {
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			switch(criteria)
			{
				case FIND_BY_FILM_ID:{
					prepStatement = connection.prepareStatement(SQL_FIND_BY_FILM_ID);
					prepStatement.setShort(1,(Short)parametr);
				}break;
				case FIND_BY_USER_ID:{
					prepStatement = connection.prepareStatement(SQL_FIND_BY_EMAIL_USER);
					prepStatement.setString(1,(String)parametr);
				}break;
				case FIND_ALL:{
					prepStatement = connection.prepareStatement(SQL_FIND_ALL);
				}break;
			}
			resultSet = prepStatement.executeQuery();
			while(resultSet.next()){
				comment = new Comment();
				fillComment(comment, resultSet);
				listComment.add(comment);
			}
		}
		catch(PoolConnectionException|SQLException e){
			logger.error("Error creating of PreparedStatement.Can't find comment("+criteria.name()+")",e);
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
		return listComment;
	}
	
	private void fillComment(Comment comment,ResultSet resultSet) throws SQLException{
		CommentPK commentPK = new CommentPK();
		commentPK.setUserEmail(resultSet.getString(TABLE_COMMENT_EMAIL_USER));
		commentPK.setFilmId(resultSet.getShort(TABLE_COMMENT_FILM_ID));
		comment.setId(commentPK);
		comment.setContent(resultSet.getString(TABLE_COMMENT_CONTENT));
		comment.setDate(resultSet.getTimestamp(TABLE_COMMENT_DATE));
	}
	
}
