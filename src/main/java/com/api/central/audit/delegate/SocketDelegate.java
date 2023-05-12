package com.api.central.audit.delegate;

import java.util.List;

import com.api.central.audit.entity.SocketBean;

public interface SocketDelegate {

	public List<SocketBean> getMessage(String userName);

}
