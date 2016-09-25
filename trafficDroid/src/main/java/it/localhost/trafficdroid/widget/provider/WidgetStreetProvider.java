package it.localhost.trafficdroid.widget.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.activity.MainActivity;
import it.localhost.trafficdroid.common.Utility;
import it.localhost.trafficdroid.dto.MainDTO;
import it.localhost.trafficdroid.dto.StreetDTO;
import it.localhost.trafficdroid.service.PersistanceService;

public class WidgetStreetProvider extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int appWidgetId : appWidgetIds)
			updateAppWidget(context, appWidgetManager, appWidgetId);
	}

	public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int mAppWidgetId) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		views.setOnClickPendingIntent(R.id.widget, PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0));
		try {
			MainDTO dto = PersistanceService.retrieve(context);
			StreetDTO street = dto.getStreet(Utility.getWidgetStreetStreet(context, mAppWidgetId));
			if (street != null) {
				views.setTextViewText(R.id.zoneName, DateFormat.getTimeFormat(context).format(dto.getTrafficTime()) + " " + street.getName());
				views.setTextViewText(R.id.zoneSpeedLeft, Short.toString(street.getSpeedLeft()));
				views.setTextViewText(R.id.zoneSpeedRight, Short.toString(street.getSpeedRight()));
				views.setImageViewResource(R.id.streetDirLeft, street.getDirectionLeft());
				views.setImageViewResource(R.id.streetDirRight, street.getDirectionRight());
				views.setImageViewResource(R.id.trendLeft, street.getTrendLeft());
				views.setImageViewResource(R.id.trendRight, street.getTrendRight());
			}
		} catch (Exception e) {
			// Do nothing
		}
		appWidgetManager.updateAppWidget(mAppWidgetId, views);
	}
}