<%@include file="includes/head.jsp" %>
<div class="page login-page">
    <div class="container d-flex align-items-center">
        <div class="form-holder has-shadow">
            <div class="row">
                <!-- Logo & Information Panel-->
                <div class="col-lg-6">
                    <div class="info d-flex align-items-center">
                        <div class="content">
                            <div class="logo">
                                <h1>Any Order Delivery Service</h1>
                            </div>
                            <p>Business is all about the customer: what the customer wants and what they get. Generally,
                                every customer wants a product or service that solves their problem, worth their money,
                                and is delivered with amazing customer service.
                            </p>
                        </div>
                    </div>
                </div>
                <!-- Form Panel    -->
                <div class="col-lg-6 bg-white">
                    <div class="form d-flex align-items-center">
                        <div class="content">
                            <form action="/j_spring_security_check" id="loginForm" method="POST" class="form-signin">
                                <div class="text-center mb-4">
                                    <img class="mb-4" src="resources/custom/images/bg.jpg" alt="" width="72"
                                         height="72">
                                    <h1 class="h3 mb-3 font-weight-normal">Login</h1>
                                </div>
                                <div class="row form-group floating">
                                    <input type="text" id="username" name="login_username"
                                           class="form-control input-material" autofocus required>
                                    <label for="username" class="label-material">Username</label>
                                </div>

                                <div class="row form-group floating">
                                    <input type="password" id="password" name="login_password"
                                           class="form-control input-material" required>
                                    <label for="password" class="label-material">Password</label>
                                </div>

                                <div class="text-center">
                                    <button class="btn btn-success">Login</button>
                                    <div>
                                        <small>Already have an account?</small>
                                        <a href="/signup" class="signup">Register</a>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="emailModal" tabindex="-1" role="dialog" data-type>
    <div class="modal-dialog modal-sm w400">
        <div class="modal-content ">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Forgot Password</h4>
            </div>
            <div class="modal-body">
                <form id="emailForm">
                    <%--<p class="help-block mb0">Please enter your email to recover password.</p>--%>
                    <div class="input-group ao-input-group">
                        <input type="email" class="form-control border0" placeholder="Email" name="emailRecover"
                               id="emailRecover">
                    </div>
                    <div class="clearfix"></div>
                    <button type="submit" class="btn btn-success btn-block btn-lg mt10" id="recoverSubmitBtn">Recover
                        Password
                    </button>
                </form>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<%@include file="includes/scripts.jsp" %>
<%@include file="includes/validate.jsp" %>

<script src="${pageContext.request.contextPath}/resources/custom/js/login.js"></script>
<script type="text/javascript">
    $(function () {
        loginModule.init();
        setTimeout(function () {
            if ($('#email').val()) {
                $('#email').blur().focus();
            }
        }, 1000);
    });
</script>
</body>
</html>

