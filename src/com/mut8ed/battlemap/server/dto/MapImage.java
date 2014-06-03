package com.mut8ed.battlemap.server.dto;

import java.util.Date;

public class MapImage {

	private String id;
	private Date createDate;
	private String filePath;
	private String md5;
	
	public MapImage(String id, Date createDate, String filePath, String md5) {
		this.id = id;
		this.createDate = createDate;
		this.filePath = filePath;
		this.md5 = md5;
	}

	public MapImage(String filePath, String md5) {
		createDate = new Date();
		this.filePath = filePath;
		this.md5 = md5;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	
	
}
