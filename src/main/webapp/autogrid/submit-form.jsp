<%@include file="../common/submit.jsp"%>

<%
response.setContentType("text/html; charset=iso-8859-1");

Logger log = Logger.getLogger("submit-form.jsp");

// POST parameters capture
PostMap pm = new PostMap( request);

out.write(CommonOperation.switchOperation("LISTA_COMPONENTI", 
					  pm.toHashMap(), 
					  request.getParameter("IDCOMPONENTE"),
					  pm.onlyCheck() )
);
%>
