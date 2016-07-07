package by.training.filmstore.dao.exception;

public class FilmStoreDAOException extends Exception{

	private static final long serialVersionUID = 8970307759079098839L;


	public FilmStoreDAOException(String message) {
        super(message);
    }

    public FilmStoreDAOException(Exception exception) {
        super(exception);
    }


    public FilmStoreDAOException(String message, Exception exception) {
        super(message, exception);
    }
}
