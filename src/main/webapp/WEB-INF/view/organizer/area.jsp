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
                <div class="container-fluid">
                    <h2 class="no-margin-bottom">Area</h2>
                </div>
            </header>
            <!-- Main Section-->
            <section class="main">
                <div class="content-body areas-list">
                    <div class="row">
                        <div class="col-md-3 root-areas">
                            <a class="btn btn-success pull-right mt15" href="/organizer/area_form?type=addRoot">Add Main Area</a>
                            <div class="clearfix"></div>
                            <h3 class="content-title">Main Area</h3>

                            <div class="root-area-wrapper">
                                <ul id="rootAreaList" class="list-unstyled"></ul>
                            </div>
                        </div>
                        <div class="col-md-9">
                            <div class="sub-areas">
                                <h4 class="title-border form-title">Add Main Area</h4>

                                <div id="subAreasWrapper">
                                    <div class="row">
                                        <div class="col-sm-5">
                                            <div class="box-me p25">
                                                <form action="" id="areaForm">
                                                    <div class="form-group floating">
                                                        <label for="areaName" class="control-label">Name</label>
                                                        <input type="text" class="form-control input-lg"
                                                               id="areaName"
                                                               name="areaName">
                                                    </div>
                                                    <div class="form-group floating hide">
                                                        <label for="areaStatus"
                                                               class="control-label">Status</label>
                                                        <select name="areaStatus" id="areaStatus"
                                                                class="form-control input-lg">
                                                            <option value="<%=Status.ACTIVE%>" selected>Active</option>
                                                            <option value="<%=Status.INACTIVE%>">Inactive</option>
                                                        </select>
                                                    </div>

                                                    <input type="hidden" id="latitude">
                                                    <input type="hidden" id="longitude">

                                                    <div class="form-group">
                                                        <button class="btn btn-success c-b-success pull-right btn-lg"
                                                                type="submit" id="submitButton">Save
                                                        </button>
                                                        <div class="clearfix"></div>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                        <div class="col-sm-7">
                                            <div id="googleMapsWrapper" class="">
                                                <input id="pac-input"
                                                       class="controls form-control w400 mt15 p15 border0 h50"
                                                       type="text"
                                                       placeholder="Enter postal code to focus map and make marker">
                                                <div id="map" class="w100p h500"></div>
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
<script src="${pageContext.request.contextPath}/resources/custom/js/area.js"></script>
<script>
    $("li[data-menu='area']").addClass('active');
</script>
<script>

    $(function () {
        addEditAreaModule.init();
    });
</script>

<script src="${pageContext.request.contextPath}/resources/custom/js/maps.js"></script>
<script>
    var config = {
        multiMarker: false,
        countLabel: false,
        curPage: "add_area",
        markerIcon: "${pageContext.request.contextPath}/resources/custom/images/pin-store.png",
        curLocation: "Dubai, UAE",
        hasInput: true
    };

    var initGoogleMaps = function () {
        mapsModule.init(config);
    }

    //initGoogleMaps();

</script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDjbtflnGL0mEj7aHh9VOHPAa_0cqbJabY&libraries=places&callback=initGoogleMaps"
        async defer></script>

</body>
</html>

