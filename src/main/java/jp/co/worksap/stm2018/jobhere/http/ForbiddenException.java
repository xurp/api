package jp.co.worksap.stm2018.jobhere.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    public ForbiddenException () {
        super("Permission Denied");
    }

    public ForbiddenException (String message) {
        super(message);
    }
}
