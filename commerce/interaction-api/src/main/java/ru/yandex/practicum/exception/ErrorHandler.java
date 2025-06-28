package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorized(final NotAuthorizedUserException e) {
        return new ErrorResponse("UNAUTHORIZED", "User is not authorized.", e.getMessage());
    }

    @ExceptionHandler({CartNotFoundException.class,NoDeliveryFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final RuntimeException e) {
        return new ErrorResponse("NOT FOUND", "Cart was not found", e.getMessage());
    }

    @ExceptionHandler(DeactivatedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse handleDeactivated(final DeactivatedException e) {
        return new ErrorResponse("NOT ACCEPTABLE", "Changes blocked", e.getMessage());
    }

    @ExceptionHandler({ProductIdNotFoundException.class, DuplicateProductException.class,
            ProductInShoppingCartLowQuantityInWarehouseException.class,NoOrderFoundException.class,
            NotEnoughInfoInOrderToCalculateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleProductIdNotFound(final RuntimeException e) {
        return new ErrorResponse("BAD REQUEST", "Id violation", e.getMessage());
    }


    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final ProductNotFoundException e) {
        return new ErrorResponse("NOT_FOUND", "The required object was not found.", e.getMessage());
    }


}
