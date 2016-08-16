package it.localhost.trafficdroid.widget.activity;

import it.localhost.trafficdroid.common.Utility;
import it.localhost.trafficdroid.dto.MainDTO;
import it.localhost.trafficdroid.dto.StreetDTO;
import it.localhost.trafficdroid.dto.ZoneDTO;
import it.localhost.trafficdroid.service.PersistanceService;
import it.localhost.trafficdroid.widget.provider.WidgetZoneProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ExpandableListActivity;
import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

public class WidgetZoneActivity extends ExpandableListActivity {
	private static final String NAME = "NAME";
	private MainDTO dto;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setResult(RESULT_CANCELED);
		List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
		List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
		try {
			dto = PersistanceService.retrieve(this);
			for (StreetDTO street : dto.getStreets()) {
				Map<String, String> groupElem = new HashMap<String, String>();
				List<Map<String, String>> brotherData = new ArrayList<Map<String, String>>();
				groupData.add(groupElem);
				childData.add(brotherData);
				groupElem.put(NAME, street.getName());
				for (ZoneDTO zone : street.getZones()) {
					Map<String, String> childElem = new HashMap<String, String>();
					childElem.put(NAME, zone.getName());
					brotherData.add(childElem);
				}
			}
			setListAdapter(new SimpleExpandableListAdapter(this, groupData, android.R.layout.simple_expandable_list_item_1, new String[] { NAME }, new int[] { android.R.id.text1 }, childData, android.R.layout.simple_expandable_list_item_1, new String[] { NAME }, new int[] { android.R.id.text1 }));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		int mAppWidgetId = getIntent().getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		Utility.setWidgetZoneStreet(this, mAppWidgetId, dto.getStreets().get(groupPosition).getId());
		Utility.setWidgetZoneZone(this, mAppWidgetId, dto.getStreets().get(groupPosition).getZones().get(childPosition).getId());
		WidgetZoneProvider.updateAppWidget(this, AppWidgetManager.getInstance(this), mAppWidgetId);
		setResult(RESULT_OK, getIntent());
		finish();
		return true;
	}
}
