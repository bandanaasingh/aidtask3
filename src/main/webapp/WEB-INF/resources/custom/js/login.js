var loginModule = (function () {
    "use strict";

    var login = {
        ui: {
            signInForm: $('#loginForm'),
            blinkPassword: $("#blinkPassword"),
            username: $("#username"),
            password: $("#password"),

            emailModal: $('#emailModal'),
            emailForm: $('#emailForm'),
            emailRecoverInput: $('#emailRecover'),
            recoverSubmitBtn: $('#recoverSubmitBtn')
        },

        init: function () {
            this.events();
            this.validation();
            this.validationModal();
        },

        events: function () {
            var that = this;
            Main.saveInLocalStorage("logout", "true");

            this.ui.blinkPassword.click(function () {
                that.ui.password.attr('type', 'text');
                setTimeout(function () {
                    that.ui.password.attr('type', 'password');
                }, 500);
            });

            this.ui.emailModal.on('show.modal.bs', function (e) {
                var btn = $(e.relatedTarget);
                var type = btn.attr('data-type');
                $(this).attr('data-type', type);
                if (type === "FORGOT") {
                    that.ui.emailModal.find('.modal-title').text('Forgot Password');
                    that.ui.emailForm.find('button[type="submit"]').text('Recover Password');
                } else if (type === "RESEND") {
                    that.ui.emailModal.find('.modal-title').text('Resend Link');
                    that.ui.emailForm.find('button[type="submit"]').text('Resend Link');
                }
            }).on('hidden.modal.bs', function () {
                $(this).removeAttr('data-type');
                that.ui.emailRecoverInput.val('');
            });
        },

        validationModal: function () {
            var that = this;
            this.ui.emailForm.validate({
                rules: {
                    emailRecover: {
                        required: true
                    }
                },
                submitHandler: function () {
                    var callback = function (status, data) {
                        that.ui.emailModal.modal('hide');
                        if (data.success) {

                            var button1 = function () {
                                window.location = Main.modifyURL(document.URL);
                            };
                            button1.text = "Close";
                            var button = [button1];
                            Main.popDialog("Success", data.message, button, 'success', true);

                        } else {
                            Main.popDialog('Error', data.message, null, 'error');
                        }
                    };

                    var data = {
                        "actionType": that.ui.emailModal.attr('data-type')
                    };

                    Main.request('/anon/password_assist', data, callback, {username: that.ui.emailRecoverInput.val()});
                }
            });
        },

        validation: function () {
            var that = this;
            console.log("validation called");
            this.ui.signInForm.validate({
                rules: {
                    login_username: {
                        required: true
                    },
                    login_password: {
                        required: true,
                        minlength: 6
                    }
                },
                submitHandler: function (e) {
                    var callback = function (status, data) {
                        if (data.success) {
                            console.log(data);
                            var userDetails = data.params;
                            if (userDetails) {
                                Main.saveInLocalStorage('userId', Main.fixUndefinedVariable(userDetails.id));
                                Main.saveInLocalStorage('userRole', userDetails.authorities[0].authority);
                                Main.userRole = userDetails.authorities[0].authority;
                                Main.saveInLocalStorage('userTitle', userDetails.businessTitle === undefined ? userDetails.firstName + " " + userDetails.lastName : userDetails.businessTitle);
                                Main.saveInLocalStorage('profileImage', userDetails.profileImage);
                                Main.saveInLocalStorage('currency', data.params.currency);
                                Main.saveInLocalStorage('username', userDetails.username);
                                Main.saveInLocalStorage('emailAddress', Main.fixUndefinedVariable(userDetails.email));
                                Main.saveInLocalStorage('firstName', Main.fixUndefinedVariable(userDetails.firstName));
                                Main.saveInLocalStorage('lastName', Main.fixUndefinedVariable(userDetails.lastName));

                                if (userDetails.authorities[0].authority == "ROLE_MERCHANT") {
                                    Main.saveInLocalStorage('merchantId', userDetails.merchantId);
                                    Main.saveInLocalStorage('mobileNumber', userDetails.mobileNumber);
                                    Main.saveInLocalStorage('businessName', userDetails.businessTitle);
                                }

                                if (userDetails.authorities[0].authority == "ROLE_STORE_MANAGER") {
                                    Main.saveInLocalStorage('storeManagerId', userDetails.storeManagerId);
                                }
                                Main.saveInLocalStorage("logout", "false");
                                window.location = Main.modifyURL("/organizer/dashboard");
                            } else {
                                console.log(data);
                                //Main.popDialog('Error', 'Important data not received. Please login again.', null, 'error');
                                //Main.doLogout();
                            }

                        } else {
                            Main.popDialog('Error', data.message, null, 'error');
                        }
                    };

                    var userDetail = {
                        username: that.ui.username.val(),
                        password: that.ui.password.val(),
                        stringify: false
                    };
                    callback.loaderDiv = "#loginForm";
                    Main.request('/j_spring_security_check', userDetail, callback);
                }
            });
        }
    };

    return {
        init: function () {
            login.init();
        }
    };

})();


var setPasswordModule = (function () {
    var setPassword = {
        ui: {
            blinkPassword: $('#cblinkPassword, #blinkPassword'),
            setPasswordForm: $('#setPasswordForm'),
            passwordInput: $('#password'),
            confirmPasswordInput: $('#cpassword')
        },
        init: function () {
            this.events();
            this.validation();
        },

        events: function () {
            this.ui.blinkPassword.click(function () {
                var password = $(this).parent().find('input[type="password"]');
                password.attr("type", 'text');
                setTimeout(function () {
                    password.attr("type", 'password');
                }, 500);
            });
        },

        validation: function () {
            var that = this;
            this.ui.setPasswordForm.submit(function (e) {
                e.preventDefault();
            }).validate({
                rules: {
                    password: {
                        required: true
                    },
                    cpassword: {
                        required: true,
                        equalTo: "#password"
                    }
                },
                submitHandler: function () {
                    var callback = function (status, data) {
                        if (data.success) {
                            var button1 = function () {
                                window.location = Main.modifyURL("/");
                            };
                            button1.text = "Close";
                            var button = [button1];
                            Main.popDialog("Success", data.message, button, 'success', true);
                        } else {
                            Main.popDialog('Error', data.message, null, 'error');
                        }
                    };

                    callback.requestType = "POST";

                    var data = {
                        "actionType": "NEW"
                    };

                    Main.request('/anon/password_assist', data, callback, {
                        password: that.ui.passwordInput.val(),
                        verificationCode: Main.globalReplace(Main.getNamedParameter('key'), '#', '')
                    });
                }

            })

        }
    };


    return {
        init: function () {
            setPassword.init();
        }
    }

})();


var forgotPWMobileModule = (function () {
    var forgotPassword = {
        ui: {
            emailInput: $('#email'),
            submitBtn: $('#submitBtn'),
            emailForm: $('#forgotPWForm')

        },
        init: function () {
            this.events();
            this.validation();
        },

        events: function () {

        },

        validation: function () {
            var that = this;
            this.ui.emailForm.submit(function (e) {
                e.preventDefault();
            }).validate({
                rules: {
                    email: {
                        required: true
                    }
                },
                submitHandler: function () {

                    var callback = function (status, data) {
                        if (data.success) {
                            var button1 = function () {
                                window.location = Main.modifyURL(document.URL);
                            };
                            button1.text = "Close";
                            var button = [button1];
                            Main.popDialog("Success", data.message, button, 'success', true);
                        } else {
                            Main.popDialog('Error', data.message, null, 'error');
                        }
                    };

                    var data = {
                        "actionType": "FORGOT"
                    };

                    Main.request('/anon/password_assist', data, callback, {username: that.ui.emailInput.val()});
                }
            });
        },
    };


    return {
        init: function () {
            forgotPassword.init();
        }
    }

})();