package it.localhost.trafficdroid.fragment;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.common.ListZoneResId;
import it.localhost.trafficdroid.common.ListZoneResName;

public class PreferencesFragment extends PreferenceFragment { // NO_UCD
	private static final String autovelox = "Autovelox";
	private int[] autoveloxStreet;
	private int[] autoveloxFrom;
	private int[] autoveloxTo;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		addPreferencesFromResource(R.xml.preferencescreen);
		PreferenceManager.setDefaultValues(getActivity(), R.xml.preferencescreen, false);
		int[] streetId = getResources().getIntArray(R.array.streetId);
		String[] streetName = getResources().getStringArray(R.array.streetName);
		String[] streetTag = getResources().getStringArray(R.array.streetTag);
		int[] streetFather = getResources().getIntArray(R.array.streetFather);
		autoveloxStreet = getResources().getIntArray(R.array.autoveloxStreet);
		autoveloxFrom = getResources().getIntArray(R.array.autoveloxFrom);
		autoveloxTo = getResources().getIntArray(R.array.autoveloxTo);
		PreferenceScreen root = getPreferenceScreen();
		PreferenceCategory streetsCategory = new PreferenceCategory(getActivity());
		root.addPreference(streetsCategory);
		streetsCategory.setTitle(R.string.mappedSreet);
		for (int i = 0; i < streetId.length; i++) {
			if (streetFather[i] == 0) {
				PreferenceScreen streetScreen = addStreetScreen(streetsCategory, streetId[i], streetTag[i] + " " + streetName[i]);
				PreferenceCategory subStreetCategory = null;
				for (int j = 0; j < streetFather.length; j++) {
					if (streetFather[j] == streetId[i]) {
						if (subStreetCategory == null) {
							subStreetCategory = new PreferenceCategory(getActivity());
							streetScreen.addPreference(subStreetCategory);
							subStreetCategory.setTitle(R.string.diramation);
						}
						setZonesCategory(addStreetScreen(subStreetCategory, streetId[j], streetTag[j] + " " + streetName[j]), streetId[j]);
					}
				}
				setZonesCategory(streetScreen, streetId[i]);
			}
		}
	}

	private PreferenceScreen addStreetScreen(PreferenceCategory streetsCategory, int streetId, String streetName) {
		PreferenceScreen streetScreen = getPreferenceManager().createPreferenceScreen(getActivity());
		streetsCategory.addPreference(streetScreen);
		streetScreen.setTitle(streetName);
		CheckBoxPreference streetCheck = new CheckBoxPreference(getActivity());
		streetCheck.setKey(Integer.toString(streetId));
		streetCheck.setTitle(streetName);
		streetCheck.setSummary(R.string.selectStreet);
		streetScreen.addPreference(streetCheck);
		return streetScreen;
	}

	private void setZonesCategory(PreferenceScreen streetScreen, int streetId) {
		PreferenceCategory zonesCategory = new PreferenceCategory(getActivity());
		streetScreen.addPreference(zonesCategory);
		zonesCategory.setTitle(R.string.sniper);
		int[] zonesId = getResources().getIntArray(ListZoneResId.getInstance().get(streetId));
		String[] zonesName = getResources().getStringArray(ListZoneResName.getInstance().get(streetId));
		for (int k = 0; k < zonesId.length; k++) {
			CheckBoxPreference singlezone = new CheckBoxPreference(getActivity());
			singlezone.setKey(Integer.toString(zonesId[k]));
			singlezone.setTitle(zonesName[k]);
			for (int t = 0; t < autoveloxStreet.length; t++) {
				if (autoveloxStreet[t] == streetId && ((zonesId[k] >= autoveloxFrom[t] && zonesId[k] < autoveloxTo[t]) || (zonesId[k] >= autoveloxTo[t] && zonesId[k] < autoveloxFrom[t]))) {
					singlezone.setSummary(autovelox);
				}
			}
			zonesCategory.addPreference(singlezone);
		}
	}
}