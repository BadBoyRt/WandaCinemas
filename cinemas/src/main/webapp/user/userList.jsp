<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.List"%>
    <%@ page import="com.thtf.cinemas.domain.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%List<User> list = (List<User>)request.getAttribute("list"); %>

<table>
	<tr>
		<td><%=list.get(0).getUsername()%></td>
		<td><%=list.get(0).getPassword()%></td>
	</tr>
</table>
</body>
</html>