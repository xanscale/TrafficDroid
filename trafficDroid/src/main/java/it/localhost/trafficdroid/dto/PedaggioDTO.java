package it.localhost.trafficdroid.dto;

public class PedaggioDTO extends BaseDTO {
	private String pedaggio;

	public PedaggioDTO(boolean success, String pedaggio) {
		super(success);
		this.pedaggio = pedaggio;
	}

	public String getPedaggio() {
		return pedaggio;
	}
}
