package com.nylas;

import java.util.List;

/**
 * Basic application detail information
 */
public class ApplicationDetail {

	private String application_name;
	private String icon_url;
	private List<String> redirect_uris;
	
	public String getName() {
		return application_name;
	}

	public String getIconUrl() {
		return icon_url;
	}
	
	public List<String> getRedirectUris() {
		return redirect_uris;
	}

	@Override
	public String toString() {
		return "Application [application_name=" + application_name + ", icon_url=" + icon_url + ", redirect_uris="
				+ redirect_uris + "]";
	}
}
