package kr.co._29cm.homework.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다"),
    SEARCH_API_SEVER_ERROR(INTERNAL_SERVER_ERROR, "API 서버 통신이 정상적이지 않습니다."),
    SOLD_OUT_ERROR(BAD_REQUEST, "상품이 품절되었습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    /* 500 INTERNAL_SERVER_ERROR : 서버에서 오류 발생 */
    ;

    private final HttpStatus httpStatus;
    private final String detail;
}