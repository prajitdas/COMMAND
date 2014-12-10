package edu.umbc.cs.ebiquity.mithril.comandd;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

/**
 * @purpose: This class is to handle all the constants that would be required throughout the application. 
 * @last_edit_date: 08/21/2014
 * @version 1.0
 * @author prajit.das
 */
public class EbAndMWApplication extends Application {
	private static final String CONST_ACCESS_DENIED = "Denied";

	private static final String CONST_ACCESS_GRANTED = "Granted";

	private static final String CONST_ANNONYMOUS = "anonymized";
	
	private static final String CONST_SCHEME = "content://";

	private static final String CONST_ANONYMIZED_AUTHORITY_PREFIX = "edu.umbc.cs.ebiquity.mithril.comandd.anonymizedcontentprovider.Content.";

	private static final String CONST_APP_FOR_WHICH_WE_ARE_SETTING_POLICIES = "edu.umbc.cs.ebiquity.mithril.parserapp";

	private static final String CONST_AUDIOS = "audios";

	private static final String CONST_CONTACTS = "contacts";

	private static final String CONST_CALL_LOGS = "calls";

	private static final String CONST_FAKE = "fake";

	private static final String CONST_FAKE_AUTHORITY_PREFIX = "edu.umbc.cs.ebiquity.mithril.comandd.fakecontentprovider.Content.";
	
	private static final String CONST_FILES = "files";
	
	private static final String CONST_IMAGES = "images";
	
	private static final String CONST_SLASH = "/";
	
	private static final String CONST_EBANDMW_AUTHORITY = "edu.umbc.cs.ebiquity.mithril.comandd.contentprovider.Content";
	
	private static final String CONST_VIDEOS = "videos";

	private static final String DEBUG_TAG = "EbAndMWApplicationDebugTag";

	private static final String CONST_ANDROID_ID = "androidid";

	private static EbAndMWApplication singleton;

	public static String getConstAccessDenied() {
		return CONST_ACCESS_DENIED;
	}

	public static String getConstAccessGranted() {
		return CONST_ACCESS_GRANTED;
	}

	public static String getConstAnnonymous() {
		return CONST_ANNONYMOUS;
	}
	
	public static String getConstAudios() {
		return CONST_AUDIOS;
	}

	public static String getConstContacts() {
		return CONST_CONTACTS;
	}

	public static String getConstFake() {
		return CONST_FAKE;
	}

	public static String getConstFiles() {
		return CONST_FILES;
	}

	public static String getConstImages() {
		return CONST_IMAGES;
	}

	public static String getConstSlash() {
		return CONST_SLASH;
	}

	public static String getConstEbAndMWAuthority() {
		return CONST_EBANDMW_AUTHORITY;
	}

	public static String getConstVideos() {
		return CONST_VIDEOS;
	}

	public static String getDebugTag() {
		return DEBUG_TAG;
	}
	
	public static EbAndMWApplication getSingleton() {
		return singleton;
	}

	public static void makeToast(Context context, String someString) {
		Toast.makeText(context, someString, Toast.LENGTH_SHORT).show();
	}

	public static void setSingleton(EbAndMWApplication singleton) {
		EbAndMWApplication.singleton = singleton;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		setSingleton(this);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public static String getConstAnonymizedAuthorityPrefix() {
		return CONST_ANONYMIZED_AUTHORITY_PREFIX;
	}

	public static String getConstFakeAuthorityPrefix() {
		return CONST_FAKE_AUTHORITY_PREFIX;
	}

	public static String getConstAppForWhichWeAreSettingPolicies() {
		return CONST_APP_FOR_WHICH_WE_ARE_SETTING_POLICIES;
	}

	public static String getConstScheme() {
		return CONST_SCHEME;
	}

	public static String getConstCallLogs() {
		return CONST_CALL_LOGS;
	}

	public static String getConstAndroidId() {
		return CONST_ANDROID_ID;
	}
}