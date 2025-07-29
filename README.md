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

### **회원가입**  

<table>
  <tr>
    <td width="50%">
      <img src="https://github.com/user-attachments/assets/491a7c5f-542c-4d88-9f0e-6fa582114589" width="50%">  
    </td>
    <td width="50%">
      <p><b>사용자는 이메일, 닉네임, 비밀번호, 생년월일, 주소 등을 입력하여  회원가입</b></p>
      <ul>
        <li>사용자의 비밀번호는 안전하게 암호화되어 저장</li>
        <li>가입 시 이메일(아이디)과 닉네임의 중복 여부를 검증하여 데이터의 고유성을 보장, 사용자가 입력하는 닉네임은 실시간으로 중복 확인이 가능</li>
        <li>회원가입이 완료되면 이벤트를 발행하여 마일리지 지급과 같은 후속 작업을 비동기적으로 처리</li>
      </ul>
    </td>
  </tr>
</table>



---
### **로그인**  
<table>
  <tr>
    <td width="50%">
      <img src="https://github.com/user-attachments/assets/aa6e99a5-03c0-4c64-81ff-e64b4a489eaf" width="50%">  
    </td>
    <td width="50%">
      <p><b>인증 시스템</b></p>
      <ul>
        <li>스프링 프레임워크의 인증 기술을 활용한 폼 로그인 방식으로 사용자를 인증</li>
      </ul>
      <p><b>사용자 정보 처리</b></p>
      <ul>
        <li>이메일을 기반으로 데이터베이스에서 정보를 조회하고, 이를 통해 로그인 처리</li>
      </ul>
       <p><b>로그인 결과</b></p>
      <ul>
        <li>성공 시: 메인 페이지로 이동</li>
        <li>실패 시: 로그인 실패 사유에 따라 사용자에게 적절한 메시지를 제공</li>
      </ul>
       <p><b>인증 상태 관리</b></p>
      <ul>
        <li>로그인 성공 시 사용자의 인증 정보는 시스템 내부에 안전하게 저장되어, 애플리케이션 전반에서 사용자의 로그인 상태와 권한을 쉽게 확인</li>
      </ul>
       <p><b>전역 정보 제공</b></p>
      <ul>
        <li> 로그인 여부, 닉네임, 역할(관리자/일반 사용자) 등 사용자 관련 정보가 모든 화면에 자동으로 제공되어, 사용자 맞춤형 화면을 쉽게 구현</li>
      </ul>
    </td>
  </tr>
</table>

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

 <img src="https://github.com/user-attachments/assets/1fdb23c8-87e0-4471-9bce-21247e2b7f76">

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
<img src="https://github.com/user-attachments/assets/f40590b8-31fa-4949-ac30-10222dad0d22" width="70%"> 

---

### **결제내역**  
<img src="https://github.com/user-attachments/assets/94f62c38-0ee9-4e10-971f-019b697287f6" width="70%">  

---

### **알림 시스템**  

<img src="https://github.com/user-attachments/assets/2756c754-199a-4cd0-94f3-d13181923838" width="45%">  
<img src="https://github.com/user-attachments/assets/a24d5732-cdcc-4b25-8e91-d38728ec2832" width="45%">  
<p><b> 장바구니</b></p>
      <ul>
        <li>로그인 시, 헤더의 장바구니 아이콘에 현재 담긴 상품 개수가 실시간으로 업데이트</li>
      </ul>
       <p><b>실시간 알림 (SSE)</b></p>
      <ul>
        <li>이벤트를 활용하여 새로운 알림이 도착하면 종 모양 아이콘 색상이 변하고, 알림 확인 시 목록을 불러오는 등 사용자에게 실시간으로 중요한 정보를 전달</li>
      </ul>
---

### **마이페이지**  
<img src="https://github.com/user-attachments/assets/0d8274e2-3ecf-42a7-bd99-0f267c6f08ff" width="25%">  
<img src="https://github.com/user-attachments/assets/e6696ed3-70a3-4fc7-9d0c-96bf9035b9d5" width="45%">
<img src="https://github.com/user-attachments/assets/7a1572b6-91d3-4cdd-8692-ae0719ac2eac" width="45%">
---

### **판매 대시보드**  
![Image](https://github.com/user-attachments/assets/d1e3d06d-5b7c-4a71-b5aa-136fac51292c)
---

### **상품 등록**  
![Image](https://github.com/user-attachments/assets/0c1af8a3-c9cc-4242-af87-024f46a8c66f)
---

### **상품 관리**  
![Image](https://github.com/user-attachments/assets/9ae30b3e-af40-4f6c-8d43-e957bfa64ad5)
---

### **리뷰 관리**  
![Image](https://github.com/user-attachments/assets/92afc4be-b2ad-43e3-8982-c8962032fcc7)
---

### **회원 관리**  
![Image](https://github.com/user-attachments/assets/098122fc-a1ec-44b8-bbd6-86c0809d09b1)
---
.
### **이벤트로그**  
![Image](https://github.com/user-attachments/assets/23f60174-c217-48cf-98dd-1dd5d84f6d3b)


