<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>Insert title here</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/servlet/UploadServlet" enctype="multipart/form-data" method="post">
   		上传用户:<input type="text" name="username">
   		上传文件1:<input type="file" name="file1">
   		上传文件2:<input type="file" name="file2">
   		<input type="submit" name="上传" value="上传">
	</form>
	文件下载列表<a href="${pageContext.request.contextPath }/servlet/ListFileServlet">刷新</a>
		<c:forEach var = "entry" items="${requestScope.map}">
			<c:url var="url" value="/servlet/DownLoadServlet">
				<c:param name="filename" value="${entry.key}"></c:param>
			</c:url>
			${entry.value}<a href="${url }">下载</a><br/>
		</c:forEach>
	
	
	
</body>
</html>