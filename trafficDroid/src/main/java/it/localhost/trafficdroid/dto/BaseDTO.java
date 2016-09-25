package it.localhost.trafficdroid.dto;

public class BaseDTO {
	private final boolean success;
	private String message;

	BaseDTO() {
		this.success = true;
	}

	public BaseDTO(String message) {
		this.success = false;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}
}
