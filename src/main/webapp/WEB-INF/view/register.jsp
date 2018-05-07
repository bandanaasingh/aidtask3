<%@include file="includes/head.jsp"%>
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
                            <p>Business is all about the customer: what the customer wants and what they get. Generally, every customer wants a product or service that solves their problem, worth their money, and is delivered with amazing customer service.
                            </p>
                        </div>
                    </div>
                </div>
                <!-- Form Panel    -->
                <div class="col-lg-6 bg-white">
                    <div class="form d-flex align-items-center">
                        <div class="content">
                            <form action="" method="post" id="merchantRegisterForm" novalidate>
                                <div class="form-group" >
                                    <input type="text" id="firstname" name="firstname" class="input-material" required/>
                                    <label for="firstname" class="label-material">First Name</label>
                                </div>
                                <div class="form-group">
                                    <input type="text" id="lastname" name="lastname" class="input-material" required>
                                    <label for="lastname" class="label-material">Last Name</label>
                                </div>
                                <div class="form-group">
                                    <input type="text" id="businessTitle" name="businessName" class="input-material" required>
                                    <label for="businessTitle" class="label-material">Business Title</label>
                                </div>
                                <div class="form-group" >
                                    <input type="text" id="email" name="email" class="input-material"  required>
                                    <label for="email" class="label-material">Email Address</label>
                                </div>
                                <div class="form-group">
                                    <input type="text" id="contact" name="contact" class="input-material" required>
                                    <label for="contact" class="label-material">Contact No</label>
                                </div>

                                <div class="form-group">
                                    <input type="password" id="password" name="password" class="input-material" required>
                                    <label for="password" class="label-material">Password</label>
                                </div>
                                <div class="form-group">
                                    <input type="text" id="url" class="input-material">
                                    <label for="url" class="label-material">URL</label>
                                </div>
                                <div class="text-center">
                                    <button class="btn btn-success">Register</button>
                                    <div>
                                        <small>Already have an account? </small><a href="/" class="signup">Login</a>
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

<%@include file="includes/scripts.jsp" %>
<%@include file="includes/validate.jsp" %>

<script src="${pageContext.request.contextPath}/resources/custom/js/merchantRegister.js"></script>
<script type="text/javascript">
    registerModule.init();
</script>