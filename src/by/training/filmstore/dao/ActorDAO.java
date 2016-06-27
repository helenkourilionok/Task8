package by.training.filmstore.dao;

import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.entity.Actor;

public interface ActorDAO extends AbstractDAO<Actor, Short> {
	Actor findByFIO(String fio) throws FilmStoreDAOException;
}
