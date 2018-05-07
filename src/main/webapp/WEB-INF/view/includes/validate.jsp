<script src="${pageContext.request.contextPath}/resources/vendors/jquery-validation/dist/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/vendors/jquery-validation-bootstrap-tooltip/jquery-validate.bootstrap-tooltip.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/custom/js/front.js"></script>
<script src="${pageContext.request.contextPath}/resources/custom/js/jquery.cookie.js"></script>
<script src="${pageContext.request.contextPath}/resources/custom/js/jquery.validate.min.js"></script>

<script type="text/javascript">
    $(function () {
        // $.validator.setDefaults({
        // });

        //<--- Validation Rule For Greater Than, param must be selector. --->
        $.validator.addMethod('greaterThan', function (value, element, param) {
            //console.log(value +" > "+ $(param).val()+":"+(value > $(param).val()));
            return ( Number(value) > Number($(param).val()) );
        }, 'Must be greater than From value.');
    });
</script>
