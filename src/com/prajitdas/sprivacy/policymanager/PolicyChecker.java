package com.prajitdas.sprivacy.policymanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.prajitdas.sprivacy.SPrivacyApplication;
import com.prajitdas.sprivacy.policymanager.util.AccessController;
import com.prajitdas.sprivacy.policymanager.util.PolicyQuery;
import com.prajitdas.sprivacy.policymanager.util.PolicyRule;

public class PolicyChecker {
	/**
	 * This code should be able to find what policies are there to protect what content.
	 * @return
	 */
	public static AccessController isDataAccessAllowed(PolicyQuery policyQuery, Context context) {
		PolicyDBHelper policyDBHelper = new PolicyDBHelper(context);
		SQLiteDatabase policyDB = policyDBHelper.getWritableDatabase();
		PolicyRule tempPolicyRule = policyDBHelper.findPolicyByApp(policyDB, SPrivacyApplication.getConstAppname(), policyQuery.getProviderAuthority());
		return new AccessController(tempPolicyRule.isRule(), tempPolicyRule.getAccessLevel());
	}
}