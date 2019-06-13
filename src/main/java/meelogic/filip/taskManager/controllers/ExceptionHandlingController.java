package meelogic.filip.taskManager.controllers;

import meelogic.filip.taskManager.services.exceptions.EntityDoesNotExistServiceException;
import meelogic.filip.taskManager.services.exceptions.ForbiddenOperationServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(value = EntityDoesNotExistServiceException.class)
    public ResponseEntity<String> handleEntityDoesNotExistServiceException() {
        return new ResponseEntity<>("There is no such entity in the database.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ForbiddenOperationServiceException.class)
    public ResponseEntity<String> handleForbiddenOperationServiceException() {
        return new ResponseEntity<>("Forbidden operation.", HttpStatus.FORBIDDEN);
    }
}
