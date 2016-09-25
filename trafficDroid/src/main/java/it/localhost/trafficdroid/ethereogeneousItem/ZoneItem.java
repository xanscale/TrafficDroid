package it.localhost.trafficdroid.ethereogeneousItem;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.dto.ZoneDTO;
import localhost.toolkit.widget.HeterogeneousItem;

public class ZoneItem extends HeterogeneousItem<ZoneDTO> {
	private static final int[] colorCat = new int[]{0xffffffff, 0xffff0000, 0xffff0000, 0xffff8000, 0xffffff00, 0xff47ffff, 0xff00ff00};
	private static final String noDataSpeed = "-";
	private static final char camNone = 'H';

	public ZoneItem(Context context, ZoneDTO extra) {
		super(context, extra);
	}

	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
		View view = layoutInflater.inflate(R.layout.main_item_zone, viewGroup, false);
		view.setTag(R.id.zoneName, view.findViewById(R.id.zoneName));
		view.setTag(R.id.zoneSpeedLeft, view.findViewById(R.id.zoneSpeedLeft));
		view.setTag(R.id.zoneSpeedRight, view.findViewById(R.id.zoneSpeedRight));
		view.setTag(R.id.trendLeft, view.findViewById(R.id.trendLeft));
		view.setTag(R.id.trendRight, view.findViewById(R.id.trendRight));
		view.setTag(R.id.zoneCam, view.findViewById(R.id.zoneCam));
		view.setTag(R.id.zoneAutoveloxLeft, view.findViewById(R.id.zoneAutoveloxLeft));
		view.setTag(R.id.zoneAutoveloxRight, view.findViewById(R.id.zoneAutoveloxRight));
		return view;
	}

	@Override
	public void onResume(View view) {
		view.setTag(R.id.itemKey, extra.getId());
		view.setTag(R.id.itemName, extra.getName());
		TextView zoneNameText = (TextView) view.getTag(R.id.zoneName);
		TextView leftZoneSpeedText = (TextView) view.getTag(R.id.zoneSpeedLeft);
		TextView rightZoneSpeedText = (TextView) view.getTag(R.id.zoneSpeedRight);
		ImageView trendLeftText = (ImageView) view.getTag(R.id.trendLeft);
		ImageView trendRightText = (ImageView) view.getTag(R.id.trendRight);
		ImageView cam = (ImageView) view.getTag(R.id.zoneCam);
		ImageView autoveloxLeft = (ImageView) view.getTag(R.id.zoneAutoveloxLeft);
		ImageView autoveloxRight = (ImageView) view.getTag(R.id.zoneAutoveloxRight);
		zoneNameText.setText(extra.getName());
		leftZoneSpeedText.setTextColor(colorCat[extra.getCatLeft()]);
		rightZoneSpeedText.setTextColor(colorCat[extra.getCatRight()]);
		leftZoneSpeedText.setTypeface(extra.getCatLeft() == 1 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
		rightZoneSpeedText.setTypeface(extra.getCatRight() == 1 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
		if (extra.getCatLeft() != 0)
			leftZoneSpeedText.setText(Short.toString(extra.getSpeedLeft()));
		else
			leftZoneSpeedText.setText(noDataSpeed);
		if (extra.getCatRight() != 0)
			rightZoneSpeedText.setText(Short.toString(extra.getSpeedRight()));
		else
			rightZoneSpeedText.setText(noDataSpeed);
		if (extra.getTrendLeft() != 0) {
			trendLeftText.setImageResource(extra.getTrendLeft());
			trendLeftText.setVisibility(View.VISIBLE);
		} else
			trendLeftText.setVisibility(View.INVISIBLE);
		if (extra.getTrendRight() != 0) {
			trendRightText.setImageResource(extra.getTrendRight());
			trendRightText.setVisibility(View.VISIBLE);
		} else
			trendRightText.setVisibility(View.INVISIBLE);
		if (extra.getWebcam().charAt(0) == camNone)
			cam.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
		else if (Character.isLetter(extra.getWebcam().charAt(0)))
			cam.setImageResource(android.R.drawable.ic_menu_camera);
		else
			cam.setImageResource(android.R.drawable.ic_menu_add);
		if (extra.isAutoveloxLeft())
			autoveloxLeft.setVisibility(View.VISIBLE);
		else
			autoveloxLeft.setVisibility(View.INVISIBLE);
		if (extra.isAutoveloxRight())
			autoveloxRight.setVisibility(View.VISIBLE);
		else
			autoveloxRight.setVisibility(View.INVISIBLE);
	}
}