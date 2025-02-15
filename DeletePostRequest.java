package com.eblog.demo.controller.request;


public class DeletePostRequest {
    private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public DeletePostRequest(long id) {
		super();
		this.id = id;
	}

	public DeletePostRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}


