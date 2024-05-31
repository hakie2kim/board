<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script>
	window.onload = function() {
		const code = ${code};
		const desc = '${desc}';
		
		if (typeof code !== 'undefined' && code !== null) {
			alert(desc);
		}
	}
</script>
<!--================================
	START LOGIN AREA
=================================-->
<section class="login_area section--padding2">
    <div class="container">
        <div class="row">
            <div class="col-lg-6 offset-lg-3">
                <form:form action="${pageContext.request.contextPath}/auth/login.do" method="post" modelAttribute="loginForm">
					
					<spring:hasBindErrors name="loginForm">
			            <c:forEach items="${errors.globalErrors}" var="err">
			                <div class="error">
		                        <c:out value="${err.defaultMessage}" />
			                </div>
			            </c:forEach>
				    </spring:hasBindErrors>
				    <input type="hidden" name="reqURL" value="${reqURL}">
                    <div class="cardify login">
                        <div class="login--header">
                            <h3>Welcome Back</h3>
                            <p>You can sign in with your username</p>
                        </div>
                        <!-- end .login_header -->

                        <div class="login--form">
                            <div class="form-group">
                                <label for="user_name">Username</label>
                                <%-- <form:input path="memberId" id="user_name" name="memberId" type="text" class="text_field" placeholder="Enter your username..." /> --%>
                                <c:choose>
		                            <c:when test="${empty sessionScope.rememberMe}">
		                            	<form:input path="memberId" id="user_name" name="memberId" type="text" class="text_field" placeholder="Enter your username..." />
		                            </c:when>
		                           	<c:otherwise>
		                           		<!-- remeberMe의 저장된 id 값으로 대체 -->
			                            <form:input path="memberId" id="user_name" name="memberId" type="text" class="text_field" value="${sessionScope.rememberMe}" />
		                            </c:otherwise>
	                            </c:choose>
                                <form:errors path="memberId" cssClass="error" />
                            </div>

                            <div class="form-group">
                                <label for="pass">Password</label>
                                <form:input path="passwd" id="pass" name="passwd" type="password" class="text_field" placeholder="Enter your password..." />
                            	<form:errors path="passwd" cssClass="error" />
                            </div>

                            <div class="form-group">
                                <div class="custom_checkbox">
                                	<!-- remeberMe에 값 저장 여부에 따라 체크 표시 결정 -->
                                    <input type="checkbox" id="ch2" name="rememberMe" ${not empty sessionScope.rememberMe ? "checked" : ""}>
                                    <label for="ch2">
                                        <span class="shadow_checkbox"></span>
                                        <span class="label_text">Remember me</span>
                                    </label>
                                </div>
                            </div>

                            <button class="btn btn--md btn--round" type="submit">Login Now</button>

                            <div class="login_assist">
                                <p class="recover">Lost your
                                    <a href="<c:url value='/auth/recoverPassPage.do'/>">username</a> or
                                    <a href="<c:url value='/auth/recoverPassPage.do'/>">password</a>?</p>
                                <p class="signup">Don't have an
                                    <a href="<c:url value='/auth/joinPage.do'/>">account</a>?</p>
                            </div>
                        </div>
                        <!-- end .login--form -->
                    </div>
                    <!-- end .cardify -->
                </form:form>
            </div>
            <!-- end .col-md-6 -->
        </div>
        <!-- end .row -->
    </div>
    <!-- end .container -->
</section>
<!--================================
	END LOGIN AREA
=================================-->
