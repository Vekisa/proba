package com.isap.ISAProject.model.user.base;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class UserBase implements Serializable {

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

}
