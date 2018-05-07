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
                            <h3 class="content-title">ABC Area</h3>
                        </div>

                        <div class="content-body">

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>




<%@include file="../includes/scripts.jsp" %>
<script>
    $("li[data-menu='area']").addClass('active');
</script>

</body>
</html>

