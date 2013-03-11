<%@tag description="Cache invalidator" pageEncoding="UTF-8"
%><%@attribute name="value" required="true" fragment="false"
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><c:choose><c:when test='${fn:endsWith(value, "/") or fn:endsWith(value, "jsp") or fn:contains(value, "#")}'><c:url value="${value}" /></c:when><c:otherwise><c:url value="${value}?" /><%= com.amrumpler.schedhype.Constants.RELEASE_VERSION %></c:otherwise></c:choose>