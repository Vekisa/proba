package com.isap.ISAProject.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest implements Serializable {
    private String username;
    private String password;
	
    public AuthenticationRequest() {}
    
    private AuthenticationRequest(Builder builder) {
    	this.username = builder.username;
    	this.password = builder.password;
    }
    
    public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
	public static Builder builder() {
		return new Builder();
	}
	
    public static class Builder{
    	private String username;
        private String password;
        
        public Builder username(String username) {
        	this.username = username;
        	return this;
        }
        public Builder password(String password) {
        	this.password = password;
        	return this;
        }
        
        public AuthenticationRequest build() {
        	return new AuthenticationRequest(this);
        }
    }
}
