package it.localhost.trafficdroid.exception;

public class BadConfException extends Exception {
	private static final long serialVersionUID = 1L;

	public BadConfException(String detailMessage) {
		super(detailMessage);
	}
}
