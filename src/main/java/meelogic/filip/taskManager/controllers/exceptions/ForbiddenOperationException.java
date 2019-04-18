package meelogic.filip.taskManager.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Unable to perform such operation.")
public class ForbiddenOperationException extends RuntimeException {
}
