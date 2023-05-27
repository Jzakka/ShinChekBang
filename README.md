### 프로젝트 ERD

### 구현해야할 기능
- [x] 회원가입
- [X] 책 등록
  - [X] 책 소개글추가
- [X] EBOOK 서비스만 있음 -> 재고 시스템 전부 삭제
  - [X] cart와 book의 일관성 침해
- [X] 장바구니에 책 담기
- [X] 주문방법 바꾸기(카트내용물을 전부 주문하는 거->선택한 물건 개별리스트+가격으로 주문)
- [X] 카테고리로 책을 분류
- [X] 리뷰작성
  - [X] 리뷰평점계산
    - [X] 리뷰가 arrayList에 두번들어가지는 문제
  - [X] 비구매자도 리뷰 가능하게 수정
  - [ ] 책 리뷰에서 구매자리뷰만 따로 추출 
- [X] 리뷰 수정
- [X] 내가 쓴 리뷰 보기
- [X] 주문 내역 보기
- [ ] 내 서재 보기
- [ ] 알림기능
- [X] 로그인
- [ ] OAuth 로그인
- [ ] 로그인 상태 기억
- [X] 스프링 시큐리티가 password 매칭을 하지 못하는 이유 찾기
- [X] 로그인시 403인 이유찾기
  - [x] join시 null이 반환되는 오류 해결하기
    - [x] ddl-auto=create 인데도 데이터베이스 초기화가 안되는거 해결하기
- [X] 시큐리티 설정
***
- [X] 회원가입 화면
  - [X] 회원가입 실패 에러메시지
- [X] 로그인  
- [X] 카트에 표시
- [X] 카테고리 나열
- [ ] 이름으로 도서 검색
- [ ] 서적 표시
  - [X] 서적 등록시 이미지 저장
  - [ ] 서적 이미지 용량 제한
  - [X] 서적 이미지도 함께 표시
  - [X] 신규서적 5*4
  - [X] 페이징해서 표시
- [X] 관리자로 서적 등록
- [X] 서적 장바구니에 담기
- [ ] 주문
- [X] 리뷰작성 화면
- [ ] 상품 정보 화면
  - [X] 작품소개
  - [X] 리뷰작성
  - [X] 리뷰표시
- [ ] 내 정보
  - [ ] 내가 쓴 리뷰 화면
  - [ ] 내 주문 내역
