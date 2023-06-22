package kr.co._29cm.homework.repository;

import kr.co._29cm.homework.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

/**
 * packageName   : kr.co._29cm.homework.repository
 * fileName      : ProductRepository
 * author        : kang_jungwoo
 * date          : 2023/06/14
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/06/14       kang_jungwoo         최초 생성
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsById(Long Id);

    List<Product> findByIdIn(Collection<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="3000")})
    Product findProductById(@Param("id") long productId);
}
