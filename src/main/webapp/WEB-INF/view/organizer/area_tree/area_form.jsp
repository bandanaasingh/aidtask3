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
                            <h3 class="content-title">Add Area</h3>
                        </div>

                        <div class="content-body box-me">

                            <form action="" id="addAreaForm" method="post">
                                <div class="row mt25">
                                    <div class="col-md-6">
                                        <input id="pac-input" class="controls form-control w400 mt15 p15 border0 h50"
                                               type="text"
                                               placeholder="Enter postal code to focus map and make marker">

                                        <div id="map" class="w100p h500"></div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="form-group floating">
                                            <label for="areaName" class="control-label">Area Name</label>
                                            <input type="text" class="form-control input-lg" id="areaName"
                                                   name="areaName">
                                        </div>

                                        <div class="form-group floating">
                                            <label for="streetName" class="control-label">Street Name</label>
                                            <input type="text" class="form-control input-lg" id="streetName"
                                                   name="streetName">
                                        </div>

                                        <div class="form-group floating">
                                            <label for="parentArea" class="control-label">Parent Area</label>
                                            <select class="form-control input-lg" id="parentArea" name="parentArea">
                                                <option value="">None</option>
                                            </select>
                                        </div>

                                        <div class="form-group floating">
                                            <label for="latitude" class="control-label">Latitude</label>
                                            <input type="text" class="form-control input-lg" id="latitude"
                                                   name="latitude">
                                        </div>

                                        <div class="form-group floating">
                                            <label for="longitude" class="control-label">Longitude</label>
                                            <input type="text" class="form-control input-lg" id="longitude"
                                                   name="longitude">
                                        </div>
                                    </div>




                                </div>




                                <hr class="hr-mod">
                                <div class="clearfix">
                                    <button class="btn btn-danger pull-right btn-lg" type="reset">Cancel</button>
                                    <button class="btn btn-success c-b-success pull-right btn-lg mh15" type="submit">Add Area</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<%@include file="../includes/scripts.jsp" %>


<script src="${pageContext.request.contextPath}/resources/vendors/jquery-validation/dist/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/vendors/jquery-validation-bootstrap-tooltip/jquery-validate.bootstrap-tooltip.min.js"></script>


<script>
    var config = {
        multiMarker: false,
        countLabel: false,
        curPage : "add_area",
        markerIcon: "${pageContext.request.contextPath}/resources/custom/images/pin-store.png",
        curLocation: "Dubai UAE"
    };

    var initGoogleMaps = function () {
        mapsModule.init(config);
    }

</script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDjbtflnGL0mEj7aHh9VOHPAa_0cqbJabY&callback=initGoogleMaps&libraries=places"
        async defer></script>


<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/vendors/bootstrap-select/dist/css/bootstrap-select.min.css">
<script src="${pageContext.request.contextPath}/resources/vendors/bootstrap-select/dist/js/bootstrap-select.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/custom/js/area.min.js"></script>
<script>
    //$("li[data-menu='area']").addClass('active');


    var areaId = window.location.href.substring(window.location.href.lastIndexOf('/') + 1);
    if(areaId=="" || areaId=="area_form"){
        addAreaModule.init();
    }else{
        editAreaModule.init();
    }


</script>


<script src="${pageContext.request.contextPath}/resources/custom/js/maps.min.js"></script>


</body>
</html>

