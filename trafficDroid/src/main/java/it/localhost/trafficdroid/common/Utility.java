package it.localhost.trafficdroid.common;

import it.localhost.trafficdroid.R;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

public class Utility {
	private static final String BLANK = "";
	private static final String AD_FREE = "adFree";
	private static final String INTERSTITIAL_FREE = "interstitialFree";
	private static final String PATENTE_PWD = "PatentePwd";
	private static final String PATENTE_USR = "PatenteUsr";
	private static final String exceptionCheck = "exceptionCheck";
	private static final String EX_MSG = "exceptionMsg";
	private static final String WIDGET_STREET_STREET = "widgetStreetStreet";
	private static final String WIDGET_ZONE_STREET = "widgetZoneStreet";
	private static final String WIDGET_ZONE_ZONE = "widgetZoneZone";

	public static boolean isAdFree(Context context) {
		return getPrefBoolean(context, AD_FREE, false);
	}

	public static void setAdFree(Context context, boolean value) {
		getEditor(context).putBoolean(AD_FREE, value).commit();
	}

	public static boolean isInterstitialFree(Context context) {
		return getPrefBoolean(context, INTERSTITIAL_FREE, false);
	}

	public static void setInterstitialFree(Context context, boolean value) {
		getEditor(context).putBoolean(INTERSTITIAL_FREE, value).commit();
	}

	public static String getPatenteUsr(Context context) {
		return getPrefString(context, PATENTE_USR, BLANK);
	}

	public static void setPatenteUsr(Context context, String value) {
		getEditor(context).putString(PATENTE_USR, value).commit();
	}

	public static String getPatentePwd(Context context) {
		return getPrefString(context, PATENTE_PWD, BLANK);
	}

	public static void setPatentePwd(Context context, String value) {
		getEditor(context).putString(PATENTE_PWD, value).commit();
	}

	public static String getExMsg(Context context) {
		return getPrefString(context, EX_MSG, "Unknown Error");
	}

	public static String getProviderTraffic(Context context) {
		return getPrefString(context, R.string.providerTrafficKey, R.string.providerTrafficDefault);
	}

	public static String getChiaroveggenzaTime(Context context) {
		return getPrefString(context, R.string.chiaroveggenzaTimeKey, R.string.chiaroveggenzaTimeDefault);
	}

	public static String getNotificationSpeed(Context context) {
		return getPrefString(context, R.string.notificationSpeedKey, R.string.notificationSpeedDefault);
	}

	public static String getTrendSpeed(Context context) {
		return getPrefString(context, R.string.trendSpeedKey, R.string.trendSpeedDefault);
	}

	public static int getWidgetStreetStreet(Context context, int mAppWidgetId) {
		return getPrefInt(context, WIDGET_STREET_STREET + mAppWidgetId, 0);
	}

	public static void setWidgetStreetStreet(Context context, int mAppWidgetId, int value) {
		getEditor(context).putInt(WIDGET_STREET_STREET + mAppWidgetId, value).commit();
	}

	public static int getWidgetZoneStreet(Context context, int mAppWidgetId) {
		return getPrefInt(context, WIDGET_ZONE_STREET + mAppWidgetId, 0);
	}

	public static void setWidgetZoneStreet(Context context, int mAppWidgetId, int value) {
		getEditor(context).putInt(WIDGET_ZONE_STREET + mAppWidgetId, value).commit();
	}

	public static int getWidgetZoneZone(Context context, int mAppWidgetId) {
		return getPrefInt(context, WIDGET_ZONE_ZONE + mAppWidgetId, 0);
	}

	public static void setWidgetZoneZone(Context context, int mAppWidgetId, int value) {
		getEditor(context).putInt(WIDGET_ZONE_ZONE + mAppWidgetId, value).commit();
	}

	public static boolean isBerserkKey(Context context) {
		return getPrefBoolean(context, R.string.berserkKey, R.string.berserkDefault);
	}

	public static boolean isChiaroveggenza(Context context) {
		return getPrefBoolean(context, R.string.chiaroveggenzaEnablerKey, R.string.chiaroveggenzaEnablerDefault);
	}

	public static boolean isBadnewsEnabler(Context context) {
		return getPrefBoolean(context, R.string.badnewsEnablerKey, R.string.badnewsEnablerDefault);
	}

	public static boolean isAllStreets(Context context) {
		return getPrefBoolean(context, R.string.allStreetsKey, R.string.allStreetsDefault);
	}

	public static boolean isExCheck(Context context) {
		return getPrefBoolean(context, exceptionCheck, false);
	}

	public static void setExCheck(Context context, boolean value) {
		getEditor(context).putBoolean(exceptionCheck, value).commit();
	}

	public static void setExMsg(Context context, String value) {
		getEditor(context).putString(EX_MSG, value).commit();
	}

	public static boolean isEnabled(Context context, String key) {
		return getPrefBoolean(context, key, false);
	}

	public static int getVersionCode(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}
	}

	private static Editor getEditor(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).edit();
	}

	private static boolean getPrefBoolean(Context context, String key, Boolean def) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, def);
	}

	private static boolean getPrefBoolean(Context context, int key, int def) {
		return getPrefBoolean(context, context.getString(key), Boolean.parseBoolean(context.getString(def)));
	}

	private static int getPrefInt(Context context, String key, int def) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, def);
	}

	private static String getPrefString(Context context, String key, String def) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(key, def);
	}

	private static String getPrefString(Context context, int key, int def) {
		return getPrefString(context, context.getString(key), context.getString(def));
	}
}