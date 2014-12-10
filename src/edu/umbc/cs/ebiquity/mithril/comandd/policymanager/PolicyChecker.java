package edu.umbc.cs.ebiquity.mithril.comandd.policymanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import edu.umbc.cs.ebiquity.mithril.comandd.EbAndMWApplication;
import edu.umbc.cs.ebiquity.mithril.comandd.policymanager.util.AccessControl;
import edu.umbc.cs.ebiquity.mithril.comandd.policymanager.util.PolicyInfo;
import edu.umbc.cs.ebiquity.mithril.comandd.policymanager.util.PolicyQuery;
/**
 * @purpose: This class returns an {@link #AccessControl} object which contains the policy information (granted/denied) and the access level.
 * The access level determines whether data provided will be real data, fake data, no data or anonymous data (will be implemented eventually).
 * @last_edit_date: 08/21/2014
 * @version 1.0
 * @author prajit.das
 */
public class PolicyChecker {
	/**
	 * This code should be able to find what policies are there to protect what content.
	 * @return
	 */
	public static AccessControl isDataAccessAllowed(PolicyQuery policyQuery, Context context) {
		getCurrentContext(policyQuery);
		PolicyDBHelper policyDBHelper = new PolicyDBHelper(context);
		SQLiteDatabase policyDB = policyDBHelper.getWritableDatabase();
		PolicyInfo tempPolicyInfo = policyDBHelper.findPolicyByAppProv(policyDB, 
				EbAndMWApplication.getConstAppForWhichWeAreSettingPolicies(), 
				policyQuery.getProviderAuthority());
		return new AccessControl(tempPolicyInfo.isRule(), tempPolicyInfo.getAccessLevel());
	}

	private static void getCurrentContext(PolicyQuery policyQuery) {
		policyQuery.getUserContext().setLocation("*");
		policyQuery.getUserContext().setActivity("*");
		policyQuery.getUserContext().setIdentity("*");
		policyQuery.getUserContext().setTime("*");
	}
}