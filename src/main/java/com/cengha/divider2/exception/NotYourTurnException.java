package com.cengha.divider2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "Not player's turn")
public class NotYourTurnException extends RuntimeException{

}
