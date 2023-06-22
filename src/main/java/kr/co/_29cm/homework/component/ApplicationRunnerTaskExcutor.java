package kr.co._29cm.homework.component;

import kr.co._29cm.homework.model.Product;
import kr.co._29cm.homework.service.OrderService;
import kr.co._29cm.homework.service.ProductService;
import kr.co._29cm.homework.util.CSVUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * packageName   : kr.co._29cm.homework.component
 * fileName      : ApplicationRunnerTaskExcutor
 * author        : kang_jungwoo
 * date          : 2023/06/15
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/06/15       kang_jungwoo         최초 생성
 */
@Profile("!test")
@Component
@RequiredArgsConstructor
class ApplicationRunnerTaskExecutor implements ApplicationRunner {
    private final ProductService productService;
    private final OrderService orderService;

    @Override
    public void run(ApplicationArguments args) {
        List<Product> results = CSVUtil.readCsvFile();
        productService.addAllProducts(results);
        orderService.order();
    }
}
