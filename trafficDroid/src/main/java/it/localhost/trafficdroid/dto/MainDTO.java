package it.localhost.trafficdroid.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

public class MainDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private LinkedHashMap<Integer, StreetDTO> streets;
	private Date trafficTime;
	private ArrayList<String> congestedZones;
	private byte congestionThreshold;
	private int versionCode;

	public MainDTO() {
		streets = new LinkedHashMap<Integer, StreetDTO>();
		congestedZones = new ArrayList<String>();
	}

	public Date getTrafficTime() {
		return trafficTime;
	}

	public void setTrafficTime(Date trafficTime) {
		this.trafficTime = trafficTime;
	}

	public void putStreet(StreetDTO street) {
		streets.put(street.getId(), street);
	}

	public ArrayList<StreetDTO> getStreets() {
		return new ArrayList<StreetDTO>(streets.values());
	}

	public StreetDTO getStreet(int key) {
		return streets.get(key);
	}

	public ArrayList<String> getCongestedZones() {
		return congestedZones;
	}

	public void addCongestedZone(String zone) {
		congestedZones.add(zone);
	}

	public byte getCongestionThreshold() {
		return congestionThreshold;
	}

	public void setCongestionThreshold(byte congestionThreshold) {
		this.congestionThreshold = congestionThreshold;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
}