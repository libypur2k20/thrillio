package com.semanticsquare.thrillio.entities;

public class WebLink extends Bookmark{
	
	private String url;
	
	private String host;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public boolean isKidFriendlyEligible() {

		if (getUrl().contains("porn") || getTitle().contains("porn")){
			return false;
		}
		else if (getHost().contains("adult")){
			return false;
		}
		else if (getUrl().contains("adult") && !getHost().contains("adult")){
			return true;
		}
		else if (getTitle().contains("adult") && !getHost().contains("adult") && !getUrl().contains("adult")){
			return true;
		}

		return true;
	}

	@Override
	public String toString() {
		return "WebLink{" +
				"url='" + url + '\'' +
				", host='" + host + '\'' +
				"} " + super.toString();
	}
}
