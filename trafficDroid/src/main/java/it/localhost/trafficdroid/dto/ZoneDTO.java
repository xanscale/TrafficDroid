package it.localhost.trafficdroid.dto;

import java.io.Serializable;

public class ZoneDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private boolean[] autovelox = { false, false };
	private String name;
	private String km;
	private String webcam;
	private short[] speed;
	private byte[] cat;
	private int[] trend;

	public ZoneDTO(int id, String name, String webcam) {
		this.id = id;
		this.name = name;
		this.webcam = webcam;
		speed = new short[2];
		cat = new byte[2];
		trend = new int[2];
	}

	public void setAutoveloxLeft() {
		autovelox[0] = true;
	}

	public void setAutoveloxRight() {
		autovelox[1] = true;
	}

	public boolean isAutoveloxLeft() {
		return autovelox[0];
	}

	public boolean isAutoveloxRight() {
		return autovelox[1];
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
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

	public byte getCatLeft() {
		return cat[0];
	}

	public void setCatLeft(byte catLeft) {
		this.cat[0] = catLeft;
	}

	public byte getCatRight() {
		return cat[1];
	}

	public void setCatRight(byte catRight) {
		this.cat[1] = catRight;
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

	public String getKm() {
		return km;
	}

	public void setKm(String km) {
		this.km = km;
	}

	public String getWebcam() {
		return webcam;
	}
}