<%@ page import="java.util.Set" %>
<%@include file="init.inc.jsp"%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Bookshelf - Browse Categories</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <%@include file="menu.inc.jsp"%>

    <h2>Bookshelf - Browse Categories</h2>
    <%
        Set<String> categories = sessionBean.getService().getCategories(sessionBean.getSessionId());
    %>
    <ul>
        <% for ( String category : categories) {%>
            <li><a></a></li>
        <%}%>
    </ul>
</body>
</html>