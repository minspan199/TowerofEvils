package com.michael.pan.eviltower.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import androidx.annotation.StringDef;
import androidx.preference.PreferenceManager;

import com.michael.pan.eviltower.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public class LocaleManager {

	@Retention(RetentionPolicy.SOURCE)
	@StringDef({ ENGLISH, CHINESE_SIMPLIFIED, CHINESE_TRADITIONAL})
	public @interface LocaleDef {
		String[] SUPPORTED_LOCALES = { ENGLISH, CHINESE_SIMPLIFIED, CHINESE_TRADITIONAL};
	}

	static final String ENGLISH = "en";
	static final String CHINESE_SIMPLIFIED = "zh";
	static final String CHINESE_TRADITIONAL = "hk";

	/**
	 * SharedPreferences Key
	 */
	private static final String LANGUAGE_KEY = "language_key";

	/**
	 * set current pref locale
	 */
	public static Context setLocale(Context context) {
		return updateResources(context, getLanPref(context));
	}

	/**
	 * Set new Locale with context
	 */
	public static Context setNewLocale(Context context, @LocaleDef String language) {
		setLanguagePref(context, language);
		return updateResources(context, language);
	}

	/**
	 * Get saved Locale from SharedPreferences
	 *
	 * @param context current context
	 * @return current locale key by default return english locale
	 */
	private static String getLanPref(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(context.getString(R.string.pref_lan_key), context.getString(R.string.pref_lan_english_value));
	}

	/**
	 * set pref key
	 */
	private static void setLanguagePref(Context context, String localeKey) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.edit().putString(context.getString(R.string.pref_lan_key), localeKey).apply();
	}

	/**
	 * update resource
	 */
	public static Context updateResources(Context context, String language) {
		Locale locale = Locale.US;
		Log.i("LocaleManger:", "Setting locale to " + language);
		switch (language){
			case ENGLISH:
				locale = Locale.US;
				break;
			case CHINESE_SIMPLIFIED:
				locale = Locale.CHINA;
				break;
			case CHINESE_TRADITIONAL:
				locale = new Locale("zh", "HK");
				break;
		}
		Locale.setDefault(locale);
		Resources res = context.getResources();
		Configuration config = new Configuration(res.getConfiguration());
		config.setLocale(locale);
		context = context.createConfigurationContext(config);
		return context;
	}

	/**
	 * get current locale
	 */
	public static Locale getLocale(Resources res) {
		Configuration config = res.getConfiguration();
		return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0) : config.locale;
	}
}