# 주문 프로그램 개발
## 개발 환경
- 언어 : Java
- 프레임워크 : SpringBoot
- DB : H2
- Persistence 프레임워크 : JPA

## 설계 내용, 설계 이유, 고도화 방안

- CSV 문서 상품 데이터 
  - Application 실행 시 OpenCsv를 사용하여 CSV 문서 데이터를 Product Class로 변환하여 H2 In Memory DB에 저장합니다.


- 어플리케이션이 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에 문제가 없도록 설계하였습니다.
  - 상품 주문 통해 일어날 수 있는 재고 차감 동시성 문제를 막기 위해 Lock을 사용하였습니다.
  - 비관적락을 통해 주문이 일어날 때 데이터베이스의 Product row를 잠금 처리 하였습니다.


- 레이어드 아키텍쳐 구조로 개발하였습니다.
  - service repository의 계층 구조로 설계하였습니다.
  - service에서는 비즈니스 로직이 실행되고 repository를 통해 데이터의 CRUD를 진행합니다.
  - 실제 엔티티의 변화(재고 차감) 액션의 경우에는 Product Entity 내부에 로직이 정의되어 있습니다.


- 고도화 방안
  - 많은 트래픽이 발생하는 상황을 대비하고 데이터베이스의 효율성을 높이기 위해 read 연산과 write 연산을 분리
  - read 연산의 경우 secondary DB로 요청이 들어가도록 구현, @Transactional(read-only=True) 인 경우에 해당.
  - write 연산의 경우 primary DB로 요청이 들어가도록 구현, @Transactional 인 경우에 해당.
  - 조회용 상품 데이터를 캐싱 처리, 재고는 Redis를 사용하여 관리, 분산락 사용
