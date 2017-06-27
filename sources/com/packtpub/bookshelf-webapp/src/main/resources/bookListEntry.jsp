<%@ page import="com.packtpub.felix.bookshelf.inventory.api.*" %>
<%@ page import="com.packtpub.felix.bookshelf.inventory.api.model.Book" %>
<%@ page import="com.packtpub.felix.bookshelf.inventory.api.exceptions.BookNotFoundException" %>
<%@include file="init.inc.jsp"%>

<%
    String isbn = request.getParameter("isbn");
    Book book = null;

    try{
        book = sessionBean.getService().getBook(sessionBean.getSessionId(), isbn);%>
    <tr class="BookListEntry">
        <td><%= book.getCategory() %></td>
        <td><%= book.getIsbn() %></td>
        <td><%= book.getTitle() %></td>
        <td><%= book.getAuthor() %></td>
        <td><%= book.getRating() %></td>
    </tr>
<%  }catch (BookNotFoundException e){ %>
        <tr class="BookListEntry">
            <td>-</td>
            <td><%= isbn %></td>
            <td>"<%= e.getMessage() %>"</td>
            <td>-</td>
            <td>-</td>
        </tr>
<%  }%>