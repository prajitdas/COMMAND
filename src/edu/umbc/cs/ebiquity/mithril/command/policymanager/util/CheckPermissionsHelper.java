package edu.umbc.cs.ebiquity.mithril.command.policymanager.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import edu.umbc.cs.ebiquity.mithril.command.COMMANDApplication;

public final class CheckPermissionsHelper {
	/**
	 * Since we are using Marshmallow we have to check for these permissions now 
	 * <uses-permission android:name="android.permission.READ_CONTACTS" />
	 * <uses-permission android:name="android.permission.READ_CALL_LOG" />
	 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
	 * The next one isn't a dangerous permission so why care?
	 * <uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES"/>
	 * <!-- Required permission not required at or below Android 4.3 API level -->
	 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
	 * <!-- Unusual but true! -->
	 */
	public static void checkMarshMallowPermissions(Context context, Activity activity) {
		if (android.os.Build.VERSION.SDK_INT >= 23) {
			checkReadContactsPermission(context, activity);
			checkReadCallLogsPermission(context, activity);
			checkReadExternalStoragePermission(context, activity);
			checkWriteExternalStoragePermission(context, activity);
		     // only for Marshmallow and newer versions 
		}
	}

	public static void checkReadExternalStoragePermission(Context context, Activity activity) {
		String[] tempList = new String[]{COMMANDApplication.getPermissionsCOMMAND()[3]};
		// Here, contextActivity is the current activity
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
				Log.d(COMMANDApplication.getDebugTag(),"shouldShowRequestPermissionRationale");
				// Show an expanation to the user *asynchronously* -- don't block
				// context thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.
			} 
			else {
				// No explanation needed, we can request the permission.
				ActivityCompat.requestPermissions(activity, tempList, COMMANDApplication.getConstPermissionReadExternalStorage());
				Log.d(COMMANDApplication.getDebugTag(),"requestPermissions");
				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}
		}
	}

	public static void checkReadCallLogsPermission(Context context, Activity activity) {
		String[] tempList = new String[]{COMMANDApplication.getPermissionsCOMMAND()[1]};
		// Here, contextActivity is the current activity
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CALL_LOG)) {
				Log.d(COMMANDApplication.getDebugTag(),"shouldShowRequestPermissionRationale");
				// Show an expanation to the user *asynchronously* -- don't block
				// context thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.
			} 
			else {
				// No explanation needed, we can request the permission.
				ActivityCompat.requestPermissions(activity, tempList, COMMANDApplication.getConstPermissionReadCallLog());
				Log.d(COMMANDApplication.getDebugTag(),"requestPermissions");
				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}
		}
	}

	public static void checkWriteExternalStoragePermission(Context context, Activity activity) {
		String[] tempList = new String[]{COMMANDApplication.getPermissionsCOMMAND()[2]};
		// Here, contextActivity is the current activity
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				Log.d(COMMANDApplication.getDebugTag(),"shouldShowRequestPermissionRationale");
				// Show an expanation to the user *asynchronously* -- don't block
				// context thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.
			} 
			else {
				// No explanation needed, we can request the permission.
				ActivityCompat.requestPermissions(activity, tempList, COMMANDApplication.getConstPermissionWriteExternalStorage());
				Log.d(COMMANDApplication.getDebugTag(),"requestPermissions");
				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}
		}
	}

	public static void checkReadContactsPermission(Context context, Activity activity) {
		String[] tempList = new String[]{COMMANDApplication.getPermissionsCOMMAND()[0]};
		// Here, contextActivity is the current activity
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)) {
				Log.d(COMMANDApplication.getDebugTag(),"shouldShowRequestPermissionRationale");
				// Show an expanation to the user *asynchronously* -- don't block
				// context thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.
			} 
			else {
				// No explanation needed, we can request the permission.
				ActivityCompat.requestPermissions(activity, tempList, COMMANDApplication.getConstPermissionReadContacts());
				Log.d(COMMANDApplication.getDebugTag(),"requestPermissions");
				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}
		}
	}
}