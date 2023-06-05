package com.gymManagement.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String resourceName;
    private String resourceField;
    private String fieldValue;

    public ResourceNotFoundException(String resourceName, String resourceField, String fieldValue) {
        super(String.format("%s resource not found with %s:%s", resourceName, resourceField, fieldValue));
        this.resourceName = resourceName;
        this.resourceField = resourceField;
        this.fieldValue = fieldValue;
    }


}