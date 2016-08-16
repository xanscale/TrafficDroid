package it.localhost.trafficdroid.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class StreetDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String tag;
	private int id;
	private LinkedHashMap<Integer, ZoneDTO> zones;
	private ArrayList<BadNewsDTO> badNews;
	private int[] directions;
	private ArrayList<Integer> allZonesId;
	private short[] speed;
	private int[] trend;

	public StreetDTO(int id, int[] allZonesId) {
		this.id = id;
		this.allZonesId = new ArrayList<Integer>();
		zones = new LinkedHashMap<Integer, ZoneDTO>();
		badNews = new ArrayList<BadNewsDTO>();
		directions = new int[2];
		speed = new short[2];
		trend = new int[2];
		for (int i : allZonesId)
			this.allZonesId.add(i);
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public ArrayList<ZoneDTO> getZones() {
		return new ArrayList<ZoneDTO>(zones.values());
	}

	public ZoneDTO getZone(int key) {
		return zones.get(key);
	}

	public int getZonesSize() {
		return zones.size();
	}

	public void addBadNews(BadNewsDTO event) {
		badNews.add(event);
	}

	public void putZone(ZoneDTO zone) {
		zones.put(zone.getId(), zone);
	}

	public int getDirectionLeft() {
		return directions[0];
	}

	public int getDirectionRight() {
		return directions[1];
	}

	public void setDirectionLeft(int direction) {
		this.directions[0] = direction;
	}

	public void setDirectionRight(int direction) {
		this.directions[1] = direction;
	}

	public ArrayList<BadNewsDTO> getBadNews() {
		return badNews;
	}

	public ArrayList<Integer> getAllZonesId() {
		return allZonesId;
	}

	public short getSpeedLeft() {
		return speed[0];
	}

	public void setSpeedLeft(Short speedLeft) {
		this.speed[0] = speedLeft;
	}

	public short getSpeedRight() {
		return speed[1];
	}

	public void setSpeedRight(short speedRight) {
		this.speed[1] = speedRight;
	}

	public void addSpeedLeft(Short speedLeft) {
		speed[0] += speedLeft;
	}

	public void addSpeedRight(short speedRight) {
		speed[1] += speedRight;
	}

	public int getTrendLeft() {
		return trend[0];
	}

	public void setTrendLeft(int trendLeft) {
		this.trend[0] = trendLeft;
	}

	public int getTrendRight() {
		return trend[1];
	}

	public void setTrendRight(int trendRight) {
		this.trend[1] = trendRight;
	}

	@Override
	public String toString() {
		return getName();
	}
}