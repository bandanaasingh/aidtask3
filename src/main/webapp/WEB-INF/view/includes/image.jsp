<%--
  Created by IntelliJ IDEA.
  User: Frank
  Date: 4/26/2018
  Time: 12:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Modal -->
<div class="modal fade" id="picCropModal" tabindex="-1" role="dialog" aria-labelledby="picCropModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="picCropModalLabel">Crop Image</h4>
            </div>
            <div class="modal-body">

                <div id="cropWrapper" class="positionrelative">
                    <button class="btn btn-success positionabsolute" id="btnSetImage">Set Image</button>
                </div>
            </div>
        </div>
    </div>
</div>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/vendors/Croppie/croppie.css">
<script src="${pageContext.request.contextPath}/resources/vendors/Croppie/croppie.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/custom/js/imagecropper.js"></script>
<script>
    $(function(){
        imageCropperModule.init();
    });

</script>
