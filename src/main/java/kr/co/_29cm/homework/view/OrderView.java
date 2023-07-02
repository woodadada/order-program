package kr.co._29cm.homework.view;

import kr.co._29cm.homework.constant.TextConstant;
import kr.co._29cm.homework.model.OrderProduct;
import kr.co._29cm.homework.model.Product;
import kr.co._29cm.homework.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@Component
@RequiredArgsConstructor
public class OrderView {

    private final String TAB = "    ";
    private final int FREE_DELIVERY_PRICE = 50000;
    private final int DELIVERY_PRICE = 2500;

    private final ProductService productService;

    public String toDisplay(Product product) {
        StringBuilder sb = new StringBuilder();
        sb.append(product.getId()).append("    ")
                .append(product.getName())
                .append(TAB)
                .append(product.getPrice())
                .append(TAB)
                .append(product.getQuantity());

        return sb.toString();
    }

    public int calculateTotalOrderAmount(List<OrderProduct> orderItems) {
        int price = 0;
        Map<Long, Product> productMap = productService.getProductMap(orderItems.stream().map(OrderProduct::getProductId).collect(Collectors.toList()));

        for (OrderProduct orderItem : orderItems) {
            Product product = productMap.get(orderItem.getProductId());
            price += product.getPrice() * orderItem.getQuantity();
        }

        return price;
    }

    public void printOrderInfo(List<OrderProduct> orderItems) {
        int totalOrderAmount = calculateTotalOrderAmount(orderItems);
        Map<Long, Product> productMap = productService.getProductMap(orderItems.stream().map(OrderProduct::getProductId).collect(Collectors.toList()));
        System.out.println(TextConstant.DONE_ORDER);
        System.out.println(TextConstant.ORDER_HISTORY);
        System.out.println(TextConstant.LINE);

        for (OrderProduct orderItem : orderItems) {
            System.out.println(productMap.get(orderItem.getProductId()).getName() + " - " + orderItem.getQuantity());
        }

        System.out.println(TextConstant.LINE);
        System.out.println(TextConstant.ORDER_PRICE + priceFormat(totalOrderAmount) + "원");
        if(totalOrderAmount < FREE_DELIVERY_PRICE) {
            System.out.println(TextConstant.DELIVERY_PRICE + priceFormat(DELIVERY_PRICE));
            totalOrderAmount += DELIVERY_PRICE;
        }
        System.out.println(TextConstant.LINE);
        System.out.println(TextConstant.AMOUNT_ORDER_PRICE + priceFormat(totalOrderAmount) + "원");
    }

    public static String priceFormat(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedNumber = decimalFormat.format(price);

        return formattedNumber;
    }
}
