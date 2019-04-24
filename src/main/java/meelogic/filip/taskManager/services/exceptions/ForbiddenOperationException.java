package meelogic.filip.taskManager.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Forbidden operation.")
public class ForbiddenOperationException extends RuntimeException{
}
