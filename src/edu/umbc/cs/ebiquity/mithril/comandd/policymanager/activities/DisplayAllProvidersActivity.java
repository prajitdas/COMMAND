package edu.umbc.cs.ebiquity.mithril.comandd.policymanager.activities;

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
import edu.umbc.cs.ebiquity.mithril.comandd.R;
import edu.umbc.cs.ebiquity.mithril.comandd.policymanager.PolicyDBHelper;
import edu.umbc.cs.ebiquity.mithril.comandd.policymanager.util.ProvInfo;
/**
 * @purpose: Activity to display the content providers on the phone
 * @last_edit_date: 08/21/2014
 * @version 1.0
 * @author prajit.das
 */
public class DisplayAllProvidersActivity extends Activity {
	private ArrayList<HashMap<String, String>> listOfResources;
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
		listOfResources = new ArrayList<HashMap<String, String>>();

		mapFrom = new String[] {labelData, detailData};
		mapTo = new int[] {R.id.labelData, R.id.detailData};

		for(ProvInfo aProvider : db.findAllProviders(database)) {
			HashMap<String, String> tempMap = new HashMap<String, String>();
			tempMap.put(labelData, aProvider.getLabelData());
			tempMap.put(detailData, aProvider.getDetailData());
			listOfResources.add(tempMap);
		}
	    Collections.sort(listOfResources, new Comparator<HashMap< String,String >>() {

	        @Override
	        public int compare(HashMap<String, String> first,
	                HashMap<String, String> second) {
	            String firstValue = first.get(labelData);
	            String secondValue = second.get(labelData);
	            return firstValue.compareTo(secondValue);
	        }
	    });
	    
		loadView(listOfResources);
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