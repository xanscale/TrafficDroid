package it.localhost.trafficdroid.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.common.AdManager;

public class WebviewFragment extends Fragment {
	public static final String ALCOL_URL = "http://voti.kataweb.it/etilometro/index.php";
	public static final String BOLLO_URL = "https://online.aci.it/acinet/calcolobollo";
	private static final String URL_KEY = "URL_KEY";

	public static WebviewFragment newInstance(String url) {
		WebviewFragment instance = new WebviewFragment();
		Bundle args = new Bundle(1);
		args.putString(URL_KEY, url);
		instance.setArguments(args);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		WebView webView = (WebView) inflater.inflate(R.layout.webview, container, false);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(getArguments().getString(URL_KEY));
		webView.setWebViewClient(new WebViewClient());
		new AdManager().load(getActivity(), null, true);
		return webView;
	}
}