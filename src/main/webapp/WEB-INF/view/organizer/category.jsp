<%@ page import="ae.anyorder.bigorder.enums.Status" %>
<%--
  Created by IntelliJ IDEA.
  User: Frank
  Date: 4/25/2018
  Time: 4:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                        <h3 class="content-title">Category</h3>
                    </div>
                    <div class="col-sm-6">
                        <button class="btn btn-success pull-right mt15" href="java" id="addCategory">Add Category</button>
                    </div>
                </div>
            </header>
            <!-- Main Section-->
            <section class="main">
                <div class="content-body areas-list">
                    <div class="row">
                        <div class="col-md-3 root-areas">
                            <div class="card">
                                <div class="card-header">
                                    <h2 class="no-margin-bottom">Category</h2>
                                </div>
                                <div class="card-body">
                                    <div class="root-area-wrapper">
                                        <ul id="rootCategoryList" class="list-unstyled"></ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-9">
                            <div class="sub-areas">
                                <div class="card">
                                    <div class="card-header">
                                        <h4 class="title-border form-title">Add Category</h4>
                                    </div>
                                    <div class="card-body">
                                        <div id="categoryWrapper">
                                            <div class="row">
                                                <div class="col-sm-5">
                                                    <div class="box-me p25">
                                                        <form action="" id="categoryForm">
                                                            <div class="form-group floating">
                                                                <label for="categoryName" class="control-label">Name</label>
                                                                <input type="hidden" id="categoryId" name="categoryId">
                                                                <input type="text" class="form-control input-lg" id="categoryName" name="categoryName">
                                                            </div>
                                                            <div class="form-group floating hide cat-status">
                                                                <label for="categoryStatus"
                                                                       class="control-label">Status</label>
                                                                <select name="categoryStatus" id="categoryStatus"
                                                                        class="form-control input-lg">
                                                                    <option value="<%=Status.ACTIVE%>" selected>Active</option>
                                                                    <option value="<%=Status.INACTIVE%>">Inactive</option>
                                                                </select>
                                                            </div>

                                                            <div class="form-group">
                                                                <button class="btn btn-success c-b-success pull-right btn-lg"
                                                                        type="submit" id="submitButton">Save
                                                                </button>
                                                                <div class="clearfix"></div>
                                                            </div>
                                                        </form>
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
<%@include file="../includes/validate.jsp" %>
<script src="${pageContext.request.contextPath}/resources/custom/js/category.js"></script>
<script>
    $("li[data-menu='category']").addClass('active');
</script>
<script>
    $(function () {
        addEditCategoryModule.init();
    });
</script>


</body>
</html>

