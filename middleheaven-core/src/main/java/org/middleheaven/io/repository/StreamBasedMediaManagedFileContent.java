package org.middleheaven.io.repository;


public abstract class StreamBasedMediaManagedFileContent  implements MediaStreamableContent {

	private String contentType = "octet-stream";

	public StreamBasedMediaManagedFileContent() {

	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}



}
