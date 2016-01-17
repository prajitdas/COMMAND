package edu.umbc.cs.ebiquity.mithril.command.policymanager.util;
/**
 * @purpose: Util class to handle installed services information
 * @last_edit_date: 01/17/2016
 * @version 1.0
 * @author prajit.das
 */
public class ServInfo {
	private int id;
	private String serviceName;
	private String serviceLabel;
	private boolean enabled;
	private boolean exported;
	private String process;
	private String permission;
	
	public ServInfo(int id, String serviceName, String serviceLabel, 
			boolean enabled, boolean exported, String process, String permission) {
		setId(id);
		setServiceName(serviceName);
		setServiceLabel(serviceLabel);
		setEnabled(enabled);
		setExported(exported);
		setProcess(process);
		setPermission(permission);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServInfo other = (ServInfo) obj;
		if (enabled != other.enabled)
			return false;
		if (exported != other.exported)
			return false;
		if (id != other.id)
			return false;
		if (permission == null) {
			if (other.permission != null)
				return false;
		} else if (!permission.equals(other.permission))
			return false;
		if (process == null) {
			if (other.process != null)
				return false;
		} else if (!process.equals(other.process))
			return false;
		if (serviceLabel == null) {
			if (other.serviceLabel != null)
				return false;
		} else if (!serviceLabel.equals(other.serviceLabel))
			return false;
		if (serviceName == null) {
			if (other.serviceName != null)
				return false;
		} else if (!serviceName.equals(other.serviceName))
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public String getPermission() {
		return permission;
	}

	public String getProcess() {
		return process;
	}

	public String getServiceLabel() {
		return serviceLabel;
	}

	public String getServiceName() {
		return serviceName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + (exported ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result + ((permission == null) ? 0 : permission.hashCode());
		result = prime * result + ((process == null) ? 0 : process.hashCode());
		result = prime * result + ((serviceLabel == null) ? 0 : serviceLabel.hashCode());
		result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
		return result;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isExported() {
		return exported;
	}

	private void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	private void setExported(boolean exported) {
		this.exported = exported;
	}

	private void setId(int id) {
		this.id = id;
	}

	private void setPermission(String permission) {
		this.permission = permission;
	}

	private void setProcess(String process) {
		this.process = process;
	}

	private void setServiceLabel(String serviceLabel) {
		this.serviceLabel = serviceLabel;
	}

	private void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return "ServInfo [id=" + id + ", enabled=" + enabled + ", exported=" + exported + ", serviceName=" + serviceName
				+ ", serviceLabel=" + serviceLabel + ", process=" + process + ", permission=" + permission + "]";
	}

}