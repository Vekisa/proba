package com.isap.ISAProject.domain.factory;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.isap.ISAProject.domain.security.CerberusUser;
import com.isap.ISAProject.model.user.SystemUser;

public class CerberusUserFactory {
	
	public static CerberusUser create(SystemUser user) {
		Collection<? extends GrantedAuthority> authorities;
		try {
			authorities = AuthorityUtils.createAuthorityList(user.getAuthority().toString());
		} catch (Exception e) {
			System.out.println("Doslo je do greske prilikom kreiranja autorizacije.");
			authorities = null;
		}
		return new CerberusUser(
				user.getId(),
				user.getEmail(),
				user.getUsername(),
				user.getPassword(),
				user.getFirstName(),
				user.getLastName(),
				user.getCity(),
				user.getPhoneNumber(),
				authorities
		);
	}
}
