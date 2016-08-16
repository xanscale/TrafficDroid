package it.localhost.trafficdroid.ethereogeneousItem;

import it.localhost.trafficdroid.R;
import localhost.toolkit.widget.HeterogeneousItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdViewItem extends HeterogeneousItem {
	private AdRequest adRequest;

	public AdViewItem(Context context, int extra) {
		super(context, extra);
		adRequest = new AdRequest.Builder().build();
	}

	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
		return layoutInflater.inflate((Integer) extra, viewGroup, false);
	}

	@Override
	public void onResume(View view) {
		((AdView) view.findViewById(R.id.adView)).loadAd(adRequest);
	}
}