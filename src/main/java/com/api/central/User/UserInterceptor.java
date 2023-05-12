package com.api.central.User;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserInterceptor extends HandlerInterceptorAdapter{

	private UserDetail user;

	@Autowired
	public void setUser(UserDetail user) {
		this.user = user;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object userDtls = (auth != null) ? auth.getPrincipal() :  null;
		
		if (userDtls instanceof UserDetail)
			user = (UserDetail) userDtls;
		Set<Roles> r = new HashSet<>();
		if(auth != null){
			Roles role = new Roles();
			user.setUsername(auth.getName());
			for(GrantedAuthority authorities : auth.getAuthorities()){
				role.setRolename(authorities.toString());
				r.add(role);
			}
			r.add(role);
			user.setRoles(r);
			}
		
		if(userDtls == null)
			return false;

		return true;
	}
}
