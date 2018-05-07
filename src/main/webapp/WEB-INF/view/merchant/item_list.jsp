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
                        <h2 class="no-margin-bottom">Item sad</h2>
                    </div>
                    <div class="col-sm-6">
                        <a href="/merchant/item_form/add" class="btn btn-success pull-right" id="addItemBtn">Add item</a>
                    </div>
                </div>
            </header>
            <!-- Main Section-->
            <section class="forms">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-12">
                            <div id="itemList" class="item-list">
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <%@include file="../includes/footer.jsp" %>
        </div>
    </div>
</div>

<div class="item_container_template hidden">
    <div class="item_container items_list grid-cell">
        <div class="item">
            <div class="item-head">
                <img class="img-responsive" width="200px" height="200px" >
                <button type="button" class="item-display btn btn-default" data-id="" data-display=""> <i class="glyphicon glyphicon-phone"> </i> </button>
                <div class="item-actions invisible-full">
                    <div class="btn-group center-vh" aria-label="store-actions" role="group">
                        <a class="btn c-b-pure c-t-shade item-edit" data-action="Edit" href="#">
                            <i class="kravicon krav-edit"></i>
                        </a>
                        <a class="btn c-b-pure c-t-shade item-view" data-action="View" href="#1">
                            <i class="glyphicon glyphicon-eye-open"></i>
                        </a>
                    </div>
                </div>
            </div>
            <div class="item-body">
                <a class="item_name item-link" href="#"></a>
                <span class="item_price"></span>
            </div>
        </div>
    </div>
</div>

<div class="pagination_template hidden">
    <div class="pagination_list col-lg-12">
        <ul class="pagination pull-left">
        </ul>
        <div class="num_items pull-right">
            Show per Page
            <select class="select_num_items" name="select_num_items" data-width="auto">
                <option value="0">All</option>
            </select>
        </div>
    </div>
</div>

<%@include file="../includes/scripts.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/custom/js/item.js"></script>
<script type="text/javascript">
    $(function () {
        itemModule.list();
    });
</script>