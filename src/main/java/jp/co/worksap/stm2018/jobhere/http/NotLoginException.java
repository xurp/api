package jp.co.worksap.stm2018.jobhere.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Please Login first")
public class NotLoginException extends RuntimeException {
}
