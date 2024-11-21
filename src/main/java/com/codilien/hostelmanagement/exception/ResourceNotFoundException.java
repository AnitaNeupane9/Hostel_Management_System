package com.codilien.hostelmanagement.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

  private String resourceName;
  private Long resourceId;

  public ResourceNotFoundException(String resourceName, Long resourceId) {
    super(resourceName + " with ID " + resourceId + " not found.");
    this.resourceName = resourceName;
    this.resourceId = resourceId;

  }
}
