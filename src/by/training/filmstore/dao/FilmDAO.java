package by.training.filmstore.dao;

import java.util.List;

import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.entity.Film;

public interface FilmDAO extends AbstractDAO<Film, Short>{
	List<Film> findFilmByGenre(String genre) throws FilmStoreDAOException;
	List<Film> findFilmByName(String name) throws FilmStoreDAOException;
	boolean createFilmActor(Film entity) throws FilmStoreDAOException;
	boolean deleteFilmActor(Short filmId) throws FilmStoreDAOException;
}
