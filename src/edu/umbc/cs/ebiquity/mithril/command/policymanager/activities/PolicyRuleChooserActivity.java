package edu.umbc.cs.ebiquity.mithril.command.policymanager.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;
import edu.umbc.cs.ebiquity.mithril.command.COMMANDApplication;
import edu.umbc.cs.ebiquity.mithril.command.R;
import edu.umbc.cs.ebiquity.mithril.command.policymanager.PolicyDBHelper;
import edu.umbc.cs.ebiquity.mithril.command.policymanager.util.CheckPermissionsHelper;
import edu.umbc.cs.ebiquity.mithril.command.policymanager.util.PolicyInfo;
/**
 * @purpose: Activity to control the policies that are being implemented on the phone
 * @last_edit_date: 08/21/2014
 * @version 1.0
 * @author prajit.das
 */
public class PolicyRuleChooserActivity extends Activity {
	private Button mBtnDBOps;
	private TableLayout mTableOfPolicies;
//	private TextView mLargeTextView;
	
	private PolicyDBHelper db;
	private SQLiteDatabase database;
	
	private ArrayList<TableRow> mTableRows;
	private ArrayList<ToggleButton> mToggleButtons;
	private ArrayList<RadioButton> mRadioButtons;
	private ArrayList<RadioGroup> mRadioGroups;
	
	private SparseArray<PolicyInfo> policyViewMap;
	
	private static int viewCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policy_rule_chooser);
		
		mTableRows = new ArrayList<TableRow>();
		mToggleButtons = new ArrayList<ToggleButton>();
		mRadioButtons = new ArrayList<RadioButton>();
		mRadioGroups = new ArrayList<RadioGroup>();
		policyViewMap = new SparseArray<PolicyInfo>();
		viewCount = 0;
		
		db = new PolicyDBHelper(this);
		database = db.getWritableDatabase();
		
		instantiateViews();
		addOnClickListener();
		CheckPermissionsHelper.checkMarshMallowPermissions(getApplicationContext(),this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.policy_rule_chooser, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		db.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		database = db.getWritableDatabase();
	}
	
	private void instantiateViews() {
		mBtnDBOps = (Button) findViewById(R.id.btnDBOps);
		mTableOfPolicies = (TableLayout) findViewById(R.id.tableOfPolicies);
		/**
		 * This is for testing the Android ID content provider
		 */
//		mLargeTextView = (TextView) findViewById(R.id.textViewAndroidId);
//		mLargeTextView.setText("The Android ID obtained is: "+getAndroidId());
		/*-------------------------------------------------------------*/
		addTableRow();
		addDataRows();
	}

	private void addDataRows() {
		try {
			addTableRow(db.findPolicyByAppProv(database, 
					COMMANDApplication.getConstAppForWhichWeAreSettingPolicies(), 
					COMMANDApplication.getConstImages()));
			addTableRow(db.findPolicyByAppProv(database, 
					COMMANDApplication.getConstAppForWhichWeAreSettingPolicies(), 
					COMMANDApplication.getConstFiles()));
			addTableRow(db.findPolicyByAppProv(database, 
					COMMANDApplication.getConstAppForWhichWeAreSettingPolicies(), 
					COMMANDApplication.getConstContacts()));
		} catch(SQLException e) {
			COMMANDApplication.makeToast(this, "Seems like there is no data in the database");
		}
	}
	
	@SuppressWarnings("deprecation")
	private void addTableRow() {
		TableRow tblRow = new TableRow(this);
		TextView mTextViewPolicyStmt = new TextView(this);
		TextView mTextViewPolicyValu = new TextView(this);
		TextView mTextViewPolicyLevl = new TextView(this);
		
		mTextViewPolicyStmt.setText(R.string.text_view_policy_conditions);
		if (android.os.Build.VERSION.SDK_INT >= 23)
			mTextViewPolicyStmt.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
		else
			mTextViewPolicyStmt.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
		mTextViewPolicyStmt.setTypeface(Typeface.SERIF, Typeface.BOLD);
		mTextViewPolicyStmt.setGravity(Gravity.CENTER);
		mTextViewPolicyStmt.setTextColor(ContextCompat.getColor(this, R.color.WhiteSmoke));
		
		mTextViewPolicyValu.setText(R.string.text_view_policy_setting);
		if (android.os.Build.VERSION.SDK_INT >= 23)
			mTextViewPolicyStmt.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
		else
			mTextViewPolicyStmt.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
		mTextViewPolicyValu.setTypeface(Typeface.SERIF, Typeface.BOLD);
		mTextViewPolicyValu.setGravity(Gravity.CENTER);
		mTextViewPolicyValu.setTextColor(ContextCompat.getColor(this, R.color.WhiteSmoke));
		
		mTextViewPolicyLevl.setText(R.string.text_view_policy_level);
		if (android.os.Build.VERSION.SDK_INT >= 23)
			mTextViewPolicyStmt.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
		else
			mTextViewPolicyStmt.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
		mTextViewPolicyLevl.setTypeface(Typeface.SERIF, Typeface.BOLD);
		mTextViewPolicyLevl.setGravity(Gravity.CENTER);
		mTextViewPolicyLevl.setTextColor(ContextCompat.getColor(this, R.color.WhiteSmoke));
		
		tblRow.addView(mTextViewPolicyStmt);
		tblRow.addView(mTextViewPolicyValu);
		tblRow.addView(mTextViewPolicyLevl);
		tblRow.setBackgroundColor(ContextCompat.getColor(this, R.color.Black));
		mTableOfPolicies.addView(tblRow);
	}
	
	@SuppressWarnings("deprecation")
	private void addTableRow(PolicyInfo aPolicyRule) {
		if(aPolicyRule!=null){
			policyViewMap.put(viewCount++, aPolicyRule);
			
			TableRow tempTableRow = new TableRow(this);
			
			TextView tempViewPolcStmt = new TextView(this);
			ToggleButton tempToggleButton = new ToggleButton(this);
			
			RadioButton tempRadioButtonNoData = new RadioButton(this);
			RadioButton tempRadioButtonFakeData = new RadioButton(this);
//			RadioButton tempRadioButtonAnonymizedData = new RadioButton(this);
			RadioGroup tempViewPolGroup = new RadioGroup(this);

			tempViewPolcStmt.setText(aPolicyRule.toString());
			tempViewPolcStmt.setTextColor(ContextCompat.getColor(this, R.color.DarkBlue));
			tempViewPolcStmt.setTypeface(Typeface.SERIF, Typeface.NORMAL);
			tempViewPolcStmt.setGravity(Gravity.CENTER);
			
			tempToggleButton.setTextOn(COMMANDApplication.getConstAccessGranted());
			tempToggleButton.setTextOff(COMMANDApplication.getConstAccessDenied());
			tempToggleButton.setChecked(aPolicyRule.isRule());
			if(aPolicyRule.isRule())
				tempToggleButton.setBackgroundColor(ContextCompat.getColor(this, R.color.Green));
			else
				tempToggleButton.setBackgroundColor(ContextCompat.getColor(this, R.color.Red));
			tempToggleButton.setTextColor(ContextCompat.getColor(this, R.color.White));
			tempToggleButton.setTypeface(Typeface.SERIF, Typeface.NORMAL);
			
			tempRadioButtonNoData.setText(R.string.radio_button_text_no_data);
			
			if (android.os.Build.VERSION.SDK_INT >= 23)
				tempRadioButtonNoData.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
			else
				tempRadioButtonNoData.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
			if (android.os.Build.VERSION.SDK_INT >= 23)
				tempRadioButtonNoData.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
			else
				tempRadioButtonNoData.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
			
			tempRadioButtonNoData.setTextColor(ContextCompat.getColor(this, R.color.DarkBlue));
			tempRadioButtonNoData.setTypeface(Typeface.SERIF, Typeface.NORMAL);

			tempRadioButtonFakeData.setText(R.string.radio_button_text_fake_data);
			if (android.os.Build.VERSION.SDK_INT >= 23)
				tempRadioButtonFakeData.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
			else
				tempRadioButtonFakeData.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
			tempRadioButtonFakeData.setTextColor(ContextCompat.getColor(this, R.color.DarkBlue));
			tempRadioButtonFakeData.setTypeface(Typeface.SERIF, Typeface.NORMAL);

//			tempRadioButtonAnonymizedData.setText(R.string.radio_button_text_anonymous_data);
//			tempRadioButtonAnonymizedData.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
//			tempRadioButtonAnonymizedData.setTextColor(ContextCompat.getColor(this, R.color.DarkBlue));
//			tempRadioButtonAnonymizedData.setTypeface(Typeface.SERIF, Typeface.NORMAL);

			tempViewPolGroup.setOrientation(RadioGroup.VERTICAL);
			tempViewPolGroup.addView(tempRadioButtonNoData);
			tempViewPolGroup.addView(tempRadioButtonFakeData);
//			tempViewPolGroup.addView(tempRadioButtonAnonymizedData);
			tempViewPolGroup.clearCheck();
			if(aPolicyRule.isRule()) {
				tempRadioButtonNoData.setEnabled(false);
				tempRadioButtonFakeData.setEnabled(false);
//				tempRadioButtonAnonymizedData.setEnabled(false);
			} else {
				tempRadioButtonNoData.setEnabled(true);
				tempRadioButtonFakeData.setEnabled(true);
//				tempRadioButtonAnonymizedData.setEnabled(true);
				if(aPolicyRule.getAccessLevel() == 1)
					tempRadioButtonNoData.setChecked(true);
				else if(aPolicyRule.getAccessLevel() == 2)
					tempRadioButtonFakeData.setChecked(true);
//				else
//					tempRadioButtonAnonymizedData.setChecked(true);
			}
			
			tempTableRow.addView(tempViewPolcStmt);
			tempTableRow.addView(tempToggleButton);
			tempTableRow.addView(tempViewPolGroup);
			tempTableRow.setGravity(Gravity.CENTER_VERTICAL);
			tempTableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.LightBlue));

			mTableOfPolicies.addView(tempTableRow);

			mTableRows.add(tempTableRow);
			mToggleButtons.add(tempToggleButton);
			mRadioButtons.add(tempRadioButtonNoData);
			mRadioButtons.add(tempRadioButtonFakeData);
//			mRadioButtons.add(tempRadioButtonAnonymizedData);
			mRadioGroups.add(tempViewPolGroup);
		}
	}

	private void togglePolicy(int indexOfPolicy) {
//		EbAndMWApplication.makeToast(this, "The index I am receiving is: "
//				+Integer.toString(indexOfPolicy));
		PolicyInfo tempPolicyRule = db.findPolicyByID(database, policyViewMap.get(indexOfPolicy).getId());
		tempPolicyRule.togglePolicy();
		mRadioGroups.get(indexOfPolicy).clearCheck();
//		if(!tempPolicyRule.isRule()) {
//			mRadioButtons.get(indexOfPolicy*3).setEnabled(true);
//			mRadioButtons.get((indexOfPolicy*3)+1).setEnabled(true);
//			mRadioButtons.get((indexOfPolicy*3)+2).setEnabled(true);
//
//			mRadioButtons.get(indexOfPolicy*3).setChecked(true);
//			mRadioButtons.get((indexOfPolicy*3)+1).setChecked(false);
//			mRadioButtons.get((indexOfPolicy*3)+2).setChecked(false);
//			tempPolicyRule.changeAccessLevel(1);
//		}
//		else{
//			mRadioButtons.get(indexOfPolicy*3).setEnabled(false);
//			mRadioButtons.get((indexOfPolicy*3)+1).setEnabled(false);
//			mRadioButtons.get((indexOfPolicy*3)+2).setEnabled(false);
//
//			mRadioButtons.get(indexOfPolicy*3).setChecked(false);
//			mRadioButtons.get((indexOfPolicy*3)+1).setChecked(false);
//			mRadioButtons.get((indexOfPolicy*3)+2).setChecked(false);
//			tempPolicyRule.changeAccessLevel(0);
//		}
		if(!tempPolicyRule.isRule()) {
			mRadioButtons.get(indexOfPolicy*2).setEnabled(true);
			mRadioButtons.get((indexOfPolicy*2)+1).setEnabled(true);

			mRadioButtons.get(indexOfPolicy*2).setChecked(true);
			mRadioButtons.get((indexOfPolicy*2)+1).setChecked(false);
			tempPolicyRule.changeAccessLevel(1);
		}
		else{
			mRadioButtons.get(indexOfPolicy*2).setEnabled(false);
			mRadioButtons.get((indexOfPolicy*2)+1).setEnabled(false);

			mRadioButtons.get(indexOfPolicy*2).setChecked(false);
			mRadioButtons.get((indexOfPolicy*2)+1).setChecked(false);
			tempPolicyRule.changeAccessLevel(0);
		}
		policyViewMap.put(indexOfPolicy, tempPolicyRule);
		db.updatePolicyRule(database, tempPolicyRule);
	}

	private void changeAccessLevel(int indexOfPolicy, int accessLevel) {
//		EbAndMWApplication.makeToast(this, "The access level I am receiving is: "
//				+Integer.toString(accessLevel));
		PolicyInfo tempPolicyRule = db.findPolicyByID(database, policyViewMap.get(indexOfPolicy).getId());
		tempPolicyRule.changeAccessLevel(accessLevel);
		policyViewMap.put(indexOfPolicy, tempPolicyRule);
		db.updatePolicyRule(database, tempPolicyRule);
	}

	private void addOnClickListener() {
		mBtnDBOps.setOnClickListener(new OnClickListener() {
			//Button to show all the policies at the same time
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DBOpsActivity.class);
				startActivity(intent);
			}
		});
		
		for(int i = 0; i < mToggleButtons.size(); i++) {
			final int index = i;
			mToggleButtons.get(i).setOnClickListener(new OnClickListener() {
				//Toggle Button to identify which policy was modified
				@Override
				public void onClick(View v) {
					if(policyViewMap.get(index).isRule())
						v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.Red));
					else
						v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.Green));
					togglePolicy(index);
				}
			});
		}

		for(int i = 0; i < mRadioButtons.size(); i++) {
			final int index = i;
			mRadioButtons.get(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					changeAccessLevel(index/2, index%2+1);
//					changeAccessLevel(index/3, index%3+1);
				}
			});
		}
	}
	/**
	 * This is for testing the Android ID content provider
	 */
	private static final Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
	private static final String ID_KEY = "android_id";
	@SuppressWarnings("unused")
	private String getAndroidId() {
		String[] selectionArgs = { ID_KEY };
		Cursor c = getContentResolver().query(URI, null, null, selectionArgs, null);

		int countOfColumns = c.getColumnCount();
		int countOfRows = c.getCount();
		Log.v(COMMANDApplication.getDebugTag(), "Count of columns: "+countOfColumns);
		Log.v(COMMANDApplication.getDebugTag(), "Count of rows: "+countOfRows);
		for(String columnName : c.getColumnNames())
			Log.v(COMMANDApplication.getDebugTag(), "Column: "+columnName);
		Log.v(COMMANDApplication.getDebugTag(), URI.getEncodedAuthority());
		ProviderInfo[] providerArray;
		PackageManager pm = getPackageManager();
		for(PackageInfo pack : pm.getInstalledPackages(PackageManager.GET_PROVIDERS)) {
			providerArray = pack.providers;
			if (providerArray != null)
				for (ProviderInfo provider : providerArray)
					if(provider.authority.equals(URI.getEncodedAuthority()))
						Log.v(COMMANDApplication.getDebugTag(), pm.getApplicationLabel(provider.applicationInfo).toString()+" and "+provider.name);
		}
		if (!c.moveToFirst() || c.getColumnCount() < 2)
			return null;
		try {
			return Long.toHexString(Long.parseLong(c.getString(1)));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}