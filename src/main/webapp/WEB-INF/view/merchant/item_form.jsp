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
                <div class="container-fluid">
                    <h2 class="no-margin-bottom">Item</h2>
                </div>
            </header>
            <!-- Main Section-->
            <section class="main">
                <div class="container-fluid">
                    <form id="itemForm">
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="card">
                                    <div class="card-header">
                                         <h2>Basic Info</h2>
                                    </div>
                                    <div class="card-body">
                                        <div class="form-group row">
                                            <label for="itemImageContainer" class="col-sm-3 control-label">Logo</label>
                                            <div class="col-sm-9">
                                                <div class="fileinput fileinput-new" data-provides="fileinput">
                                                    <div class="fileinput-new thumbnail positionrelative"
                                                         style="width: 200px; height: 150px;" data-trigger="fileinput">
                                                        <div class="center-me positionabsolute text-center">
                                                            <i class="fa fa-image fa-4x"></i>

                                                            <p class="text-center">Item Logo</p>
                                                        </div>

                                                    </div>
                                                    <div id="itemImageContainer"
                                                         class="fileinput-preview fileinput-exists thumbnail"
                                                         style="max-width: 200px; max-height: 150px;"></div>
                                                    <div>
                                                                <span class="btn btn-default btn-file hide">
                                                                    <span class="fileinput-new">Select image</span>
                                                                    <span class="fileinput-exists">Change</span>
                                                                    <input type="file" id="itemImageOutput" data-upload="image"
                                                                           accept="image/*">
                                                                </span>
                                                        <a href="#" class="btn btn-default fileinput-exists"
                                                           data-dismiss="fileinput">Remove</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group row">
                                            <label for="itemName" class="col-sm-3 control-label">Item Name</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control input-lg"
                                                       id="itemName" name="itemName">
                                            </div>
                                        </div>

                                        <div class="form-group row">
                                            <label for="itemDescription" class="col-sm-3 control-label">Description</label>
                                            <div class="col-sm-9">
                                                <textarea type="text" class="form-control input-lg" id="itemDescription" name="itemDescription"></textarea>
                                            </div>
                                        </div>

                                        <div class="form-group row">
                                            <label for="itemOverview" class="col-sm-3 control-label">Overview</label>
                                            <div class="col-sm-9">
                                                <textarea type="text" class="form-control input-lg" id="itemOverview" name="itemOverview"></textarea>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="card">
                                    <div class="card-header">
                                        <h2>More Detail</h2>
                                    </div>
                                    <div class="card-body">
                                        <div class="form-group row">
                                            <label for="itemStartTime" class="col-sm-3 control-label">Avaliable Start Time</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control input-lg"
                                                       id="itemStartTime" name="itemStartTime">
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label for="itemEndTime" class="col-sm-3 control-label">Avaliable End Time</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control input-lg"
                                                       id="itemEndTime" name="itemEndTime">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-sm-6">
                                <div class="card">
                                    <div class="card-header">
                                        <h2>Item Detail</h2>
                                    </div>
                                    <div class="card-body">
                                        <div class="row form-group">
                                            <label for="itemCategory" class="col-sm-3 control-label">Category</label>
                                            <div class="col-sm-9">
                                                <select name="itemCategory" id="itemCategory" class="form-control input-lg select-picker"
                                                        data-size="7" data-live-search="true" data-title="Select Category">
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="card">
                                    <div class="card-header">
                                        <h2>Pricing and Attribute</h2>
                                    </div>
                                    <div class="card-body">
                                        <div class="form-group row">
                                            <label for="minPerson" class="col-sm-3 control-label">Number of People</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control input-lg"
                                                       id="minPerson" name="minPerson">
                                            </div>
                                        </div>

                                        <div class="form-group row">
                                            <label for="itemUnitPrice" class="col-sm-3 control-label">Unit Price</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control input-lg"
                                                       id="itemUnitPrice" name="itemUnitPrice" required>
                                            </div>
                                        </div>
                                        <!-- Item Attribute stuff -->
                                        <div class="row form-group">
                                            <div class="col-md-12">
                                                <button type="button" id="addAttributeTypeButton" class="btn btn-krave"
                                                        aria-label="Add Attribute Type"> Add Attribute
                                                </button>
                                            </div>
                                        </div>
                                        <div id="attributeTypeTemplate" class="hidden bordered-block clearfix well">
                                            <div class="card">
                                                <div class="card-header">
                                                    <div class="row form-group attributeType">
                                                        <div class="col-sm-11">
                                                            <input type="text" class="form-control attributeName"
                                                                   placeholder="Item Name" required>
                                                        </div>
                                                        <div class="col-sm-1">
                                                            <button type="button" class="btn removeAttributeTypeButton"
                                                                    aria-label="Remove Item">
                                                                <span class="btn btn_red remove_attr_block fa fa-2x fa-remove" aria-hidden="true"></span>
                                                            </button>
                                                        </div>
                                                    </div>
                                                    <div id="attributeTemplate" class="">
                                                    <div class="row form-group">
                                                        <div class="col-sm-6">
                                                            <input type="text" class="form-control attributeDescription"
                                                                   placeholder="Item Detail" required>
                                                        </div>
                                                        <div class="col-sm-6">
                                                            <div class="input-group">
                                                                <span class="input-group-addon js-currency"></span>
                                                                <input type="number"
                                                                       class="form-control attributeQuantity" placeholder="Quantity" required>
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
                        <div class="col-xs-12 col-lg-12">
                            <div class="container-fluid content-footer clearfix">
                                <button type="submit" id="addItemButton" class="btn btn-outline btn-outline-yup btn-save t-uppercase pull-right"> Add Item
                                </button>
                            </div>
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
<link rel="stylesheet" type="text/css"
      href="${pageContext.request.contextPath}/resources/vendors/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.min.css">
<script src="${pageContext.request.contextPath}/resources/vendors/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/custom/js/image.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/custom/js/item.js"></script>
<script type="text/javascript">
    $(function () {
        itemModule.add();
        imageModule.bindCropComplete();
    });
</script>
