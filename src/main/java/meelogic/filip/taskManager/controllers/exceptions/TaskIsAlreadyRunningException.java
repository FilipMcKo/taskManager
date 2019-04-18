package meelogic.filip.taskManager.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Task is already running.")
public class TaskIsAlreadyRunningException extends RuntimeException{
}
