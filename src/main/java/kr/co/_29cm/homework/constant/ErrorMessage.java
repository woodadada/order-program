package kr.co._29cm.homework.constant;

/**
 * packageName   : kr.co._29cm.homework.constant
 * fileName      : ErrorMessage
 * author        : kang_jungwoo
 * date          : 2023/06/14
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/06/14       kang_jungwoo         최초 생성
 */
public class ErrorMessage {
    public static final String NOT_FOUND_PRODUCT = "존재하지 않는 상품입니다. 다시 입력하세요.";
    public static final String NOT_FOUND_ORDER_ITEMS = "주문한 상품이 없습니다.";
    public static final String QUANTITY_NOT_NUMBER = "잘못된 입력입니다. 수량은 숫자로 입력하세요.";
    public static final String SOLD_OUT = "SoldOutException 발생. 주문한 상품량이 재고량보다 큽니다.";
}
