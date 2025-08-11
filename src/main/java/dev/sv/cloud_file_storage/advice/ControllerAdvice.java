package dev.sv.cloud_file_storage.advice;

import dev.sv.cloud_file_storage.dto.ErrorResponseDto;
import dev.sv.cloud_file_storage.exception.InvalidFormInputException;
import dev.sv.cloud_file_storage.exception.UsernameAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

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
