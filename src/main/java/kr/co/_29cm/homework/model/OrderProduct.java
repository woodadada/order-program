package kr.co._29cm.homework.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName   : kr.co._29cm.homework.model
 * fileName      : Order
 * author        : kang_jungwoo
 * date          : 2023/06/14
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/06/14       kang_jungwoo         최초 생성
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {
    private long productId;
    private int quantity;
}
