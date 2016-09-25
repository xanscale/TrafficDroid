package it.localhost.trafficdroid.fragment.dialog;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.common.Utility;
import it.localhost.trafficdroid.dto.BadNewsDTO;
import it.localhost.trafficdroid.dto.StreetDTO;
import it.localhost.trafficdroid.ethereogeneousItem.AdViewItem;
import it.localhost.trafficdroid.ethereogeneousItem.BadNewsDialogItem;
import localhost.toolkit.widget.HeterogeneousArrayAdapter;
import localhost.toolkit.widget.HeterogeneousItem;

public class BadnewsDialogFragment extends DialogFragment {
	private static final String STREET = "STREET";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Builder builder = new Builder(getActivity());
		StreetDTO street = (StreetDTO) getArguments().getSerializable(STREET);
		builder.setTitle(street.getName());
		ListView listview = (ListView) ListView.inflate(getActivity(), R.layout.dialog_badnews, null);
		ArrayList<HeterogeneousItem> items = new ArrayList<>();
		if (!Utility.isAdFree(getActivity()))
			items.add(new AdViewItem(getActivity(), R.layout.adview_medium_rectangle));
		for (BadNewsDTO badNews : street.getBadNews())
			items.add(new BadNewsDialogItem(getActivity(), badNews));
		listview.setAdapter(new HeterogeneousArrayAdapter(getActivity(), items));
		builder.setView(listview);
		return builder.create();
	}

	public void show(FragmentManager fragmentManager, StreetDTO street) {
		Bundle arguments = new Bundle(2);
		arguments.putSerializable(STREET, street);
		setArguments(arguments);
		show(fragmentManager, BadnewsDialogFragment.class.getSimpleName());
	}
}