package by.training.filmstore.dao;

import by.training.filmstore.dao.impl.FilmStoreDAOFactoryImpl;

public abstract class FilmStoreDAOFactory {
	private static final FilmStoreDAOFactoryImpl filmStoreDAOFactoryImpl 
										= new FilmStoreDAOFactoryImpl();
	public abstract UserDAO getUserDao();
	public abstract FilmDAO getFilmDAO();
	public static FilmStoreDAOFactory getDAOFactory(){
		return filmStoreDAOFactoryImpl;
	}
}
