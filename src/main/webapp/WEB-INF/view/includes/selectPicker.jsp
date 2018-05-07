<link rel="stylesheet"
      href="${pageContext.request.contextPath}/resources/vendors/bootstrap-select/dist/css/bootstrap-select.min.css">
<script src="${pageContext.request.contextPath}/resources/vendors/bootstrap-select/dist/js/bootstrap-select.min.js"></script>
<script>
    $(document).ready(function () {
        $('.select-picker').selectpicker({
            liveSearch: true,
            style: "btn-ao-input"
        });
    });
</script>