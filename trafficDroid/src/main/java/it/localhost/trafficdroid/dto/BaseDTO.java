package it.localhost.trafficdroid.dto;

public class BaseDTO {
	private boolean success;
	private String message;

	protected BaseDTO(boolean success) {
		this.success = success;
	}

	public BaseDTO(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}
}
