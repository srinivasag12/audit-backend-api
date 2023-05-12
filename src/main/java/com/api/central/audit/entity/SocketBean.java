package com.api.central.audit.entity;

public class SocketBean {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public SocketBean(String message) {
		super();
		this.message = message;
	}

	/*@Override
	public String toString() {
		return "SocketBean [message=" + message + "]";
	}*/
	
}
