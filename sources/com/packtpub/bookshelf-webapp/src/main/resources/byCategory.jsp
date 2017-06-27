<%@page import="java.util.*" %>
<%@include file="init.inc.jsp"%>

<%
    String category = request.getParameter("category");
    if(category==null || category.equals("")){
        response.sendRedirect("index.jsp");
    }
%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Bookshelf - Browse Category: <%=category%></title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <%@include file="menu.inc.jsp"%>
    <h2>Bookshelf - Browse Category: <%=category%></h2>
    <%
        Set<String> isbns = sessionBean.getService().searchBooksByCategory(sessionBean.getSessionId(), category);
    %>
    <table class="BookList">
        <%@include file="bookListHeaders.jsp"%>
        <% for (String isbn : isbns) {%>
            <jsp:include page="bookListEntry.jsp">
                <jsp:param name="sessionBean" value="<%=sessionBean%>"/>
                <jsp:param name="isbn" value="<%=isbn%>"/>
            </jsp:include>
        <%}%>
    </table>
</body>
</html>