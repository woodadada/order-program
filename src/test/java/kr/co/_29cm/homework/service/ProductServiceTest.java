package kr.co._29cm.homework.service;

import kr.co._29cm.homework.constant.ErrorCode;
import kr.co._29cm.homework.exception.SoldOutException;
import kr.co._29cm.homework.model.OrderProduct;
import kr.co._29cm.homework.model.Product;
import kr.co._29cm.homework.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName   : kr.co._29cm.homework.service
 * fileName      : ProductServiceTest
 * author        : kang_jungwoo
 * date          : 2023/06/15
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/06/15       kang_jungwoo         최초 생성
 */
@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductService productService;

    @Test
    @DisplayName("상품을 저장한다.")
    @Transactional
    public void 상품_생성() {
        // given
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product build = Product.builder().id(i).name("상품" + i).quantity(i + 10).price(1000 * i).build();
            list.add(build);
        }
        // when
        List<Product> products = productRepository.saveAll(list);

        // then
        assertEquals(products.size(), 10);
    }

    @Test
    @DisplayName("상품을 존재 여부를 체크한다.")
    @Transactional
    public void 상품_존재_여부_체크() {
        // given
        Product build = Product.builder().id(1000L).name("상품").quantity(10).price(1000).build();
        // when
        productRepository.save(build);
        boolean isExist = productRepository.existsById(1000L);

        // then
        assertTrue(isExist);
    }

    @Test
    @DisplayName("상품 ID로 상품 목록을 조회한다.")
    @Transactional
    public void 상품_목록_조회() {
        // given
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product build = Product.builder().id(i).name("상품" + i).quantity(i + 10).price(1000 * i).build();
            list.add(build);
        }
        productRepository.saveAll(list);
        List<Long> ids = list.stream().map(Product::getId).collect(Collectors.toList());
        // when
        List<Product> products = productRepository.findByIdIn(ids);

        // then
        assertEquals(products.size(), ids.size());
    }

    @Test
    @DisplayName("상품 목록을 조회하여 MAP으로 가공한다.")
    @Transactional
    public void 상품_목록_조회_MAP_가공() {
        // given
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product build = Product.builder().id(i).name("상품" + i).quantity(i + 10).price(1000 * i).build();
            list.add(build);
        }
        productRepository.saveAll(list);
        List<Long> ids = list.stream().map(Product::getId).collect(Collectors.toList());
        // when
        Map<Long, Product> map = productRepository.findByIdIn(ids).stream().collect(Collectors.toMap(Product::getId, Function.identity()));

        // then
        assertEquals(map.size(), ids.size());
    }

    @Test
    @DisplayName("상품 재고를 차감한다.")
    @Transactional
    public void 상품_재고_차감() throws InterruptedException {
        // given
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product build = Product.builder().id(i).name("상품" + i).quantity(i + 10).price(1000 * i).build();
            list.add(build);
        }
        productRepository.saveAllAndFlush(list);
        List<Long> ids = list.stream().map(Product::getId).collect(Collectors.toList());

        // when
        List<OrderProduct> orderItems = new ArrayList<>();
        OrderProduct orderProduct = OrderProduct.builder().productId(3L).quantity(5).build();// 3번 상품 quantity 12개
        orderItems.add(orderProduct);

        Product productById = productRepository.findProductById(orderProduct.getProductId());
        productById.decreaseQuantity(orderProduct.getQuantity());
        Optional<Product> findProduct = productRepository.findById(3L);
        //then
        assertEquals(productById.getQuantity(), findProduct.get().getQuantity());
    }

    @Test
    @DisplayName("여러번 상품 재고를 차감한다.")
    public void 상품_재고_차감_멀티() throws InterruptedException {
        // given
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product build = Product.builder().id(i).name("상품" + i).quantity(i + 10).price(1000 * i).build();
            list.add(build);
        }
        productRepository.saveAll(list);
        OrderProduct orderProduct = OrderProduct.builder().productId(3L).quantity(6).build();// 3번 상품 quantity 13개
        List<OrderProduct> items = new ArrayList<>() {{
            add(orderProduct);
        }};

        // when
        int threadCount = 3;
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicBoolean exceptionOccurred = new AtomicBoolean(false);

        for (int i = 0; i < threadCount; i++) {
            service.execute(() -> {
                try {
                    //테스트 메소드
                    productService.decreaseProductQuantity(items);
                } catch (SoldOutException e) {
                    exceptionOccurred.set(true);
                }
                latch.countDown();
            });
        }
        latch.await();

        //then
        assertThrows(SoldOutException.class, () -> {
            if (exceptionOccurred.get()) {
                throw new SoldOutException(ErrorCode.SOLD_OUT_ERROR);
            }
        });
    }
}