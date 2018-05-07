/**
 * Created by Frank on 6/7/2016.
 */
var addEditStoreModule = (function () {
    var storesData = {};
    var merchantId = Main.getFromLocalStorage('merchantId');

    var curLocationKey;
    var storeBrandId;
    var isEdit = false;
    var addStore = {
        ui: {
            pageTitle: $('.content-title'),
            storeForm: $('#storeForm'),
            storeNameInput: $('#storeName'),
            storeImageContainer: $('#storeImageContainer'),
            fileInputWrapper: $('.fileinput'),

            newStoreId: $('#newStoreId'),
            contactNumber: $('#contactNumber'),
            streetAddressInput: $('#streetAddress'),
            streetAddressTwoInput: $('#streetAddressTwo'),
            mainAreaInput: $('#mainArea'),
            subAreaInput: $('#subArea'),
            priorityInput: $('#priority'),
            isDistanceBaseRatePlanInput: $('#isDistanceBaseRatePlan'),
            addressNoteInput: $('#addressNote'),

            description: $('#description'),
            servingDistance: $('#servingDistance'),
            minOrder: $('#minOrder'),
            placeOrderBefore:$('#placeOrderBefore'),


            statementInput: $('input[name="modeOfStatementGeneration"]'),
            submitBtn: $('#submitBtn'),
            editBtn: $('#editBtn'),
            formBtns: $('#formBtns'),
            removeMarkerBtn: $('#removeMarker'),

            storeDetailsForm: $('#storeDetailsForm')
        },

        init: function () {
            this.getMerchantIdFromUrl();
            this.events();

            this.validation();

            this.ui.editBtn.hide();

            storeBrandId = Main.getNamedParameter('storeBrandId');
            if (storeBrandId) {
                this.pageMode('view');
                storeBrandId = Main.globalReplace(storeBrandId, '#', '');

                this.ui.submitBtn.text('Save Changes');
                this.ui.fileInputWrapper.removeClass('fileinput-new').addClass('fileinput-exists');
                this.ui.editBtn.show();
                isEdit = true;
            }
            this.getMainAreas();
            this.ui.storeDetailsForm.find(':input').prop('disabled', true);

        },

        events: function () {
            var that = this;

            this.ui.editBtn.click(function () {
                var formtype = $(this).attr('data-formtype');
                if (formtype == "view") {
                    $(this).attr('data-formtype', 'edit');
                    that.pageMode('edit');

                } else if (formtype == "edit") {
                    $(this).attr('data-formtype', 'view');
                    that.pageMode('view');

                }
            });

            this.ui.mainAreaInput.on('changed.ao', function () {
                var selectedMainAreaId = $(this).val();
                if (selectedMainAreaId != "") {
                    that.getSubAreas(selectedMainAreaId);
                }
            }).on('change', function () {
                var selectedMainAreaId = $(this).val();
                if (selectedMainAreaId != "") {
                    that.getSubAreas(selectedMainAreaId);
                }
            });

            $(document).on('maps.marker.add', function (e, data) {
                that.setStoresData(data);
                if (Object.keys(storesData).length) {
                    that.ui.storeDetailsForm.find(':input').removeAttr('disabled');
                    $('.select-picker').selectpicker('refresh');
                }
            }).on('maps.marker.remove', function (e, locationKey) {
                that.removeStoreData(locationKey);
                if (Object.keys(storesData).length == 0) {
                    that.ui.storeDetailsForm.find(':input').attr('disabled', 'disabled');
                    $('.select-picker').selectpicker('refresh');
                }
            }).on('maps.marker.active', function (e, locationKey) {
                console.log(locationKey);
                that.setActiveStore(locationKey);
                curLocationKey = locationKey;
            });

            this.ui.newStoreId.on('change', function () {
                if (!$.isEmptyObject(storesData[curLocationKey])) {
                    storesData[curLocationKey].newStoreId = $(this).val();
                    console.log($(this).val());
                }
            });

            this.ui.contactNumber.on('change', function () {
                if (!$.isEmptyObject(storesData[curLocationKey])) {
                    storesData[curLocationKey].contactNo = $(this).val();
                    console.log($(this).val());
                }
            });

            this.ui.addressNoteInput.on('change', function () {
                if (!$.isEmptyObject(storesData[curLocationKey])) {
                    storesData[curLocationKey].addressNote = $(this).val();
                }
            });

            this.ui.priorityInput.on('change', function () {
                if (!$.isEmptyObject(storesData[curLocationKey])) {
                    storesData[curLocationKey].priority = $(this).val();
                }
            });

            this.ui.isDistanceBaseRatePlanInput.on('change', function () {
                if (!$.isEmptyObject(storesData[curLocationKey])) {
                    storesData[curLocationKey].isDistanceBaseRatePlan = $(this).val();
                }
            });

            this.ui.subAreaInput.on('change', function () {
                if (!$.isEmptyObject(storesData[curLocationKey])) {
                    storesData[curLocationKey].area.areaId = $(this).val();
                }
            });

            this.ui.mainAreaInput.on('change', function () {
                if (!$.isEmptyObject(storesData[curLocationKey])) {
                    storesData[curLocationKey].area.mainAreaId = $(this).val();
                }
            });

            this.ui.streetAddressInput.on('change', function () {
                if (!$.isEmptyObject(storesData[curLocationKey])) {
                    storesData[curLocationKey].givenLocation1 = $(this).val();
                }
            });

            this.ui.streetAddressTwoInput.on('change', function () {
                if (!$.isEmptyObject(storesData[curLocationKey])) {
                    storesData[curLocationKey].givenLocation2 = $(this).val();
                }
            });

        },

        setStoresData: function (data) {
            var sData = {
                "givenLocation1": data.locality + " " + data.sublocality,
                "givenLocation2": data.city + " " + data.state,
                "latitude": data.position.lat,
                "longitude": data.position.lng,
                "contactNo": "",
                "isDistanceBaseRatePlan": "",
                "priority": "",
                "addressNote": "",
                "area": {
                    "areaId": "",
                    "mainAreaId": ""
                }
            };

            storesData[data.key] = sData;

            this.ui.streetAddressInput.val(sData.givenLocation1).focus().blur();
            this.ui.streetAddressTwoInput.val(sData.givenLocation2).focus().blur();

        },

        setActiveStore: function (locationKey) {
            console.log(locationKey);
            var sData = storesData[locationKey];
            console.log(storesData);
            console.log(sData);
            this.ui.contactNumber.val(Main.fixUndefinedVariable(sData.contactNo)).focus().blur();
            this.ui.streetAddressInput.val(sData.givenLocation1).focus().blur();
            //this.ui.mainAreaInput.val(sData.area.mainAreaId);

            //console.log(sData.area.mainAreaId);

            this.ui.mainAreaInput.trigger('changed.ao');

            this.ui.subAreaInput.on('subarea.loaded', function () {
                $(this).val(sData.area.areaId).focus().blur();
                $('.select-picker').selectpicker('refresh');
            });

            this.ui.priorityInput.val(sData.priority).focus().blur();
            //console.log(sData);
            this.ui.isDistanceBaseRatePlanInput.val(sData.isDistanceBaseRatePlan + "").focus().blur();
            this.ui.addressNoteInput.val(sData.addressNote).focus().blur();

            $('.select-picker').selectpicker('refresh');
        },

        removeStoreData: function (locationKey) {
            //console.log(locationKey);
            //console.log(storesData);

            delete storesData[locationKey + ""];
            // console.log(storesData);

            this.ui.newStoreId.val('').focus().blur();
            this.ui.contactNumber.val('').focus().blur();
            this.ui.streetAddressInput.val('').focus().blur();
            this.ui.streetAddressTwoInput.val('').focus().blur();
            this.ui.mainAreaInput.val('').focus().blur();
            this.ui.subAreaInput.val('').focus().blur();
            this.ui.priorityInput.val('').focus().blur();
            this.ui.isDistanceBaseRatePlanInput.val('').focus().blur();
            this.ui.addressNoteInput.val('').focus().blur();

            $('.select-picker').selectpicker('refresh');

        },

        getMerchantIdFromUrl: function () {
            var url = document.URL;

            if (merchantId == null && url.indexOf("merchantId=") >= 0) {
                merchantId = Main.getNamedParameter('merchantId');
            }
        },

        getSubAreas: function (parentId, subAreaId) {
            if (parentId == null)
                return;

            if (parentId !== "") {
                var that = this;
                var url = "/smanager/get_area";
                var callback = function (status, data) {
                    if (data.success) {
                        var mainAreaOptions = "";
                        $.each(data.params.child, function (index, data) {
                            mainAreaOptions += "<option value='" + data.id + "'>" + data.name + "</option>"
                        });

                        that.ui.subAreaInput.html(mainAreaOptions).selectpicker('refresh');

                        if (subAreaId) {
                            that.ui.subAreaInput.val(subAreaId).selecpicker('refresh');
                        }

                        that.ui.subAreaInput.trigger('subarea.loaded');

                    } else {
                        Main.popDialog("Error", data.message, null, 'error');
                    }
                };
                callback.requestType = "GET";
                Main.request(url, {}, callback, {areaId: parentId});
            }
        },

        getMainAreas: function () {
            var that = this;
            var url =  "/smanager/get_active_area";
            var callback = function (status, data) {
                if (data.success) {
                    var mainAreaOptions = "";
                    //console.log(data.params.areas);
                    $.each(data.params, function (index, data) {
                        mainAreaOptions += "<option value='" + data.id + "'>" + data.name + "</option>"
                    });
                    $("#mainArea").append(mainAreaOptions);
                    console.log('load data');
                    $("#mainArea").selectpicker("refresh");
                    console.log('refreshed');
                    if (isEdit) {
                        that.getStoreDetail(storeBrandId);
                    }
                    $('#mainArea').selectpicker('refresh');
                } else {
                    Main.popDialog("Error", data.message, null, 'error');
                }
            };
            callback.requestType = "GET";
            Main.request(url, {}, callback, {});
        },

        validation: function () {
            var that = this;
            this.ui.storeForm.submit(function (e) {
                e.preventDefault();
            }).validate({
                rules: {
                    storeName: {
                        required: true
                    },
                    contactNumber: {
                        required: true,
                        number: true,
                        minlength: 9,
                        maxlength: 10
                    },
                    streetAddress: {
                        required: true,
                    },
                    mainArea: {
                        required: true,
                    },
                    subArea: {
                        required: true,
                    },
                    addressNote: {
                        required: true,
                    }
                },
                submitHandler: function () {

                    if ($.isEmptyObject(storesData)) {
                        Main.popDialog('Error', "Please provide atleast one store by placing a marker in the map", null, 'error');
                        return;
                    }

                    if (typeof(merchantId) == 'undefined' || merchantId === "" || merchantId === null) {
                        Main.popDialog('Error', "Request from invalid user.", null, 'error');
                        return;
                    }

                    var callback = function (status, data) {
                        //console.log(data);
                        if (data.success) {
                            that.ui.storeForm.trigger("reset");
                            var button1 = function () {
                                window.location = Main.modifyURL('merchant/store_list?merchantId=' + Main.getNamedParameter('merchantId'));
                            };
                            button1.text = "Close";
                            var button = [button1];
                            Main.popDialog("", data.message, button, 'success', true);
                        } else {
                            Main.popDialog('Error', data.message, null, 'error');
                        }
                    };

                    callback.requestType = "PUT";

                    var stores = [];

                    for (var key in storesData) {
                        if (storesData.hasOwnProperty(key)) {
                            //console.log(storesData[key]);
                            var data = storesData[key];
                            var newData = {
                                "id": data.storeId,
                                "contactNo": data.contactNo,
                                "givenLocation": data.givenLocation1,
                                "addressNote": data.addressNote,
                                "latitude": data.latitude,
                                "longitude": data.longitude,
                                "areaId": data.area.areaId
                            };
                            stores.push(newData);
                        }
                    }

                    var data = {
                        "name": that.ui.storeNameInput.val(),
                        "logo": $('.fileinput-preview.fileinput-exists.thumbnail img').attr('src'),
                        "description": that.ui.description.val(),
                        "servingDistance": that.ui.servingDistance.val(),
                        "minOrderAmount": that.ui.minOrder.val(),
                        "placeOrderBefore": that.ui.placeOrderBefore.val(),
                        "stores": stores
                    };

                    if (typeof(data.logo) === 'undefined') {
                        Main.popDialog('Error', "Please select store image.", null, 'error');
                        return;
                    }

                    //console.log(data);

                    if (!isEdit) {
                        Main.request('/merchant/save_store_brand', data, callback, {merchantId: merchantId});
                    } else {
                        Main.request('/merchant/update_store_brand', data, callback, {
                            merchantId: Main.globalReplace(merchantId, '#', ''),
                            brandId: Main.globalReplace(storeBrandId, '#', '')
                        });
                    }
                }
            });
        },

        getStoreDetail: function (storeBrandId) {
            var that = this;
            var url = "/merchant/get_brand_detail";
            var callback = function (status, data) {
                if (data.success) {
                    var storeDetail = data.params;

                    that.ui.pageTitle.html('Store: ' + storeDetail.brandName);

                    var imgSrc = storeDetail.brandLogo;
                    if (typeof(imgSrc) == "undefined") {
                        imgSrc = 'http://placehold.it/175x175&text=SLogo';
                    }

                    that.ui.storeImageContainer.html('<img src="' + imgSrc + '" style="max-height: 140px;">')

                    that.ui.storeNameInput.val(storeDetail.name).focus().blur();
                    that.ui.description.val(storeDetail.description).focus().blur();
                    that.ui.servingDistance.val(storeDetail.servingDistance).focus().blur();
                    that.ui.minOrder.val(storeDetail.minOrderAmount).focus().blur();
                    that.ui.placeOrderBefore.val(storeDetail.placeOrderBefore).focus().blur();

                    var stores = data.params.stores;
                    var locationKeyOut = "";

                    $.each(stores, function (index, storeLocs) {
                        var location = {
                            'latitude': Number(storeLocs.latitude),
                            'longitude': Number(storeLocs.longitude)
                        };
                        locationKeyOut = newMapModule.getKeyByLocation(location);
                        var sData = {
                            "storeId": storeLocs.storeId,
                            "givenLocation1": storeLocs.givenLocation,
                            "latitude": storeLocs.latitude,
                            "longitude": storeLocs.longitude,
                            "contactNo": storeLocs.contactNo,
                            "addressNote": storeLocs.addressNote,
                            "area": {
                                "areaId": storeLocs.area.id,
                                "mainAreaId": storeLocs.area.parent.id
                            }
                        };

                        storesData[locationKeyOut] = sData;
                        newMapModule.addMarkersToMap(superMap, {
                            'lat': Number(storeLocs.latitude),
                            'lng': Number(storeLocs.longitude)
                        }, false);
                        curLocationKey = locationKeyOut;
                    });

                    that.setActiveStore(locationKeyOut);

                } else {
                    Main.popDialog("Error", data.message, null, 'error');
                }
            };
            callback.requestType = "GET";
            Main.request(url, {}, callback, {brandId: storeBrandId});
        },

        pageMode: function (mode) {
            var that = this;
            if (mode == "edit") {
                readOnly = false;
                that.ui.editBtn.text('Cancel');
                that.ui.formBtns.removeClass('hide');
                that.ui.removeMarkerBtn.removeClass('hide');
                that.ui.fileInputWrapper.find('.btn-file, .fileinput-exists.btn').removeClass('hide');
                that.ui.storeForm.find('input, select, textarea').prop('disabled', false);
                $('.btn-ao-input').prop('disabled', false);
                that.ui.storeForm.find('.select-picker').selectpicker('refresh');
                //that.ui.storeNameInput.attr('disabled', 'disabled');
                //that.ui.resetBtn.hide();

                newMapModule.updateMapStatus(false, true);

            } else if (mode == "view") {
                readOnly = true;
                that.ui.editBtn.text('Edit');
                that.ui.formBtns.addClass('hide');
                that.ui.removeMarkerBtn.addClass('hide');
                that.ui.fileInputWrapper.find('.btn-file, .fileinput-exists.btn').addClass('hide');
                $('.btn-ao-input').prop('disabled', true);
                that.ui.storeForm.find('input, select, textarea').prop('disabled', true);
                newMapModule.updateMapStatus(true, true);

            }
        },
    }

    return {
        init: function () {
            addStore.init();
        }
    }
})();


var listStoreModule = (function () {
    //var storesObj = [];

    var merchantId = "";
    var listStore = {
        ui: {
            storeBoxWrapper: $('#storeBoxWrapper'),
            locationModal: $('.store-location-modal'),
            locationBoxWrapper: $('#storeLocationWrapper'),
            addStoreBtn: $('#addStoreBtn, .addStoreBtn'),
            activeStoresWrapper: $('#storeBoxWrapper'),
            inactiveStoresWrapper: $('#inactiveStores'),
            searchStoreButton: $('#showStoreSearchInput'),
            searchStoreInput: $('#searchStoreInList'),
            searchStoreWrapper: $('#searchStoreInList').closest('.store-search')
        },
        init: function () {
            this.events();
            //this.getMerchantDetail();
            this.getStoresByStatus("ACTIVE", this.ui.activeStoresWrapper);

            this.getStoresByStatus("INACTIVE", this.ui.inactiveStoresWrapper);

            this.ui.searchStoreWrapper.removeClass('hidden').slideUp('1');
        },

        events: function () {
            var that = this;
            merchantId = Main.getNamedParameter('merchantId');
            if(!merchantId)
                merchantId = Main.getFromLocalStorage('merchantId');
            this.ui.searchStoreButton.on('click', function (event) {
                event.preventDefault();
                that.ui.searchStoreWrapper.slideToggle(function () {
                    if (that.ui.searchStoreWrapper.is(':visible')) {
                        that.ui.searchStoreInput.focus();
                        that.ui.searchStoreButton.attr('data-original-title', 'Clear Search');
                    } else {
                        that.ui.searchStoreInput.val('').trigger('keyup');
                        that.ui.searchStoreButton.attr('data-original-title', 'Search Store');
                    }
                });
            });

            this.ui.searchStoreInput.on('keyup', that.searchStore);
        },

        getMerchantDetail: function () {
            var that = this;
            var url =  "/merchant/get_store_list";
            var callback = function (status, data) {
                //console.log(data);
                if (data.success) {
                    var stores = "";
                    var storeLogo = "";

                    $.each(data.params.data, function (index, storeDetail) {
                        if (typeof(storeDetail.logo) !== "undefined") {
                            storeLogo = storeDetail.logo;
                        } else {
                            storeLogo = "http://placehold.it/175x175&text=SLogo";
                        }


                        stores += '<div class="col-sm-3"> ' +
                            '<div class="store-box media positionrelative"> ' +
                            '<div class="media-left media-middle"> ' +
                            '<img src="' + storeLogo + '" alt="Store Logo" class="store-icon p25" width="175" height="175"> ' +
                            '</div> ' +
                            '<div class="media-body media-middle"> ' +
                            '<p class="store-name">' + storeDetail.name + '</p> ' +
                            '<p class="store-location text-muted">' + storeDetail.stores.length + ' Store/s</p> ' +
                            '</div> ' +
                            '<div class="overlay"> ' +
                            '<div class="overlay-content"> ' +
                            '<div class="row"> ' +
                            '<div class="col-xs-6 text-center"> ' +
                            '<a href="merchant/item_list/' + storeDetail.id + '" >Add Item</a> ' +
                            '</div> ' +
                            '<div class="col-xs-6 text-center"> ' +
                            '<a href="merchant/store_form?merchantId=' + merchantId + '&storeBrandId=' + storeDetail.id + '"><i class="fa fa-eye"></i> &nbsp; View Store</a> ' +
                            '</div> ' +
                            '</div> ' +
                            '</div> ' +
                            '</div> ' +
                            '</div> ' +
                            '</div>';
                    });
                    if (stores != "") {
                        that.ui.storeBoxWrapper.html(stores);
                    }

                } else {
                    Main.popDialog("Error", data.message, null, 'error');
                }
            };
            callback.requestType = "POST";


            var requestData = {
                "page": {
                    "pageSize": 100,
                    "pageNumber": 1
                }
            };

            if (merchantId) {
                requestData.merchant = {
                    "merchantId": merchantId
                };
            } else {
                requestData.user = {
                    "userId": Main.getFromLocalStorage('userId')
                }
            }


            Main.request(url, requestData, callback, {});
        },

        getStoresByStatus: function (status, storeWrapper) {
            var that = this;
            var callback = function (status, data) {
                if (data.success) {
                    var stores = "";
                    var storeLogo = "";
                    $.each(data.params.data, function (index, storeDetail) {
                        if (typeof(storeDetail.logo) !== "undefined") {
                            storeLogo = storeDetail.logo;
                        } else {
                            storeLogo = "/resources/custom/images/avatar-1.jpg";
                        }
                        storeLogo = "/resources/custom/images/avatar-1.jpg";
                        stores += '<div class="col-lg-2 col-md-4 col-sm-6 col-xs-12"><div class="card">'+
                            '<div class="card-header"> <div class="hovereffect">' +
                            '<img class="img-responsive store-image" src="'+storeLogo+'" alt="">' +
                            '<div class="overlay">' +
                            '<h2><a href="/merchant/store_form?merchantId=' + merchantId + '&storeBrandId=' + storeDetail.id + '">View Store</a></h2>' +
                            '<h2><a href="/merchant/item_list/' + storeDetail.id + '">View Item</a></h2>' +
                            '</div>' +
                            '</div></div><div class="card-body"><div class="store-name">'+storeDetail.name+'</div> </div></div> ' +
                            '</div>';
                    });
                    if (stores != "") {
                        storeWrapper.html(stores);
                    }

                } else {
                    Main.popDialog("Error", data.message, null, 'error');
                }
            };
            callback.requestType = "POST";

            var url = "/merchant/stores_by_status";

            var params = {
                "pageNumber": 1,
                "pageSize": 1000,
                "status": status
            };

            var merchantUserId = Main.getNamedParameter('userId');
            var header = {};
            if (merchantUserId) {
                header.userId = merchantUserId;
            } else {
                header.userId = Main.getFromLocalStorage('userId');
            }


            Main.request(url, params, callback, header);
        },
        searchStore: function (event) {
            event.preventDefault();
            var searchTerm = $.trim($(this).val()).replace(/[^\w\s]/g, "\\$&");
            var searchPattern = '^(?=.*\\b' + searchTerm.split(/\s+/).join('\\b)(?=.*\\b') + ').*$';
            var reg = RegExp(searchPattern, 'i');
            var selector = $(".store-box").closest('.store-list');
            selector.removeClass('hidden').filter(function (index) {
                var id = $(this).find('.store-name').text();
                return !reg.test(id);
            }).addClass('hidden');
        },
    };

    return {
        init: function () {
            listStore.init();
        }
    }
})();


var viewEditStoreModule = (function () {
    var stores;
    var viewStore = {
        ui: {
            pageTitle: $('.content-title'),
            storeForm: $('#storeForm'),
            storeNameInput: $('#storeName'),
            streetAddressInput: $('#streetAddress'),
            mainAreaInput: $('#mainArea'),
            subAreaInput: $('#subArea'),
            priorityInput: $('#priority'),
            addressNoteInput: $('#addressNote'),
            statementInput: $('input[name="modeOfStatementGeneration"]'),
            submitBtn: $('#submitBtn'),
            formBtns: $('#formBtns'),
            storeImageContainer: $('#storeImageContainer'),
            removeMarkerBtn: $('#removeMarker'),
            fileInputWrapper: $('.fileinput'),
            editBtn: $('#editBtn'),
            resetBtn: $('[type="reset"]'),
        },
        init: function () {
            //console.log("sss");
            this.getStoreDetail();
            this.getMainAreas();
            this.events();
            this.validation();

            this.ui.submitBtn.text('Save Changes');
            this.ui.fileInputWrapper.removeClass('fileinput-new').addClass('fileinput-exists');

            this.pageMode('view');
        },

        pageMode: function (mode) {
            var that = this;
            if (mode == "edit") {
                readOnly = false;
                that.ui.editBtn.text('Cancel');
                that.ui.formBtns.removeClass('hide');
                that.ui.removeMarkerBtn.removeClass('hide');
                that.ui.fileInputWrapper.find('.btn-file, .fileinput-exists.btn').removeClass('hide');
                that.ui.storeForm.find('input, select, textarea').removeAttr('disabled');
                that.ui.storeForm.find('.select-picker').selectpicker('refresh');
                //that.ui.storeNameInput.attr('disabled', 'disabled');
                that.ui.resetBtn.hide();


            } else if (mode == "view") {
                readOnly = true;
                that.ui.editBtn.text('Edit');
                that.ui.formBtns.addClass('hide');
                that.ui.removeMarkerBtn.addClass('hide');
                that.ui.fileInputWrapper.find('.btn-file, .fileinput-exists.btn').addClass('hide');
                that.ui.storeForm.find('input, select, textarea').attr('disabled', 'disabled');

            }
        },

        events: function () {
            var that = this;
            this.ui.mainAreaInput.change(function () {
                var selectedMainAreaId = $(this).val();
                //console.log(selectedMainAreaId);
                if (selectedMainAreaId != "") {
                    that.getSubAreas(selectedMainAreaId);
                }

            });

            this.ui.editBtn.click(function () {
                var formtype = $(this).attr('data-formtype');
                if (formtype == "view") {
                    $(this).attr('data-formtype', 'edit');
                    that.pageMode('edit');
                } else if (formtype == "edit") {
                    $(this).attr('data-formtype', 'view');
                    that.pageMode('view');
                }
            });
        },

        /*getMainAreas: function () {
            var that = this;
            var url = "/smanager/get_active_area";
            var callback = function (status, data) {
                if (data.success) {
                    var mainAreaOptions = "";
                    //console.log(data.params.areas);
                    $.each(data.params, function (index, data) {
                        mainAreaOptions += "<option value='" + data.id + "'>" + data.name + "</option>"
                    });

                    that.ui.mainAreaInput.append(mainAreaOptions).selectpicker({
                        liveSearch: true,
                        /!*title: "Main Area",*!/
                        style: "btn-ao-input"
                    });

                    //that.ui.mainAreaInput.parents().eq(1).addClass('focused');

                } else {
                    Main.popDialog("Error", data.message, null, 'error');
                }
            };
            callback.requestType = "GET";
            Main.request(url, {}, callback, {});
        },*/

        getSubAreas: function (parentId) {
            //console.log(parentId);
            if (parentId == null)
                return;

            if (parentId !== "") {
                var that = this;
                var url = "/smanager/get_area";
                var callback = function (status, data) {
                    if (data.success) {
                        var mainAreaOptions = "";
                        //console.log(data);
                        $.each(data.params.child, function (index, data) {
                            mainAreaOptions += "<option value='" + data.id + "'>" + data.name + "</option>"
                        });

                        that.ui.subAreaInput.html(mainAreaOptions).selectpicker({
                            liveSearch: true,
                            style: "btn-ao-input"
                        }).selectpicker('refresh');
                    } else {
                        Main.popDialog("Error", data.message, null, 'error');
                    }
                };
                callback.requestType = "GET";
                Main.request(url, {}, callback, {areaId: parentId});
            }
        },

        getStoreDetail: function () {
            var that = this;
            var url = "/merchant/get_brand_detail";
            var callback = function (status, data) {
                console.log(data);
                if (data.success) {
                    var storeDetail = data.params;
                    console.log(storeDetail);
                    that.ui.pageTitle.html('Store: ' + storeDetail.name);

                    var imgSrc = storeDetail.brandLogo;
                    //console.log(imgSrc);

                    console.log(imgSrc);
                    console.log(storeDetail.name);

                    if (typeof(imgSrc) == "undefined") {
                        imgSrc = 'http://placehold.it/175x175&text=SLogo';
                    }

                    that.ui.storeImageContainer.html('<img src="' + imgSrc + '" style="max-height: 140px;">')

                    that.ui.storeNameInput.val(storeDetail.name).focus().blur();
                    //that.ui.statementInput.filter('[value="' + storeDetail.statementGenerationType + '"]').attr('checked', 'checked');

                    stores = data.params.storeBrandDetail.stores;
                    $.each(stores, function (index, storeLocs) {
                        var location = [{'lat': Number(storeLocs.latitude), 'lng': Number(storeLocs.longitude)}];
                        //console.log(storeLocs);
                        mapsModule.addMarkers(location);
                    });
                    //console.log(locations);
                    that.waitForMarkers(stores.length);

                } else {
                    Main.popDialog("Error", data.message, null, 'error');
                }
            };
            callback.requestType = "GET";
            Main.request(url, {}, callback, {brandId: Main.getNamedParameter('storeBrandId')});
        },

        waitForMarkers: function (countStores) {
            var that = this;
            $('body').append("<div class='loader'></div>");
            if (countStores != Object.keys(arrGeoPoints).length) {
                Main.log('Loading...', 'warning');
                setTimeout(function () {
                    that.waitForMarkers(countStores);
                }, 1000);
            } else {
                Main.log('Loading complete...', 'warning');

                $('.loader').remove();

                $.each(stores, function (index, storeLocs) {
                    var location = {'latitude': storeLocs.latitude, 'longitude': storeLocs.longitude};
                    var locationKey = mapsModule.getKeyByLocation(location);
                    //console.log(storeLocs.contactNo);
                    arrGeoPoints[locationKey].addressNote = storeLocs.addressNote;
                    arrGeoPoints[locationKey].priority = storeLocs.priority;
                    arrGeoPoints[locationKey].mainArea = storeLocs.area.parent.id;
                    arrGeoPoints[locationKey].subArea = storeLocs.area.id;
                    arrGeoPoints[locationKey].storeId = storeLocs.storeId;
                    arrGeoPoints[locationKey].contactNumber = storeLocs.contactNo;

                });
                google.maps.event.trigger(marker, 'click');
            }
        },

        validation: function () {
            var that = this;
            this.ui.storeForm.submit(function (e) {
                e.preventDefault();
            }).validate({
                rules: {
                    storeName: {
                        required: true
                    },
                    streetAddress: {
                        required: true
                    },
                    contactNumber: {
                        number: true,
                        required: true,
                    },
                    mainArea: {
                        required: true
                    },
                    subArea: {
                        required: true
                    },
                    addressNote: {
                        required: true
                    },
                    priority: {
                        required: true
                    }
                    /*modeOfStatementGeneration: {
                     required: true,
                     }*/
                },
                submitHandler: function () {
                    var callback = function (status, data) {
                        //console.log(data);
                        if (data.success) {
                            var button1 = function () {
                                window.location = Main.modifyURL(document.URL);
                            };
                            button1.text = "Close";
                            var button = [button1];
                            Main.popDialog("", data.message, button, 'success', true);
                        } else {
                            Main.popDialog('Error', data.message, null, 'error');
                        }
                    };

                    callback["requestType"] = "PUT";

                    var stores = [];
                    $.each(arrGeoPoints, function (index, data) {
                        var newData = {
                            "storeId": data.id,
                            "givenLocation1": data.streetAddress,
                            "givenLocation2": data.locality,
                            "contactNo": data.contactNumber,
                            "addressNote": data.addressNote,
                            "latitude": data.position.lat,
                            "longitude": data.position.lng,
                            "priority": data.priority,
                            "area": {
                                "areaId": data.subArea
                            }
                        }
                        stores.push(newData)
                    });


                    var data = {
                        "brandName": that.ui.storeNameInput.val(),
                        "statementGenerationType": "MONTHLY",
                        "brandLogo": $('.fileinput-preview.fileinput-exists.thumbnail img').attr('src'),
                        "stores": stores
                    };

                    console.log(data);
                    //console.log(JSON.stringify(data));


                    /*if (typeof(merchantId) == 'undefined' || merchantId == "" || merchantId == null) {
                     Main.popDialog('Error', "Request from invalid user.", null, 'error');
                     return;
                     }*/

                    if (data.brandLogo == 'undefined' || data.brandLogo == null) {
                        Main.popDialog('Error', "Please select image to continue.", null, 'error');
                        return;
                        //data.brandLogo = "";
                    }

                    Main.request('merchant/update_stores_brand', data, callback, {
                        merchantId: Main.getNamedParameter('merchantId'),
                        brandId: Main.getNamedParameter('storeBrandId')
                    });
                }
            });
        }
    };

    return {
        init: function () {
            viewStore.init();
        }
    }
})();


