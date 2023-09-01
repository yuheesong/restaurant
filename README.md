# 프로젝트 🏘
## 1. 기간  
2023-07~2023-08
## 2. 멤버 구성
<img src="https://github.com/suwonwon/restaurant/assets/98306847/ef46ab75-c9a7-4b6a-b8f8-1f81bd86df66" width="800"/>  

## 3. 기술 스택  
- Java
- Spring Boot
- Gradle
- MySQL
- BootStrap
- HTML,JS,CSS
## 4. 핵심 기능  
- 회원가입 및 로그인
  
  <img width="600" alt="회원가입" src="https://github.com/suwonwon/restaurant/assets/98306847/34dada97-26ad-4229-99c8-bdccf39db274">  

- 메인페이지 및 지역/테마별 식당 페이지

  <img width="600" alt="회원가입" src="https://github.com/suwonwon/restaurant/assets/98306847/99248c6d-e3b6-4dad-b2fd-a8bbd4355499">  

- 식당 등록 및 관리
- 식당 상세페이지

  <img width="600" src="https://github.com/suwonwon/restaurant/assets/98306847/d67c3d58-d473-4171-b242-58265854dfd8"/>

- 식당 예약하기
- 마이페이지


  <img width="600" alt="마이페이지" src="https://github.com/suwonwon/restaurant/assets/98306847/30ffff5d-7af7-4e80-89c8-9d5809eafbfe">  

## 5. 회고하며  
## 6. 문제 해결 
- MemberFormDto에서 이름, 아이디, 이메일이 필수값으로 설정되어있기 때문에, 비밀번호 확인만 하는 "회원 확인" 기능을 구현하며 바인딩 오류 발생
  
  [해결 방법] : 먼저 bindingResult.getAllErrors()에서, error.getDefaultMessage() 로 문제를 확인한 후 password와 password2만 포함하는 MemberCheckDto를 따로 만들어서 회원 확인에 이용

- 작업 진행 중 Admin 계정으로 로그인 해도 식당 등록/관리 메뉴가 보이지 않는 현상. 찾아보니 CustomUserDetails 클래스를 작성하며 생긴 오류인 것을 알게되었다.

  [해결 방법] : GrandtedAuthority 설정 부분에 "ROLE_" 접두어가 없어서 생긴 오류였다. Spring Security는 기본적으로 "ROLE_" 접두어를 사용해서 권한을 확인한다고 한다. 따라서 getAuthorities 메소드에서 SimpleGrantedAuthority를 설정할 때 이 접두어를 추가해주어야 한다.
  ```
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
      List<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority(role.toString()));
      return authorities;
  }
  ```
  에서 "ROLE_" + role.toString()으로 수정하였더니, ROLE_ADMIN과 같은 형식의 권한 문자열이 생성되어 Thymeleaf에서 sec:authorize="hasAnyAuthority('ROLE_ADMIN')"와 같은 방식으로 권한을 올바르게 검사할    수 있게 되었다.

- JPA의 메서드 네이밍 전략에 따르면, 메서드 이름을 통해 질의를 자동으로 생성한다. findByAddressStartingWith는 "Address 필드 값이 주어진 값으로 시작하는" 엔티티를 찾는 것을 의미한다. 
findByAddressStartingWithSeoul 대신 findByAddressWithSeoul라는 메서드를 사용하려 했지만 findByAddressWithSeoul 같은 메서드 이름은 JPA의 네이밍 전략에 따라 자동으로 해석되지 않기 때문에 이러한 이름을 사용하려면 별도의 구현이 필요하다. 따라서, @Query 애노테이션을 사용하여 직접 쿼리를 (JPQL 혹은 SQL을) 작성해야 한다. 따라서,
   ```
  @Query("SELECT r FROM Rest r WHERE r.address LIKE '서울%'") List<Rest> findByAddressWithSeoul();

   ```
  를 RestRepsitory에 추가하여 사용하였다. 이 방법을 사용하면 메서드 이름을 자유롭게 정할 수 있지만 별도의 쿼리를 작성해야하는 번거로움은 있다.

- 지역별 페이지를 구현하며, 성남시와 하남시 식당을 각각 따로 가져오는 대신 동시에 가져오는 방법으로 바꾸게 되었다. 이를 위해선 여러 파라미터를 URL에 포함시켜 가져오는 방법을 이용하였다. 즉, region 파라미터의 값들을 쉼표로 구분하여 전달하였다. 그리고 백엔드에서는 리스트 형식으로 regions를 다루고, 이 값을 쉼표로 분할하여 각각의 지역을 처리한다.
  ```
  <a class="nav-link" href="/rest/gyeongin?regions=성남시,하남시,광주시">성남/하남/광주</a>
  ```
  백엔드에서는 ```@RequestParam(required = false)List<String> regions``` 처럼 리스트 형식으로 받아서 처리한다. (웹 URL에서 쿼리 스트링의 값을 쉼표로 구분하는 것이 일반적이진 않지만) Spring에서 @RequestParam을 사용하여 List 타입으로 변환할 때는 쉼표를 기본 구분자로 사용할 수 있다.
