package it.localhost.trafficdroid.ethereogeneousItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import it.localhost.trafficdroid.R;
import localhost.toolkit.widget.HeterogeneousItem;

public class AdViewItem extends HeterogeneousItem<Integer> {
	private AdRequest adRequest;

	public AdViewItem(Context context, int extra) {
		super(context, extra);
		adRequest = new AdRequest.Builder().build();
	}

	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
		return layoutInflater.inflate(extra, viewGroup, false);
	}

	@Override
	public void onResume(View view) {
		((AdView) view.findViewById(R.id.adView)).loadAd(adRequest);
	}
}