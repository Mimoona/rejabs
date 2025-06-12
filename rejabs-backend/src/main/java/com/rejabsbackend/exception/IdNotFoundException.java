package com.rejabsbackend.exception;

public class IdNotFoundException extends Exception {

    public IdNotFoundException(String id, String entityName) {

        super(entityName + " Id " + id + " not found");
    }
}
