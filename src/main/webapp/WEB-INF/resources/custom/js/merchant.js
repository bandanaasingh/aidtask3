/**
 * Created by Frank on 4/25/2018.
 */

var merchantListModule = (function () {
    var listMerchant = {
            ui: {
                superTable: $('#listMerchantTable'),
                merchantStatus: $('.activation-status'),
                addMerchantBtn: $('#addMerchantBtn')
            },
            init: function () {
                this.getMerchantList();
                this.events();
            },
            events: function () {
                var that = this;
                this.ui.superTable.on('click', this.ui.merchantStatus, function (e) {
                    var link = $(e.target);
                    var id = link.attr('data-id');
                    var status = link.attr('data-status');
                    if (typeof(id) != "undefined" && typeof(status) != "undefined") {
                        var btnCancel = function () {
                            $('#popDialog').modal('hide');
                        };
                        btnCancel.text = "Cancel";

                        var btnConfirm = function () {
                            that.changeMerchantStatus(id, status);
                            $('#popDialog').modal('hide');
                        };
                        btnConfirm.text = "Yes";

                        var button = [btnConfirm, btnCancel];

                        Main.popDialog("Confirm", "Are you sure you want to change the status?", button, null, null);

                    }
                });

                /*this.ui.addMerchantBtn.on('click', function () {
                 Main.doLogout('/signup');
                 });*/
            },

            getMerchantList: function () {
                var that = this;
                var dataFilter = function (data, type) {
                    if (data.success) {
                        var responseRows = data.params.numberOfRows;
                        var merchants = data.params.data;
                        var tableData = [];
                        var businessTitle = "",
                            name = "",
                            email = "",
                            mobile = "",
                            status = "",
                            actionActivation = "",
                            action = "",
                            row = [];
                        $.each(merchants, function (index, data) {
                            var user = data.user || {};
                            businessTitle = "";
                            name = "";
                            email = "";
                            mobile = "";
                            status = "";
                            actionActivation = "";
                            action = "";
                            row = [];

                            businessTitle = data.businessTitle || "";
                            name = user.firstName || "";
                            name += " ";
                            name += user.lastName || "";
                            name = name.trim();

                            email = user.email || "";
                            mobile = user.mobileNumber || "";
                            status = user.status || "";
                            status = Main.ucfirst(status);

                            if (user.status) {
                                if (user.status === "ACTIVE") {
                                    actionActivation = ' <span class="ph5">|</span> ' +
                                        '<a class="activation-status " href="#" data-id="' + data.id + '" data-status="INACTIVE">Deactivate</a>';
                                } else if (data.user.status === "UNVERIFIED") {
                                    actionActivation = '';
                                } else if (data.user.status === "INACTIVE") {
                                    actionActivation = ' <span class="ph5">|</span> ' +
                                        '<a class="activation-status " href="#" data-id="' + data.id + '" data-status="ACTIVE">Activate</a>';
                                }
                            }

                            action = '<div class="action_links">' +
                                '<a href="#' + data.id + '">Profile</a>' +
                                actionActivation +
                                '</div>';

                            row = [
                                index + 1,
                                "<a href='/merchant/store_list?merchantId=" + data.id+ "&userId="+data.user.id+"'>" + businessTitle + "</a>",
                                name,
                                email,
                                mobile,
                                status,
                                action
                            ];

                            row = $.extend({}, row);
                            tableData.push(row);
                        });

                        var response = {};
                        response.data = tableData;
                        response.recordsTotal = responseRows;
                        response.recordsFiltered = responseRows;
                        return response;

                        //console.log(merchants);
                        //that.generateFootable(merchants);

                    } else {
                        Main.popDialog("Error", data.message, null, 'error');
                    }
                };

                var params = {
                    "pageNumber": 1,
                    "pageSize": 10
                };

                dataFilter.columns = [
                    {"sortable": false},
                    {"name": "businessTitle"},
                    {"name": "user#firstName"},
                    {"name": "user#email"},
                    {"name": "user#mobileNumber"},
                    {"name": "user#status"},
                    {"sortable": false}
                ];

                dataFilter.requestType = "POST";
                dataFilter.url = "/organizer/get_merchant_list";
                dataFilter.params = params;
                that.ui.superTable = Main.createDataTable(that.ui.superTable, dataFilter);
            },

            changeMerchantStatus: function (merchantId, status) {
                var that = this;
                var url = "/merchant/change_merchant_status";
                var callback = function (status, data) {
                    if (data.success) {
                        that.ui.superTable.ajax.reload();
                    } else {
                        Main.popDialog('Error', data.message, null, 'error');
                    }
                };
                callback.requestType = "POST";
                var data = {
                    "status": status
                };
                Main.request(url, data, callback, {"merchantId": merchantId});
            }
        }
        ;

    return {
        init: function () {
            listMerchant.init();
        }
    }
})
();