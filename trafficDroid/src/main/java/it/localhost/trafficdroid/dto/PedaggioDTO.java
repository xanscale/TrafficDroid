package it.localhost.trafficdroid.dto;

public class PedaggioDTO extends BaseDTO {
	private final Double pedaggio;

	public PedaggioDTO(Double pedaggio) {
		super();
		this.pedaggio = pedaggio;
	}

	public Double getPedaggio() {
		return pedaggio;
	}
}
