# 주문 프로그램 개발
## 개발 환경
- 언어 : Java
- 프레임워크 : SpringBoot
- DB : H2
- ORM : JPA

## 설계

- CSV 문서 상품 데이터 
  - Application 실행 시 OpenCsv를 사용하여 CSV 문서 데이터를 Product Class로 변환하여 H2 In Memory DB에 저장합니다.


- 동시성 이슈 대응
  - 상품 주문 통해 일어날 수 있는 재고 차감 동시성 문제를 막기 위해 Lock을 사용하였습니다.
  - 비관적락을 통해 주문이 일어날 때 데이터베이스의 Product row를 잠금 처리 하였습니다.


- 고도화 방안
  - 많은 트래픽이 발생하는 상황을 대비하고 데이터베이스의 효율성을 높이기 위해 read 연산과 write 연산을 분리
  - read 연산의 경우 secondary DB로 요청이 들어가도록 구현, @Transactional(read-only=True) 인 경우에 해당.
  - write 연산의 경우 primary DB로 요청이 들어가도록 구현, @Transactional 인 경우에 해당.
  - 조회용 상품 데이터를 캐싱 처리, 재고는 Redis를 사용하여 관리, 분산락 사용
 
- 리펙토링 방안
  - 롬복 어노테이션 수정(Data -> Getter로 변경, 불필요한 어노테이션 삭제)
  - 사용하지 않는 코드 제거(미사용 에러 코드 삭제)
  - 비즈니스 로직과 View로직 분리(OrderView Class 생성하여 분리)
  - 도메인 영역에 있는 View 로직 분리(OrderView 로직으로 분리)
  - 주석 제거
  - order 메소드 역할 별 분리, 행위에 이름을 지어서 가독성을 높히기(OrderService 내부 메소드 생성으로 분리)
  - 테스트 코드 수정(테스트하려는 대상의 메소드를 호출)
