package com.msc.sinhalasongpredictorbackend.modal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiResponse {

    private boolean success;
    private int httpStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private String message;

    @JsonIgnore
    private String exception;

    private Object data;


    private ApiResponse() {
        timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() {
        return success;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getException() {
        return exception;
    }

    public Object getData() {
        return data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setData(Object data) {
        this.data = data;
    }

    //success response
    public ApiResponse(Object data) {
        this();
        this.success = true;
        this.httpStatus = HttpStatus.OK.value();
        this.data = data;
    }

    //success response with message
    public ApiResponse(HttpStatus httpStatus, String message, Object data) {
        this(data);
        this.httpStatus = httpStatus.value();
        this.message = message;
    }

    //failure response - server side
    public ApiResponse(String message, Throwable ex) {
        this();
        this.success = false;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = message;
        this.exception = ex.getLocalizedMessage();
    }

    //failure response - client side
    public ApiResponse(String message, HttpStatus httpStatus) {
        this();
        this.success = false;
        this.httpStatus = httpStatus.value();
        this.message = message;
    }

}
