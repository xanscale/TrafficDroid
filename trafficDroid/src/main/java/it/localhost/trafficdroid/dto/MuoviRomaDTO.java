package it.localhost.trafficdroid.dto;

import java.util.ArrayList;

public class MuoviRomaDTO extends BaseDTO {
	private ArrayList<String[]> ztl;

	public MuoviRomaDTO(boolean success, ArrayList<String[]> ztl) {
		super(success);
		this.ztl = ztl;
	}

	public ArrayList<String[]> getZtl() {
		return ztl;
	}
}