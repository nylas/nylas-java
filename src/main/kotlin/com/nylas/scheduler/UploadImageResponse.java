package com.nylas.scheduler;

public class UploadImageResponse {

	private String filename;
	private String originalFilename;
	private String publicUrl;
	private String signedUrl;

	public String getFilename() {
		return filename;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public String getPublicUrl() {
		return publicUrl;
	}

	public String getSignedUrl() {
		return signedUrl;
	}

	@Override
	public String toString() {
		return "UploadImageResponse [" +
				"filename='" + filename + '\'' +
				", originalFilename='" + originalFilename + '\'' +
				", publicUrl='" + publicUrl + '\'' +
				", signedUrl='" + signedUrl + '\'' +
				']';
	}
}
