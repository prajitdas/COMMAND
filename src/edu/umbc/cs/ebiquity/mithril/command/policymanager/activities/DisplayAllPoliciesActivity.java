package edu.umbc.cs.ebiquity.mithril.command.policymanager.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import edu.umbc.cs.ebiquity.mithril.command.R;
import edu.umbc.cs.ebiquity.mithril.command.policymanager.PolicyDBHelper;
import edu.umbc.cs.ebiquity.mithril.command.policymanager.util.PolicyInfo;
/**
 * @purpose: Activity to display all the policies being implemented on the phone
 * @last_edit_date: 08/21/2014
 * @version 1.0
 * @author prajit.das
 */
public class DisplayAllPoliciesActivity extends Activity {
	private ArrayList<HashMap<String, String>> listOfPoliciesInStringForm;
	private ListView mListView;
	private SimpleAdapter mAdapter;
	private PolicyDBHelper db;
	private SQLiteDatabase database; 
	private String[] mapFrom;
	private int[] mapTo;
	private final String labelData = "labelData";
	private final String detailData = "detailData";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_all);
		db = new PolicyDBHelper(this);
		database = db.getWritableDatabase();
		listOfPoliciesInStringForm = new ArrayList<HashMap<String, String>>();

		mapFrom = new String[] {labelData, detailData};
		mapTo = new int[] {R.id.labelData, R.id.detailData};

		for(PolicyInfo aPolicyRule : db.findAllPolicies(database)) {
			HashMap<String, String> tempMap = new HashMap<String, String>();
			tempMap.put(labelData, aPolicyRule.getLabelData());
			tempMap.put(detailData, aPolicyRule.getDetailData());
			listOfPoliciesInStringForm.add(tempMap);
		}
	    Collections.sort(listOfPoliciesInStringForm, new Comparator<HashMap< String,String >>() {

	        @Override
	        public int compare(HashMap<String, String> first,
	                HashMap<String, String> second) {
	            String firstValue = first.get(labelData);
	            String secondValue = second.get(labelData);
	            return firstValue.compareTo(secondValue);
	        }
	    });
	    
	    loadView(listOfPoliciesInStringForm);
	}

	private void loadView(ArrayList<HashMap<String, String>> list) {
		mListView = (ListView) findViewById(R.id.listViewAllEntities);
		mAdapter = new SimpleAdapter(this, list, R.layout.list_item, mapFrom, mapTo);
		mListView.setAdapter(mAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		database = db.getWritableDatabase();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		db.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_all_data, menu);
		return true;
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
}