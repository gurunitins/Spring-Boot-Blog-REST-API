package com.sopromadze.blogapi.exception;

import com.sopromadze.blogapi.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NotAcceptableException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private ApiResponse apiResponse;

	public NotAcceptableException(ApiResponse apiResponse) {
		super();
		this.apiResponse = apiResponse;
	}

	public NotAcceptableException(String message) {
		super(message);
	}

	public ApiResponse getApiResponse() {
		return apiResponse;
	}
}
