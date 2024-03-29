package com.semanticsquare.thrillio.entities;

import com.semanticsquare.thrillio.partner.Shareable;

public class WebLink extends Bookmark implements Shareable {
	
	private String url;
	private String host;
	private String htmlPage;
	private DownloadStatus downloadStatus = DownloadStatus.NOT_ATTEMPTED;

	public enum DownloadStatus {
		NOT_ATTEMPTED,
		SUCCESS,
		FAILED,
		NOT_ELIGIBLE	//Not eligible for download.
	}

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
	public String getHtmlPage() {
		return htmlPage;
	}

	public void setHtmlPage(String htmlPage) {
		this.htmlPage = htmlPage;
	}

	public DownloadStatus getDownloadStatus() {
		return downloadStatus;
	}

	public void setDownloadStatus(DownloadStatus downloadStatus) {
		this.downloadStatus = downloadStatus;
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

	@Override
	public String getItemData() {
		StringBuilder sb = new StringBuilder();

		sb.append("<item>");
			sb.append("<type>Weblink</type>");
			sb.append("<title>").append(getTitle()).append("</title>");
			sb.append("<url>").append(getUrl()).append("</url>");
			sb.append("<host>").append(getHost()).append("</host>");
		sb.append("</item>");

		return sb.toString();
	}
}
