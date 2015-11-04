package edu.umbc.cs.ebiquity.mithril.command;

import android.Manifest;
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
public class COMMANDApplication extends Application {
	private static final String [] PermissionsCOMMAND = {
			Manifest.permission.READ_CONTACTS,
			Manifest.permission.READ_CALL_LOG,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			"com.google.android.providers.gsf.permission.READ_GSERVICES"
		};

	private static final int CONST_PERMISSION_READ_CONTACTS = 1;

	private static final int CONST_PERMISSION_READ_CALL_LOG = 1;
	
	private static final int CONST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1;
	
	private static final int CONST_PERMISSION_READ_EXTERNAL_STORAGE = 1;
	
	private static final int CONST_PERMISSION_READ_GSERVICES = 1;

	private static final String CONST_ACCESS_DENIED = "Denied";

	private static final String CONST_ACCESS_GRANTED = "Granted";

	private static final String CONST_ANDROID_ID = "androidid";
	
	private static final String CONST_ANNONYMOUS = "anonymized";

	private static final String CONST_ANONYMIZED_AUTHORITY_PREFIX = "edu.umbc.cs.ebiquity.mithril.command.anonymizedcontentprovider.Content.";

	private static final String CONST_APP_FOR_WHICH_WE_ARE_SETTING_POLICIES = "edu.umbc.cs.ebiquity.mithril.parserapp";

	private static final String CONST_AUDIOS = "audios";

	private static final String CONST_CALL_LOGS = "calls";

	private static final String CONST_CONTACTS = "contacts";

	private static final String CONST_COMMAND_APP_BROADCAST_INTENT = "edu.umbc.cs.ebiquity.mithril.command.intent.action.DATA_REQUEST";

	private static final String CONST_COMMAND_AUTHORITY = "edu.umbc.cs.ebiquity.mithril.command.contentprovider.Content";
	
	private static final String CONST_FAKE = "fake";
	
	private static final String CONST_FAKE_AUTHORITY_PREFIX = "edu.umbc.cs.ebiquity.mithril.command.fakecontentprovider.Content.";
	
	private static final String CONST_FILES = "files";
	
	private static final String CONST_IMAGES = "images";
	
	private static final String CONST_SCHEME = "content://";

	private static final String CONST_SLASH = "/";

	private static final String CONST_VIDEOS = "videos";

	private static final String DEBUG_TAG = "COMANDDApplicationDebugTag";
	
	private static COMMANDApplication singleton;

	public static String getConstAccessDenied() {
		return CONST_ACCESS_DENIED;
	}

	public static String getConstAccessGranted() {
		return CONST_ACCESS_GRANTED;
	}

	public static String getConstAndroidId() {
		return CONST_ANDROID_ID;
	}
	
	public static String getConstAnnonymous() {
		return CONST_ANNONYMOUS;
	}

	public static String getConstAnonymizedAuthorityPrefix() {
		return CONST_ANONYMIZED_AUTHORITY_PREFIX;
	}

	public static String getConstAppForWhichWeAreSettingPolicies() {
		return CONST_APP_FOR_WHICH_WE_ARE_SETTING_POLICIES;
	}

	public static String getConstAudios() {
		return CONST_AUDIOS;
	}

	public static String getConstCallLogs() {
		return CONST_CALL_LOGS;
	}

	public static String getConstContacts() {
		return CONST_CONTACTS;
	}

	/**
	 * @return the constCOMMANDAppBroadcastIntent
	 */
	public static String getConstCOMMANDAppBroadcastIntent() {
		return CONST_COMMAND_APP_BROADCAST_INTENT;
	}

	public static String getConstEbAndMWAuthority() {
		return CONST_COMMAND_AUTHORITY;
	}

	public static String getConstFake() {
		return CONST_FAKE;
	}
	
	public static String getConstFakeAuthorityPrefix() {
		return CONST_FAKE_AUTHORITY_PREFIX;
	}

	public static String getConstFiles() {
		return CONST_FILES;
	}

	public static String getConstImages() {
		return CONST_IMAGES;
	}

	public static String getConstScheme() {
		return CONST_SCHEME;
	}

	public static String getConstSlash() {
		return CONST_SLASH;
	}

	public static String getConstVideos() {
		return CONST_VIDEOS;
	}

	public static String getDebugTag() {
		return DEBUG_TAG;
	}

	public static COMMANDApplication getSingleton() {
		return singleton;
	}

	public static void makeToast(Context context, String someString) {
		Toast.makeText(context, someString, Toast.LENGTH_SHORT).show();
	}

	public static void setSingleton(COMMANDApplication singleton) {
		COMMANDApplication.singleton = singleton;
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

	public static int getConstPermissionReadContacts() {
		return CONST_PERMISSION_READ_CONTACTS;
	}

	public static int getConstPermissionReadCallLog() {
		return CONST_PERMISSION_READ_CALL_LOG;
	}

	public static int getConstPermissionWriteExternalStorage() {
		return CONST_PERMISSION_WRITE_EXTERNAL_STORAGE;
	}

	public static int getConstPermissionReadExternalStorage() {
		return CONST_PERMISSION_READ_EXTERNAL_STORAGE;
	}

	public static int getConstPermissionReadGservices() {
		return CONST_PERMISSION_READ_GSERVICES;
	}

	public static String [] getPermissionsCOMMAND() {
		return PermissionsCOMMAND;
	}
}