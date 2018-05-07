<%--
  Created by IntelliJ IDEA.
  User: Frank
  Date: 4/25/2018
  Time: 4:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../includes/head.jsp" %>
<div class="page">
    <%@include file="../includes/header.jsp" %>
    <div class="page-content d-flex align-items-stretch">
        <%@include file="../includes/sidebar.jsp" %>
        <div class="content-inner">
            <!-- Page Header-->
            <header class="page-header">
                <div class="row container-fluid">
                    <div class="col-sm-6">
                        <h2 class="no-margin-bottom">Area</h2>
                    </div>
                    <div class="col-sm-6">
                        <a class="btn btn-success pull-right mt15" href="/organizer/area_form?type=addRoot">Add Main Area</a>
                    </div>
                </div>
            </header>
            <!-- Main Section-->
            <section class="main">
                <div class="row">
                    <div class="col-md-12 ph20">
                        <div class="row content-body box-me">
                            <div class="col-md-3 root-areas">
                                <div class="card">
                                    <div class="card-header">
                                        <h3 class="content-title">Main Area</h3>
                                    </div>
                                    <div class="card-body">
                                        <div class="root-area-wrapper">
                                            <ul id="rootAreaList" class="list-unstyled"></ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-9">
                                <div class="sub-areas">
                                    <div class="card">
                                        <div class="card-header">
                                            <div class="row">
                                                <div class="col-sm-6">
                                                    <h4 class="title-border">Sub Areas</h4>
                                                </div>
                                                <div class="col-sm-6">
                                                    <a class="btn btn-success pull-right mt15neg hide" id="addSubAreaBtn" href="">Add Sub Area</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="card-body">
                                            <div id="subAreasWrapper">
                                                <div class="row">
                                                    <div class="col-sm-4">
                                                        <div class="box-me">
                                                            <ul class="list-unstyled" id="subAreaList"></ul>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <%@include file="../includes/footer.jsp" %>
        </div>
    </div>
</div>

<%@include file="../includes/scripts.jsp" %>

<script src="${pageContext.request.contextPath}/resources/custom/js/area.js"></script>
<script>
    $("li[data-menu='area']").addClass('active');
</script>
<script>
    $(function(){
        listAreaModule.init();
    });
</script>
