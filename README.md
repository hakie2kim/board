# 게시판

## 💬 소개

배포 경험을 위해 CRUD의 가장 기본적인 기능을 포함한 회원 가입, 로그인/로그아웃, 게시판, 댓글 기능을 구현한 웹 페이지

## 📚 기술 스택

- 서버 사이드 렌더링: `Tiles` `JSP`
- 벡엔드: `Spring`
- 데이터베이스: `MyBatis` `MySQL` `Docker`
- 배치: `Maven`
- 클라우드: `AWS - EC2, RDS`
- 서버: `Ngnix`
- 기타 도구: `MobaXterm` `Sourcetree` `Github` `Notion`

## 🔨 기능 요구사항

### 회원 가입

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

#### [Sequence Diagram](https://velog.io/@hakie2kim/Sequence-Diagram)

#### [프로젝트 환경 설정](https://velog.io/@hakie2kim/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%99%98%EA%B2%BD-%EC%84%A4%EC%A0%95)

### RestController 구성 및 요청

### Servlet 구성 및 요청

### 예외 처리

### 기타

## 🚨 트러블 슈팅

#### [Data truncation: Incorrect datetime value: '1713673255884' for column 'expire_dtm'](https://velog.io/@hakie2kim/Data-truncation-Incorrect-datetime-value-1713673255884-for-column-expiredtm)

- `member_auth` 테이블의 `expire_dtm` 컬럼을 `INT`로 변경한 후, 회원 가입 시 "Data truncation: Incorrect datetime value" 오류 발생
- `expire_dtm` 컬럼의 데이터 타입을 더 큰 범위를 담을 수 있는 `BIGINT`로 변경하여 오류 해결

#### [Neither BindingResult nor plain target object for bean name 'joinForm' available as request attribute](https://velog.io/@hakie2kim/Neither-BindingResult-nor-plain-target-object-for-bean-name-joinForm-available-as-request-attribute)

- `/auth/joinPage.do` 요청 시 "Neither BindingResult nor plain target object for bean name 'joinForm' available as request attribute" 오류 발생
- `JoinController`의 `joinPage` 메소드에 `joinForm` 객체를 `Model`에 추가하여 해결

#### [잘못된 이메일 인증 링크로 접속](https://velog.io/@hakie2kim/%EC%9E%98%EB%AA%BB%EB%90%9C-%EC%9D%B4%EB%A9%94%EC%9D%BC-%EC%9D%B8%EC%A6%9D-%EB%A7%81%ED%81%AC%EB%A1%9C-%EC%A0%91%EC%86%8D)

- `/auth/emailAuth.do` 요청 시 "Required request parameter 'uri' for method parameter type String is not present" 오류 발생
- `@RequestParam`에 `defaultValue=""`를 설정하여 `uri` 값이 `null`인 경우를 방지

#### [로그인 폼 제약 메시지와 로그인 실패 메시지가 동시에 보임](https://velog.io/@hakie2kim/%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%8F%BC-%EC%A0%9C%EC%95%BD-%EB%A9%94%EC%8B%9C%EC%A7%80%EC%99%80-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%8B%A4%ED%8C%A8-%EB%A9%94%EC%8B%9C%EC%A7%80%EA%B0%80-%EB%8F%99%EC%8B%9C%EC%97%90-%EB%B3%B4%EC%9E%84)

- 로그인 페이지에서 아이디 또는 패스워드만 입력 시, 각각의 필드에 "공백일 수 없습니다" 메시지와 "아이디 또는 비밀번호가 맞지 않습니다" 메시지가 동시에 보임
- `bindingResult`에 에러가 있는 경우 로그인 페이지를 다시 보여주는 부분의 위치를 로그인 폼 입력 검증 후로 이동하여 해결

#### [회원 가입하지 않은 아이디로 로그인 시도](https://velog.io/@hakie2kim/%ED%9A%8C%EC%9B%90-%EA%B0%80%EC%9E%85%ED%95%98%EC%A7%80-%EC%95%8A%EC%9D%80-%EC%95%84%EC%9D%B4%EB%94%94%EB%A1%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%8B%9C%EB%8F%84)

- 회원가입하지 않은 아이디로 로그인 시도 시, `EmptyResultDataAccessException` 예외 발생
- `MemberController`에서 `EmptyResultDataAccessException`을 `catch`하여 "존재하지 않는 아이디입니다." 메시지를 설정하고 로그인 페이지를 다시 보여줌

#### [Filter에서 Redirect 후 chain.doFilter를 한 경우](https://velog.io/@hakie2kim/Filter%EC%97%90%EC%84%9C-Redirect-%ED%9B%84-chain.doFilter%EB%A5%BC-%ED%95%9C-%EA%B2%BD%EC%9A%B0)

- 로그인을 하지 않은 상태에서 로그인 후에만 접근 가능한 기능에 접근 시, "응답이 이미 커밋된 후에는, sendRedirect()를 호출할 수 없습니다" 오류 발생
- `LoginFilter`에서 `sendRedirect()` 후 `return;`을 추가하여 리다이렉트 후 추가 작업이 진행되지 않도록 수정

#### [게시물 목록에서 필요한 파라미터가 없는 경우](https://velog.io/@hakie2kim/%EA%B2%8C%EC%8B%9C%EB%AC%BC-%EB%AA%A9%EB%A1%9D%EC%97%90%EC%84%9C-%ED%95%84%EC%9A%94%ED%95%9C-%ED%8C%8C%EB%9D%BC%EB%AF%B8%ED%84%B0%EA%B0%80-%EC%97%86%EB%8A%94-%EA%B2%BD%EC%9A%B0)

- `/forum//notice/listPage.do?page=1&size=10` 요청 시, `query string` 값이 없을 경우 "Cannot parse null string" 오류 발생
- `@RequestParam`에 `defaultValue`를 설정하여 `page`와 `size` 값이 없을 때 기본값을 가지도록 설정하여 해결

#### [JSP의 EL 값 조회](https://velog.io/@hakie2kim/JSP%EC%9D%98-EL-%EA%B0%92-%EC%A1%B0%ED%9A%8C)

- `${pagination.postsPerPage}`를 호출할 때, `Pagination` 클래스에 해당 필드에 대한 `getter` 메서드가 없어서 발생한 예외
- `Pagination` 클래스에 `getPostsPerPage()` 메서드를 추가하여 문제 해결

#### [boolean 필드에 Lombok `@Getter` 애너테이션 사용 하는 경우](https://velog.io/@hakie2kim/boolean-%ED%95%84%EB%93%9C%EC%97%90-Lombok-Getter-%EC%95%A0%EB%84%88%ED%85%8C%EC%9D%B4%EC%85%98-%EC%82%AC%EC%9A%A9-%ED%95%98%EB%8A%94-%EA%B2%BD%EC%9A%B0)

- `list.jsp`에서 `pagination.isPrev`와 `pagination.isNext`를 호출할 때, `Pagination` 클래스에 해당 필드에 대한 `getter`가 없어서 발생한 예외
- `Lombok`의 `@Getter`는 `boolean` 타입 필드의 접근자를 `is[필드명]`으로 생성하는데, 이를 위해 `isPrev`와 `isNext`를 각각 `prev`와 `next`로 필드 이름을 변경하여 문제를 해결

#### [Integer 타입에는 `@NotBlank`가 아닌 `@NotNull`을 사용](https://velog.io/@hakie2kim/Integer-%ED%83%80%EC%9E%85%EC%97%90%EB%8A%94-NotBlank%EA%B0%80-%EC%95%84%EB%8B%8C-NotNull%EC%9D%84-%EC%82%AC%EC%9A%A9)

- 공지사항 게시글 작성 시, `@NotBlank` 애너테이션을 `boardTypeSeq` 필드에 적용했으나, 이는 `Integer` 타입에서는 사용할 수 없어서 발생한 오류
- `boardTypeSeq` 필드의 검증 애너테이션을 `@NotNull`로 변경하여 문제를 해결

## 📝 메모

#### [Github SSH Key](https://velog.io/@hakie2kim/Github-SSH-Key)

#### [Git 브랜치 전략 - GitFlow](https://velog.io/@hakie2kim/Git-%EB%B8%8C%EB%9E%9C%EC%B9%98-%EC%A0%84%EB%9E%B5-GitFlow)

#### [Conventional Commits](https://velog.io/@hakie2kim/Conventional-Commits)

#### [Bean Validation](https://velog.io/@hakie2kim/Bean-Validation)

#### [BindingResult - `reject()`, `rejectValue()`](https://velog.io/@hakie2kim/BindingResult-reject-rejectValue)

#### [RedirectAttributes - `addAttributes()` vs. `addFlashAttributes()`](https://velog.io/@hakie2kim/RedirectAttributes-addAttributes-vs.-addFlashAttributes)

#### [이메일](https://velog.io/@hakie2kim/%EC%9D%B4%EB%A9%94%EC%9D%BC)

#### [Jasypt를 이용한 이메일, 비밀번호 암호화](https://velog.io/@hakie2kim/Jasypt%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%9D%B4%EB%A9%94%EC%9D%BC-%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8-%EC%95%94%ED%98%B8%ED%99%94)

#### [Bean 수동 등록 방법](https://velog.io/@hakie2kim/bean-%EC%88%98%EB%8F%99-%EB%93%B1%EB%A1%9D-%EB%B0%A9%EB%B2%95)

#### [PRG (Post/Redirect/Get)](https://velog.io/@hakie2kim/PRG-PostRedirectGet)

#### [`MimeMessageHelper`의 `setText()`, `EmailUtil.java`의 `sendMail()` 메서드 오버로딩](https://velog.io/@hakie2kim/MimeMessageHelper%EC%9D%98-setText-EmailUtil.java%EC%9D%98-sendMail-%EB%A9%94%EC%84%9C%EB%93%9C-%EC%98%A4%EB%B2%84%EB%A1%9C%EB%94%A9)

#### [쿠키 대신 세션을 사용해서 로그인 후 사용자의 정보를 저장해야 하는 이유](https://velog.io/@hakie2kim/%EC%BF%A0%ED%82%A4-%EB%8C%80%EC%8B%A0-%EC%84%B8%EC%85%98%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%B4%EC%84%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%9B%84-%EC%82%AC%EC%9A%A9%EC%9E%90%EC%9D%98-%EC%A0%95%EB%B3%B4%EB%A5%BC-%EC%A0%80%EC%9E%A5%ED%95%B4%EC%95%BC-%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0)

#### [`HttpSession`의 `getSession()`](https://velog.io/@hakie2kim/HttpSession%EC%9D%98-getSession)

#### [스프링 예외 추상화](https://velog.io/@hakie2kim/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%98%88%EC%99%B8-%EC%B6%94%EC%83%81%ED%99%94)

#### [아이디 기억](https://velog.io/@hakie2kim/%EC%95%84%EC%9D%B4%EB%94%94-%EA%B8%B0%EC%96%B5)

#### [페이지네이션 (Pagination)](https://velog.io/@hakie2kim/%ED%8E%98%EC%9D%B4%EC%A7%80%EB%84%A4%EC%9D%B4%EC%85%98-Pagination)

#### [SQL LIMIT x OFFSET y](https://velog.io/@hakie2kim/SQL-LIMIT-x-OFFSET-y)

#### [식별 vs. 비식별 관계](https://velog.io/@hakie2kim/%EC%8B%9D%EB%B3%84-vs.-%EB%B9%84%EC%8B%9D%EB%B3%84-%EA%B4%80%EA%B3%84)

#### [`@NotNull`, `@NotEmpty`, `@NotBlank` 의 차이점](https://velog.io/@hakie2kim/NotNull-NotEmpty-NotBlank-%EC%9D%98-%EC%B0%A8%EC%9D%B4%EC%A0%90)

#### [`@RequestBody`, `@RequestParam`, `@ModelAttribute`](https://velog.io/@hakie2kim/RequestBody-RequestParam-ModelAttribute)
