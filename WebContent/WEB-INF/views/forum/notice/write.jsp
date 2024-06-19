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
                    <!-- <form method="post" enctype="multipart/form-data"> -->
                    <div class="form-group">
                        <label>제목</label>
                        <input type="text" name="title" placeholder="Enter title here" required>
                    </div>
                    <div class="form-group">
                        <label>Description</label>
                        <div id="content"></div>
                    </div>
                    <div class="form-group">
                        <div class="attachments">
                            <label>Attachments</label>
                            <label>
                                <span class="lnr lnr-paperclip"></span> Add File
                                <span>or Drop Files Here</span>
                                <input type="file" name="attFile" style="display:inline-block;" multiple>
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
		let formData = new FormData();
		
		/* formData.append('boardTypeSeq', 1); // 공지사항
		formData.append('title', $('input[name=title]').val());
		formData.append('content', $('#content').trumbowyg('html')); */
		
        let jsonData = {
	        boardTypeSeq: 1, // 공지사항
	        title: $('input[name=title]').val(),
	        content: $('#content').trumbowyg('html')
	    };
		let jsonBlob = new Blob([JSON.stringify(jsonData)], { type: 'application/json' });
		formData.append('boardWriteDto', jsonBlob);
		
		// input type=file이 multiple이 아닌 경우
		formData.append("attFiles", $('input[name=attFile]')[0].files[0]);
		
		/* // input type=file이 multiple인 경우
		let attFiles = $('input[name=attFile]')[0].files;
		for (let i = 0; i < attFiles.length; i++) {
			formData.append("attFile", attFiles[i]);
		} */
		
		console.dir(formData);
		
		/* let entries = formData.entries();
		for (const pair of entries) {
		    console.log(pair[0]+ ', ' + pair[1]); 
		} */
		
	  	$.ajax({        
	  		type : 'post',
	  		// enctype: 'multipart/form-data', // 파일 업로드
	  		url: '<%=ctx%>/forum/notice/write.rest',
	  		data: formData,
	  		// async: false, // ERR_CONNECT_RESET 해결
	  		processData: false,
	  		contentType: false,
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
