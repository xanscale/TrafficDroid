package it.localhost.trafficdroid.dto;

public class BolloDTO extends BaseDTO {
	private String bollo;

	public BolloDTO(boolean success, String bollo) {
		super(success);
		this.bollo = bollo;
	}

	public String getBollo() {
		return bollo;
	}
}
