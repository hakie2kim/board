# 게시판

## 💬 소개

## 🔨 기능 요구사항

### 회원 가입

`join.jsp` - `<form>` ⮂ `JoinController.java` - `join()` ⮂ `JoinService.java` - `join()` ⮂ `MemberRepository.java` - `addMember()`, `EmailUtil.java` - `sendEmail()`

- [x] 회원 가입 시 폼에는 다음과 같은 제약 사항이 있습니다.
  - [x] 아이디는 공백 또는 빈 칸일 수 없고 4~20자의 영어 소문자, 숫자만 사용 가능합니다.
  - [x] 이미 존재하는 아이디로는 가입할 수 없습니다.
  - [x] 비밀번호는 8~16자의 영문 대/소문자, 숫자를 사용하고, 특수문자를 1개 이상 포함해야 합니다.
  - [x] 이름은 공백 또는 빈 칸일 수 없습니다.
  - [x] 이메일은 공백 또는 빈 칸일 수 없고 이메일 형식이어야 합니다.
- [x] 패스워드는 DB에 암호화 후 저장되어야 합니다.
- [ ] 인증 링크를 포함한 이메일을 보내야 합니다.

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

## 📝 메모

### Github SSH Key

#### Github SSH Key를 비밀번호 입력 대신 사용해야 하는 이유

- **보안 강화**: SSH 키는 비밀번호보다 보안이 뛰어나며 브루트 포스 공격에 덜 취약합니다.
- **편리성**: 비밀번호를 반복해서 입력할 필요가 없어서 더 빠르고 편리하게 리포지토리에 접근할 수 있습니다.
- **자동화**: CI/CD 파이프라인 등 자동화된 시스템에서 인증을 쉽게 관리할 수 있습니다.
- **권한 관리**: 각 SSH 키는 특정 권한을 부여할 수 있어 세분화된 접근 제어가 가능합니다.

#### Github SSH Key란 무엇인가?

- **SSH (Secure Shell) Key**: 원격 서버와 안전하게 통신하기 위한 암호화 키 페어입니다.
  - **Public Key**: 공개 키로, Github 계정에 등록됩니다.
  - **Private Key**: 개인 키로, 사용자 컴퓨터에 안전하게 보관됩니다.
- **역할**: Github와 같은 서비스와의 통신 시 암호화된 인증을 제공합니다.

#### Github SSH Key를 생성하는 방법

1. **SSH Key 생성**

   ```bash
   ssh-keygen -t ed25519 -C "your_email@example.com"
   ```

   - `-t ed25519`: 생성할 키의 타입을 지정합니다. `rsa` 대신 `ed25519`를 사용하는 것이 권장됩니다.
   - `-C "your_email@example.com"`: 키를 식별할 주석을 추가합니다.

2. **생성된 SSH Key 확인**

   ```bash
   ls ~/.ssh/id_ed25519*
   ```

   - `id_ed25519`: Private key
   - `id_ed25519.pub`: Public key

3. **Github에 공개 키 추가**

   - 공개 키 파일(`~/.ssh/id_ed25519.pub`)의 내용을 복사합니다.
   - Github 웹사이트에 로그인 후, `Settings` ⭢ `SSH and GPG keys`로 이동합니다.
   - `New SSH key` 버튼을 클릭하고, 제목과 키 내용을 입력 후 `Add SSH key` 버튼을 클릭합니다.

4. **SSH Agent에 키 추가**

   ```bash
   eval "$(ssh-agent -s)"
   ssh-add ~/.ssh/id_ed25519
   ```

5. **Github과의 연결 테스트**

   ```bash
   ssh -T git@github.com
   ```

   - 연결이 성공하면 Github로부터 환영 메시지를 받을 것입니다.

### Git 브랜치 전략 - GitFlow

#### Feature Branch

- **목적**: 새로운 기능을 개발하기 위한 브랜치입니다.
- **네이밍**: `feature/기능명` 형식으로 브랜치를 생성합니다.
- **생성 시점**: 기능 개발이 시작될 때마다 생성하며, develop 브랜치에서 분기됩니다.
- **특징**: 개발 작업이 완료되면 develop 브랜치로 병합됩니다.

#### Hotfix Branch

- **목적**: 프로덕션 환경에서 발생한 긴급한 버그를 수정하기 위한 브랜치입니다.
- **네이밍**: `hotfix/버그명` 형식으로 브랜치를 생성합니다.
- **생성 시점**: 긴급한 버그 발견 시 main 브랜치에서 분기됩니다.
- **특징**: 수정이 완료되면 main과 develop 브랜치로 병합됩니다.

#### Bugfix Branch

- **목적**: 개발 중 또는 QA 과정에서 발견된 일반적인 버그를 수정하기 위한 브랜치입니다.
- **네이밍**: `bugfix/버그명` 형식으로 브랜치를 생성합니다.
- **생성 시점**: 버그 발견 시 develop 브랜치에서 분기됩니다.
- **특징**: 수정이 완료되면 develop 브랜치로 병합됩니다.

#### Develop Branch

- **목적**: 개발 중인 기능들이 통합되는 메인 개발 브랜치입니다.
- **생성 시점**: 초기 프로젝트 설정 후 생성되며 feature 브랜치에서의 작업들이 병합됩니다.
- **특징**: 개발 중인 기능을 통합하고 QA 및 테스트를 진행합니다.

#### Main Branch

- **목적**: 프로덕션에 배포되는 안정된 소스 코드가 저장되는 메인 브랜치입니다.
- **생성 시점**: 초기 프로젝트 설정 후 생성되며 main 브랜치에는 배포 가능한 안정된 코드만이 반영됩니다.
- **특징**: 모든 기능이 통합되고 검증된 후 main 브랜치로 배포 준비가 완료됩니다.
- **주의**: 배포 후에는 main 브랜치에서만 분기될 수 있습니다.

### Conventional Commits

#### 형식

```
<type>: <description>
[optional body]
[optional footer]
```

- **`<type>`**: 커밋의 종류를 나타냅니다.
  - feat: 새로운 기능 추가
  - fix: 버그 수정
  - docs: 문서 변경 (예: README.md 수정)
  - style: 코드의 포맷팅, 세미콜론 누락 등 스타일 변경
  - refactor: 코드 리팩토링 (기능 변경 없이 코드 구조 변경)
  - test: 테스트 코드 추가, 변경
  - chore: 빌드 프로세스, 도구 설정 변경 등 그 외의 변경
- **`<description>`**: 커밋의 간단한 설명입니다. 어떤 변경이 이루어졌는지 명확하게 작성합니다.
- **`[optional body]`**: 변경의 상세한 설명을 추가할 수 있는 부분입니다. 변경 내용이나 이유 등을 자세히 설명할 때 사용합니다.
- **`[optional footer]`**: 커밋에 관련된 이슈 번호 등 추가 정보를 포함할 수 있는 부분입니다.

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

오류 메시지 파일의 위치를 인식할 수 있게 이 설정을 추가한다.

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

#### `errors.properties`

`NotBlank`라는 오류 코드를 통해 `MessageCodesResolver`가 어떤 메시지 코드를 순서대로 만드는지 알아보자. 처음이 구체적이고 마지막이 덜 구체적이다.

```
1. NotBlank.item.itemName
2. NotBlank.itemName
3. NotBlank.java.lang.String
4. NotBlank
```

오류 코드는 구체적 ⭢ 덜 구체적인 것을 우선으로 만들어준다. 이때 크게 중요하지 않은 메시지 같은 경우에는 기본 메시지를 사용하도록 한다. 설정된 메시지 파일에서 첫번재로 찾은 오류 코드에 맵핑된 오류 메시지 `아이디는 공백일 수 없습니다` 를 출력한다.

### BindingResult - `rejectValue()`

`rejectValue()` 메서드는 Spring Framework의 BindingResult 인터페이스에서 제공하는 메서드로, 특정 필드에 대한 검증 오류를 등록하는 데 사용됩니다.

#### 메서드 선언부

```java
void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
```

- `field`: 오류가 발생한 필드의 이름
- `errorCode`: 오류 코드를 지정. 이 오류 코드는 메시지에 등록된 코드가 아니라 `MessageCodesResolver`를 위한 오류 코드입니다.
- `defaultMessage`: 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지
- `errorArgs`: 오류 메시지에서 사용할 추가 인수

#### 사용 이유 - 축약된 오류 코드

`rejectValue()` 메서드를 사용하면 오류 코드를 간단하게 입력할 수 있습니다. 예를 들어, `range.item.price` 대신 `range`로만 지정해도 오류 메시지를 잘 찾아서 출력합니다. 이러한 기능은 `MessageCodesResolver` 덕분입니다.

#### 원리

이러한 축약된 오류 코드를 사용할 수 있는 이유는

```java
bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
```

```properties
range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
```

위와 같이 `rejectValue()`를 통해 오류를 등록하고 `errors.properties`에 오류 메시지를 등록하면 `MessageCodesResolver`는 `range`라는 오류 코드를 다음과 같은 순서로 변환하여 메시지를 찾는다.

1. `range.item.price`
2. `range.price`
3. `range.item`
4. `range`

설정된 메시지 파일에서 첫번재로 찾은 오류 코드에 맵핑된 오류 메시지 `가격은 1000 ~ 1000000 까지 허용합니다.` 를 출력한다.
