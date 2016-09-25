package it.localhost.trafficdroid.service;

import android.net.Uri;
import android.os.AsyncTask;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

import it.localhost.trafficdroid.dto.BaseDTO;
import it.localhost.trafficdroid.dto.PedaggioDTO;

public class PedaggioService extends AsyncTask<String, Void, BaseDTO> {
	private static final String DTXP_A = "dtxpA";
	private static final String DTXP_DA = "dtxpDa";
	private static final String G = "G";
	private static final String TIPO = "tipo";
	private static final String PATH = "autostrade-gis/ricercaPercorso.do";
	private static final String AUTHORITY = "www.autostrade.it";
	private static final String SCHEME = "http";
	private static final String SPAN_OPEN = "<span class=\"km\">";
	private static final String AND = "&";
	private static final String SEP = "\\A";
	private static final String BAD_PARAMS = "Parametri non validi";

	@Override
	protected BaseDTO doInBackground(String... args) {
		try {
			Uri.Builder b = new Uri.Builder();
			b.scheme(SCHEME);
			b.authority(AUTHORITY);
			b.path(PATH);
			b.appendQueryParameter(TIPO, G);
			b.appendQueryParameter(DTXP_DA, args[0]);
			b.appendQueryParameter(DTXP_A, args[1]);
			BaseDTO out;
			Scanner sc = new Scanner(new URL("http://www.autostrade.it/autostrade-gis/ricercaPercorso.do?tipo=G&dtxpDa=" + args[0] + "&dtxpA=" + args[1]).openConnection().getInputStream());
			String s = sc.useDelimiter(SEP).next();
			int start = s.indexOf(SPAN_OPEN);
			if (start != -1) {
				start = start + SPAN_OPEN.length();
				out = new PedaggioDTO(NumberFormat.getInstance(Locale.ITALY).parse(s.substring(start, s.indexOf(AND, start)).trim()).doubleValue());
			} else
				out = new BaseDTO(BAD_PARAMS);
			sc.close();
			return out;
		} catch (Exception e) {
			return new BaseDTO(e.getMessage());
		}
	}
}
