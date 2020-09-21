<%@tag description="Home Page template" pageEncoding="UTF-8"%>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>

<html>
  <head>
    <jsp:include page="/includes/head.jsp"/>
  </head>
  <body>
    <div class="page-container">
      <div class="content-wrap">
        <jsp:include page="/includes/homepage-header.jsp"/>
        <jsp:doBody/>
      </div>
      <jsp:include page="/includes/my-footer.jsp"/>
    </div>
  </body>
</html>