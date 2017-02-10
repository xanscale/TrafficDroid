package it.localhost.trafficdroid.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilderFactory;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.activity.MainActivity;
import it.localhost.trafficdroid.common.ListZoneResId;
import it.localhost.trafficdroid.common.ListZoneResName;
import it.localhost.trafficdroid.common.ListZoneResWebcam;
import it.localhost.trafficdroid.common.Utility;
import it.localhost.trafficdroid.dto.BadNewsDTO;
import it.localhost.trafficdroid.dto.MainDTO;
import it.localhost.trafficdroid.dto.StreetDTO;
import it.localhost.trafficdroid.dto.ZoneDTO;

public class TdService extends IntentService { // NO_UCD
	private static final String disconnectedMessage = "Connessione di rete inesistente";
	private static final int NOTIFICATION_ID = 1;
	private static final String path = "/engine/traffic_server.php";
	private static final String traffic = "https://etraffic.";
	private static final String N = "N";
	private static final String E = "E";
	private static final String S = "S";
	private static final String O = "O";
	private static final String I = "I";
	private static final String NEWLINE = "\n";
	private static final String SPACE = " ";
	private static final String DC_DATE = "dc:date";
	private static final String DESCRIPTION = "description";
	private static final String TITLE = "title";
	private static final String ITEM = "item";
	private static final String TEMPLATE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String URL = "http://www.cciss.it/portale/rss?rsstype=traffic";

	public TdService() {
		super(PersistanceService.tdData);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		sendBroadcast(new Intent(getString(R.string.BEGIN_UPDATE)));
		MainDTO currDTO = getMainDTO();
		MainDTO pastDTO;
		try {
			pastDTO = PersistanceService.retrieve(this);
		} catch (Exception e) {
			pastDTO = null;
		}
		NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
			try {
				parseTraffic(currDTO);
				configureDTO(currDTO, pastDTO);
				setupNotification(currDTO.getCongestedZones());
			} catch (Exception e) {
				Utility.setExCheck(this, true);
				Utility.setExMsg(this, e.getMessage());
			}
			try {
				if (Utility.isBadnewsEnabler(this))
					parseBadnews(currDTO);
			} catch (Exception e) {
				Utility.setExCheck(this, true);
				Utility.setExMsg(this, e.getMessage());
			}
			try {
				currDTO.setTrafficTime(new Date());
				PersistanceService.store(this, currDTO);
			} catch (Exception e) {
				Utility.setExCheck(this, true);
				Utility.setExMsg(this, e.getMessage());
			}
		} else {
			Utility.setExCheck(this, true);
			Utility.setExMsg(this, disconnectedMessage);
		}
		sendBroadcast(new Intent(getString(R.string.END_UPDATE)));
		WakefulBroadcastReceiver.completeWakefulIntent(intent);
	}

	private MainDTO getMainDTO() {
		MainDTO mainDto = new MainDTO();
		mainDto.setVersionCode(Utility.getVersionCode(this));
		mainDto.setCongestionThreshold(Byte.parseByte(Utility.getNotificationSpeed(this)));
		int[] streetsId = getResources().getIntArray(R.array.streetId);
		String[] streetsName = getResources().getStringArray(R.array.streetName);
		String[] streetsTag = getResources().getStringArray(R.array.streetTag);
		String[] streetsDir = getResources().getStringArray(R.array.streetDir);
		int[] autoveloxStreet = getResources().getIntArray(R.array.autoveloxStreet);
		int[] autoveloxFrom = getResources().getIntArray(R.array.autoveloxFrom);
		int[] autoveloxTo = getResources().getIntArray(R.array.autoveloxTo);
		boolean allStreets = Utility.isAllStreets(this);
		for (int i = 0; i < streetsId.length; i++) {
			int[] zonesId = getResources().getIntArray(ListZoneResId.getInstance().get((streetsId[i])));
			StreetDTO street = new StreetDTO(streetsId[i], zonesId);
			boolean streetEnabled = Utility.isEnabled(this, Integer.toString(street.getId()));
			String[] zonesWebcam = getResources().getStringArray(ListZoneResWebcam.getInstance().get((streetsId[i])));
			String[] zonesName = getResources().getStringArray(ListZoneResName.getInstance().get(streetsId[i]));
			for (int j = 0; j < zonesId.length; j++)
				if (allStreets || streetEnabled || Utility.isEnabled(this, Integer.toString(zonesId[j]))) {
					ZoneDTO zone = new ZoneDTO(zonesId[j], zonesName[j], zonesWebcam[j]);
					for (int k = 0; k < autoveloxStreet.length; k++) {
						if (autoveloxStreet[k] == streetsId[i] && zonesId[j] >= autoveloxFrom[k] && zonesId[j] < autoveloxTo[k])
							zone.setAutoveloxLeft();
						if (autoveloxStreet[k] == streetsId[i] && zonesId[j] >= autoveloxTo[k] && zonesId[j] < autoveloxFrom[k])
							zone.setAutoveloxRight();
					}
					street.putZone(zone);
				}
			if (street.getZonesSize() > 0) {
				street.setName(streetsName[i]);
				street.setTag(streetsTag[i]);
				switch (streetsDir[i]) {
					case N:
						street.setDirectionLeft(R.drawable.ic_north);
						street.setDirectionRight(R.drawable.ic_south);
						break;
					case E:
						street.setDirectionLeft(R.drawable.ic_east);
						street.setDirectionRight(R.drawable.ic_west);
						break;
					case S:
						street.setDirectionLeft(R.drawable.ic_south);
						street.setDirectionRight(R.drawable.ic_north);
						break;
					case O:
						street.setDirectionLeft(R.drawable.ic_west);
						street.setDirectionRight(R.drawable.ic_east);
						break;
					case I:
						street.setDirectionLeft(R.drawable.ic_rotate_right_white_48dp);
						street.setDirectionRight(R.drawable.ic_rotate_left_white_48dp);
						break;
				}
				mainDto.putStreet(street);
			}
		}
		return mainDto;
	}

	private void parseTraffic(MainDTO dto) {
		for (StreetDTO street : dto.getStreets())
			try {
				HttpsURLConnection connection = (HttpsURLConnection) new URL(traffic + Utility.getProviderTraffic(this) + path).openConnection();
				connection.setRequestMethod("POST");
				OutputStream os = connection.getOutputStream();
				os.write(("user=robAnd_ev3nt5.appU&pwd=daP-2012_00005,android.ev&sq=1&type=4&roa=" + street.getId()).getBytes("UTF-8"));
				os.close();
				NodeList segments = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(connection.getInputStream()).getDocumentElement().getLastChild().getFirstChild().getChildNodes();
				for (int i = 0; i < segments.getLength(); i++) {
					NodeList segChildrens = segments.item(i).getChildNodes();
					int from = Integer.parseInt(segChildrens.item(0).getTextContent());
					int to = Integer.parseInt(segChildrens.item(1).getTextContent());
					short speed = Short.parseShort(segChildrens.item(2).getTextContent());
					int fromIndex = street.getAllZonesId().indexOf(from);
					int toIndex = street.getAllZonesId().indexOf(to);
					if (fromIndex != -1 && toIndex != -1)
						if (fromIndex < toIndex) {
							for (int id : street.getAllZonesId().subList(fromIndex, toIndex))
								if (street.getZone(id) != null)
									street.getZone(id).setSpeedLeft(speed);
						} else
							for (int id : street.getAllZonesId().subList(toIndex, fromIndex))
								if (street.getZone(id) != null)
									street.getZone(id).setSpeedRight(speed);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	private void parseBadnews(MainDTO dto) throws Exception {
		SimpleDateFormat sdfBnParse = new SimpleDateFormat(TEMPLATE, Locale.ITALY);
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(URL);
		NodeList items = doc.getElementsByTagName(ITEM);
		for (int i = 0; i < items.getLength(); i++) {
			NodeList values = items.item(i).getChildNodes();
			String[] tris = new String[3];
			for (int j = 0; j < values.getLength(); j++) {
				if (values.item(j).getNodeName().equals(TITLE))
					tris[0] = values.item(j).getTextContent().split(SPACE)[0];
				else if (values.item(j).getNodeName().equals(DESCRIPTION))
					tris[1] = values.item(j).getTextContent();
				else if (values.item(j).getNodeName().equals(DC_DATE))
					tris[2] = values.item(j).getTextContent();
			}
			for (StreetDTO street : dto.getStreets())
				if (street.getTag().equals(tris[0])) {
					String[] desc = tris[1].split(NEWLINE);
					try {
						street.addBadNews(new BadNewsDTO(desc[0], desc[1] + desc[2], sdfBnParse.parse(tris[2])));
					} catch (Exception e) {
						street.addBadNews(new BadNewsDTO(desc[0], desc[1], new Date()));
					}
				}
		}
	}

	private void configureDTO(MainDTO currDTO, MainDTO pastDTO) {
		for (StreetDTO currStreet : currDTO.getStreets()) {
			byte availableLeft = 0, availableRight = 0;
			List<ZoneDTO> currZones = currStreet.getZones();
			for (ZoneDTO currZone : currZones) {
				if (currZone.getSpeedLeft() < 1)
					currZone.setCatLeft((byte) 0);
				else if (currZone.getSpeedLeft() < 11)
					currZone.setCatLeft((byte) 1);
				else if (currZone.getSpeedLeft() < 31)
					currZone.setCatLeft((byte) 2);
				else if (currZone.getSpeedLeft() < 51)
					currZone.setCatLeft((byte) 3);
				else if (currZone.getSpeedLeft() < 71)
					currZone.setCatLeft((byte) 4);
				else if (currZone.getSpeedLeft() < 91)
					currZone.setCatLeft((byte) 5);
				else
					currZone.setCatLeft((byte) 6);
				if (currZone.getSpeedRight() < 1)
					currZone.setCatRight((byte) 0);
				else if (currZone.getSpeedRight() < 11)
					currZone.setCatRight((byte) 1);
				else if (currZone.getSpeedRight() < 31)
					currZone.setCatRight((byte) 2);
				else if (currZone.getSpeedRight() < 51)
					currZone.setCatRight((byte) 3);
				else if (currZone.getSpeedRight() < 71)
					currZone.setCatRight((byte) 4);
				else if (currZone.getSpeedRight() < 91)
					currZone.setCatRight((byte) 5);
				else
					currZone.setCatRight((byte) 6);
				if (currZone.getCatLeft() > 0 && currZone.getCatLeft() <= currDTO.getCongestionThreshold() || currZone.getCatRight() > 0 && currZone.getCatRight() <= currDTO.getCongestionThreshold())
					currDTO.addCongestedZone(currZone.getName());
				if (pastDTO != null) {
					StreetDTO pastStreet = pastDTO.getStreet(currStreet.getId());
					if (pastStreet != null) {
						ZoneDTO pastZone = pastStreet.getZone(currZone.getId());
						if (pastZone != null) {
							int trendSpeed = Integer.parseInt(Utility.getTrendSpeed(this));
							if (currZone.getCatLeft() == 0 || pastZone.getCatLeft() == 0)
								currZone.setTrendLeft(0);
							else if (currZone.getSpeedLeft() - pastZone.getSpeedLeft() >= trendSpeed)
								currZone.setTrendLeft(R.drawable.ic_add_white_24dp);
							else if (pastZone.getSpeedLeft() - currZone.getSpeedLeft() >= trendSpeed)
								currZone.setTrendLeft(R.drawable.ic_remove_white_24dp);
							else
								currZone.setTrendLeft(0);
							if (currZone.getCatRight() == 0 || pastZone.getCatRight() == 0)
								currZone.setTrendRight(0);
							else if (currZone.getSpeedRight() - pastZone.getSpeedRight() >= trendSpeed)
								currZone.setTrendRight(R.drawable.ic_add_white_24dp);
							else if (pastZone.getSpeedRight() - currZone.getSpeedRight() >= trendSpeed)
								currZone.setTrendRight(R.drawable.ic_remove_white_24dp);
							else
								currZone.setTrendRight(0);
						}
					}
				}
				if (currZone.getCatLeft() != 0) {
					currStreet.addSpeedLeft(currZone.getSpeedLeft());
					availableLeft++;
				}
				if (currZone.getCatRight() != 0) {
					currStreet.addSpeedRight(currZone.getSpeedRight());
					availableRight++;
				}
			}
			if (availableLeft != 0)
				currStreet.setSpeedLeft((short) (currStreet.getSpeedLeft() / availableLeft));
			else
				currStreet.setSpeedLeft((short) 0);
			if (availableRight != 0)
				currStreet.setSpeedRight((short) (currStreet.getSpeedRight() / availableRight));
			else
				currStreet.setSpeedRight((short) 0);
			if (pastDTO != null) {
				StreetDTO pastStreet = pastDTO.getStreet(currStreet.getId());
				if (pastStreet != null) {
					int trendSpeed = Integer.parseInt(Utility.getTrendSpeed(this));
					if (currStreet.getSpeedLeft() == 0 || pastStreet.getSpeedLeft() == 0)
						currStreet.setTrendLeft(0);
					else if (currStreet.getSpeedLeft() - pastStreet.getSpeedLeft() >= trendSpeed)
						currStreet.setTrendLeft(R.drawable.ic_add_white_24dp);
					else if (pastStreet.getSpeedLeft() - currStreet.getSpeedLeft() >= trendSpeed)
						currStreet.setTrendLeft(R.drawable.ic_remove_white_24dp);
					else
						currStreet.setTrendLeft(0);
					if (currStreet.getSpeedRight() == 0 || pastStreet.getSpeedRight() == 0)
						currStreet.setTrendRight(0);
					else if (currStreet.getSpeedRight() - pastStreet.getSpeedRight() >= trendSpeed)
						currStreet.setTrendRight(R.drawable.ic_add_white_24dp);
					else if (pastStreet.getSpeedRight() - currStreet.getSpeedRight() >= trendSpeed)
						currStreet.setTrendRight(R.drawable.ic_remove_white_24dp);
					else
						currStreet.setTrendRight(0);
				}
			}
		}
	}

	private void setupNotification(ArrayList<String> congestedZones) {
		if (!congestedZones.isEmpty()) {
			NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
			String title = getString(R.string.notificationTitle, congestedZones.size());
			inboxStyle.setBigContentTitle(title);
			for (String string : congestedZones)
				inboxStyle.addLine(string);
			NotificationCompat.Builder bui = new NotificationCompat.Builder(this);
			bui.setSmallIcon(R.drawable.ic_stat_notify_trafficdroid);
			bui.setContentTitle(title);
			bui.setDefaults(Notification.DEFAULT_ALL);
			bui.setStyle(inboxStyle);
			bui.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT));
			((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, bui.build());
		} else
			((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(NOTIFICATION_ID);
	}
}