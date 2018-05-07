<%--
  Created by IntelliJ IDEA.
  User: Frank
  Date: 4/25/2018
  Time: 4:58 PM
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
                        <h2 class="no-margin-bottom">Store</h2>
                    </div>
                    <div class="col-sm-6">
                        <a class="btn btn-primary pull-right" href="/merchant/store_form" id="addStoreBtn">Add Store</a>
                    </div>

                </div>
            </header>
            <!-- Main Section-->
            <section class="main">
                <div class="col-sm-12">
                    <div class="content-body box-me">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="ph10">Active Store Brands</h4>
                            </div>
                            <div class="card-body">
                                <div class="row flex-grid" id="storeBoxWrapper">
                                    <div class="col-sm-3">
                                        <div class="box-me ph20">
                                            No Active Stores Available.
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="card">
                            <div class="card-header">
                                <h4 class="ph10">Inactive Store Brands</h4>
                            </div>
                            <div class="card-body">
                                <div class="row flex-grid" id="inactiveStores">
                                    <div class="col-sm-3 mb25 ph20">
                                        <div class="box-me">
                                            No Inactive Stores Available.
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
<script src="${pageContext.request.contextPath}/resources/custom/js/store.js"></script>
<script>
    $(function () {
        listStoreModule.init();
    });
    $("li[data-menu='topOrderPlacedStores']").addClass('active');
</script>

