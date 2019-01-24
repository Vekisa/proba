package com.isap.ISAProject.domain.base;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class UserModelBase implements Serializable{

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
