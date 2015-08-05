<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<sql:query var="rs" dataSource="jdbc/mineSQL">
select table_name from dictionary
</sql:query>

<html>
  <head>
    <title>DB Test</title>
  </head>
  <body>

<c:out value="prova_di_cout" /> 

  <h2>Results</h2>
<c:forEach var="i" begin="1" end="20" step="1" varStatus ="status">
<c:out value="${i}" /> 
</c:forEach>

<c:forEach var="row" items="${rs.rows}">
    Table Name ${row.table_name}<br/>
</c:forEach>

  </body>
</html>

