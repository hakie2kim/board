<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!--================================
	START SIGNUP AREA
=================================-->
<section class="signup_area section--padding2">
    <div class="container">
        <div class="row">
            <div class="col-lg-6 offset-lg-3">
               <form:form action="${pageContext.request.contextPath}/auth/join.do" method="post" modelAttribute="joinForm">
                    <div class="cardify signup_form">
                        <div class="login--header">
                            <h3>Create Your Account</h3>
                            <p>Please fill the following fields with appropriate information to register a new MartPlace
                                account.
                            </p>
                        </div>
                        <!-- end .login_header -->

                        <div class="login--form">
                            <div class="form-group">
                                <label for="urname">Your Name</label>
                                <form:input path="memberNm" id="urname" name="memberNm" type="text" class="text_field" placeholder="Enter your Name" />
                                <form:errors path="memberNm" cssClass="error" />
                            </div>

                            <div class="form-group">
                                <label for="email_ad">Email Address</label>
                                <form:input path="email" id="email_ad" name="email" type="text" class="text_field" placeholder="Enter your email address" />
                                <form:errors path="email" cssClass="error" />
                            </div>

                            <div class="form-group">
                                <label for="user_name">Username</label>
                                <form:input path="memberId" id="user_name" name="memberId" type="text" class="text_field" placeholder="Enter your username..." />
                                <form:errors path="memberId" cssClass="error" />
                            </div>

                            <div class="form-group">
                                <label for="password">Password</label>
                                <form:input path="passwd" id="password" name="passwd" type="password" class="text_field" placeholder="Enter your password..." />
                                <form:errors path="passwd" cssClass="error" />
                            </div>

                            <div class="form-group">
                                <label for="con_pass">Confirm Password</label>
                                <input type="password" id="con_pass" type="text" class="text_field" placeholder="Confirm password">
                            </div>

                            <button class="btn btn--md btn--round register_btn" type="submit">Register Now</button>

                            <div class="login_assist">
                                <p>Already have an account?
                                    <a href="<c:url value='/auth/loginPage.do'/>">Login</a>
                                </p>
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
	END SIGNUP AREA
=================================-->