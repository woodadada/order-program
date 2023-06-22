package kr.co._29cm.homework.util;

import com.opencsv.bean.CsvToBeanBuilder;
import kr.co._29cm.homework.model.Product;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName   : kr.co._29cm.homework.util
 * fileName      : CSVUtil
 * author        : kang_jungwoo
 * date          : 2023/06/14
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/06/14       kang_jungwoo         최초 생성
 */
public class CSVUtil {
    private static final String FILE_PATH = "[29CM 23 SS 공채] 백엔드 과제 _items.csv";

    public static List<Product> readCsvFile() {
        List<Product> result = new ArrayList<>();
        try {
            // CSV 파일 로드
            Resource resource = new ClassPathResource(FILE_PATH);
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            result = new CsvToBeanBuilder<Product>(reader)
                    .withType(Product.class)
                    .build()
                    .parse();
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
        }
        return result;
    }
}
