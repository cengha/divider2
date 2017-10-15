package com.cengha.divider2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Game is not found")
public class NoSuchGameException extends RuntimeException{

}
