package kr.co._29cm.homework.exception.handler;


import kr.co._29cm.homework.exception.SoldOutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {SoldOutException.class })
    protected String handleCustomException(SoldOutException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return e.getErrorCode().toString();
    }
}
