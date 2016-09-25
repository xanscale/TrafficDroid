package it.localhost.trafficdroid.dto;

public class PatenteDTO extends BaseDTO {
	private final String numeoPatente;
	private final String saldo;
	private final String scadenzaPatente;

	public PatenteDTO(String numeoPatente, String saldo, String scadenzaPatente) {
		super();
		this.numeoPatente = numeoPatente;
		this.saldo = saldo;
		this.scadenzaPatente = scadenzaPatente;
	}

	public String getNumeoPatente() {
		return numeoPatente;
	}

	public String getSaldo() {
		return saldo;
	}

	public String getScadenzaPatente() {
		return scadenzaPatente;
	}
}
