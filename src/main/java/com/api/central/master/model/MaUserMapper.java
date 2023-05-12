package com.api.central.master.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.api.central.master.entity.MaUsers;

public class MaUserMapper {

	public List<MaUsersWOPwd> getMaUsersWOPwdList(List<MaUsers> maUsers) {

		List<MaUsersWOPwd> usersWithoutPWD = new ArrayList<MaUsersWOPwd>();
		for (MaUsers usr : maUsers) {
			usersWithoutPWD.add(getMaUsersWOPwd(usr));
		}
		return usersWithoutPWD;
	}

	public MaUsersWOPwd getMaUsersWOPwd(MaUsers maUser) {
		MaUsersWOPwd userWithoutPwd = new MaUsersWOPwd();
		BeanUtils.copyProperties(maUser, userWithoutPwd);
		return userWithoutPwd;
	}
}
