package it.localhost.trafficdroid.dto;

public class PatenteDTO extends BaseDTO {
	private String numeoPatente;
	private String saldo;
	private String scadenzaPatente;

	public PatenteDTO(boolean success, String numeoPatente, String saldo, String scadenzaPatente) {
		super(success);
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
