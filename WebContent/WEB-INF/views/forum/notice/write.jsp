<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/scripts.jsp" %>
<link rel="stylesheet" href="<%=ctx%>/assest/template/css/trumbowyg.min.css">
<script src="<%=ctx%>/assest/template/js/vendor/trumbowyg.min.js"></script>
<script src="<%=ctx%>/assest/template/js/vendor/trumbowyg/ko.js"></script>
<!--================================
	START DASHBOARD AREA
=================================-->
<section class="support_threads_area section--padding2">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="question-form cardify p-4">
                    <div class="form-group">
                        <label>제목</label>
                        <input type="text" name="title" placeholder="Enter title here" required>
                    </div>
                    <div class="form-group">
                        <label>Description</label>
                        <div id="content"></div>
                    </div>
                    <!-- <form action="#"> -->
                    <div class="form-group">
                        <div class="attachments">
                            <label>Attachments</label>
                            <label>
                                <span class="lnr lnr-paperclip"></span> Add File
                                <span>or Drop Files Here</span>
                                <input type="file" style="display:none;">
                            </label>
                        </div>
                    </div>
                    <!-- </form> -->
                    <div class="form-group">
                        <button class="btn btn--md btn-primary" onClick="javascript:writePost();">Submit Request</button>
                    	<a href="<c:url value='/forum/notice/listPage.do'/>" class="btn btn--md btn-light">Cancel</a>
                    </div>
                </div><!-- ends: .question-form -->
            </div>
            <!-- end .col-md-12 -->
        </div>
        <!-- end .row -->
    </div>
    <!-- end .container -->
</section>
<!--================================
	END DASHBOARD AREA
=================================-->
<script type="text/javascript">
	/* trumbowyg init*/
	let trmbg_container = $('#content');
	if (trmbg_container.length) {
	    trmbg_container.trumbowyg({lang: 'kr'});
	}
	
	/* $('#content').trumbowyg({
	    lang: 'kr'
	}); */
	
    function writePost() {
	  	$.ajax({        
	  		type : 'post',
	  		url : '<%=ctx%>/forum/notice/write.rest',
	  		headers : {
	  			'content-type': 'application/json'
	  		},
	  		dataType : 'json',
	  		data : JSON.stringify({
	  			boardTypeSeq: 1, // 공지사항
	  			title: $('input[name=title]').val(),
	  			content: $('#content').trumbowyg('html')
	  		}),
	  		success : function(result) {
	  			let url = '<%=ctx%>/forum/notice/readPage.do?boardSeq=';
				url += result.boardSeq;
				url += '&boardTypeSeq=';
				url += result.boardTypeSeq;
		  			
				window.location.replace(url);
	  		},
	  		error : function(request, status, error) {
	  			const errorResults = request.responseJSON;
	  			for (const i in errorResults) {
	  				alert(errorResults[i].field + ' - ' + errorResults[i].message);
	  			}
	  		}
	  	});
	}
</script>
