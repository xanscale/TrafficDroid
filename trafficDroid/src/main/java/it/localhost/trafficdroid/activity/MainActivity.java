package it.localhost.trafficdroid.activity;

import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.common.Utility;
import it.localhost.trafficdroid.fragment.BolloFragment;
import it.localhost.trafficdroid.fragment.MainFragment;
import it.localhost.trafficdroid.fragment.PatenteFragment;
import it.localhost.trafficdroid.fragment.PedaggioFragment;
import it.localhost.trafficdroid.fragment.PreferencesFragment;
import it.localhost.trafficdroid.fragment.WebviewFragment;
import it.localhost.trafficdroid.fragment.dialog.QuizDialogFragment;
import it.localhost.trafficdroid.fragment.dialog.SetupDialogFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener { // NO_UCD
	private static final String INAPPB_PKG = "com.android.vending";
	private static final String INAPPB_ACT = INAPPB_PKG + ".billing.InAppBillingService.BIND";
	public static final String EVENT_CAT_WEBCAM = "Webcam";
	public static final String EVENT_CAT_BADNEWS = "BadNews";
	public static final String EVENT_CAT_IAB = "InAppBilling";
	public static final String EVENT_ACTION_REQUEST = "Request";
	public static final String EVENT_ACTION_OPEN = "Open";
	public static final String EVENT_ACTION_NONE = "None";
	public static final String EVENT_ACTION_LAUNCHPURCHASEFLOW = "LaunchPurchaseFlow";
	private static final String ITEM_TYPE_INAPP = "inapp";
	public static final String SKU_AD_FREE = "ad_free";
	public static final String SKU_QUIZ_FREE = "quiz_free";
	public static final String SKU_INTERSTITIAL_FREE = "interstitial_free";
	private static final String RESPONSE_CODE = "RESPONSE_CODE";
	private static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
	private static final String RESPONSE_INAPP_PURCHASE_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
	private static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
	private static final String RESPONSE_INAPP_DATA_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
	private static final String RESPONSE_INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
	private static final String KEY_FACTORY_ALGORITHM = "RSA";
	private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
	private static final String RSA_PUBKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgg7ckVCx3779Q4Dq99wMFYlwS4+jmbrTtBjLzG2cL4xoz6pZhLct4vIL2sfhA588Vfp4vRDHaIN3lpiCOGIxWRxI3krOoF+n1G/F9kUdiGaK4hYMPYa41MPbG6wc9tWJgcGe0PdYExCmeIvFiQrc4HU63J9zN+C1HRqw1t91YC2vzyZFxNLoIp3kcoz6rBCopm4GA01ZPZrP5RTR2hiJLrDVJl5mzuDrl7yoMq6OQ1SasVaWkgN7yTDyh9Df9hv5FsE8haVFddSJfTEh4BFZcFSW+17xgeImNtCgDtQ/GuTG3FIOiOotugIa1OjKC4z5zbZFl8Zz+cz8fFOxzfLkTQIDAQAB";
	private IInAppBillingService inAppBillingService;
	private ServiceConnection serviceConnection;
	private BroadcastReceiver receiver;
	private IntentFilter intentFilter;
	private Tracker tracker;
	private boolean progress;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private boolean addToBackStack;
	private NavigationView nv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// android.os.StrictMode.setThreadPolicy(new android.os.StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
		// android.os.StrictMode.setVmPolicy(new android.os.StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
		setContentView(R.layout.drawer);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open_menu, R.string.close_menu);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		nv = (NavigationView) findViewById(R.id.navigationView);
		nv.setNavigationItemSelectedListener(this);
		serviceConnection = new TdServiceConnection();
		bindService(new Intent(INAPPB_ACT).setPackage(INAPPB_PKG), serviceConnection, Context.BIND_AUTO_CREATE);
		intentFilter = new IntentFilter();
		intentFilter.addAction(getString(R.string.BEGIN_UPDATE));
		intentFilter.addAction(getString(R.string.END_UPDATE));
		tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.analytics);
		tracker.enableAdvertisingIdCollection(true);
		receiver = new UpdateReceiver();
		if (Utility.getProviderTraffic(this).equals(getString(R.string.providerTrafficDefault)))
			new SetupDialogFragment().show(getFragmentManager(), SetupDialogFragment.class.getSimpleName());
		selectNavigationItemId(R.id.menuMain);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, intentFilter);
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unbindService(serviceConnection);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_option, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.menuRefresh).setVisible(!progress);
		if (Utility.isAdFree(this))
			menu.removeItem(R.id.menuAdFree);
		if (Utility.isInterstitialFree(this))
			menu.removeItem(R.id.menuInterstitialFree);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		} else {
			switch (item.getItemId()) {
				case R.id.menuRefresh:
					if (!Utility.getProviderTraffic(this).equals(getString(R.string.providerTrafficDefault)))
						sendBroadcast(new Intent(getString(R.string.RUN_UPDATE)));
					return true;
				case R.id.menuAdFree:
					launchPurchaseFlow(SKU_AD_FREE);
					return true;
				case R.id.menuInterstitialFree:
					launchPurchaseFlow(SKU_INTERSTITIAL_FREE);
					return true;
				default:
					return super.onOptionsItemSelected(item);
			}
		}
	}

	public void sendEvent(String category, String action, String label) {
		tracker.send(new HitBuilders.EventBuilder(category, action).setLabel(label).build());
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
			mDrawerLayout.closeDrawer(GravityCompat.START);
		else
			super.onBackPressed();
	}

	public void selectNavigationItemId(int id) {
		onNavigationItemSelected(nv.getMenu().findItem(id));
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		mDrawerLayout.closeDrawer(GravityCompat.START);
		item.setChecked(true);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		switch (item.getItemId()) {
			case R.id.menuMain:
				ft.replace(R.id.content_frame, new MainFragment());
				break;
			case R.id.menuPedaggio:
				ft.replace(R.id.content_frame, new PedaggioFragment());
				break;
			case R.id.menuBollo:
				ft.replace(R.id.content_frame, new BolloFragment());
				break;
			case R.id.menuPatente:
				ft.replace(R.id.content_frame, new PatenteFragment());
				break;
			case R.id.menuAlcol:
				ft.replace(R.id.content_frame, WebviewFragment.newInstance(WebviewFragment.ALCOL_URL));
				break;
			case R.id.menuSettings:
				ft.replace(R.id.content_frame, new PreferencesFragment());
				break;
		}
		if (addToBackStack)
			ft.addToBackStack("");
		ft.commit();
		addToBackStack = true;
		return false;
	}

	public void setScreenName(int screenName) {
	}

	private final class UpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(getString(R.string.BEGIN_UPDATE))) {
				getSupportActionBar().setSubtitle(R.string.update);
				progress = true;
			} else if (intent.getAction().equals(getString(R.string.END_UPDATE))) {
				progress = false;
				getSupportActionBar().setSubtitle(null);
			}
			invalidateOptionsMenu();
		}
	}

	public void launchPurchaseFlow(String sku) {
		sendEvent(EVENT_CAT_IAB, EVENT_ACTION_LAUNCHPURCHASEFLOW, sku);
		try {
			Bundle buyIntentBundle = inAppBillingService.getBuyIntent(3, getPackageName(), sku, ITEM_TYPE_INAPP, "");
			if (buyIntentBundle.getInt(RESPONSE_CODE) == 0)
				startIntentSenderForResult(((PendingIntent) buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT)).getIntentSender(), 0, new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class TdServiceConnection implements ServiceConnection {
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			inAppBillingService = IInAppBillingService.Stub.asInterface(service);
			try {
				if (inAppBillingService.isBillingSupported(3, getPackageName(), ITEM_TYPE_INAPP) == 0)
					new RetrievePurchasesService().execute();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public class RetrievePurchasesService extends AsyncTask<Void, Void, ArrayList<String>> {
		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			ArrayList<String> out = new ArrayList<String>();
			String continueToken = null;
			try {
				Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
				sig.initVerify(KeyFactory.getInstance(KEY_FACTORY_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.decode(RSA_PUBKEY, Base64.DEFAULT))));
				do {
					Bundle ownedItems = inAppBillingService.getPurchases(3, getPackageName(), ITEM_TYPE_INAPP, continueToken);
					if (ownedItems.getInt(RESPONSE_CODE) == 0) {
						ArrayList<String> skuList = ownedItems.getStringArrayList(RESPONSE_INAPP_PURCHASE_ITEM_LIST);
						ArrayList<String> purchaseDataList = ownedItems.getStringArrayList(RESPONSE_INAPP_PURCHASE_DATA_LIST);
						ArrayList<String> signatureList = ownedItems.getStringArrayList(RESPONSE_INAPP_DATA_SIGNATURE_LIST);
						for (int i = 0; i < skuList.size(); ++i) {
							sig.update(purchaseDataList.get(i).getBytes());
							if (sig.verify(Base64.decode(signatureList.get(i), Base64.DEFAULT)))
								out.add(skuList.get(i));
						}
					}
					continueToken = ownedItems.getString(RESPONSE_INAPP_CONTINUATION_TOKEN);
				} while (continueToken != null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return out;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			super.onPostExecute(result);
			Utility.setAdFree(MainActivity.this, result.contains(SKU_AD_FREE) ? true : false);
			Utility.setInterstitialFree(MainActivity.this, result.contains(SKU_INTERSTITIAL_FREE) ? true : false);
			View ad = findViewById(R.id.adView);
			if (ad != null)
				ad.setVisibility(result.contains(SKU_AD_FREE) ? View.GONE : View.VISIBLE);
			if (!result.contains(SKU_QUIZ_FREE) && !Utility.getProviderTraffic(MainActivity.this).equals(getString(R.string.providerTrafficDefault)))
				new QuizDialogFragment().show(getFragmentManager(), getString(R.string.patenteQuiz));
			invalidateOptionsMenu();
		}
	}
}