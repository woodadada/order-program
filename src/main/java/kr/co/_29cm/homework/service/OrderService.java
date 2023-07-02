package kr.co._29cm.homework.service;

import kr.co._29cm.homework.constant.ErrorMessage;
import kr.co._29cm.homework.constant.OrderConstant;
import kr.co._29cm.homework.constant.TextConstant;
import kr.co._29cm.homework.exception.SoldOutException;
import kr.co._29cm.homework.model.OrderProduct;
import kr.co._29cm.homework.model.Product;
import kr.co._29cm.homework.view.OrderView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    private final OrderView orderView;

    public void order() {

        Scanner scanner = new Scanner(System.in);
        boolean isOrdering = true;
        boolean isDone = false;
        List<OrderProduct> orderItems = new ArrayList<>();
        List<Product> products = productService.findAllProducts();

        System.out.print(TextConstant.ORDER_GUIDE_TEXT);
        String input = scanner.nextLine();

        checkOrderCommandAndPrintText(input, isOrdering, products);
        checkQuitCommandAndPrintText(input, isOrdering);

        while (isOrdering) {
            isDone = false;
            System.out.print(TextConstant.ORDER_GUIDE_TEXT_PRODUCT);
            input = scanner.nextLine();

            if (input.isEmpty()) {
                // 주문 완료
                isDone = true;
            } else {
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
                productService.decreaseProductQuantity(orderItems);
                orderView.printOrderInfo(orderItems);
            } catch (SoldOutException e) {
                System.out.println(ErrorMessage.SOLD_OUT);
            }
        }
        order();
    }

    public void checkOrderCommandAndPrintText(String input, boolean isOrdering, List<Product> products) {
        if(!input.equalsIgnoreCase(OrderConstant.ORDER_COMMAND)) {
            System.out.println(TextConstant.ORDER_START_GUIDE_TEXT);
            isOrdering = false;
        }

        if(input.equalsIgnoreCase(OrderConstant.ORDER_COMMAND)) {
            System.out.println(OrderConstant.HEADER);
            products.forEach(product -> System.out.println(orderView.toDisplay(product)));
        }
    }

    public void checkQuitCommandAndPrintText(String input, boolean isOrdering) {
        if (input.equalsIgnoreCase(OrderConstant.QUIT_COMMAND) || input.equalsIgnoreCase(OrderConstant.QUIT_COMMAND_ALT)) {
            System.out.println(TextConstant.DONE_ORDER);
            isOrdering = false;
        }
    }
}
