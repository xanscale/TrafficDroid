package it.localhost.trafficdroid.fragment.dialog;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;

import com.google.android.gms.ads.AdView;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.common.AdManager;
import it.localhost.trafficdroid.fragment.MainFragment;

public class WebviewDialogFragment extends DialogFragment {
	private static final String TAG_URL = "url";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Builder builder = new Builder(getActivity());
		WebView webView = (WebView) View.inflate(getActivity(), R.layout.webview, null);
		webView.getSettings().setJavaScriptEnabled(true);
		String url = getArguments().getString(TAG_URL);
		if (url != null && url.endsWith(MainFragment.jpg)) {
			webView.getSettings().setLoadWithOverviewMode(true);
			webView.getSettings().setUseWideViewPort(true);
		}
		webView.loadUrl(url);
		builder.setView(webView);
		Dialog d = builder.create();
		d.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		new AdManager().load(getActivity(), ((AdView) webView.findViewById(R.id.adView)), true);
		return d;
	}

	public void show(FragmentManager fragmentManager, String url) {
		Bundle arguments = new Bundle(2);
		arguments.putString(TAG_URL, url);
		setArguments(arguments);
		super.show(fragmentManager, WebviewDialogFragment.class.getSimpleName());
	}
}