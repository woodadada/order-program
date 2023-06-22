package kr.co._29cm.homework.service;

import kr.co._29cm.homework.constant.ErrorMessage;
import kr.co._29cm.homework.constant.OrderConstant;
import kr.co._29cm.homework.constant.TextConstant;
import kr.co._29cm.homework.exception.SoldOutException;
import kr.co._29cm.homework.model.OrderProduct;
import kr.co._29cm.homework.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * packageName   : kr.co._29cm.homework.service
 * fileName      : OrderService
 * author        : kang_jungwoo
 * date          : 2023/06/14
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/06/14       kang_jungwoo         최초 생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductService productService;

    private final int FREE_DELIVERY_PRICE = 50000;
    private final int DELIVERY_PRICE = 2500;

    public void order() {

        Scanner scanner = new Scanner(System.in);
        boolean isOrdering = true;
        boolean isDone = false;
        List<OrderProduct> orderItems = new ArrayList<>();
        List<Product> products = productService.findAllProducts();

        System.out.print(TextConstant.ORDER_GUIDE_TEXT);
        String input = scanner.nextLine();

        if(!input.equalsIgnoreCase(OrderConstant.ORDER_COMMAND)) {
            System.out.println(TextConstant.ORDER_START_GUIDE_TEXT);
            isOrdering = false;
        }

        if(input.equalsIgnoreCase(OrderConstant.ORDER_COMMAND)) {
            // 상품 목록 출력
            System.out.println(OrderConstant.HEADER);
            products.forEach(product -> System.out.println(product.toDisplay()));
        }

        if (input.equalsIgnoreCase(OrderConstant.QUIT_COMMAND) || input.equalsIgnoreCase(OrderConstant.QUIT_COMMAND_ALT)) {
            System.out.println(TextConstant.DONE_ORDER);
            isOrdering = false;
        }

        while (isOrdering) {
            isDone = false;
            System.out.print(TextConstant.ORDER_GUIDE_TEXT_PRODUCT);
            input = scanner.nextLine();

            if (input.isEmpty()) {
                // 주문 완료
                isDone = true;
            } else {
                // 상품 존재 여부
                if(!productService.isExist(Long.parseLong(input))) {
                    System.out.println(ErrorMessage.NOT_FOUND_PRODUCT);
                    continue;
                }
            }
            String productId = input;

            System.out.print(TextConstant.ORDER_GUIDE_TEXT_QUANTITY);
            String quantityInput = scanner.nextLine();

            if(quantityInput.isEmpty() && isDone) {
                isOrdering = false;
                break;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityInput);
            } catch (NumberFormatException e) {
                System.out.println(ErrorMessage.QUANTITY_NOT_NUMBER);
                continue;
            }

            OrderProduct order = OrderProduct.builder().productId(Long.parseLong(productId)).quantity(quantity).build();
            orderItems.add(order);
        }

        if(orderItems.isEmpty()) {
            System.out.println(ErrorMessage.NOT_FOUND_ORDER_ITEMS);
        } else {
            try {
                // 상품 재고 차감
                productService.decreaseProductQuantity(orderItems);
                // 주문 상품 정보 출력
                printOrderInfo(orderItems);
            } catch (SoldOutException e) {
                System.out.println(ErrorMessage.SOLD_OUT);
            }
        }
        order();
    }

    private int calculateTotalOrderAmount(List<OrderProduct> orderItems) {
        int price = 0;
        Map<Long, Product> productMap = productService.getProductMap(orderItems.stream().map(OrderProduct::getProductId).collect(Collectors.toList()));

        for (OrderProduct orderItem : orderItems) {
            Product product = productMap.get(orderItem.getProductId());
            price += product.getPrice() * orderItem.getQuantity();
        }

        return price;
    }

    private void printOrderInfo(List<OrderProduct> orderItems) {
        // 주문 상품 가격
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

    private String priceFormat(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedNumber = decimalFormat.format(price);

        return formattedNumber;
    }

}
