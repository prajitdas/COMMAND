package edu.umbc.cs.ebiquity.mithril.command.policymanager.util;
/**
 * @purpose: Util class to handle user context information
 * @last_edit_date: 08/21/2014
 * @version 1.0
 * @author prajit.das
 */
public class UserContext {
	private String location;
	private String activity;
	private String time;
	private String identity;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public UserContext(String location, String activity, String time,
			String identity) {
		setLocation(location);
		setActivity(activity);
		setTime(time);
		setIdentity(identity);
	}
	public UserContext() {
	}
}