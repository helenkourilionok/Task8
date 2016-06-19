package by.training.filmstore.dao;

import java.util.List;

import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.entity.GoodOfOrder;
import by.training.filmstore.entity.GoodOfOrderPK;

public interface GoodOfOrderDAO extends AbstractDAO<GoodOfOrder, GoodOfOrderPK> {
	List<GoodOfOrder> findGoodByOrderId(int id) throws FilmStoreDAOException;
	List<GoodOfOrder> findGoodByFilmId(short id) throws FilmStoreDAOException;
}
