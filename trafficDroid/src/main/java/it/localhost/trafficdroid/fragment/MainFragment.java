package it.localhost.trafficdroid.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.android.gms.ads.AdView;

import java.io.Serializable;
import java.util.ArrayList;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.activity.MainActivity;
import it.localhost.trafficdroid.common.AdManager;
import it.localhost.trafficdroid.common.Utility;
import it.localhost.trafficdroid.dto.MainDTO;
import it.localhost.trafficdroid.dto.StreetDTO;
import it.localhost.trafficdroid.dto.ZoneDTO;
import it.localhost.trafficdroid.ethereogeneousItem.AdViewItem;
import it.localhost.trafficdroid.ethereogeneousItem.BadNewsItem;
import it.localhost.trafficdroid.ethereogeneousItem.StreetItem;
import it.localhost.trafficdroid.ethereogeneousItem.ZoneItem;
import it.localhost.trafficdroid.fragment.dialog.BadnewsDialogFragment;
import it.localhost.trafficdroid.fragment.dialog.WebviewDialogFragment;
import it.localhost.trafficdroid.service.PersistanceService;
import localhost.toolkit.app.MessageDialogFragment;
import localhost.toolkit.widget.HeterogeneousExpandableListAdapter;
import localhost.toolkit.widget.HeterogeneousItem;
import localhost.toolkit.widget.HeterogeneousItem.OnHeterogeneousItemClickListener;

public class MainFragment extends Fragment {
	private static final String autostradeDESKURL = "http://www.autostrade.it/autostrade-gis/popupTelecameran.do?tlc=";
	private static final String cavspa = "http://www.cavspa.it/webcam/temp-imgs/camsbig/";
	private static final String edidomus = "http://telecamere.edidomus.it/vp2/vpimage.aspx?camid=";
	private static final String autofiori = "http://www.autofiori.it/cgi-bin/cgiwebcam.exe?site=";
	private static final String autobspd = "http://www.autobspd.it/images/telecamereAutobspd/";
	private static final String brennero = "http://www.autobrennero.it/WebCamImg/km";
	private static final String SERRAVALLE = "http://www.serravalle.it/ftp_upload/telecamere/";
	public static final String jpg = ".jpg";
	private static final char camAutostrade = 'A';
	private static final char camCavspa = 'C';
	private static final char camEdidomus = 'E';
	private static final char camAutofiori = 'F';
	private static final char camAutobspd = 'B';
	private static final char camBRENNERO = 'R';
	private static final char camSERRAVALLE = 'S';
	private static final char camNone = 'H';
	private ExpandableListView listView;
	private BroadcastReceiver receiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main, container, false);
		listView = (ExpandableListView) v.findViewById(R.id.mainTable);
		listView.setOnChildClickListener(HeterogeneousExpandableListAdapter.getOnChildClickListener());
		if (Utility.isBerserkKey(getActivity()))
			getActivity().sendBroadcast(new Intent(getString(R.string.RUN_UPDATE)));
		((MainActivity) getActivity()).setScreenName(0);
		new AdManager().load(getActivity(), ((AdView) v.findViewById(R.id.adView)), false);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		receiver = new UpdateReceiver();
		getActivity().registerReceiver(receiver, new IntentFilter(getString(R.string.END_UPDATE)));
		new RefreshTask().execute();
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(receiver);
	}

	private class RefreshTask extends AsyncTask<Void, Void, MainDTO> {
		@Override
		protected MainDTO doInBackground(Void... params) {
			try {
				return PersistanceService.retrieve(getActivity());
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(MainDTO mainDTO) {
			if (mainDTO != null && mainDTO.getTrafficTime() != null) {
				((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(DateFormat.getTimeFormat(getActivity()).format(mainDTO.getTrafficTime()));
				ArrayList<HeterogeneousItem> groupItems = new ArrayList<HeterogeneousItem>();
				ArrayList<ArrayList<HeterogeneousItem>> childItems = new ArrayList<ArrayList<HeterogeneousItem>>();
				OnBadNewsItemClickListener onBadNewsItemClickListener = new OnBadNewsItemClickListener();
				OnZoneItemClickListener onZoneItemClickListener = new OnZoneItemClickListener();
				for (StreetDTO street : mainDTO.getStreets()) {
					groupItems.add(new StreetItem(getActivity(), street));
					ArrayList<HeterogeneousItem> childData = new ArrayList<HeterogeneousItem>();
					BadNewsItem badNewsItem = new BadNewsItem(getActivity(), street);
					badNewsItem.setOnHeterogeneousItemClickListener(onBadNewsItemClickListener);
					childData.add(badNewsItem);
					for (ZoneDTO zone : street.getZones()) {
						ZoneItem zoneItem = new ZoneItem(getActivity(), zone);
						zoneItem.setOnHeterogeneousItemClickListener(onZoneItemClickListener);
						childData.add(zoneItem);
					}
					childItems.add(childData);
				}
				for (int i = 0; i < childItems.size(); i++) {
					int size = childItems.get(i).size();
					for (int j = 0; j < size; j++)
						if (Math.random() < 0.05 && !Utility.isAdFree(getActivity())) {
							childItems.get(i).add(j++, new AdViewItem(getActivity(), R.layout.adview_smart_banner));
							size++;
						}
				}
				listView.setAdapter(new HeterogeneousExpandableListAdapter(getActivity(), groupItems, childItems));
				for (int i = 0; i < listView.getExpandableListAdapter().getGroupCount(); i++)
					listView.expandGroup(i);
			}
			if (Utility.isExCheck(getActivity())) {
				new MessageDialogFragment().show(getFragmentManager(), getString(R.string.error), Utility.getExMsg(getActivity()), false);
				Utility.setExCheck(getActivity(), false);
			}
		}
	}

	private class UpdateReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			new RefreshTask().execute();
		}
	}

	private class OnZoneItemClickListener implements OnHeterogeneousItemClickListener {
		@Override
		public boolean onHeterogeneousItemClick(View view, int position, Serializable extra) {
			String webcam = ((ZoneDTO) extra).getWebcam();
			if (webcam.charAt(0) == camNone) {
				((MainActivity) getActivity()).sendEvent(MainActivity.EVENT_CAT_WEBCAM, MainActivity.EVENT_ACTION_NONE, webcam);
				new MessageDialogFragment().show(getFragmentManager(), getString(R.string.info), getString(R.string.webcamNone), false);
			} else if (webcam.charAt(0) == camAutostrade) {
				((MainActivity) getActivity()).sendEvent(MainActivity.EVENT_CAT_WEBCAM, MainActivity.EVENT_ACTION_OPEN, webcam);
				new WebviewDialogFragment().show(getFragmentManager(), autostradeDESKURL + webcam.substring(1));
			} else if (webcam.charAt(0) == camCavspa) {
				((MainActivity) getActivity()).sendEvent(MainActivity.EVENT_CAT_WEBCAM, MainActivity.EVENT_ACTION_OPEN, webcam);
				new WebviewDialogFragment().show(getFragmentManager(), cavspa + webcam.substring(1) + jpg);
			} else if (webcam.charAt(0) == camEdidomus) {
				((MainActivity) getActivity()).sendEvent(MainActivity.EVENT_CAT_WEBCAM, MainActivity.EVENT_ACTION_OPEN, webcam);
				new WebviewDialogFragment().show(getFragmentManager(), edidomus + webcam.substring(1));
			} else if (webcam.charAt(0) == camAutofiori) {
				((MainActivity) getActivity()).sendEvent(MainActivity.EVENT_CAT_WEBCAM, MainActivity.EVENT_ACTION_OPEN, webcam);
				new WebviewDialogFragment().show(getFragmentManager(), autofiori + webcam.substring(1));
			} else if (webcam.charAt(0) == camAutobspd) {
				((MainActivity) getActivity()).sendEvent(MainActivity.EVENT_CAT_WEBCAM, MainActivity.EVENT_ACTION_OPEN, webcam);
				new WebviewDialogFragment().show(getFragmentManager(), autobspd + webcam.substring(1) + jpg);
			} else if (webcam.charAt(0) == camBRENNERO) {
				((MainActivity) getActivity()).sendEvent(MainActivity.EVENT_CAT_WEBCAM, MainActivity.EVENT_ACTION_OPEN, webcam);
				new WebviewDialogFragment().show(getFragmentManager(), brennero + webcam.substring(1) + jpg);
			} else if (webcam.charAt(0) == camSERRAVALLE) {
				((MainActivity) getActivity()).sendEvent(MainActivity.EVENT_CAT_WEBCAM, MainActivity.EVENT_ACTION_OPEN, webcam);
				new WebviewDialogFragment().show(getFragmentManager(), SERRAVALLE + webcam.substring(1) + jpg);
			} else {
				((MainActivity) getActivity()).sendEvent(MainActivity.EVENT_CAT_WEBCAM, MainActivity.EVENT_ACTION_REQUEST, webcam);
				new MessageDialogFragment().show(getFragmentManager(), getString(R.string.info), getString(R.string.webcamAdd), false);
			}
			return true;
		}
	}

	private class OnBadNewsItemClickListener implements OnHeterogeneousItemClickListener {
		@Override
		public boolean onHeterogeneousItemClick(View view, int position, Serializable extra) {
			StreetDTO street = (StreetDTO) extra;
			if (street.getBadNews().size() != 0) {
				((MainActivity) getActivity()).sendEvent(MainActivity.EVENT_CAT_BADNEWS, MainActivity.EVENT_ACTION_OPEN, street.getName());
				new BadnewsDialogFragment().show(getFragmentManager(), street);
			}
			return true;
		}
	}
}
