<%@include file="../includes/header.jsp" %>
<%@include file="../includes/menu_sidebar.jsp" %>

        <div class="content-wrapper c-b-body" id="content" data-content="sm">
    <div class="container-fluid">
        <div class="row">
            <div class="pr0">
                <div class="content">
                    <div class="content-nav">
                        <%@include file="../includes/menu_content.jsp" %>
                    </div>

                    <div class="ph50">
                        <div class="content-header">
                            <h3 class="content-title">Area View</h3>
                        </div>

                        <div class="content-body">
                            <div class="row mb30">
                                <div class="col-md-3">
                                    <button class="btn btn-primary" id="categoryHomeBtn">Category Home</button>
                                </div>
                                <div class="col-md-9">
                                    <button class="btn btn-primary pull-right" id="addRootAreaBtn">Add Root Area
                                    </button>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-3">
                                    <div id="tree"></div>
                                </div>
                                <div class="col-md-9">
                                    <div class="row">
                                        <div id="areaViewWrapper" class="hide"></div>
                                        <div id="areaFormWrapper" class="hide">
                                            <div class="col-md-4">
                                                <button class="pull-right btn btn-primary mb15" id="addSubAreaBtn"
                                                        data-parent-id="">Add Sub Area
                                                </button>

                                                <button class="pull-right btn btn-primary mr10" id="editAreaBtn">Edit</button>
                                                <div class="clearfix"></div>
                                                <form action="" id="areaDetailForm">
                                                    <div class="form-group floating">
                                                        <label for="areaName" class="control-label">Name</label>
                                                        <input type="text" class="form-control input-lg" id="areaName"
                                                               name="areaName">
                                                    </div>

                                                    <div id="formlatlng">
                                                        <div class="form-group floating">
                                                            <label for="latitude" class="control-label">Latitude</label>
                                                            <input type="text" class="form-control input-lg"
                                                                   id="latitude" name="latitude">
                                                        </div>

                                                        <div class="form-group floating">
                                                            <label for="longitude"
                                                                   class="control-label">Longitude</label>
                                                            <input type="text" class="form-control input-lg"
                                                                   id="longitude" name="longitude">
                                                        </div>


                                                    </div>


                                                    <div class="form-group floating">
                                                        <label for="areaStatus" class="control-label">Status</label>
                                                        <select name="areaStatus" id="areaStatus"
                                                                class="form-control input-lg">
                                                            <option value="" selected disabled></option>
                                                            <option value="ACTIVE">Active</option>
                                                            <option value="INACTIVE">Inactive</option>
                                                        </select>
                                                    </div>

                                                    <div>
                                                        <button class="btn btn-success c-b-success pull-right hide"
                                                                type="submit" id="submitButton">Save Changes
                                                        </button>
                                                    </div>
                                                </form>
                                            </div>
                                            <div class="col-md-8">
                                                <div id="googleMapsWrapper" class="hide">
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
                </div>
            </div>
        </div>
    </div>
</div>


<%@include file="../includes/scripts.jsp" %>
<%@include file="../includes/validate.jsp" %>

<script src="${pageContext.request.contextPath}/resources/vendors/jstree/dist/jstree.min.js"></script>
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/resources/vendors/jstree/dist/themes/default/style.min.css">

<script src="${pageContext.request.contextPath}/resources/custom/js/area.min.js"></script>
<script>
    listAreaModule.init();
</script>
<script>
    $("li[data-menu='area']").addClass('active');
</script>
<script src="${pageContext.request.contextPath}/resources/custom/js/maps.min.js"></script>
<script>
    var config = {
        multiMarker: false,
        countLabel: false,
        curPage: "add_area",
        markerIcon: "${pageContext.request.contextPath}/resources/custom/images/pin-store.png",
        curLocation: "Dubai UAE"
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

<%--<script src="/resources/vendors/footable-bootstrap/js/footable.min.js"></script>
<link rel="stylesheet" href="/resources/vendors/footable-bootstrap/css/footable.bootstrap.min.css">--%>