package by.training.filmstore.dao.impl;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.filmstore.dao.FilmDAO;
import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.dao.pool.PoolConnection;
import by.training.filmstore.dao.pool.PoolConnectionException;
import by.training.filmstore.entity.Film;
import by.training.filmstore.entity.Quality;

public class FilmDAOImpl implements FilmDAO {

	private static final Logger logger = LogManager.getLogger(FilmDAOImpl.class);
	private static final String SQL_INSERT = 
			"insert into film(film.fm_name,film.fm_genre,film.fm_country,film.fm_year_of_release,"+
			"film.fm_quality,film.fm_film_director,film.fm_description,film.fm_price,"+
			"film.fm_count_film,film.fm_image) "+
			"VALUES(?,?,?,?,?,?,?,?,?,?)";
	private static final String SQL_UPDATE = 
			"update film  SET film.fm_price = ?,film.fm_count_film = ? where film.fm_uid = ?";
	private static final String SQL_DELETE = "DELETE FROM film WHERE film.fm_uid = ?";
	private static final String SQL_FIND_ALL = 
			"select film.fm_uid,film.fm_name,film.fm_genre,film.fm_country,film.fm_year_of_release,"+
			"film.fm_quality,film.fm_film_director,film.fm_description,film.fm_price,"+
			"film.fm_count_film,film.fm_image from film";
	private static final String SQL_FIND_FILM_BY_ID = 
			"SELECT film.fm_uid,film.fm_name,film.fm_genre,film.fm_country,film.fm_year_of_release,"+
			"film.fm_quality,film.fm_film_director,film.fm_description,"+
	        "film.fm_price,film.fm_count_film,film.fm_image "+
        	"FROM film where film.fm_uid = ?";
	private static final String SQL_FIND_BY_GENRE = 
			"select film.fm_uid,film.fm_name,film.fm_genre,film.fm_country,film.fm_year_of_release,"+
			"film.fm_quality,film.fm_film_director,film.fm_description,film.fm_price,"+
			"film.fm_count_film,film.fm_image from film where find_in_set(?,film.fm_genre)"; 
	private static final String SQL_FIND_BY_NAME = 
			"select film.fm_uid,film.fm_name,film.fm_genre,film.fm_country,film.fm_year_of_release,"+
			"film.fm_quality,film.fm_film_director,film.fm_description,film.fm_price,"+
			"film.fm_count_film,film.fm_image from film where film.fm_name = ?";

	@Override
	public Film find(Short id) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		Film film = null;
		try
		{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement =  connection.prepareStatement(SQL_FIND_FILM_BY_ID);
			prepStatement.setShort(1, id);
			resultSet = prepStatement.executeQuery();
			if(resultSet.next())
			{
				film = new Film();
				fillFilm(film,resultSet);
			}
		}
		catch(PoolConnectionException|SQLException e)
		{
			logger.error("Error creating of PreparedStatement.Can't find film",e);
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
		return film;
	}

	@Override
	public List<Film> findAll() throws FilmStoreDAOException {
		return findFilmByCriteria(null, FindFilmCriteria.FIND_ALL);
	}

	@Override
	public List<Film> findFilmByGenre(String genre) throws FilmStoreDAOException {
		return findFilmByCriteria(genre, FindFilmCriteria.FIND_BY_GENGE);
	}

	@Override
	public List<Film> findFilmByName(String name) throws FilmStoreDAOException {
		return findFilmByCriteria(name, FindFilmCriteria.FIND_BY_NAME);
	}

	
	@Override
	public boolean create(Film entity) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		boolean success = false;
		try{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_INSERT,PreparedStatement.RETURN_GENERATED_KEYS);
			prepStatement.setString(1, entity.getName());
			prepStatement.setString(2, entity.getGenre());
			prepStatement.setString(3, entity.getCountry());
			prepStatement.setInt(4, entity.getYearOfRelease());
			prepStatement.setString(5, entity.getQuality().getNameQuality());
			prepStatement.setShort(6, entity.getFilmDirector());
			prepStatement.setString(7, entity.getDescription());
			prepStatement.setBigDecimal(8, entity.getPrice());
			prepStatement.setShort(9, entity.getCountFilms());
			prepStatement.setBytes(10,entity.getImage());
			int affectedRows = prepStatement.executeUpdate();
			ResultSet resultset = prepStatement.getGeneratedKeys();
			if(resultset!=null && resultset.next()){
				entity.setId(resultset.getShort(1));
			}
			if (affectedRows != 0) {
                success = true;
            }
		}
		catch(PoolConnectionException|SQLException e){
			logger.error("Error creating of PreparedStatement.Can't create film",e);
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
	public boolean update(Film entity) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		boolean success = false;
		try{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_UPDATE);
			prepStatement.setBigDecimal(1, entity.getPrice());
			prepStatement.setShort(2, entity.getCountFilms());
			prepStatement.setShort(3, entity.getId());
			int affectedRows = prepStatement.executeUpdate();
            if (affectedRows != 0) {
                success = true;
            }
		}
		catch(PoolConnectionException|SQLException e){
			logger.error("Error creating of PreparedStatement.Can't update film fields",e);
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
	public boolean delete(Short id) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		boolean success = false;
		try{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			prepStatement = connection.prepareStatement(SQL_DELETE);
			prepStatement.setShort(1,id);
			int affectedRows = prepStatement.executeUpdate();
            if (affectedRows != 0) {
                success = true;
            }
		}
		catch(PoolConnectionException|SQLException e){
			logger.error("Error creating of PreparedStatement.Can't delete film",e);
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
	
	private void fillFilm(Film film,ResultSet resultSet) throws SQLException 
	{
		film.setId(resultSet.getShort(DatabaseColumnName.TABLE_FILM_UID));
		film.setName(resultSet.getString(DatabaseColumnName.TABLE_FILM_NAME));
		film.setGenre(resultSet.getString(DatabaseColumnName.TABLE_FILM_GENRE));
		film.setCountry(resultSet.getString(DatabaseColumnName.TABLE_FILM_COUNTRY));
		film.setYearOfRelease(resultSet.getShort(DatabaseColumnName.TABLE_FILM_YEAR_OF_RELEASE));
		Quality quality = Quality.valueOf(resultSet.getString(DatabaseColumnName.TABLE_FILM_QUALITY).replaceFirst("-", ""));
		film.setQuality(quality);
		film.setFilmDirector(resultSet.getShort(DatabaseColumnName.TABLE_FILM_FILM_DIRECTOR));
		film.setDescription(resultSet.getString(DatabaseColumnName.TABLE_FILM_DESCRIPTION));
		film.setPrice(resultSet.getBigDecimal(DatabaseColumnName.TABLE_FILM_PRICE));
		film.setCountFilms(resultSet.getShort(DatabaseColumnName.TABLE_FILM_COUNT_FILM));
		Blob image = resultSet.getBlob(DatabaseColumnName.TABLE_FILM_IMAGE);
		film.setImage(image.getBytes(1, (int)image.length()));
	}


	private List<Film> findFilmByCriteria(String parametr, FindFilmCriteria criteria) throws FilmStoreDAOException {
		PoolConnection poolConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		Film film = null;
		List<Film> listFilm = new ArrayList<Film>();
		try{
			poolConnection = PoolConnection.getInstance();
			connection = poolConnection.takeConnection();
			switch(criteria)
			{
				case FIND_BY_GENGE:{
					prepStatement = connection.prepareStatement(SQL_FIND_BY_GENRE);
					prepStatement.setString(1, parametr);
				}break;
				case FIND_BY_NAME:{
					prepStatement = connection.prepareStatement(SQL_FIND_BY_NAME);
					prepStatement.setString(1, parametr);
				}break;
				case FIND_ALL:{
					prepStatement = connection.prepareStatement(SQL_FIND_ALL);
				}break;
			}
			resultSet = prepStatement.executeQuery();
			while(resultSet.next()){
				film = new Film();
				fillFilm(film, resultSet);
				listFilm.add(film);
			}
		}
		catch(PoolConnectionException|SQLException e){
			logger.error("Error creating of PreparedStatement.Can't find film("+criteria.name()+")",e);
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
		return listFilm;
	
	}

}