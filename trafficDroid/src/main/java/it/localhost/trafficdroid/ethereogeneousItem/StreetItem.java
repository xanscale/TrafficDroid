package it.localhost.trafficdroid.ethereogeneousItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.dto.StreetDTO;
import localhost.toolkit.widget.HeterogeneousItem;

public class StreetItem extends HeterogeneousItem<StreetDTO> {
	public StreetItem(Context context, StreetDTO extra) {
		super(context, extra);
	}

	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
		View view = layoutInflater.inflate(android.R.layout.simple_expandable_list_item_2, viewGroup, false);
		view.setTag(R.id.text1, view.findViewById(android.R.id.text1));
		view.setTag(R.id.text2, view.findViewById(android.R.id.text2));
		return view;
	}

	@Override
	public void onResume(View view) {
		view.setTag(R.id.itemKey, extra.getId());
		view.setTag(R.id.itemName, extra.getName());
		((TextView) view.getTag(R.id.text1)).setText(extra.getTag() + " " + extra.getName());
		TextView streetText2 = (TextView) view.getTag(R.id.text2);
		if (extra.getBadNews().size() != 0) {
			streetText2.setText(context.getString(R.string.badNews, extra.getBadNews().size()));
			streetText2.setVisibility(View.VISIBLE);
		} else {
			streetText2.setVisibility(View.INVISIBLE);
		}
	}
}