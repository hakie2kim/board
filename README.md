# 포트폴리오

## 💬 소개

## 🔨 기능 요구사항

### 회원 가입

#### Sequence Diagram

##### 회원 가입

![회원가입-2024-05-29-140926](https://github.com/hakie2kim/board/assets/115719016/273fd06c-2f90-4f35-87fc-dae4f3bb301c)

##### 이메일 인증

![이메일인증-2024-05-29-142824](https://github.com/hakie2kim/board/assets/115719016/32b975f1-9840-496e-8d6f-484bea2cd6e1)

- 회원 가입 시 제약 사항
  - [x] 아이디는 공백 또는 빈 칸일 수 없고 4~20자의 영어 소문자, 숫자만 사용 가능
  - [x] 이미 존재하는 아이디로는 가입 불가
  - [x] 비밀번호는 8~16자의 영문 대/소문자, 숫자를 사용, 특수문자를 1개 이상 포함
  - [x] 이름은 공백 또는 빈 칸일 수 없음
  - [x] 이메일은 공백 또는 빈 칸일 수 없고 이메일 형식을 준수
- [x] 패스워드는 DB에 암호화 후 저장
- 사용자 이메일 유효 여부 인증
  - [x] 인증 링크를 포함한 이메일 전송
  - [x] 사용자가 인증 링크를 클릭한 후 인증 여부를 DB에 반영
  - [ ] 만료된 인증 링크로 접속 시 이메일 재전송

### 로그인, 로그아웃

- [x] 로그인
- 다음 기능들은 로그인 후에만 가능하도록 제한
  - [ ] 공지사항 작성 페이지 접근
  - [ ] 공지사항 수정 페이지 접근
  - [ ] 댓글 달기
  - [ ] 좋아요/싫어요
  - [x] 로그인이 필요한 기능 접근 제한 후 로그인에 성공한 경우 이전 요청으로 리다이렉트
- [x] 이메일 유효 여부 인증이 완료된 사용자만 로그인 가능
- [x] 로그인 페이지의 `Remeber Me` 체크 시 일주일 동안 아이디 기억
- [x] 로그아웃

### 게시판

### 게시글 목록

- 페이지네이션 (Pagination)
  - [x] 1 페이지와 마지막 페이지에는 각각 이전, 다음 화살표를 보여주지 않음
  - [x] 한 페이지 당 출력할 게시글: 10개
  - [x] 네비게이션 바 - 한 페이지 당 출력할 페이지 번호: 10개

### 게시글 작성

- 제약 사항
  - [x] 제목은 공백 또는 빈 칸일 수 없고 5~50자 사이
  - [x] 내용은 공백 또는 빈 칸일 수 없고 5~1000자 사이
  - [ ] 파일당 10MB까지 첨부 가능

### 프로젝트 환경 설정

#### Docker DB

```
# for Windows
docker run --name mysql-lecture -p 53306:3306 -v c:/dev/docker/mysql:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=admin_123 -d mysql:8.3.0

# for Mac
docker run --name mysql-lecture -p 53306:3306 -v ~/dev/docker/mysql:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=admin_123 -d mysql:8.3.0
```

#### MyBatis

##### `pom.xml`

```xml
<dependency>
	<groupId>org.mybatis</groupId>
	<artifactId>mybatis</artifactId>
	<version>3.5.16</version>
</dependency>
<dependency>
	<groupId>org.mybatis</groupId>
	<artifactId>mybatis-spring</artifactId>
	<version>2.1.2</version>
</dependency>
```

`mybatis-spring` 의존성 추가할 때 `spring-context`, `spring-jdbc`와 호환되는 버전을 확인하자. `spring` 버전 `5.x.x`와 호환되는 것을 확인할 수 있다.

##### `src/main/resources/context-beans.xml`

```xml
<!-- MyBatis start -->
<!-- DAO 구현체 역할을 대신 해주는 클래스 기본설정 4가지가 필요 -->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
	<!-- 1. DB에 접속 하기 위해서 설정 -->
	<property name="dataSource" ref="dataSource" />

	<!-- 2. MyBatis 기본 설정 -->
	<property name="configLocation" value="classpath:mybatis-config.xml" />

	<!-- 3. query가 적힌 xml 위치 -->
	<property name="mapperLocations" value="classpath:sql/SQL.*.xml" />

	<!-- 4. 트랜잭션 관리 -->
	<property name="transactionFactory">
		<bean class="org.mybatis.spring.transaction.SpringManagedTransactionFactory" />
	</property>
</bean>

<!-- 작업 지시서 DAO 위치를 지정해야 사용 할 수 있음 -->
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
	<property name="basePackage" value="com.portfolio.www.dao.mybatis" />
</bean>

<!-- 트랜잭션 관리를 위한 bean -->
<bean id="transactionManager"
	class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
	<property name="dataSource" ref="dataSource" />
</bean>
<!-- MyBatis end -->
```

##### `src/main/resources/mybatis-config.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<settings>
		<!-- 예) member_id -> memberId -->
		<setting name="mapUnderscoreToCamelCase" value="true" />
	</settings>

  <!-- 쿼리 수행 결과를 DTO에 자동 매핑하기 위해 DTO 검색 -->
	<typeAliases>
		<package name="com.portfolio.www.dto" />
	</typeAliases>
	<!-- 개별로 setting 하는 방법
	<typeAlias alias="Employees" type="com.edu.dto.Employees" />
	-->
</configuration>
```

###### `<typeAliases>`

MyBatis가 DTO 클래스를 검색할 패키지를 지정합니다. 여기서는 `com.portfolio.www.dto` 패키지 내의 모든 클래스를 대상으로 `@Alias` 애너테이션이 없다면 클래스 이름을 소문자로 변환하여 별칭으로 등록합니다. 예를 들어, `com.portfolio.www.dto.Member` 클래스는 `member`라는 별칭으로 등록됩니다.

###### `<typeAlias>`

개별 클래스를 명시적으로 별칭과 매핑할 수 있습니다. 이 방법은 패키지 단위 설정 대신 특정 클래스에 대해 별칭을 설정할 때 사용됩니다. 주석 처리된 예제에서는 com.edu.dto.Employees 클래스를 Employees라는 별칭으로 설정합니다.

#### Tiles

##### `pom.xml`

```xml
<dependency>
	<groupId>org.apache.tiles</groupId>
	<artifactId>tiles-core</artifactId>
	<version>3.0.8</version>
</dependency>
<dependency>
	<groupId>org.apache.tiles</groupId>
	<artifactId>tiles-jsp</artifactId>
	<version>3.0.8</version>
</dependency>
<dependency>
	<groupId>org.apache.tiles</groupId>
	<artifactId>tiles-servlet</artifactId>
	<version>3.0.8</version>
</dependency>
<dependency>
	<groupId>org.apache.tiles</groupId>
	<artifactId>tiles-extras</artifactId>
	<version>3.0.8</version>
</dependency>
```

##### `WebContent/WEB-INF/tiles/tiles-config.xml`

###### 기본 레이아웃

```xml
<definition name="tiles-default" template="/WEB-INF/views/layout/default.jsp">
	<put-attribute name="menu" value="/WEB-INF/views/layout/menu.jsp" />
	<put-attribute name="body" value="" />
	<put-attribute name="footer" value="/WEB-INF/views/layout/footer.jsp" />
</definition>
```

###### 회원가입, 로그인 페이지

```xml
<!-- 회원가입 -->
<definition name="auth/join" extends="tiles-default">
	<put-attribute name="title" value="회원가입" />
	<put-attribute name="body" value="/WEB-INF/views/auth/join.jsp" />
</definition>
<!-- 로그인 -->
<definition name="auth/login" extends="tiles-default">
	<put-attribute name="title" value="로그인" />
	<put-attribute name="body" value="/WEB-INF/views/auth/login.jsp" />
</definition>
```

###### 각 요청에 대한 매핑 화면

```xml
<!-- 결과로 /WEB-INF/views/layout/default.jsp 화면이 반환되고 이 화면에는 menu, body, footer가 존재 -->
<definition name="WILDCARD:*" extends="tiles-default">
	<put-attribute name="body" value="/WEB-INF/views/{1}.jsp" />
</definition>
<definition name="WILDCARD:*/*" extends="tiles-default">
	<put-attribute name="body" value="/WEB-INF/views/{1}/{2}.jsp" />
</definition>
<definition name="WILDCARD:*/*/*" extends="tiles-default">
	<put-attribute name="body" value="/WEB-INF/views/{1}/{2}/{3}.jsp" />
</definition>
```

### RestController 구성 및 요청

### Servlet 구성 및 요청

### 예외 처리

### 기타

## 🚨 트러블 슈팅

### [Data truncation: Incorrect datetime value: '1713673255884' for column 'expire_dtm'](https://velog.io/@hakie2kim/Data-truncation-Incorrect-datetime-value-1713673255884-for-column-expiredtm)

### [Neither BindingResult nor plain target object for bean name 'joinForm' available as request attribute](https://velog.io/@hakie2kim/Neither-BindingResult-nor-plain-target-object-for-bean-name-joinForm-available-as-request-attribute)

### [잘못된 이메일 인증 링크로 접속](https://velog.io/@hakie2kim/%EC%9E%98%EB%AA%BB%EB%90%9C-%EC%9D%B4%EB%A9%94%EC%9D%BC-%EC%9D%B8%EC%A6%9D-%EB%A7%81%ED%81%AC%EB%A1%9C-%EC%A0%91%EC%86%8D)

### [로그인 폼 제약 메시지와 로그인 실패 메시지가 동시에 보임](https://velog.io/@hakie2kim/%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%8F%BC-%EC%A0%9C%EC%95%BD-%EB%A9%94%EC%8B%9C%EC%A7%80%EC%99%80-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%8B%A4%ED%8C%A8-%EB%A9%94%EC%8B%9C%EC%A7%80%EA%B0%80-%EB%8F%99%EC%8B%9C%EC%97%90-%EB%B3%B4%EC%9E%84)

### [회원 가입하지 않은 아이디로 로그인 시도](https://velog.io/@hakie2kim/%ED%9A%8C%EC%9B%90-%EA%B0%80%EC%9E%85%ED%95%98%EC%A7%80-%EC%95%8A%EC%9D%80-%EC%95%84%EC%9D%B4%EB%94%94%EB%A1%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%8B%9C%EB%8F%84)

### [Filter에서 Redirect 후 chain.doFilter를 한 경우](https://velog.io/@hakie2kim/Filter%EC%97%90%EC%84%9C-Redirect-%ED%9B%84-chain.doFilter%EB%A5%BC-%ED%95%9C-%EA%B2%BD%EC%9A%B0)

### [게시물 목록에서 필요한 파라미터가 없는 경우](https://velog.io/@hakie2kim/%EA%B2%8C%EC%8B%9C%EB%AC%BC-%EB%AA%A9%EB%A1%9D%EC%97%90%EC%84%9C-%ED%95%84%EC%9A%94%ED%95%9C-%ED%8C%8C%EB%9D%BC%EB%AF%B8%ED%84%B0%EA%B0%80-%EC%97%86%EB%8A%94-%EA%B2%BD%EC%9A%B0)

### [JSP`의 EL 값 조회](https://velog.io/@hakie2kim/JSP%EC%9D%98-EL-%EA%B0%92-%EC%A1%B0%ED%9A%8C)

### [boolean 필드에 Lombok `@Getter` 애너테이션 사용 하는 경우](https://velog.io/@hakie2kim/boolean-%ED%95%84%EB%93%9C%EC%97%90-Lombok-Getter-%EC%95%A0%EB%84%88%ED%85%8C%EC%9D%B4%EC%85%98-%EC%82%AC%EC%9A%A9-%ED%95%98%EB%8A%94-%EA%B2%BD%EC%9A%B0)

### [Integer 타입에는 `@NotBlank`가 아닌 `@NotNull`을 사용](https://velog.io/@hakie2kim/Integer-%ED%83%80%EC%9E%85%EC%97%90%EB%8A%94-NotBlank%EA%B0%80-%EC%95%84%EB%8B%8C-NotNull%EC%9D%84-%EC%82%AC%EC%9A%A9)

## 📝 메모

### [Github SSH Key](https://velog.io/@hakie2kim/Github-SSH-Key)

### [Git 브랜치 전략 - GitFlow](https://velog.io/@hakie2kim/Git-%EB%B8%8C%EB%9E%9C%EC%B9%98-%EC%A0%84%EB%9E%B5-GitFlow)

### [Conventional Commits](https://velog.io/@hakie2kim/Conventional-Commits)

### [Bean Validation](https://velog.io/@hakie2kim/Bean-Validation)

### [BindingResult - reject(), rejectValue()](https://velog.io/@hakie2kim/BindingResult-reject-rejectValue)

### [RedirectAttributes - `addAttributes()` vs. `addFlashAttributes()`](https://velog.io/@hakie2kim/RedirectAttributes-addAttributes-vs.-addFlashAttributes)

### [이메일](https://velog.io/@hakie2kim/%EC%9D%B4%EB%A9%94%EC%9D%BC)

### [Jasypt를 이용한 이메일, 비밀번호 암호화](https://velog.io/@hakie2kim/Jasypt%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%9D%B4%EB%A9%94%EC%9D%BC-%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8-%EC%95%94%ED%98%B8%ED%99%94)

### [Bean 수동 등록 방법](https://velog.io/@hakie2kim/bean-%EC%88%98%EB%8F%99-%EB%93%B1%EB%A1%9D-%EB%B0%A9%EB%B2%95)

### [PRG (Post/Redirect/Get)](https://velog.io/@hakie2kim/PRG-PostRedirectGet)

### [`MimeMessageHelper`의 `setText()`, `EmailUtil.java`의 `sendMail()` 메서드 오버로딩](https://velog.io/@hakie2kim/MimeMessageHelper%EC%9D%98-setText-EmailUtil.java%EC%9D%98-sendMail-%EB%A9%94%EC%84%9C%EB%93%9C-%EC%98%A4%EB%B2%84%EB%A1%9C%EB%94%A9)

### [쿠키 대신 세션을 사용해서 로그인 후 사용자의 정보를 저장해야 하는 이유](https://velog.io/@hakie2kim/%EC%BF%A0%ED%82%A4-%EB%8C%80%EC%8B%A0-%EC%84%B8%EC%85%98%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%B4%EC%84%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%9B%84-%EC%82%AC%EC%9A%A9%EC%9E%90%EC%9D%98-%EC%A0%95%EB%B3%B4%EB%A5%BC-%EC%A0%80%EC%9E%A5%ED%95%B4%EC%95%BC-%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0)

### [`HttpSession`의 `getSession()`](https://velog.io/@hakie2kim/HttpSession%EC%9D%98-getSession)

### [스프링 예외 추상화](https://velog.io/@hakie2kim/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%98%88%EC%99%B8-%EC%B6%94%EC%83%81%ED%99%94)

### [아이디 기억](https://velog.io/@hakie2kim/%EC%95%84%EC%9D%B4%EB%94%94-%EA%B8%B0%EC%96%B5)

### [페이지네이션 (Pagination)](https://velog.io/@hakie2kim/%ED%8E%98%EC%9D%B4%EC%A7%80%EB%84%A4%EC%9D%B4%EC%85%98-Pagination)

### [SQL LIMIT x OFFSET y](https://velog.io/@hakie2kim/SQL-LIMIT-x-OFFSET-y)

### [식별 vs. 비식별 관계](https://velog.io/@hakie2kim/%EC%8B%9D%EB%B3%84-vs.-%EB%B9%84%EC%8B%9D%EB%B3%84-%EA%B4%80%EA%B3%84)

### [`@NotNull`, `@NotEmpty`, `@NotBlank` 의 차이점](https://velog.io/@hakie2kim/NotNull-NotEmpty-NotBlank-%EC%9D%98-%EC%B0%A8%EC%9D%B4%EC%A0%90)

### [`@RequestBody`, `@RequestParam`, `@ModelAttribute`](https://velog.io/@hakie2kim/RequestBody-RequestParam-ModelAttribute)
