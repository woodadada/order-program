package kr.co._29cm.homework.exception;

import kr.co._29cm.homework.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SoldOutException extends RuntimeException {
    private final ErrorCode errorCode;
}