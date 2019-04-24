package meelogic.filip.taskManager.services.exceptions;

import org.springframework.http.HttpStatus;

public class Preconditions {
    public static void checkArgument(boolean expression, HttpStatus httpStatus) {
        if (!expression) {
            if(httpStatus==HttpStatus.NOT_FOUND){
                throw new EntityDoesNotExistException();
            }
            else if(httpStatus==HttpStatus.FORBIDDEN){
                throw new ForbiddenOperationException();
            }
        }
    }
}
