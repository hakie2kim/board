# 게시판

## 💬 소개

## 🔨 기능 요구사항

### 회원 가입

`join.jsp` - `<form>` ⮂ `JoinController.java` - `join()` ⮂ `JoinService.java` - `join()` ⮂ `MemberRepository.java` - `addMember()`, `EmailUtil.java` - `sendEmail()`

- 회원 가입 시 폼에는 다음과 같은 제약 사항이 있습니다.
  - [x] 아이디는 공백 또는 빈 칸일 수 없고 4~20자의 영어 소문자, 숫자만 사용 가능합니다.
  - [x] 이미 존재하는 아이디로는 가입할 수 없습니다.
  - [x] 비밀번호는 8~16자의 영문 대/소문자, 숫자를 사용하고, 특수문자를 1개 이상 포함해야 합니다.
  - [x] 이름은 공백 또는 빈 칸일 수 없습니다.
  - [x] 이메일은 공백 또는 빈 칸일 수 없고 이메일 형식이어야 합니다.
- [x] 패스워드는 DB에 암호화 후 저장되어야 합니다.
- 사용자 이메일 유효 여부를 인증해야 합니다.
  - [x] 인증 링크를 포함한 이메일을 보내야 합니다.
  - [ ] 사용자가 인증 링크를 클릭한 후 인증 여부가 DB에 반영되어야 합니다.

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
- 보다 폐쇄적인 데이터 전달 방식입니다.

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

1. 필드 사용

```xml
<bean id="joinDao" class="com.portfolio.www.dao.JoinDao">
  <property name="dataSource" ref="dataSource" />
</bean>
```

- `name`: 주입 받을 `JoinDao`의 필드(멤버 변수) 이름
- `ref`: `JoinDao`의 필드(멤버 변수) `dataSource`의 참조

2. 생성자 사용

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
