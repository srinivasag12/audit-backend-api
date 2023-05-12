package com.api.central.User;


import java.util.HashSet;

import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository(value = "userRepository")
public class UserRepositoryImpl implements UserRepository {	

	@Autowired
	private SessionFactory sessionFactory;
		
	
	@Override
	@SuppressWarnings("unchecked")
	public UserDetail findUser(String name) {
		
		String findUserId = "SELECT COUNT(A.USER_ID) FROM MA_USERS A WHERE  LOWER(A.USER_ID)=:userid AND A.ACTIVE_STATUS = 1";						
		List<Object[]> userVals = sessionFactory.openSession().createSQLQuery(findUserId).setParameter("userid", name.toLowerCase()).list();			
		
		if(userVals.size()<0)
			return new UserDetail();

		String fetchUserVal = "SELECT A.USER_ID, A.PASSWORD, A.ACTIVE_STATUS, A.COMPANY_ID, C.ROLE_ID, C.ROLE_DESC FROM"
				+ " MA_USERS A, MA_USER_ROLES B, MA_ROLES C WHERE  LOWER(A.USER_ID) = :USERID "
				+ "AND LOWER(B.USER_ID) = LOWER(A.USER_ID) AND B.COMPANY_ID = A.COMPANY_ID AND C.ROLE_ID = B.ROLE_ID AND C.COMPANY_ID = B.COMPANY_ID";
		
		List<Object[]> userDtl = sessionFactory.openSession().createSQLQuery(fetchUserVal).setParameter("USERID", name.toLowerCase()).list();
		
		UserDetail userdtls = new UserDetail();
		for(Object[] userVal : userDtl){
			userdtls.setUsername(userVal[0].toString().toLowerCase());
			userdtls.setPassword(userVal[1].toString());				
			userdtls.setActivestatus(Integer.parseInt(userVal[2].toString()));
			userdtls.setCompanyid(Long.parseLong(userVal[3].toString()));
			Set<Roles> roles = new HashSet<Roles>();
			Roles role = new Roles();				
			role.setRoleid(Integer.parseInt(userVal[4].toString()));
			role.setRolename(userVal[5].toString().toUpperCase());				
			roles.add(role);				
			userdtls.setRoles(roles);
		}		
		return userdtls;		
	}

}
