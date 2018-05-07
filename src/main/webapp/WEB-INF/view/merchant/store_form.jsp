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
                <div class="container-fluid row">
                    <div class="col-sm-6">
                        <h2 class="no-margin-bottom">Store</h2>
                    </div>
                    <div class="col-sm-6 pull-right">
                        <button class="btn btn-primary pull-right" id="editBtn" data-formtype="view">Edit</button>
                    </div>
                </div>
            </header>
            <!-- Main Section-->
            <section class="main">
                <div class="container-fluid form-holder has-shadow form-horizontal">
                    <form id="storeForm" method="POST" action="">
                        <div class="row mb50">
                            <div class="col-sm-6">
                                <div class="card">
                                    <div class="card-header">
                                        <legend>Store Info</legend>
                                    </div>
                                    <div class="card-body">
                                        <div class="form-group row">
                                            <label for="storeName" class="col-sm-3 control-label">Store Name</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control input-lg"
                                                       id="storeName" name="storeName">
                                            </div>
                                        </div>

                                        <div class="form-group row">
                                            <label for="imgStore" class="col-sm-3 control-label">Logo</label>
                                            <div class="col-sm-9">
                                                <div class="fileinput fileinput-new" data-provides="fileinput">
                                                    <div class="fileinput-new thumbnail positionrelative"
                                                         style="width: 200px; height: 150px;" data-trigger="fileinput">
                                                        <div class="center-me positionabsolute text-center">
                                                            <i class="fa fa-image fa-4x"></i>

                                                            <p class="text-center">Store Logo</p>
                                                        </div>

                                                    </div>
                                                    <div id="storeImageContainer"
                                                         class="fileinput-preview fileinput-exists thumbnail"
                                                         style="max-width: 200px; max-height: 150px;"></div>
                                                    <div>
                                                                <span class="btn btn-default btn-file hide">
                                                                    <span class="fileinput-new">Select image</span>
                                                                    <span class="fileinput-exists">Change</span>
                                                                    <input type="file" id="imgStore" data-upload="image"
                                                                           accept="image/*">
                                                                </span>
                                                        <a href="#" class="btn btn-default fileinput-exists"
                                                           data-dismiss="fileinput">Remove</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group row">
                                            <label for="description" class="col-sm-3 control-label">Description</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control input-lg"
                                                       id="description" name="description">
                                            </div>
                                        </div>

                                        <div class="form-group row">
                                            <label for="servingDistance" class="col-sm-3 control-label">Serving Distance</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control input-lg"
                                                       id="servingDistance" name="servingDistance" required>
                                            </div>
                                        </div>

                                        <div class="form-group row">
                                            <label for="minOrder" class="col-sm-3 control-label">Minimum Order Amount</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control input-lg"
                                                       id="minOrder" name="minOrder" required>
                                            </div>
                                        </div>

                                        <div class="form-group row">
                                    <label for="placeOrderBefore" class="col-sm-3 control-label">Order Place Before(Minute)</label>
                                    <div class="col-sm-9">
                                        <input type="text" class="form-control input-lg"
                                               id="placeOrderBefore" name="placeOrderBefore" required>
                                    </div>
                                </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-sm-6">
                                <div class="card">
                                    <div class="card-header">
                                        <legend>Store Locations</legend>
                                    </div>
                                    <div class="card-body">
                                        <div class="row">
                                    <div class="col-md-12">
                                        <input id="pac-input" class="controls form-control w400 mt15 p15 border0 h50"
                                               type="text"
                                               placeholder="Enter postal code to focus map and make marker">

                                        <div id="map" class="w100p h400"></div>

                                        <div class="clearfix mt15">
                                            <button class="btn" id="removeMarker" type="button">Remove Marker
                                            </button>
                                            <button class="btn marker_nav" id="prevMarker" type="button"
                                                    disabled="disabled">Prev Marker
                                            </button>
                                            <button class="btn marker_nav" id="nextMarker" type="button"
                                                    disabled="disabled">Next Marker
                                            </button>

                                            <small class="pull-right text-muted">
                                                Right click on store location marker to remove the store.
                                            </small>
                                        </div>
                                    </div>

                                    <div class="col-md-12" id="storeDetailsForm">
                                        <div class="row">
                                            <div class="col-sm-6">
                                                <div class="form-group floating">
                                                    <label for="streetAddress" class="control-label">Address Line</label>
                                                    <input type="text" class="form-control input-lg"
                                                           id="streetAddress" name="streetAddress">
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group floating">
                                                    <label for="contactNumber" class="control-label">Contact
                                                        Number</label>
                                                    <input type="text" class="form-control input-lg"
                                                           id="contactNumber" name="contactNumber">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group floating mb15 hide">
                                            <label for="streetAddressTwo" class="control-label">Address Line 2</label>
                                            <input type="hidden" class="form-control input-lg"
                                                   id="streetAddressTwo" name="streetAddressTwo">
                                        </div>

                                        <div class="row">
                                            <div class="col-sm-6">
                                                <label for="mainArea" class="control-label-select">Main Area</label>
                                                <div class="form-group floating mb15">
                                                    <select name="mainArea" id="mainArea" class="form-control input-lg select-picker"
                                                            data-size="7" data-live-search="true" data-title="Select Main Area">
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <label for="subArea" class="control-label-select">Sub Area</label>
                                                <div class="form-group floating mb15">
                                                    <select class="form-control input-lg select-picker"
                                                            id="subArea" name="subArea" data-title="Select Sub-Area">
                                                    </select>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group mt20">
                                                <textarea class="form-control h140"
                                                          placeholder="Address Note" id="addressNote"
                                                          name="addressNote"></textarea>
                                        </div>
                                    </div>
                                </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row mt25">
                            <div class="location-box-wrapper" id="locationBoxWrapper">
                            </div>
                        </div>


                        <div class="row">
                            <div class="col-md-12">
                            </div>
                        </div>


                        <hr class="hr-mod">
                        <div class="clearfix" id="formBtns">
                            <button class="btn btn-danger pull-right btn-lg" type="button"
                                    onclick="window.history.back()">Cancel
                            </button>
                            <button class="btn btn-success c-b-success pull-right btn-lg mr15" id="submitBtn">
                                Add Store
                            </button>
                        </div>
                    </form>
                </div>
            </section>
            <%@include file="../includes/footer.jsp" %>
        </div>
    </div>
</div>

<%@include file="../includes/scripts.jsp" %>
<%@include file="../includes/validate.jsp" %>
<%@include file="../includes/image.jsp" %>
<%@include file="../includes/selectPicker.jsp" %>

<script src="/resources/custom/js/maps.js"></script>
<script src="/resources/custom/js/store.js"></script>

<script>
    var superMap;
    var initGoogleMaps = function () {
        var myOptions1 = {
            zoom: 14,
            center: new google.maps.LatLng(27.6920438,85.3171415), //change here take lat lang form google map
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            mapCanvas: "map",
            searchInput: "pac-input",
            mapVar: superMap,
            readOnly: false,
            isMultiMarker: true,
            disableDoubleClickZoom: true,
            markerIcon: {
                url: "/resources/custom/images/map-ico/map_merchant.png",
                scaledSize: new google.maps.Size(50, 50)
            }
        };

        superMap = newMapModule.init(myOptions1);

        addEditStoreModule.init()
    }
</script>


<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDjbtflnGL0mEj7aHh9VOHPAa_0cqbJabY&callback=initGoogleMaps&libraries=places"
        async defer></script>