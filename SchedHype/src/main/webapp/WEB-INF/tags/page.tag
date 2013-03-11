<%@tag description="Page template" pageEncoding="UTF-8"%>
<%@attribute name="bodyClass" required="true"%>
<%@attribute name="title" required="true"%>
<%@attribute name="metaDefs" fragment="true"%>
<%@attribute name="cssDefs" fragment="true"%>
<%@attribute name="jsDefs" fragment="true"%>
<%@attribute name="layout" fragment="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!doctype html>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7 ie6" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8 ie7" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9 ie8" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
  <head>
    <title>${title}</title>
    <jsp:invoke fragment="metaDefs" />
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, minimum-scale=1, maximum-scale=1.0" />
    <meta name="robots" content="noodp,noydir" />
    <link rel="shortcut icon" href='<t:url value="/images/icons/favicon.ico"/>' />
    <!-- media -->
    <jsp:include page="/header_css.jsp" />
    <jsp:invoke fragment="cssDefs"/>
    <!-- /media -->

    <!-- common-js -->
    <script type="text/javascript" src='<t:url value="/js-ext/modernizr-2.5.3.min.js"/>'></script>
  </head>
  <body class="${bodyClass}">
    <div id="page-wrap">
      <div id="layout">
        <jsp:include page="/header.jsp" />
      </div>
      <div class="content_wrap">
        <jsp:doBody />
      </div>
    </div>
    <jsp:include page="/footer.jsp" />
    
    <!-- common-js -->
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="<t:url value="/js-ext/jquery-1.7.2.min.js"/>"><\/script>')</script>
    <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.6/jquery-ui.min.js"></script>

    <!--[if IE]>
        <script type="text/javascript" src='<t:url value="/js-ext/foutbgone.js"/>'></script>
    <![endif]-->

    <!--[if lt IE 9]>
        <script type="text/javascript" src='<t:url value="/js-ext/json2-min.js"/>'></script>
    <![endif]-->

    <!--[if IE 7]>
        <script type="text/javascript" src='<t:url value="/js-ext/jquery.pseudo.js"/>'></script>
    <![endif]-->
    
    <script type="text/javascript" src='<t:url value="/js-ext/knockout-2.2.1.js"/>'></script>
    <script type="text/javascript" src='<t:url value="/js-ext/knockout.mapping.js"/>'></script>
    <!-- /common-js -->

    <jsp:invoke fragment="jsDefs"/>

    </body>
</html>
