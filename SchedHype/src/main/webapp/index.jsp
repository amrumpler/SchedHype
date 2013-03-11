<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:page bodyClass="example" title="Hello World">
  <jsp:attribute name="cssDefs">
    <link href='<t:url value="/css/test.css"/>' rel="stylesheet" type="text/css" />
  </jsp:attribute>
  <jsp:attribute name="jsDefs">
    <script type="text/javascript" src='<t:url value="/js/HelloWorld.js"/>'></script>
  </jsp:attribute>
  <jsp:body>
		<div class='liveExample'>   
		    <p>First name: <input data-bind='value: firstName' /></p> 
		    <p>Last name: <input data-bind='value: lastName' /></p> 
		    <h2>Hello, <span data-bind='text: fullName'> </span>!</h2>  
		</div>
	</jsp:body>
</t:page>