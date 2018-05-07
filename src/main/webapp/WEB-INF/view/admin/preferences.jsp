<%--
  Created by IntelliJ IDEA.
  User: Frank
  Date: 4/25/2018
  Time: 4:45 PM
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
                    <h2 class="no-margin-bottom">Prefrences</h2>
                </div>
            </header>
            <!-- Main Section-->
            <section class="forms">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-12 ph20">
                            <div class="content-body box-me pv25">
                                <section class="preferences-form ao-prefs  clearfix">
                                    <div class="btn_tabs main_tabs">
                                        <ul class="nav nav-tabs" role="tablist">
                                            <li class="col-xs-3 p0 active" role="presentation">
                                                <a href="/admin/preferences">General</a>
                                            </li>
                                            <li class="col-xs-3 p0" role="presentation">
                                                <a href="/admin/algorithm">Algorithm</a>
                                            </li>
                                        </ul>
                                    </div>
                                    <div class="main_content mt25">

                                        <div class="form_container form_editable form_section">
                                            <form id="form_settings" action="" method="POST" role="form"
                                                  class="display_settings">

                                            </form>
                                        </div>
                                    </div>
                                </section>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <%@include file="../includes/footer.jsp" %>
        </div>
    </div>
</div>

<div class="form_field_template hidden">
    <div class="form-group clearfix">
        <label class="col-lg-4 floated_label"></label>

        <div class="col-lg-8">
            <div class="form-control info_display none_editable"></div>
            <div class="info_edit editable hidden">
                <input type="text" class="form-control">
            </div>
        </div>
    </div>
</div>

<div class="form_select_template hidden">
    <div class="form-group clearfix">
        <label class="col-lg-4"></label>

        <div class="col-lg-8">
            <div class="form-control info_display none_editable"></div>
            <div class="info_edit editable hidden">
                <select class="col-xs-12 p0 m0 selectpicker" data-style="form-control">
                </select>
            </div>
        </div>
    </div>
</div>

<div class="form_section_template hidden">
    <section class="form_group">
        <div class="form_head">
            <div class="detail_options pull-right">
                <a class="p0 btn edit_btn none_editable glyphicon glyphicon-edit"></a>

                <div class="action_buttons editable hidden">
                    <a class="p0 btn save_btn glyphicon glyphicon-floppy-disk"></a>
                    <a class="p0 btn cancel_btn glyphicon glyphicon-remove"></a>
                </div>
            </div>
            <span class="section_title"></span>
        </div>
        <div class="form_content">
            <div class="row-ao">
            </div>
        </div>
    </section>
</div>


<div class="image_template hidden">
    <div class="form-group clearfix">
        <label class="col-md-4"> </label>
        <div class="col-md-8">
            <div class="">
                <div class="image_container">
                    <img src="" class="h120 w120" id="smanagerImageOutput" name="smanagerImageOutput">
                </div>
                <div>
                    <span class="btn btn-default btn-file mt5">
                        <span class="fileinput-exists">Change</span>
                        <input type="file" data-upload="image"
                               accept="image/*" class="image_input"
                               aria-invalid="false">
                    </span>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
</div>

<%@include file="../includes/scripts.jsp" %>
<%@include file="../includes/selectPicker.jsp" %>
<%@include file="../includes/image.jsp" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/custom/js/preferences.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        $('.main_tabs .active').click(function (e) {
            e.preventDefault();
        });
        Preferences.loadSettings(1);
        Preferences.loadEditSettings();
    });
</script>