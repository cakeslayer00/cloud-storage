package dev.sv.cloud_file_storage.exception;

public class UserNotFoundException extends InvalidFormInputException {

    public UserNotFoundException(String message) {
        super(message);
    }

}
