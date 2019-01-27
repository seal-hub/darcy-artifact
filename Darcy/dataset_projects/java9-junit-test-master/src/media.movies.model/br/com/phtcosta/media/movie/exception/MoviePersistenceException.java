package br.com.phtcosta.media.movie.exception;

public class MoviePersistenceException extends Exception {
	private static final long serialVersionUID = -6379562401366954442L;

	public MoviePersistenceException() {
		super();
	}

	public MoviePersistenceException(String msg, Throwable t) {
		super(msg, t);
	}

	public MoviePersistenceException(String msg) {
		super(msg);
	}

	public MoviePersistenceException(Throwable t) {
		super(t);
	}

}
