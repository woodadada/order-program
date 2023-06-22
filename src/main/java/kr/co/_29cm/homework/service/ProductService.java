package kr.co._29cm.homework.service;

import kr.co._29cm.homework.model.OrderProduct;
import kr.co._29cm.homework.model.Product;
import kr.co._29cm.homework.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * packageName   : kr.co._29cm.homework.service
 * fileName      : ProductService
 * author        : kang_jungwoo
 * date          : 2023/06/14
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/06/14       kang_jungwoo         최초 생성
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void addAllProducts(List<Product> products) {
        if(CollectionUtils.isEmpty(products)) return;
        productRepository.saveAll(products);
    }

    public List<Product> findAllProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    public boolean isExist(Long id) {
        return productRepository.existsById(id);
    }

    public List<Product> findProductByIds(Collection<Long> ids) {
        return productRepository.findByIdIn(ids);
    }

    public Map<Long, Product> getProductMap(Collection<Long> ids) {
        return findProductByIds(ids).stream().collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    // 상품 재고 수 차감 처리
    @Transactional
    public void decreaseProductQuantity(List<OrderProduct> list) {
        for (OrderProduct product : list) {
            Product productById = productRepository.findProductById(product.getProductId());
            productById.decreaseQuantity(product.getQuantity());
        }
    }
}
