<%@tag description="Home Page template" pageEncoding="UTF-8"%>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<html>
  <head>
    <jsp:include page="/includes/head.jsp"/>
  </head>
  <body>
    <jsp:include page="/includes/homepage-header.html"/>
    <jsp:doBody/>
    <jsp:include page="/includes/my-footer.jsp"/>
  </body>
</html>