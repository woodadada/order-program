package kr.co._29cm.homework.view;

import kr.co._29cm.homework.model.Product;

/**
 * packageName   : kr.co._29cm.homework.view
 * fileName      : OrderView
 * author        : kang_jungwoo
 * date          : 2023/07/02
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/07/02       kang_jungwoo         최초 생성
 */
public class OrderView {

    private static final String TAB = "    ";

    public static String toDisplay(Product product) {
        StringBuilder sb = new StringBuilder();
        sb.append(product.getId()).append("    ")
                .append(product.getName())
                .append(TAB)
                .append(product.getPrice())
                .append(TAB)
                .append(product.getQuantity());

        return sb.toString();
    }
}
