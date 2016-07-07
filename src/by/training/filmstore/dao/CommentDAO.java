package by.training.filmstore.dao;

import java.util.List;

import by.training.filmstore.dao.exception.FilmStoreDAOException;
import by.training.filmstore.entity.Comment;
import by.training.filmstore.entity.CommentPK;

public interface CommentDAO extends AbstractDAO<Comment, CommentPK>{
	List<Comment> findCommentByIdFilm(short filmId) throws FilmStoreDAOException;
	List<Comment> findCommentByIdUser(String userEmail) throws FilmStoreDAOException;
}
