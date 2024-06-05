<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% String ctx = request.getContextPath();%>
<section class="section--padding2">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="">
                    <div class="modules__content">
                        <div class="withdraw_module withdraw_history">
                            <div class="withdraw_table_header">
                                <h3>공지사항</h3>
                            </div>
                            <div class="table-responsive">
                                <table class="table withdraw__table">
                                    <thead>
                                        <tr>
                                        	<th>게시글 번호</th>
                                            <th>제목</th>
                                            <th>작성 일시</th>
                                            <th>작성자</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                       	<c:forEach var="board" items="${boardList}">
	                                       	<tr>
                                               <td>${board.boardSeq}</td>
                                               <td>
                                               	<a href="<c:url value='/forum/notice/readPage.do?boardSeq=${board.boardSeq}&boardTypeSeq=${board.boardTypeSeq}'/>">
                                               		${board.title} 
                                               	</a>
                                               </td>
                                               <td><fmt:formatDate value="${board.regDtm}" pattern="yyyy/MM/dd hh:mm:ss" /></td>
                                               <td>${board.memberNm}</td>
                                           </tr>
                   						</c:forEach>
                                    </tbody>
                                </table>
                                <div style="display: inline-block; margin: 0 5px; float: right; padding-right:10px;">
									<a href="<c:url value='/forum/notice/writePage.do'/>">
										<button class="btn btn--round btn--bordered btn-sm btn-secondary">작성</button>
									</a>
                          		</div>
                                <div class="pagination-area" style="padding-top: 45px;">
                        			<nav class="navigation pagination" role="navigation">
			                            <div class="nav-links">
			                            	<!-- 이전 화살표 -->
			                                <c:if test="${pagination.prev}">
				                                <a 
					                                class="prev page-numbers" 
					                                href="<c:url value='/forum/notice/listPage.do?currentPage=${pagination.startPageNum - 1}&postsPerPage=${pagination.postsPerPage}'/>">
				                                    	<span class="lnr lnr-arrow-left"></span>
				                                </a>
			                                </c:if>
			                                <!-- 페이지 번호 -->
			                                <c:forEach var="pageNum" begin="${pagination.startPageNum}" end="${pagination.endPageNum}">
			                                	<a 
				                                	class="page-numbers <c:if test="${pagination.currentPage eq pageNum}">current</c:if>" 
				                                	href="<c:url value='/forum/notice/listPage.do?currentPage=${pageNum}&postsPerPage=${pagination.postsPerPage}'/>">
				                                		${pageNum}
			                                	</a>
			                                </c:forEach>
			                                <!-- 다음 화살표 -->
			                                <c:if test="${pagination.next}">
				                                <a 
					                                class="next page-numbers" 
					                                href="<c:url value='/forum/notice/listPage.do?currentPage=${pagination.endPageNum + 1}&postsPerPage=${pagination.postsPerPage}'/>">
					                                	<span class="lnr lnr-arrow-right"></span>
				                                </a>
			                                </c:if>
			                            </div>
                        			</nav>
                    			</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end .col-md-6 -->
        </div>
        <!-- end .row -->
    </div>
    <!-- end .container -->
</section>
   
