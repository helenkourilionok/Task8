package by.training.filmstore.dao.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.entity.Film;
import by.training.filmstore.entity.Quality;

public class FilmDAOTest {
	private static FilmDAOImpl filmDAOImpl;
	private static Film film;
	
	@BeforeClass
	public static void initFilmDAO()
	{
		filmDAOImpl = new FilmDAOImpl();
		film = new Film();
		film.setName("filmTest");
		film.setCountFilms((short)7);
		film.setGenre("Комедия");
		film.setCountry("США,Россия");
		film.setYearOfRelease((short)2015);
		film.setQuality(Quality.WEBDLRip);
		film.setDescription("description");
		//film.setFilmDirector((short)1);
		film.setPrice(new BigDecimal(155000));
		File file = new File("‪D://MyFolder//imageDB//ludiX.jpg");
		byte[] image = new byte[(int)file.length()];
		try (FileInputStream fin=new FileInputStream("D://MyFolder//imageDB//ludiX.jpg")){
			fin.read(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		film.setImage(image);
	}
	
	@Test
	public void crudFilm() throws FilmStoreDAOException
	{
		boolean actual = filmDAOImpl.create(film);
		film.setCountFilms((short)5);
		boolean actual2 = filmDAOImpl.update(film);
		boolean actual3 = filmDAOImpl.delete(film.getId());
		boolean expected = true;
		assertEquals("Error creating film",expected, actual);
		assertEquals("Error updating film",expected, actual2);
		assertEquals("Can't delete film",expected, actual3);
	}
	
	@Test
	public void findFilmById() throws FilmStoreDAOException
	{
		short id  = 6;
		short nid = -1;
		Film film = filmDAOImpl.find(id);
		Film nfilm = filmDAOImpl.find((short)nid);
		assertNotNull("Film wasn't found",film);
		assertNull("Film with that id doesn't exist",nfilm);
	}
	
	@Test
	public void findAllFilmsTest() throws FilmStoreDAOException
	{
		List<Film> listFilms = filmDAOImpl.findAll();
		assertNotNull("Operation failed. No film was found",listFilms);
		assertEquals("Operation failed. No film was found",false,listFilms.isEmpty());
	}
	
	@Test
	public void findFilmByName() throws FilmStoreDAOException
	{
		List<Film> listFilms = filmDAOImpl.findFilmByName("Соседи. На тропе войны 2");
		assertNotNull("Operation failed. No user was found",listFilms);
		assertEquals("Operation failed. No user was found",false,listFilms.isEmpty());
	}
	
	@Test
	public void findFilmByGenre() throws FilmStoreDAOException
	{
		List<Film> listFilms = filmDAOImpl.findFilmByGenre("Комедия");
		assertNotNull("Operation failed. No user was found",listFilms);
		assertEquals("Operation failed. No user was found",false,listFilms.isEmpty());
	}
}
