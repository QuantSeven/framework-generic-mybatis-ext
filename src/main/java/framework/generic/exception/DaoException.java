package framework.generic.exception;

public class DaoException extends RuntimeException {

	private static final long serialVersionUID = 4271448665763790692L;

	public DaoException() {
	}

	public DaoException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public DaoException(String msg) {
		super(msg);
	}

	public DaoException(Throwable throwable) {
		super(throwable);
	}
}