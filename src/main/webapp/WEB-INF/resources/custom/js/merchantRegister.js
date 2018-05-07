var registerModule = (function () {
    "use strict";

    var signUp = {
        ui: {
            datePicker: $("[data-action='datePicker']"),
            merchantRegisterForm: $("#merchantRegisterForm"),
            paymentForm: $("#paymentForm"),
            signupButton: $("#btnNext"),

            //signup form UIs
            businessTitle: $("#businessTitle"),
            firstName: $("#firstname"),
            lastName: $("#lastname"),
            email: $("#email"),
            password: $("#password"),
            contactNumber: $("#contact"),
            url: $("#url")
        },

        init: function () {
            this.events();
            this.ui.paymentForm.hide();
        },

        events: function () {
            var that = this;
            this.ui.merchantRegisterForm.validate({
                submitHandler: function (form) {
                    var submitData = {
                        "username": that.ui.email.val(),
                        "firstName": that.ui.firstName.val(),
                        "lastName": that.ui.lastName.val(),
                        "email": that.ui.email.val(),
                        "mobileNumber": that.ui.contactNumber.val(),
                        "password": that.ui.password.val(),
                        "businessTitle": that.ui.businessTitle.val(),
                        "url": that.ui.url.val()
                    };
                    that.doSignup(submitData);
                }
            });

            this.ui.paymentForm.validate({
                submitHandler: function (form) {
                    alert("form valid");
                }
            });
        },

        doSignup: function (data) {
            var that = this;
            this.ui.signupButton.prop("disabled", true);
            console.log(data);

            var callback = function (status, data) {
                if (data.success) {
                    var button1 = function () {
                        window.location = Main.modifyURL("/");
                    };
                    button1.text = "Close";
                    var button = [button1];
                    Main.popDialog("Congratulations", data.message, button, 'success', true);
                    that.ui.signupButton.prop("disabled", false);
                    that.ui.merchantRegisterForm.trigger("reset");
                } else {
                    Main.popDialog("Error", data.message, null, 'error');
                    that.ui.signupButton.prop("disabled", false);
                }
            };

            Main.request('/anon/merchant_register', data, callback);
        }
    };

    return {
        init: function () {
            signUp.init();
        }
    }

})();
