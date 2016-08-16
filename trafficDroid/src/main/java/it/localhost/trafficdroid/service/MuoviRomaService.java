package it.localhost.trafficdroid.service;

import it.localhost.trafficdroid.dto.BaseDTO;
import it.localhost.trafficdroid.dto.MuoviRomaDTO;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.AsyncTask;

public class MuoviRomaService extends AsyncTask<String, Void, BaseDTO> {
	private static final String P = "p";
	private static final String H3 = "h3";
	private static final String SPAN_CLASS_I = "<span class=\"i\">";
	private static final String START = "<h2 class=\"vskip\">Orari di oggi</h2>";
	private static final String END = "<p class=\"vskip\">";
	private static final String A_CLOSE = "</a>";
	private static final String A_OPEN = "<a>";
	private static final String SPAN_CLOSE = "</span>";
	private static final String SPAN_OPEN = "<span>";
	private static final String BLANK = "";
	private static final String URL = "http://muovi.roma.it/ztl/";
	private static final String FILE = "\\A";

	@Override
	protected BaseDTO doInBackground(String... args) {
		try {
			ArrayList<String[]> out = new ArrayList<String[]>();
			Scanner s = new Scanner(new URL(URL).openStream());
			String data = s.useDelimiter(FILE).next().replace(SPAN_CLASS_I, BLANK).replace(SPAN_OPEN, BLANK).replace(SPAN_CLOSE, BLANK);
			int start = data.indexOf(START);
			int end = data.indexOf(END, start);
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(A_OPEN + data.substring(start, end) + A_CLOSE)));
			NodeList nl = doc.getChildNodes().item(0).getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				if (nl.item(i).getNodeName().equals(H3))
					out.add(new String[] { nl.item(i).getTextContent(), BLANK });
				else if (nl.item(i).getNodeName().equals(P))
					out.get(out.size() - 1)[1] = nl.item(i).getTextContent().trim();
			}
			s.close();
			return new MuoviRomaDTO(true, out);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseDTO(false, e.getMessage());
		}
	}
}
