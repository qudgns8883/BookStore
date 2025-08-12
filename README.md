# 📚 Book Store

## 1. 사용 기술 스택

**Backend**  
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![Spring JPA](https://img.shields.io/badge/Spring%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)  
![SSE](https://img.shields.io/badge/SSE-FF4500?style=for-the-badge)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)
![Elasticsearch](https://img.shields.io/badge/Elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white)
![Logstash](https://img.shields.io/badge/Logstash-F47A20?style=for-the-badge&logo=logstash&logoColor=white)
![Kibana](https://img.shields.io/badge/Kibana-005571?style=for-the-badge&logo=kibana&logoColor=white)  
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)

---

## 2. 프로젝트 기능

### 2.1 회원 기능
- **회원가입 & 로그인**: 닉네임, 이메일, 주소검색 등 입력
- **상품 조회**: 등록된 상품 상세 조회
- **리뷰**: 구매한 상품에 한해 리뷰 작성 가능
- **장바구니**: 수량추가, 마일리지 적용
- **주문 & 결제**: 카카오페이를 이용한 결제
- **마이페이지**: 구매 내역 확인, 정보 수정, 작성한 리뷰 관리
- **알림**: 회원가입, 구매 완료 등의 이벤트 알림

### 2.2 관리자 기능
- **판매 대시보드**: 최근 1주일간 판매 데이터를 시각화하여 제공
- **상품 등록**:  상품명, 분류, 가격, 재고 수량 등 입력
- **상품 관리**:  게시 상태 및 분류에 따른 검색 및 수정   
- **리뷰 관리**: 상태 및 기간에 따른 리뷰 검색, 리뷰 댓글 작성
- **회원 관리**: 상태에 따른 회원 검색
- **로그 페이지**: AOP를 사용하여 특정 이벤트 발생 시 로그 수집 및 관리  

---
## **3. 기능별 화면 및 소개**

---
### **메인화면**  
<table>
  <tr>
    <td width="50%">
      <img width="1043" height="539" alt="image" src="https://github.com/user-attachments/assets/7ce25651-ccdb-4844-9599-52dffe7ddc7c" width="80%"/>
    </td>
    <td width="50%">
      <p><b>동적 UI</b></p>
      <ul>
        <li>로그인 여부와 사용자 권한(관리자/일반 사용자)에 따라 내비게이션 바의 메뉴(예: 로그인/로그아웃 버튼, 관리자 페이지 링크)가 유동적으로 변경</li>
      </ul>
       <p><b>상품 목록 표시</b></p>
      <ul>
        <li>판매 중지 상품을 제외하고, 품절 상품은 별도 표시하여 사용자에게 직관적인 상품 정보를 제공</li>
      </ul>
    </td>
  </tr>
</table>



---
### **상품 조회**  

<table>
  <tr>
    <td width="50%">
     <img src="https://github.com/user-attachments/assets/389216e2-062b-4bab-8dc6-e7f46083804e" width="90%">  
    </td>
    <td width="50%">
      <p><b>상품 상세 정보</b></p>
      <ul>
        <li>상품명, 이미지, 가격, 재고, 상세 설명 등을 제공하며, 품절 여부를 직관적으로 표시</li>
      </ul>
       <p><b>리뷰 시스템</b></p>
      <ul>
        <li>상품의 평균 별점 및 총 리뷰 수</li>
        <li>리뷰 작성은 구매 이력이 있는 사용자에게 상품당 1회로 제한</li>
      </ul>
       <p><b>알림</b></p>
      <ul>
        <li>리뷰 작성 시 카프카(Kafka)를 활용하여 관리자에게 '새로운 리뷰 등록' 알림을 비동기적으로 전송</li>
      </ul>
    </td>
  </tr>
</table>

---

### **장바구니**  

<table>
  <tr>
    <td>
     <img width="1329" height="565" alt="image" src="https://github.com/user-attachments/assets/cb23038e-7dff-4adc-b907-cbbe91804a1c" />
    </td>
    <td width="30%">
      <br>
       <br>
      <p><b>상품 관리</b></p>
      <ul>
        <li>장바구니 내 상품의 수량을 조절하고 개별 삭제가 가능. 이미 담긴 상품을 추가하면 수량이 합산</li>
      </ul>
       <p><b실시간 금액 계산</b></p>
      <ul>
        <li>수량 변경 및 마일리지 사용에 따라 상품별 가격, 적립 포인트, 마일리지 활용 그리고 총 결제 금액이 실시간으로 업데이트</li>
      </ul>
    </td>
  </tr>
</table>


---

### **주문**  

<table>
  <tr>
    <td>
     <img src="https://github.com/user-attachments/assets/f40590b8-31fa-4949-ac30-10222dad0d22" width="90%"> 
    </td>
    <td width="30%">
      <br>
       <br>
      <p><b>외부 결제 연동</b></p>
      <ul>
        <li> 아임포트(Iamport) API를 활용하여 카카오페이 외부 결제 수단</li>
      </ul>
       <p><b동시성 재고 관리</b></p>
      <ul>
        <li>비관적 락을 활용하여 동시 주문 시 발생할 수 있는 재고 문제를 방지, @Retryable 어노테이션으로 락 경합 시 자동으로 재시도</li>
      </ul>
         <p><b알림</b></p>
      <ul>
        <li>주문 완료 시 카프카(Kafka)를 활용하여 관리자에게 새로운 주문 알림을 비동기적으로 전송</li>
      </ul>
    </td>
  </tr>
</table>


---

### **결제내역**  

<table>
  <tr>
    <td>
     <img src="https://github.com/user-attachments/assets/94f62c38-0ee9-4e10-971f-019b697287f6" width="70%">  
    </td>
    <td width="30%">
      <br>
       <br>
      <p><b>결제 완료 후 즉시 확인</b></p>
      <ul>
        <li> 결제가 성공적으로 완료되면, 해당 주문의 상세 내역 페이지로 이동</li>
      </ul>
    </td>
  </tr>
</table>


---

### **알림 시스템**  

<table>
  <tr>
    <td>
     <img src="https://github.com/user-attachments/assets/2756c754-199a-4cd0-94f3-d13181923838" width="45%">  
<img src="https://github.com/user-attachments/assets/a24d5732-cdcc-4b25-8e91-d38728ec2832" width="45%">  
    </td>
    <td width="30%">
      <br>
       <br>
      <p><b>장바구니</b></p>
       <ul>
        <li>로그인 시, 헤더의 장바구니 아이콘에 현재 담긴 상품 개수가 실시간으로 업데이트</li>
      </ul>
       <p><b>실시간 알림 (SSE)</b></p>
      <ul>
        <li>이벤트를 활용하여 새로운 알림이 도착하면 종 모양 아이콘 색상이 변하고, 알림 확인 시 목록을 불러오는 등 사용자에게 실시간으로 중요한 정보를 전달</li>
      </ul>
    </td>
  </tr>
</table>
    
---


### **판매 대시보드 & 이벤트로그**  
![Image](https://github.com/user-attachments/assets/d1e3d06d-5b7c-4a71-b5aa-136fac51292c)
![Image](https://github.com/user-attachments/assets/23f60174-c217-48cf-98dd-1dd5d84f6d3b)
* **Kafka**: 실시간 로그 데이터를 안정적이고 효율적으로 수집 및 전달하기 위한 분산 메시징 시스템으로 활용
* **Logstash**: Kafka로부터 로그를 받아 표준화된 형태로 가공하고, Elasticsearch로의 원활한 데이터 적재 파이프라인을 구축
* **Elasticsearch**: 방대한 로그 데이터를 빠르게 저장하고 색인하여, Kibana에서 실시간에 가까운 검색 및 복합 분석을 가능
* **Kibana**: Elasticsearch의 데이터를 기반으로 직관적이고 동적인 판매 대시보드를 구축하여, 관리자가 데이터를 시각적으로 쉽게 이해하고 모니터링할 수 있도록 웹에 임베드

---

### **상품 관리**  
![Image](https://github.com/user-attachments/assets/9ae30b3e-af40-4f6c-8d43-e957bfa64ad5)
* **QueryDSL** : 다양한 검색 조건들을 BooleanExpression으로 유연하게 조합하여 동적인 쿼리를 생성하고 실행


[![HitCount](https://hits.dwyl.com/qudgns8883/BookStore.svg?style=flat-square&show=unique)](http://hits.dwyl.com/qudgns8883/BookStore)



