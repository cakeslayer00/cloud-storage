package dev.sv.cloud_file_storage.advice;

import dev.sv.cloud_file_storage.dto.ErrorResponseDto;
import dev.sv.cloud_file_storage.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(InvalidPathException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleInvalidPath(InvalidPathException ex) {
        return new ErrorResponseDto(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleResourceNotFound(ResourceNotFoundException ex) {
        return new ErrorResponseDto(ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        return new ErrorResponseDto(ex.getMessage());
    }

    @ExceptionHandler(InvalidFileNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleInvalidFilename(InvalidFileNameException ex) {
        return new ErrorResponseDto(ex.getMessage());
    }

    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleInvalidOperationException(InvalidOperationException ex) {
        return new ErrorResponseDto(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleInvalidRequestCredentials(MethodArgumentNotValidException ex) {
        return new ErrorResponseDto(ex.getMessage());
    }

    @ExceptionHandler(InvalidFormInputException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto handleUserRetrievalException(InvalidFormInputException ex) {
        return new ErrorResponseDto(ex.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto handleUserAlreadyTakenException(UsernameAlreadyTakenException ex) {
        return new ErrorResponseDto(ex.getMessage());
    }

}
