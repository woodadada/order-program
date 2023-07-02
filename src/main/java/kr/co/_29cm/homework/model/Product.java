package kr.co._29cm.homework.model;

import com.opencsv.bean.CsvBindByName;
import kr.co._29cm.homework.constant.ErrorCode;
import kr.co._29cm.homework.exception.SoldOutException;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * packageName   : kr.co._29cm.homework.model
 * fileName      : Product
 * author        : kang_jungwoo
 * date          : 2023/06/14
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/06/14       kang_jungwoo         최초 생성
 */
@Getter
@Entity
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @CsvBindByName(column = "상품번호")
    private long id;
    @CsvBindByName(column = "상품명")
    private String name;
    @CsvBindByName(column = "판매가격")
    private long price;
    @CsvBindByName(column = "재고수량")
    @Column(columnDefinition = "INT UNSIGNED")
    private int quantity;

    public void decreaseQuantity(int quantity) {
        if(this.quantity - quantity < 0) {
            throw new SoldOutException(ErrorCode.SOLD_OUT_ERROR);
        }
        this.quantity -= quantity;
    }
}
