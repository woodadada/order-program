package kr.co._29cm.homework.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName   : kr.co._29cm.homework.model
 * fileName      : ProductTest
 * author        : kang_jungwoo
 * date          : 2023/06/15
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/06/15       kang_jungwoo         최초 생성
 */
class ProductTest {
    @Test
    @DisplayName("재고를 차감한다.")
    public void 상품_재고_차감() {
        // given
        Product product = Product.builder().id(111111L).name("테스트상품").quantity(20).price(24000L).build();

        // when
        product.decreaseQuantity(10);

        // then
        assertEquals(10, product.getQuantity());
    }


}