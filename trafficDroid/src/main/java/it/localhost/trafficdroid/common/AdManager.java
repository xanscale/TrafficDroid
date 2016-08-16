package it.localhost.trafficdroid.common;

import java.util.Random;

import it.localhost.trafficdroid.R;
import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AdManager {
	public void load(Context context, AdView adView, boolean interstitialAd) {
		if (adView != null) {
			if (!Utility.isAdFree(context))
				adView.loadAd(new AdRequest.Builder().build());
			else
				adView.setVisibility(View.GONE);
		}
		if (interstitialAd && !Utility.isInterstitialFree(context) && new Random().nextInt(5) == 0) {
			InterstitialAd interstitial = new InterstitialAd(context);
			interstitial.setAdUnitId(context.getString(R.string.adUnitId));
			interstitial.setAdListener(new MyAdListener(interstitial));
			interstitial.loadAd(new AdRequest.Builder().build());
		}
	}

	private class MyAdListener extends AdListener {
		InterstitialAd interstitial;

		public MyAdListener(InterstitialAd interstitial) {
			this.interstitial = interstitial;
		}

		@Override
		public void onAdLoaded() {
			super.onAdLoaded();
			if (interstitial.isLoaded())
				interstitial.show();
		}
	}
}
