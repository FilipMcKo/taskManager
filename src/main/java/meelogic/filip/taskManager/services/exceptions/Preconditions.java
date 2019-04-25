package meelogic.filip.taskManager.services.exceptions;

public class Preconditions {
    public static void checkArgument(boolean expression, OperationStatus operationStatus) {
        if (!expression) {
            if(operationStatus==OperationStatus.ENTITY_NOT_FOUND){
                throw new EntityDoesNotExistServiceException();
            }
            else if(operationStatus==OperationStatus.FORBIDDEN_OPERATION){
                throw new ForbiddenOperationServiceException();
            }
        }
    }
}
