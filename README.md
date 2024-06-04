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

### Data truncation: Incorrect datetime value: '1713673255884' for column 'expire_dtm'

#### 문제 상황

시간을 대소비교 할 때는 숫자로 비교하는 것이 가장 편하기 때문에 기존 `member_auth`의 `expire_dtm`의 데이터 타입을 `INT`로 변경했더니 회원 가입을 할 떄 오류가 발생한다.

##### 오류 메시지

```
Request processing failed; nested exception is org.springframework.dao.DataIntegrityViolationException: StatementCallback;
SQL [INSERT INTO forum.member_auth (member_seq, auth_num, auth_uri, reg_dtm, expire_dtm, auth_yn) VALUES(56, '', 'e67ff01ee499423285426bbb44d05df5', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), 1713673255884, 'N');];
Data truncation: Incorrect datetime value: '1713673255884' for column 'expire_dtm' at row 1;
nested exception is com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Incorrect datetime value: '1713673255884' for column 'expire_dtm' at row 1
```

##### 오류 발생 부분

`MemberAuthDao.java`

```java
public class MemberAuthDao extends JdbcTemplate {
	private DataSource dataSource;

	public int addMemberAuthInfo(MemberAuthDto dto) {
		/*
		 * String sql = String.format("INSERT INTO forum.member_auth " +
		 * "(member_seq, auth_num, auth_uri, reg_dtm, expire_dtm, auth_yn) " +
		 * "VALUES(%d, '', '%s', DATE_FORMAT(NOW(), '%%Y%%m%%d%%H%%i%%s'), %d, 'N'); ",
		 * dto.getMemberSeq(), dto.getAuthUri(), dto.getExpireDtm());
		 */
		String sql = "INSERT INTO forum.member_auth (member_seq, auth_num, auth_uri, reg_dtm, expire_dtm, auth_yn) "
				+ "VALUES(?, '', ?, DATE_FORMAT(NOW(), '%%Y%%m%%d%%H%%i%%s'), ?, 'N'); ";
		Object[] args = {dto.getMemberSeq(), dto.getAuthUri(), dto.getExpireDtm()};
		return update(sql, args);
	}
```

#### 해결 방법

Data truncation이라는 단어에서 알 수 있듯이 `1713673255884`을 `DATETIME` 타입으로 변경하려다 값의 범위를 넘어 데이터 일부가 소실된다는 에러이다. `member_auth`의 `expire_dtm`의 데이터 타입을 더 큰 값의 범위를 담을 수 있는 `BIGINT`로 변경했다.

### Neither BindingResult nor plain target object for bean name 'joinForm' available as request attribute

#### 문제 상황

`/auth/joinPage.do`를 요청했을 때 오류가 발생한다.

##### 오류 메시지

```
Neither BindingResult nor plain target object for bean name 'joinForm' available as request attribute
at org.apache.jsp.WEB_002dINF.views.auth.join_jsp._jspService(join_jsp.java:182)
```

##### 오류 발생 부분

`join.jsp`

```jsp
12 <form:form action="${pageContext.request.contextPath}/auth/join.do" method="post" modelAttribute="joinForm">
```

#### 해결 방법

스프링 프레임워크 form 태그 라이브러리의 `modelAttribute`는 폼에 있는 요소들의 값을 채우기 위한 객체를 지정해주는 속성 중 하나이다. 오류 메시지를 살펴 보면 joinForm이 존재하지 않는다고 헌다. `/auth/join.do`를 POST 방식으로 요청할 때는 오류 없이 잘 작동했는데 왜 그럴까? `@ModelAttribute`이 하는 역할을 한번 살펴보자.

`@ModelAttribute` 애너테이션이 붙은 파라미터에는 다음과 같은 작업이 순서대로 진행된다.

1. 파라미터로 넘겨 준 타입의 오브젝트를 자동으로 생성한다.
2. 생성된 오브젝트에 HTTP로 넘어 온 값들을 자동으로 바인딩한다.
3. 마지막으로 `@ModelAttribute` 어노테이션이 붙은 객체가 자동으로 `Model` 객체에 추가되고 `View`로 전달된다.

마지막 작업에서 알 수 있듯이 `join()`에서 `joinForm`이 `Model` 객체에 추가되고 `View`로 전달됐기 때문에 오류 없이 잘 작동했던 것이다.

따라서 아래와 같이 `model`에 `joinForm`에 대한 객체 정보를 저장하여 `View`f로 전달해야 한다. 처음 입력 폼 페이지를 조회할 때 입력 폼은 모두 비어져 있어야 하기 때문에 빈 객체(`new JoinForm()`)을 전달해야 한다.

`JoinController.java`

```java
@Controller
@RequiredArgsConstructor
public class JoinController {
	private final JoinService joinService;

	@RequestMapping("/auth/joinPage.do")
	public ModelAndView joinPage(@RequestParam HashMap<String, String> params) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("key", Calendar.getInstance().getTimeInMillis());
		mv.addObject("joinForm", new JoinForm());
		mv.setViewName("auth/join");
		return mv;
	}

	@PostMapping("/auth/join.do")
	public String join(@Validated @ModelAttribute JoinForm joinForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
	if (joinService.doesMemberIdExist(joinForm.getMemberId())) {
		bindingResult.rejectValue("memberId", "exist", null, null);
	}
```

### 잘못된 이메일 인증 링크로 접속

#### 문제 상황

`/auth/emailAuth.do`를 요청했을 때 오류가 발생한다.

##### 오류 메시지

```
Required request parameter 'uri' for method parameter type String is not present
```

##### 오류 발생 부분

`JoinController.java`

```java
54 @RequestMapping("/auth/emailAuth.do")
55 public String emailAuth(@RequestParam String uri, RedirectAttributes redirectAttributes) {
```

#### 해결 방법

위 문제 상황은 쿼리 파라미터의 `uri`의 값이 `null`이기 때문에 발생하는 것이다. 이를 방지하기 위해서 아래와 같이 `uri`의 값에 기본값 `""`을 할당했다.

```java
54 @RequestMapping("/auth/emailAuth.do")
55 public String emailAuth(@RequestParam(defaultValue="") String uri, RedirectAttributes redirectAttributes) {
```

하지만 이와 같은 방식으로 처리하는 경우 `JoinService.java`의 메서드를 일부분 사용하게 되는 또 다른 문제를 낳는다. 이후 서블릿 예외 처리를 적용해 컨트롤러에서 이러한 요청 접근을 방지할 예정이다.

### 로그인 폼 제약 메시지와 로그인 실패 메시지가 동시에 보임

#### 문제 상황

로그인 페이지에서 아이디 또는 패스워드만 입력했을 때 각각의 필드에 `공백일 수 없습니다` 메시지와 로그인 실패 메시지인 `아이디 또는 비밀번호가 맞지 않습니다.`가 동시에 보인다.

##### 오류 메시지

없음.

##### 오류 발생 부분

`LoginController.java`

```java
@Controller
@RequiredArgsConstructor
public class LoginController {
	private final LoginService loginService;

	@PostMapping("/auth/login.do")
	public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest req, Model model) {
		// 로그인 폼 입력을 바탕으로 회원 찾기
		MemberDto memberDto = loginService.login(loginForm);

		// 로그인 실패 -> ObjectError 추가
		if (memberDto == null) {
			bindingResult.reject("loginFail", Message.ID_OR_PWD_IS_WRONG.getDescription());
		}

		// 로그인 폼 입력이 잘못된 경우
		if (bindingResult.hasErrors()) {
			return "auth/login";
		}
```

#### 해결 방법

```java
@Controller
@RequiredArgsConstructor
public class LoginController {
	private final LoginService loginService;

	@PostMapping("/auth/login.do")
	public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest req, Model model) {
		// 로그인 폼 입력이 잘못된 경우
		if (bindingResult.hasErrors()) {
			return "auth/login";
		}

		// 로그인 폼 입력을 바탕으로 회원 찾기
		MemberDto memberDto = loginService.login(loginForm);

		// 로그인 실패 -> ObjectError 추가, 로그인 페이지를 다시 보여줌
		if (memberDto == null) {
			bindingResult.reject("loginFail", Message.ID_OR_PWD_IS_WRONG.getDescription());
			return "auth/login";
		}
```

애초에 로그인 폼의 입력이 잘못된 경우 그 이후 단계가 진행되지 못하도록 `bindingResult`에 에러가 있는 경우 로그인 페이지를 다시 보여주는 부분의 위치를 변경해준다. 로그인 실패 시에 로그인 페이지를 다시 보여주는 부분도 추가한다.

### 회원 가입하지 않은 아이디로 로그인 시도

#### 문제 상황

회원가입하지 않은 아이디로 로그인을 시도했을 때 다음과 같은 예외가 발생한다.

##### 오류 메시지

```
org.springframework.dao.EmptyResultDataAccessException: Incorrect result size: expected 1, actual 0
com.portfolio.www.dao.MemberDao.findMemberByUsername(MemberDao.java:37)
```

##### 오류 발생 부분

`MemberController.java`

```java
@RequestMapping("/login.do")
public String login(@ModelAttribute LoginForm form, HttpServletRequest request, Model model) {
  String msg = "";

	MemberDto memberDto = memberService.login(form);

	if (!ObjectUtils.isEmpty(memberDto)) {
		// 세션 처리
		HttpSession session = request.getSession(false);
		session.setAttribute("memberId", memberDto.getMemberId());
		msg = "로그인에 성공했습니다.";
		return "redirect:/main-page.do";

	} else {
		msg = "로그인에 실패했습니다.";
	}

  model.addAttribute("msg", msg);

  return "login";
}
```

#### 해결 방법

```java
@RequestMapping("/login.do")
public String login(@ModelAttribute LoginForm form, HttpServletRequest request, Model model) {
  String msg = "";

  try {
    MemberDto memberDto = memberService.login(form);

    if (!ObjectUtils.isEmpty(memberDto)) {
      // 세션 처리
      HttpSession session = request.getSession(false);
      session.setAttribute("memberId", memberDto.getMemberId());
      msg = "로그인에 성공했습니다.";
      return "redirect:/main-page.do";

    } else {
      msg = "로그인에 실패했습니다.";
    }
  } catch (EmptyResultDataAccessException e) {
    msg = "존재하지 않는 아이디입니다.";
  }

  model.addAttribute("msg", msg);

  return "login";
}
```

에러에 대한 설명을 공식 문서에서 살펴 보니 다음과 같다.

```
Data access exception thrown when a result was expected to have at least one row (or element) but zero rows (or elements) were actually returned.
```

해당 예외는 사용자의 입력값으로 인해 발생했기 때문에 사용자에게 알려주어야 한다. 예외가 발생하면 다음과 같은 방향 `MemberDao` → `MemberService` → `MemberController`으로 예외가 전파된다. 마지막 컨트롤러에서 예외를 `catch`해 뷰에 전달할 메시지를 설정했다.
참고로 `JdbcTemplate`을 사용했을 때 발생했던 문제이며 `MyBatis`를 사용할 경우 쿼리 조회 후 해당 값이 없는 경우 `null`을 반환한다.

```java
@RequestMapping("/login.do")
public String login(@ModelAttribute LoginForm form, HttpServletRequest request, Model model) {
  String msg = "";

  try {
    MemberDto memberDto = memberService.login(form);

    if (!ObjectUtils.isEmpty(memberDto)) {
      // 세션 처리
      HttpSession session = request.getSession(false);
      session.setAttribute("memberId", memberDto.getMemberId());
      msg = "로그인에 성공했습니다.";
      return "redirect:/main-page.do";

    } else {
      msg = "로그인에 실패했습니다.";
    }
  } catch (EmptyResultDataAccessException e) {
    msg = "존재하지 않는 아이디입니다.";
  }

  model.addAttribute("msg", msg);

  return "login";
}
```

### Filter에서 Redirect 후 chain.doFilter를 한 경우

#### 문제 상황

로그인을 하지 않은 상황에서 로그인 후에만 가능하도록 제한한 기능에 접근하려고 할 때 다음과 같은 오류가 발생한다.

##### 오류 메시지

```
응답이 이미 커밋된 후에는, sendRedirect()를 호출할 수 없습니다.
com.portfolio.www.filter.LoginFilter.doFilter(LoginFilter.java:61)
```

##### 오류 발생 부분

`LoginFilter.java` 일부

```java
49	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
50		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
51		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
52
53		String requestURI = httpServletRequest.getRequestURI();
54		String contextPath = httpServletRequest.getContextPath();
55
56		// 요청 URI가 로그인이 필요한 URI 배열에 존재하는지 확인
57		if (Arrays.asList(LOGIN_REQUIRED_URIS).contains(requestURI.replace(contextPath, ""))) {
58		// 세션에 memberId의 값이 존재하는지 확인
59			if (httpServletRequest.getSession().getAttribute(SessionCookieConst.LOGIN_MEMBER) == null) {
60				// 존재하지 않으면 로그인 페이지로 리다이렉트
61				httpServletResponse.sendRedirect(contextPath + LOGIN_PAGE_URI);
62			}
63		}
64
65		chain.doFilter(request, response);
66	}
```

#### 해결 방법

필터는 서블릿에서 지원하는 기능으로 HTTP 요청이 올 때 해당 요청으로 서블릿을 호출하기 전에 처리하는 전처리 작업과 같다. 위와 같은 방식에서 로그인을 안 했을 때 필터는 다음과 같은 순서로 진행된다.

1. 로그인 페이지로 리다이렉트
2. 서블릿 로그인 페이지에 맵핑된 내용을 모두 수행
3. `chain.doFilter()`를 통해 다음 서블릿 또는 필터를 호출

처음에 발생한 HTTP 요청은 이미 2번에서 종료가 된 상태이다. 하지만 위 필터에서는 리다이렉트 후 다시 3번 작업을 진행하려고 한다. 이미 종료된 응답에 또 다시 작업을 진행하려고 하기에 위와 같은 오류가 발생한 것이다. 따라서 아래와 같이 sendRedirect() 후 그 뒤 작업을 하지 못하도록 `return;`을 추가해 주었다.

`LoginFilter.java` 일부

```java
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	HttpServletResponse httpServletResponse = (HttpServletResponse) response;

	String requestURL = httpServletRequest.getRequestURL().toString();
	String requestURI = httpServletRequest.getRequestURI();
	HttpSession session = httpServletRequest.getSession(false);
	String contextPath = httpServletRequest.getContextPath();

	// 요청 URI가 로그인이 필요한 URI 배열에 존재하는지 확인
	if (Arrays.asList(LOGIN_REQUIRED_URIS).contains(requestURI.replace(contextPath, ""))) {
		// 세션에 memberId의 값이 존재하는지 확인
		if (session == null || session.getAttribute(SessionCookieConst.LOGIN_MEMBER) == null) {
			// 존재하지 않으면 로그인 페이지로 리다이렉트
			httpServletResponse.sendRedirect(contextPath + LOGIN_PAGE_URI + "?reqURL=" + requestURL);
			return;
		}
	}

	chain.doFilter(request, response);
}
```

## 📝 메모

### Github SSH Key

#### Github SSH Key를 비밀번호 입력 대신 사용해야 하는 이유

- 보안 강화: SSH 키는 비밀번호보다 보안이 뛰어나며 브루트 포스 공격에 덜 취약합니다.
- 편리성: 비밀번호를 반복해서 입력할 필요가 없어서 더 빠르고 편리하게 리포지토리에 접근할 수 있습니다.
- 자동화: CI/CD 파이프라인 등 자동화된 시스템에서 인증을 쉽게 관리할 수 있습니다.
- 권한 관리: 각 SSH 키는 특정 권한을 부여할 수 있어 세분화된 접근 제어가 가능합니다.

#### Github SSH Key란 무엇인가?

- SSH (Secure Shell) Key: 원격 서버와 안전하게 통신하기 위한 암호화 키 페어입니다.
  - Public Key: 공개 키로, Github 계정에 등록됩니다.
  - Private Key: 개인 키로, 사용자 컴퓨터에 안전하게 보관됩니다.
- 역할: Github와 같은 서비스와의 통신 시 암호화된 인증을 제공합니다.

#### Github SSH Key를 생성하는 방법

1. SSH Key 생성

   ```bash
   ssh-keygen -t ed25519 -C "your_email@example.com"
   ```

   - `-t ed25519`: 생성할 키의 타입을 지정합니다. `rsa` 대신 `ed25519`를 사용하는 것이 권장됩니다.
   - `-C "your_email@example.com"`: 키를 식별할 주석을 추가합니다.

2. 생성된 SSH Key 확인

   ```bash
   ls ~/.ssh/id_ed25519*
   ```

   - `id_ed25519`: Private key
   - `id_ed25519.pub`: Public key

3. Github에 공개 키 추가

   - 공개 키 파일(`~/.ssh/id_ed25519.pub`)의 내용을 복사합니다.
   - Github 웹사이트에 로그인 후, `Settings` ⭢ `SSH and GPG keys`로 이동합니다.
   - `New SSH key` 버튼을 클릭하고, 제목과 키 내용을 입력 후 `Add SSH key` 버튼을 클릭합니다.

4. SSH Agent에 키 추가

   ```bash
   eval "$(ssh-agent -s)"
   ssh-add ~/.ssh/id_ed25519
   ```

5. Github과의 연결 테스트

   ```bash
   ssh -T git@github.com
   ```

   - 연결이 성공하면 Github로부터 환영 메시지를 받을 것입니다.

### Git 브랜치 전략 - GitFlow

#### Feature Branch

- 목적: 새로운 기능을 개발하기 위한 브랜치입니다.
- 네이밍: `feature/기능명` 형식으로 브랜치를 생성합니다.
- 생성 시점: 기능 개발이 시작될 때마다 생성하며, develop 브랜치에서 분기됩니다.
- 특징: 개발 작업이 완료되면 develop 브랜치로 병합됩니다.

#### Hotfix Branch

- 목적: 프로덕션 환경에서 발생한 긴급한 버그를 수정하기 위한 브랜치입니다.
- 네이밍: `hotfix/버그명` 형식으로 브랜치를 생성합니다.
- 생성 시점: 긴급한 버그 발견 시 main 브랜치에서 분기됩니다.
- 특징: 수정이 완료되면 main과 develop 브랜치로 병합됩니다.

#### Bugfix Branch

- 목적: 개발 중 또는 QA 과정에서 발견된 일반적인 버그를 수정하기 위한 브랜치입니다.
- 네이밍: `bugfix/버그명` 형식으로 브랜치를 생성합니다.
- 생성 시점: 버그 발견 시 develop 브랜치에서 분기됩니다.
- 특징: 수정이 완료되면 develop 브랜치로 병합됩니다.

#### Develop Branch

- 목적: 개발 중인 기능들이 통합되는 메인 개발 브랜치입니다.
- 생성 시점: 초기 프로젝트 설정 후 생성되며 feature 브랜치에서의 작업들이 병합됩니다.
- 특징: 개발 중인 기능을 통합하고 QA 및 테스트를 진행합니다.

#### Main Branch

- 목적: 프로덕션에 배포되는 안정된 소스 코드가 저장되는 메인 브랜치입니다.
- 생성 시점: 초기 프로젝트 설정 후 생성되며 main 브랜치에는 배포 가능한 안정된 코드만이 반영됩니다.
- 특징: 모든 기능이 통합되고 검증된 후 main 브랜치로 배포 준비가 완료됩니다.
- 주의: 배포 후에는 main 브랜치에서만 분기될 수 있습니다.

### Conventional Commits

#### 형식

```
<type>: <description>
[optional body]
[optional footer]
```

- `<type>`: 커밋의 종류를 나타냅니다.
  - feat: 새로운 기능 추가
  - fix: 버그 수정
  - docs: 문서 변경 (예: README.md 수정)
  - style: 코드의 포맷팅, 세미콜론 누락 등 스타일 변경
  - refactor: 코드 리팩토링 (기능 변경 없이 코드 구조 변경)
  - test: 테스트 코드 추가, 변경
  - chore: 빌드 프로세스, 도구 설정 변경 등 그 외의 변경
- `<description>`: 커밋의 간단한 설명입니다. 어떤 변경이 이루어졌는지 명확하게 작성합니다.
- `[optional body]`: 변경의 상세한 설명을 추가할 수 있는 부분입니다. 변경 내용이나 이유 등을 자세히 설명할 때 사용합니다.
- `[optional footer]`: 커밋에 관련된 이슈 번호 등 추가 정보를 포함할 수 있는 부분입니다.

#### 예시

```
feat: add user login feature
This commit adds the user login feature including authentication and session management.
Fixes #42
```

### Bean Validation

#### `context-bean.xml`

```xml
<bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean"/>
<bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
	<property name="defaultEncoding" value="UTF-8" />
		<property name="basename" value="classpath:errors" />
</bean>
<bean id="localeResolver" class="org.springframework.web.servlet.i18n.SessionLocaleResolver">
	<property name="defaultLocale" value="ko" />
</bean>
```

##### `bean id="validator"`

`LocalValidatorFactoryBean` 을 글로벌 Validator로 등록한다. 이 Validator는 `@NotNull` 같은 애노테이션을 보고 검증을 수행한다. 이렇게 글로벌 Validator가 적용되어 있기 때문에, `@Valid` , `@Validated` 만 적용하면 된다. 검증 오류가 발생하면 `FieldError` , `ObjectError` 를 생성해서 `BindingResult` 에 담아준다.

##### `bean id="messageSource"`

오류 메시지 파일의 위치를 인식할 수 있게 이 설정을 추가한다. classpath로 지정된 곳에 `errors_ko.properties` 파일이 존재해야 한다.

##### `bean id="localeResolver"`

세션을 통해 사용자의 로케일 정보를 관리합니다.

#### `pom.xml`

```xml
<dependency>
	<groupId>javax.validation</groupId>
	<artifactId>validation-api</artifactId>
	<version>2.0.1.Final</version>
</dependency>
<dependency>
	<groupId>org.hibernate.validator</groupId>
	<artifactId>hibernate-validator</artifactId>
	<version>6.2.5.Final</version>
</dependency>
<dependency>
	<groupId>org.glassfish</groupId>
	<artifactId>jakarta.el</artifactId>
	<version>3.0.3</version>
</dependency>
```

- `jakarta.validation-api`: Bean Validation 인터페이스
- `hibernate-validator`: 구현체
- `jakarta-el`: EL 기능을 제공하는 라이브러리. Hibernate Validator가 EL을 통해 동적인 유효성 검사를 수행할 수 있게 됩니다.

#### `errors.properties`

`NotBlank`라는 오류 코드를 통해 `MessageCodesResolver`가 어떤 메시지 코드를 순서대로 만드는지 알아보자. 처음이 구체적이고 마지막이 덜 구체적이다.

```
1. NotBlank.item.itemName
2. NotBlank.itemName
3. NotBlank.java.lang.String
4. NotBlank
```

오류 코드는 구체적 ⭢ 덜 구체적인 것을 우선으로 만들어준다. 이때 크게 중요하지 않은 메시지 같은 경우에는 기본 메시지를 사용하도록 한다. 설정된 메시지 파일에서 첫번재로 찾은 오류 코드에 맵핑된 오류 메시지 `아이디는 공백일 수 없습니다` 를 출력한다.

### BindingResult - `reject()`, `rejectValue()`

Spring Framework의 BindingResult 인터페이스에서 제공하는 메서드 `reject()`, `rejectValue()`를 통해 `ObjectError`, `FieldError`를 직접 생성하지 않고 검증 오류를 다룰 수 있다.

#### 메서드 선언부

```java
void reject(String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
```

```java
void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
```

- `field`: 오류가 발생한 필드의 이름
- `errorCode`: 오류 코드를 지정. 이 오류 코드는 메시지에 등록된 코드가 아니라 `MessageCodesResolver`를 위한 오류 코드입니다.
- `defaultMessage`: 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지
- `errorArgs`: 오류 메시지에서 사용할 추가 인수

#### 사용 이유 - 축약된 오류 코드

`rejectValue()` 메서드를 사용하면 오류 코드를 간단하게 입력할 수 있다. 예를 들어, `range.item.price` 대신 `range`로만 지정해도 오류 메시지를 잘 찾아서 출력한다. 이러한 기능은 `MessageCodesResolver` 가 필드와 오류 코드를 바탕으로 다음과 같은 메시지 코드들을 만들어 내기 때문이다.

#### `MessageCodesResolver`의 작동 원리

```java
bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
```

```properties
range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
```

위와 같이 `rejectValue()`를 통해 오류를 등록하고 `errors.properties`에 오류 메시지를 등록하면 `MessageCodesResolver`는 `range`라는 오류 코드를 다음과 같은 순서로 변환하여 메시지를 찾는다.

1. `range.item.price=가격은 {0} ~ {1} 까지 허용합니다.`
2. `range.price`
3. `range.item`
4. `range`

설정된 메시지 파일에서 첫번재로 찾은 오류 코드에 맵핑된 오류 메시지 `가격은 1000 ~ 1000000 까지 허용합니다.` 를 출력한다.

### RedirectAttributes - `addAttributes()` vs. `addFlashAttributes()`

Spring MVC에서 RedirectAttributes는 리다이렉트 시에 데이터를 전달하기 위해 사용됩니다. 이때 데이터를 넘기는 방법으로 두 가지 메서드 addAttribute()와 addFlashAttribute()는 서로 다른 방식으로 데이터를 처리하고 전달합니다.

#### `addAttribute()`

- URL에 쿼리 파라미터 형식으로 데이터를 추가합니다.
- URL에 쿼리 파라미터로 추가되므로 짧은 정보와 노출되어도 상관없는 정보를 전달할 때 사용합니다.
- 데이터를 브라우저의 주소창에 표시합니다.
- 여러 요청에 걸쳐 접근이 가능합니다.

#### `addFlashAttribute()`

- 데이터를 세션에 임시로 저장하여 다음 요청에서만 접근 가능하게 합니다.
- 데이터가 URL에 표시되지 않습니다.
- 임시로 세션에 저장되며 다음 요청 후 자동으로 삭제됩니다.
- 검증 결과나 성공/실패 메시지 등 임시 데이터에 적합합니다.
- 폐쇄적인 데이터 전달 방식입니다.

### 이메일

#### `pom.xml`

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

#### `src/main/resources/context-beans.xml`

```xml
<bean id="javaMailSender"
	class="org.springframework.mail.javamail.JavaMailSenderImpl">
	<property name="host" value="smtp.naver.com" />
	<property name="port" value="587" />
	<property name="username" value="${email.username}" />
	<property name="password" value="${email.password}" />
	<property name="javaMailProperties">
		<props>
			<prop key="mail.smtp.starttls.enable">true</prop>
		</props>
	</property>
</bean>
```

이 설정을 통해 `JavaMailSenderImpl` 객체가 생성되고 이를 통해 이메일 전송을 위한 SMTP 서버와 연결할 수 있게 됩니다.

- `host`: 이메일을 전송할 SMTP 서버의 호스트 주소
- `port`: SMTP 서버의 포트 번호
- `username`: SMTP 서버에 로그인할 사용자 이름
- `password`: SMTP 서버에 로그인할 비밀번호
- `javaMailProperties`: 추가적인 JavaMail 속성 설정을 위한 프로퍼티.
- `mail.smtp.starttls.enable`: TLS(Transport Layer Security) 사용 설정. 여기서는 true로 설정하여 TLS를 사용합니다.

```xml
<bean id="emailUtil" class="com.portfolio.www.util.EmailUtil">
	<constructor-arg name="javaMailSender" ref="javaMailSender" />
	<constructor-arg name="senderEmail" value="${email.username}" />
</bean>
```

이 설정을 통해 `EmailUtil` 객체가 생성되고 이를 통해 이메일 전송 기능을 사용할 수 있습니다. `EmailUtil` 클래스는 `JavaMailSender`를 사용하여 이메일을 전송하는 유틸리티 역할을 합니다.

- `javaMailSender`: 이메일 전송에 사용할 JavaMailSender 객체를 주입
- `senderEmail`: 이메일을 전송할 발신자의 이메일 주소

### Jasypt를 이용한 이메일, 비밀번호 암호화

#### `pom.xml`

```xml
<dependency>
	<groupId>org.jasypt</groupId>
	<artifactId>jasypt-spring31</artifactId>
	<version>1.9.3</version>
</dependency>
```

#### `src/main/resources/context-beans.xml`

```xml
<bean id="encryptorConfig" class="org.jasypt.encryption.pbe.config.EnvironmentPBEConfig">
	<!-- 사용할 암호화 알고리즘 -->
	<property name="algorithm" value="PBEWithMD5AndDES" />
	<!-- PBE (패스워드 기반 암호화) 암호 설정 -->
	<property name="password" value="password" />
</bean>
```

이 설정을 통해 암호화 설정 정보를 담은 `EnvironmentPBEConfig` 객체가 생성됩니다.

- `algorithm`: 사용할 암호화 알고리즘. PBEWithMD5AndDES는 MD5 해시와 DES 암호화 알고리즘을 사용하는 패스워드 기반 암호화(PBE) 알고리즘입니다.
- `password`: 암호화에 사용할 패스워드. 이 패스워드는 암호화된 값을 복호화할 때에도 사용됩니다.

```xml
<bean id="encryptor" class="org.jasypt.encryption.pbe.StandardPBEStringEncryptor">
	<property name="config" ref="encryptorConfig" />
</bean>
```

이 설정을 통해 암호화 및 복호화를 수행할 `StandardPBEStringEncryptor` 객체가 생성됩니다. `StandardPBEStringEncryptor`는 앞서 설정한 암호화 알고리즘과 패스워드를 사용하여 문자열을 암호화 및 복호화합니다.

- `config`: 암호화 설정을 참조

```xml
<bean class="org.jasypt.spring31.properties.EncryptablePropertyPlaceholderConfigurer">
	<constructor-arg ref="encryptor" />
	<property name="locations" value="classpath:email.properties" />
</bean>
```

- `ref="encryptor"`: 암호화 및 복호화를 수행할 `StandardPBEStringEncryptor` 객체를 주입
- `locations`: 암호화된 속성 값을 포함하는 프로퍼티 파일의 위치를 지정

#### `src/main/resources/email.properties`

```properties
email.username=ENC(...)
email.password=ENC(...)
```

`ENC(...)`로 표시된 부분은 암호화된 값이며 Spring 애플리케이션에서는 이를 자동으로 복호화하여 사용할 수 있습니다. 이 설정은 민감한 정보를 보호하면서도 애플리케이션이 쉽게 접근할 수 있도록 합니다.

### `bean` 수동 등록 방법

#### Setter 사용

```xml
<bean id="joinDao" class="com.portfolio.www.dao.JoinDao">
  <property name="dataSource" ref="dataSource" />
</bean>
```

- `name`: 주입 받을 `JoinDao`의 필드(멤버 변수) 이름
- `ref`: `JoinDao`의 필드(멤버 변수) `dataSource`의 참조

#### 생성자 사용

```xml
<bean id="emailUtil" class="com.portfolio.www.util.EmailUtil">
	<constructor-arg name="javaMailSender" ref="javaMailSender" />
	<constructor-arg name="senderEmail" value="${email.username}" />
</bean>
```

- `value`: 생성자의 파라미터 이름 `senderEmail`의 인자로 올 값

### PRG (Post/Redirect/Get)

`JoinController.java`의 `join()`에서 리다이렉트를 하는 이유는 무엇일까? 만약 리다이렉트를을 하지 않았다고 해보자. 회원 가입을 한 페이지에서 새로 고침을 한 후 DB를 확인 해보면 회원이 또 추가됐다는 것을 알 수 있다. 새로고침을 할 때마다 기존 입력한 회원이 계속해서 추가되는 것이다. 왜 이런 현상이 발생하는 것일까?

결론부터 말하자면 웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송하는 작업을 한다. 그렇기 때문에 `POST /auth/join.do` + `회원 가입 폼에서 입력한 회원 데이터` 이 작업이 계속해서 반복된다. 따라서 회원 내용은 같고 `member_seq`만 증가한 `Member`의 데이터가 계속 DB에 추가된다. 여기에서 왜 리다이렉트를 로그인 페이지(`/loginPage.do`)로 하는지 알 수 있다. 다시 로그인 페이지(`/loginPage.do`)로 이동하게 되면 아무리 새로고침을 해도 웹 브라우저는 그저 로그인 페이지(`/loginPage.do`)만을 보여주게 된다.

위와 같은 방식을 `Post/Redirect/Get` 줄여서 `PRG`라 하며 Spring의 `redirect:이동할 주소`와 더불어 `RedirectAttributes` 기능을 사용하게 되면 폼 전송 후 자동으로 리다이렉트 하게 된다.

### `MimeMessageHelper`의 `setText()`, `EmailUtil.java`의 `sendMail()` 메서드 오버로딩

`EmailUtil.java`의 메서드 `sendMail()`을 보면 메서드가 오버로딩 되어있는 것을 볼 수 있다. 왜 그런 것일까?

회원이 클릭할 수 있는 링크가 담긴 이메일을 받기 위해서는 이메일 본문을 `HTML` 태그 형식으로 보내야 한다. 그렇게 하기 위해서는 우선 `MimeMessageHelper`의 `setText`메서드를 살펴봐야 한다.

```java
public void setText(String text) throws MessagingException {
  setText(text, false);
}

/**
	 * Set the given text directly as content in non-multipart mode
	 * or as default body part in multipart mode.
	 * The "html" flag determines the content type to apply.
	 * <p><b>NOTE:</b> Invoke {@link #addInline} <i>after</i> {@code setText};
	 * else, mail readers might not be able to resolve inline references correctly.
	 * @param text the text for the message
	 * @param html whether to apply content type "text/html" for an
	 * HTML mail, using default content type ("text/plain") else
	 * @throws MessagingException in case of errors
	 */
public void setText(String text, boolean html) throws MessagingException {
  // ...
}
```

주석을 보면 `@param html`에 대한 설명이 있는데 요약하면 다음과 같다.

- `content type "text/html"` ⭢ `html`의 값 `true`
- `content type "text/plain"` ⭢ `html`의 값 `false`

실제로도 매개 변수가 하나만 있는 `setText(String text)`를 사용하면 `"text/plain"` 형식으로 `html` 본문이 구성된다. 이때, `setText(String text)` 안에는 `setText(String text, boolean html)`가 있는 것을 확인할 수 있다. 이와 같이, 어떤 메서드의 파라미터를 기본값으로 지정(`"text/plain"`)해주고 싶을 때와 아닌 경우를 구별할 때 이러한 메서드 오버로딩 방식이 많이 사용된다.

### 쿠키 대신 세션을 사용해서 로그인 후 사용자의 정보를 저장해야 하는 이유

웹 애플리케이션에서 사용자 인증과 상태 유지를 위해 쿠키와 세션을 사용한다. 보안과 관리 측면에서 세션이 더 안전한 이유는 다음과 같다.

#### 1. 쿠키 값 변조 문제

- 문제점: 사용자가 쿠키 값을 임의로 변경할 수 있다.
  - 예시: `Cookie: memberId=1`을 `Cookie: memberId=2`로 변경하면 다른 사용자의 정보에 접근할 수 있다.
- 해결책: 세션을 사용하면 클라이언트에 중요한 정보를 직접 저장하지 않고, 추정 불가능한 세션 ID만 쿠키에 저장하여 전달한다.

#### 2. 쿠키 정보 탈취 문제

- 문제점: 쿠키에 저장된 정보는 쉽게 탈취될 수 있다.
  - 예시: 개인정보나 신용카드 정보가 쿠키에 저장되면 로컬 PC나 네트워크 전송 구간에서 탈취될 수 있다.
- 해결책: 세션을 사용하면 중요한 정보는 서버에 저장되고, 클라이언트에는 추정 불가능한 세션 ID만 전달된다. 따라서 해커가 세션 ID를 탈취해도 중요한 정보를 쉽게 얻을 수 없습니다.

#### 3. 세션을 사용한 보안 강화

- 임의의 토큰 사용: 예측 불가능한 임의의 세션 ID을 생성 후 서버에서 세션 ID과 사용자 ID를 매핑하여 관리한다.
- 세션 ID 만료 시간 설정: 세션 ID의 만료 시간을 짧게 유지(예: 30분)하거나 해킹이 의심될 경우 강제로 제거할 수 있다.

### 세션 동작 방식

1. 로그인

   - 사용자가 `id`와 `password`를 전달하면 서버에서 사용자 인증을 수행한다.

2. 세션 생성

   - 추정 불가능한 세션 ID(예: UUID)를 생성하고, 세션 저장소에 사용자 정보를 저장한다.
   - 예시: `Cookie: mySessionId=zz0101xx-bab9-4b92-9b32-dadb280f4b61`

3. 세션 ID를 응답 쿠키로 전달

   - 서버는 클라이언트에 `mySessionId` 쿠키를 전달한다.
   - 클라이언트는 이 쿠키를 저장하고, 이후 요청 시 항상 이 쿠키를 서버에 전달한다.

4. 세션 정보 조회
   - 서버는 클라이언트가 전달한 `mySessionId` 쿠키를 기반으로 세션 저장소를 조회하여 사용자 정보를 확인한다.

### 쿠키와 비교한 세션의 장점

- 변조 방지: 세션 ID는 예측 불가능한 복잡한 값으로 설정된다.
- 정보 보호: 쿠키에 중요한 정보를 저장하지 않으므로 쿠키가 탈취되더라도 중요한 정보는 보호된다.
- 세션 ID 만료 관리: 해커가 세션 ID을 탈취해도 시간이 지나면 사용할 수 없도록 만료 시간을 짧게 설정할 수 있다.

### `HttpSession`의 `getSession()`

#### 메서드 선언부

`public HttpSession getSession(boolean create);`

#### `request.getSession(true)`

- 세션이 있으면 기존 세션을 반환
- 세션이 없으면 새로운 세션을 생성해서 반환

#### `request.getSession(false)`

- 세션이 있으면 기존 세션을 반환
- 세션이 없으면 새로운 세션을 생성하지 않고 `null` 을 반환

### 스프링 예외 추상화

스프링은 데이터 접근 계층에서 발생하는 수많은 예외들을 추상화해 DB 기술에 종속적이지 않은 예외 계층을 제공하고 있다. 사실 `JdbcTemplate`을 사용하면 각 리포지토리 메서드에서 발생하는 여러 반복 작업을 대신해준다. 그 반복 작업에는 **예외 발생시 스프링 예외 변환기 실행** 또한 포함되어 있다.

존재하지 않는 아이디로 로그인 시도를 할 때 발생하는 예외 `EmptyResultDataAccessException`는 `NonTransientDataAccessException`을 상속 받는 `RuntimeException` 언체크드 예외 중 하나이다. `NonTransientDataAccessException`는 같은 SQL을 반복해서 실행하면 실패하는 예외이다. (`EmptyResultDataAccessException` → `NonTransientDataAccessException` → `DataAccessException` → `RuntimeException`)

### 아이디 기억

`LoginController.java` 일부

```java
@RequestMapping("/auth/loginPage.do")
public ModelAndView loginPage(@RequestParam HashMap<String, String> params, HttpServletRequest req) {
	ModelAndView mv = new ModelAndView();
	mv.addObject("key", Calendar.getInstance().getTimeInMillis());
	mv.addObject("loginForm", new JoinForm());
	Cookie[] cookies = req.getCookies();
  if (cookies != null) {
      for (Cookie cookie : cookies) {
          if (REMEBER_ME.equals(cookie.getName())) {
              String memberId = cookie.getValue();
              req.getSession().setAttribute(REMEBER_ME, memberId);
              break;
          }
      }
  }
	mv.setViewName("auth/login");
	return mv;
}

@PostMapping("/auth/login.do")
public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest req, HttpServletResponse resp) {

	// (생략)

	// 아이디 기억하기
	if (loginForm.isRememberMe()) {
          Cookie cookie = new Cookie(REMEBER_ME, loginForm.getMemberId());
          cookie.setMaxAge(7 * 24 * 60 * 60); // 일주일
          cookie.setHttpOnly(true);
          resp.addCookie(cookie);
  } else {
  	Cookie cookie = new Cookie(REMEBER_ME, null);
      cookie.setMaxAge(0);
      resp.addCookie(cookie);
  }

	// (생략)

	return "redirect:/index.do";
}
```

#### `cookie.setHttpOnly(true)`를 사용하는 이유

- XSS 공격 방지: `HttpOnly`를 `true`로 설정하면 클라이언트 측 스크립트가 쿠키에 접근할 수 없게 되어 크로스 사이트 스크립팅(XSS) 공격의 위험이 줄어듭니다.
- JavaScript 접근 차단: 쿠키가 서버에서만 접근 가능하게 하여 클라이언트 측 스크립트에서는 쿠키에 접근할 수 없도록 합니다.
- 보안 강화: 악성 스크립트에 의해 세션 식별자와 같은 민감한 정보가 도난당하는 것을 방지합니다.

`login.jsp` 일부

```html
<div class="login--form">
  <div class="form-group">
    <label for="user_name">Username</label>
    <c:choose>
      <c:when test="${empty sessionScope.rememberMe}">
        <form:input
          path="memberId"
          id="user_name"
          name="memberId"
          type="text"
          class="text_field"
          placeholder="Enter your username..."
        />
      </c:when>
      <c:otherwise>
        <!-- remeberMe의 저장된 id 값으로 대체 -->
        <form:input
          path="memberId"
          id="user_name"
          name="memberId"
          type="text"
          class="text_field"
          value="${sessionScope.rememberMe}"
        />
      </c:otherwise>
    </c:choose>
    <form:errors path="memberId" cssClass="error" />
  </div>

  <div class="form-group">
    <label for="pass">Password</label>
    <form:input
      path="passwd"
      id="pass"
      name="passwd"
      type="password"
      class="text_field"
      placeholder="Enter your password..."
    />
    <form:errors path="passwd" cssClass="error" />
  </div>

  <div class="form-group">
    <div class="custom_checkbox">
      <!-- remeberMe에 값 저장 여부에 따라 체크 표시 결정 -->
      <input type="checkbox" id="ch2" name="rememberMe" ${not empty
      sessionScope.rememberMe ? checked : ''}>
      <label for="ch2">
        <span class="shadow_checkbox"></span>
        <span class="label_text">Remember me</span>
      </label>
    </div>
  </div>
</div>
```
