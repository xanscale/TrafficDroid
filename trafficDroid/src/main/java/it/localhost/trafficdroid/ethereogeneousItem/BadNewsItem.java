package it.localhost.trafficdroid.ethereogeneousItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.dto.StreetDTO;
import localhost.toolkit.widget.HeterogeneousItem;

public class BadNewsItem extends HeterogeneousItem<StreetDTO> {
	static final String badNewsLabel = "Bad News: ";

	public BadNewsItem(Context context, StreetDTO extra) {
		super(context, extra);
	}

	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
		View view = layoutInflater.inflate(R.layout.main_item_badnews, viewGroup, false);
		view.setTag(R.id.streetDirLeft, view.findViewById(R.id.streetDirLeft));
		view.setTag(R.id.streetDirRight, view.findViewById(R.id.streetDirRight));
		view.setTag(R.id.badNews, view.findViewById(R.id.badNews));
		return view;
	}

	@Override
	public void onResume(View view) {
		((ImageView) view.getTag(R.id.streetDirLeft)).setImageResource(extra.getDirectionLeft());
		((ImageView) view.getTag(R.id.streetDirRight)).setImageResource(extra.getDirectionRight());
		TextView badNews = (TextView) view.getTag(R.id.badNews);
		if (extra.getBadNews().size() != 0) {
			badNews.setText(badNewsLabel + extra.getBadNews().size());
			badNews.setVisibility(View.VISIBLE);
		} else
			badNews.setVisibility(View.INVISIBLE);
	}
}