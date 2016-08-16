package it.localhost.trafficdroid.service;

import it.localhost.trafficdroid.dto.BaseDTO;
import it.localhost.trafficdroid.dto.PatenteDTO;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import android.os.AsyncTask;

public class PatenteService extends AsyncTask<String, Void, BaseDTO> {
	private static final String SCADENZA_PATENTE = "scadenzaPatente";
	private static final String SALDO = "saldo";
	private static final String NUMEO_PATENTE = "numeoPatente";
	private static final String PATENTE_URL = "https://www.ilportaledellautomobilista.it/http://voas.ilportaledellautomobilista.it:7777/PortaleFacade/SmartphonePuntiPatente?invoke=saldo&param0=";

	@Override
	protected BaseDTO doInBackground(String... args) {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(PATENTE_URL + args[0] + "%23" + args[1] + "%23" + Integer.valueOf((int) Math.pow(args[0].length() + args[1].length(), 2D) % 93) + "%2325");
			return new PatenteDTO(true, doc.getElementsByTagName(NUMEO_PATENTE).item(0).getTextContent(), doc.getElementsByTagName(SALDO).item(0).getTextContent(), doc.getElementsByTagName(SCADENZA_PATENTE).item(0).getTextContent());
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseDTO(false, e.getMessage());
		}
	}
}