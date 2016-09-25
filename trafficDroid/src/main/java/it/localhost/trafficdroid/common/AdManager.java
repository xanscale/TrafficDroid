package it.localhost.trafficdroid.common;

import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

import it.localhost.trafficdroid.R;

public class AdManager extends AdListener {
	private InterstitialAd interstitial;

	public void load(Context context, AdView adView, boolean interstitialAd) {
		if (adView != null) {
			if (!Utility.isAdFree(context))
				adView.loadAd(new AdRequest.Builder().build());
			else
				adView.setVisibility(View.GONE);
		}
		if (interstitialAd && !Utility.isInterstitialFree(context) && new Random().nextInt(5) == 0) {
			interstitial = new InterstitialAd(context);
			interstitial.setAdUnitId(context.getString(R.string.adUnitId));
			interstitial.setAdListener(this);
			interstitial.loadAd(new AdRequest.Builder().build());
		}
	}

	public void onAdLoaded() {
		super.onAdLoaded();
		if (interstitial.isLoaded())
			interstitial.show();
	}
}
