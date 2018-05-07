var signUpModule = (function () {
    "use strict";

    var signUp = {
        ui: {
            datePicker: $("[data-action='datePicker']"),
            registerForm: $("#registerForm"),
            paymentForm: $("#paymentForm"),
            signupButton : $("#btnNext"),

            //signup form UIs
            firstName : $("#firstname"),
            lastName : $("#lastname"),
            email : $("#email"),
            password : $("#password"),
            contactNumber : $("#contact")
        },

        init: function () {
            this.events();
            this.ui.paymentForm.hide();
        },

        events: function () {
            var that = this;
            this.ui.registerForm.validate({
                rules: {
                    firstname: {
                        required: true
                    },
                    lastname:{
                        required: true
                    },
                    email : {
                        required: true
                    },
                    password : {
                        required: true
                    },
                    contactNumber:{
                        required: true
                    },
                    verifyPassword:{
                        required: true
                    }
                },
                submitHandler: function (form) {
                    var submitData = {
                        "username": that.ui.email.val(),
                        "firstName": that.ui.firstName.val(),
                        "lastName": that.ui.lastName.val(),
                        "email": that.ui.email.val(),
                        "mobileNumber": that.ui.contactNumber.val(),
                        "password": that.ui.password.val()
                    };
                    console.log(submitData);
                    that.doSignup(submitData);
                }
            });

        },

        doSignup: function(data) {
            var that = this;
            this.ui.signupButton.prop("disabled", true);
            console.log(data);

            var callback = function(status, data) {
                if (data.success) {
                    var button1 = function() {
                        window.location = Main.modifyURL("/");
                    };
                    button1.text = "Close";
                    var button = [button1];
                    Main.popDialog("Congratulations", data.message, button, 'success', true);
                    that.ui.signupButton.prop("disabled", false);
                    that.ui.registerForm.trigger("reset");
                } else {
                    Main.popDialog("Error", data.message, null, 'error');
                    that.ui.signupButton.prop("disabled", false);
                }
            };

            Main.request('/anon/customer_register', data, callback);
        }
    };

    return {
        init: function () {
            signUp.init();
        }
    }

})();
