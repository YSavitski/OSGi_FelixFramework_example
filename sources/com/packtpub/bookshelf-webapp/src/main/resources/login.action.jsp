<%@include file="init.inc.jsp"%>
<%
    String user = request.getParameter("user");
    String pass = request.getParameter("pass");

    if(user==null || pass==null){
        response.sendRedirect("login.jsp");
    }
%>

<%
    try{
        sessionBean.setSessionId(sessionBean.getService().login(user, pass.toCharArray()));
    }
    catch (Throwable t){
        response.sendRedirect("login.jsp");
    }

    response.sendRedirect("index.jsp");
%>