package kr.co._29cm.homework.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    SOLD_OUT_ERROR(BAD_REQUEST, "상품이 품절되었습니다."),
    /* 500 INTERNAL_SERVER_ERROR : 서버에서 오류 발생 */
    ;
    private final HttpStatus httpStatus;
    private final String detail;
}