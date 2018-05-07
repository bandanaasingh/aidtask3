var itemModule = (function ($) {
    'use strict';
    var form = {
        ui: {
            //Item form
            itemForm: $("#itemForm"),
            itemFormState: 'add',
            itemImageInput: $("#itemImageInput"),
            itemImageOutput: $("#itemImageOutput"),
            itemId: $("#itemId"),

            itemNameInput: $("#itemName"),
            descriptionInput: $("#itemDescription"),
            itemOverviewInput: $("#itemOverview"),

            itemCategoryInput: $("#itemCategory"),

            itemAvailableStartTimeInput: $("#itemStartTime"),
            itemAvailableEndTimeInput: $("#itemEndTime"),
            itemServiceChargeInput: $("#itemServiceCharge"),
            itemCommissionPercentageInput: $("#itemCommissionPercentage"),
            // itemMrpInput: $("#itemMrp"),
            itemUnitPriceInput: $("#itemUnitPrice"),
            minPersonInput: $("#minPerson"),

            addAttributeTypeButton: $("#add_attr_type"),
            addAttributeButton: $('.add_attr'),
            removeAttributeBlock: $('.remove_attr_block'),
            removeAttribute: $('.remove_attr'),

            addItemButton: $("#addItemButton"),

            fromTimePicker: $('#itemStartTime'),
            toTimePicker: $('#itemEndTime'),
        },

        init: function () {
            this.getMainCategoryList();
            this.validation();
            this.events();
            var merchantId = "";
            if(Main.getURLvalue(3)==undefined){
                merchantId = Main.getFromLocalStorage("merchantId");
            } else{
                if(Main.getURLvalue(2) === 'add')
                    Main.saveInLocalStorage("merchantId", Main.getURLvalue(3));
                merchantId = Main.getURLvalue(3);
            }
            if (Main.getURLvalue(2) === 'edit') {
                this.ui.itemFormState = 'edit';
                document.title = 'Edit Item | BigOrder';
                this.editMode(merchantId);
            }
            console.log(Main.getURLvalue(3));
        },

        events: function () {
            var that = this;
            this.ui.fromTimePicker.datetimepicker({
                format: 'LT',
                defaultDate: moment().hours(7).minutes(0)
            });
            this.ui.toTimePicker.datetimepicker({
                format: 'LT',
                defaultDate: moment().hours(19).minutes(0)
            });
            this.ui.itemForm.on("click", "#addAttributeTypeButton", this.addAttributeTypeTemplate);
            this.ui.itemForm.on("click", ".addAttributeButton", this.addAttributeTemplate);
            this.ui.itemForm.on("click", ".removeAttributeTypeButton", this.removeAttributeTypeTemplate);
            this.ui.itemForm.on("click", ".removeAttributeButton", this.removeAttributeTemplate);
            imageModule.init(this.ui.itemImageInput, this.ui.itemImageOutput);

        },

        //Item Attribute functions
        addAttributeTypeTemplate: function () {
            console.log("-- inside $addAttributeTypeTemplate function --");

            var $cloned = $('#attributeTypeTemplate').clone();

            $("#attributeTemplate", $cloned).addClass("attributeTemplate");
            $("#attributeTemplate", $cloned).removeAttr("id");
            $(".removeAttributeButton", $cloned).prop("disabled", true);

            $cloned.removeClass("hidden");
            $cloned.removeAttr("id");
            $cloned.addClass("attributeTypeTemplate");

            $cloned.insertBefore($("#addAttributeTypeButton"));
            return $cloned;
        },

        removeAttributeTypeTemplate: function () {
            console.log("-- inside $removeAttributeTypeTemplate function --");
            $(this).parents(".attributeTypeTemplate").remove();
        },

        addAttributeTemplate: function () {
            console.log("-- inside $addAttributeTemplate function --");
            var $cloned = $('#attributeTypeTemplate').clone();
            $("#attributeTemplate", $cloned).addClass("attributeTemplate");
            $("#attributeTemplate", $cloned).removeAttr("id");
            $(".attributeTemplate", $cloned).insertAfter($(this).parents(".attributeTemplate"));
            $(".removeAttributeButton", $(this).parents(".attributeTypeTemplate")).prop("disabled", false);
            console.log($cloned);
            return $cloned;
        },

        editAttributeTemplate: function (itemAttributeTypeTemplate) {
            console.log("-- inside editAttributeTemplate function --");

            //If #attributeTemplate is cloned instead of #attributeTypeTemplate, then
            //$("#attributeTemplate", $cloned).addClass("attributeTemplate");
            //$("#attributeTemplate", $cloned).removeAttr("id");
            //Above two lines won't work
            var $cloned = $('#attributeTypeTemplate').clone();

            $("#attributeTemplate", $cloned).addClass("attributeTemplate");
            $("#attributeTemplate", $cloned).removeAttr("id");

            $(".attributeTemplate", $cloned).insertAfter($(".attributeTemplate:last-child"));
            $(".removeAttributeButton", $(".attributeTemplate .addAttributeButton", itemAttributeTypeTemplate).parents(".attributeTypeTemplate")).prop("disabled", false);

            var addedAttributeTemplate = $(".attributeTemplate").get($(".attributeTemplate").length - 1);

            return $(addedAttributeTemplate);
        },

        removeAttributeTemplate: function () {
            console.log("-- inside $removeAttributeTemplate function --");

            if ($(this).parents(".attributeTypeTemplate").children().length < 4) {
                $(".removeAttributeButton", $(this).parents(".attributeTypeTemplate")).prop("disabled", true);
            }
            $(this).parents(".attributeTemplate").remove();
        },
        //Item Attribute functions

        getMainCategoryList: function () {
            var that = this;
            var url = '/anon/get_active_parent_categories';
            var callback = function (status, data) {
                var options = "";
                if (data.success) {
                    data.params.forEach(function (option, index) {
                        options += '<option value="'+option.id+'">'+option.name+'</option>';
                    });
                    that.ui.itemCategoryInput.html(options).selectpicker('refresh');
                } else {
                    Main.popDialog('Error', data.message);
                }
            };
            callback.loaderDiv = that.ui.categoryList;
            callback.requestType = 'GET';
            Main.request(url, {}, callback, {});
        },

        editMode: function (itemId) {
            var that = this;
            var url = '/anon/get_items_detail';
            var header = {};
            header.id = itemId;
            var callback = function (status, data) {
                if (data.success) {

                    var item = data.params;
                    that.ui.itemImageOutput.attr('src', item.url);
                    that.ui.itemId.val(item.id);
                    that.ui.itemNameInput.val(item.name);
                    that.ui.descriptionInput.val(item.description);
                    that.ui.itemOverviewInput.val(item.overview);

                    that.ui.itemCategoryInput.val(item.category.id).selectpicker("refresh");

                    that.ui.itemAvailableStartTimeInput.val(item.availableStartTime);
                    $(that.ui.itemAvailableStartTimeInput).selectpicker('refresh');
                    that.ui.itemAvailableEndTimeInput.val(item.availableEndTime);
                    $(that.ui.itemAvailableEndTimeInput).selectpicker('refresh');

                    that.ui.itemUnitPriceInput.val(item.unitPrice);
                    that.ui.minPersonInput.val(item.minPerson);

                    //Populating itemAttributeTypes
                    var items = data.params.items;
                    for (var i = 0; i < items.length; i++) {
                        var item = items[i];
                        var itemTemplate = that.addAttributeTypeTemplate();
                        $(".attributeName", itemTemplate).val(item.name);
                        itemTemplate.attr("data-itemId", item.id);
                        $(".attributeTemplate .attributeQuantity", itemTemplate).val(item.quantity);
                        $(".attributeTemplate .attributeDescription", itemTemplate).val(item.description);
                    }
                    that.ui.addItemButton.text('Update Item');

                } else {
                    Main.popDialog('Error', data.message, null, 'error');
                }
            };
            callback.requestType = 'GET';

            Main.request(url, {}, callback, header);
        },

        add: function (data) {
            Main.log("-- Item add function started. --");

            var that = this;
            var url = '';
            var header = '';

            var brandId = "", itemId = "";
            if(Main.getURLvalue(2)!=undefined){
                brandId = Main.getURLvalue(2);
            } else{
                brandId = Main.getFromLocalStorage("merchantId");
            }

            if(Main.getURLvalue(3)!=undefined){
                brandId = Main.getURLvalue(3);
            } else{
                brandId = Main.getFromLocalStorage("brandId");
            }
            if (that.ui.itemFormState === 'edit') {
                url = '/smanager/update_combo_item';
                header = {
                    itemId: Main.getURLvalue(3)
                };
            } else {
                url = '/smanager/save_combo_item';
                header = {
                    brandId: brandId
                };
            }

            form.ui.addItemButton.prop("disabled", true);

            var callback = function (status, data) {
                if (data.success) {
                    if (url == '/smanager/save_combo_item') {
                        var button1 = function () {
                            document.getElementById('itemImageOutput').src = "";
                            document.getElementById('itemForm').reset();
                            $('.selectpicker').selectpicker('refresh');
                        };
                        var button2 = function () {
                            location.href = "/merchant/item_list/" + Main.getFromLocalStorage('brandId');
                        };
                        button1.text = "Ok";
                        button2.text = "Proceed";
                        var buttons = [button1, button2];
                        Main.popDialog("Done!", data.message, buttons, 'success');
                    } else if (url == '/smanager/update_combo_item') {
                        var button1 = function () {
                            location.href = "/merchant/item_list/" + Main.getFromLocalStorage('brandId');
                        };
                        button1.text = "Ok";
                        var buttons = [button1];
                        Main.popDialog("Done!", data.message, buttons, 'success');
                    }
                    location.href = "/merchant/item_list/" + Main.getFromLocalStorage('brandId');
                } else {
                    Main.popDialog('', data.message);
                }
                form.ui.addItemButton.prop("disabled", false);
            };
            //callback.async = false;
            console.log(data);
            Main.request(url, data, callback, header);

            Main.log("-- Item add function end. --");
        },
        // Basic jQuery validate fn
        // If validates then login fn is called
        validation: function () {

            var that = this;

            Main.log("-- validation started. ---");

            form.ui.itemForm.validate({
                rules: {
                    itemName: {
                        required: true
                    },
                    itemDescription: {
                        required: true
                    },
                    itemCategory: {
                        required: true
                    },
                    itemStartTime: {
                        required: true
                    },
                    itemEndTime: {
                        required: true
                    },
                    itemUnitPrice: {
                        required: true,
                        digits: true,
                        min: 1
                    },
                    minPerson:{
                        required: true
                    },
                    itemImageInput: {
                        required: true
                    }
                },
                submitHandler: function () {
                    Main.log("Submit handler.");
                    var item = {};
                    var data = {
                        id: form.ui.itemId.val() != "" ? form.ui.itemId.val() : null,
                        name:  form.ui.itemNameInput.val(),
                        overview: form.ui.itemOverviewInput.val(),
                        description: form.ui.descriptionInput.val(),
                        imageUrl: $('#itemImageContainer img').attr('src'),

                        categoryId: that.ui.itemCategoryInput.val(),

                        availableStartTime: moment(form.ui.itemAvailableStartTimeInput.val(), "h:mm a").format("hh:mm:ss"),
                        availableEndTime: moment(form.ui.itemAvailableEndTimeInput.val(), "h:mm a").format("hh:mm:ss"),

                        unitPrice: form.ui.itemUnitPriceInput.val(),
                        minPerson: form.ui.minPersonInput.val(),

                        items: form.getItemAttributeValues()
                    };
                    console.log(item);
                    form.add(data);
                    return false;
                }
            });

            Main.log("-- validation end. ---");
            return false;
        },

        getItemAttributeValues: function () {
            var itemAttributesTypes = [];
            $(".attributeTypeTemplate").each(function () {
                var $itemAttribute = {};
                var itemsAttribute = [];
                var attributeTypeId = $(this).attr("data-itemId");
                if (attributeTypeId) {
                    $itemAttribute.id = attributeTypeId;
                }
                if($(".attributeTypeInput", $(this)).val()!="") {
                    $itemAttribute.name = $(".attributeName", $(this)).val();
                    $itemAttribute.description = $(".attributeDescription", $(this)).val();
                    $itemAttribute.quantity = $(".attributeQuantity", $(this)).val();
                    itemAttributesTypes.push($itemAttribute);
                }
            });
            return itemAttributesTypes;
        }
    };

    var list = {
        init: function () {
            this.events();
            var merchantId = "";
            if(Main.getURLvalue(2)==undefined){
                merchantId = Main.getFromLocalStorage("brandId");
            } else{
                merchantId = Main.getURLvalue(2);
            }
            this.getItemList(merchantId);
            $('#addItemBtn').attr("src", "/merchant/item_form/add/'"+merchantId);
        },
        ui: {
            itemListContainer: $('#itemList'),
            currency: (Main.getFromLocalStorage('currency') !== 'null') ? Main.getFromLocalStorage('currency') : '',
            url: '',
        },
        events: function () {
            var that = $(this);
            $('#addItemBtn').on('click', function (e) {
               Main.saveInLocalStorage("brandId", Main.getURLvalue(2));
            });
        },
        doList: function (data) {
            var that = this;
            that.ui.itemListContainer.html('');
            for (var cat in data) {
                that.template(data[cat]);
            }
        },
        template: function (data) {
            var header = '',
                container = '',
                body = '',
                item = '',
                items = '',
                itemData = '',
                itemImage = '',
                i = 0,
                that = this;

            this.ui.url = window.location.pathname.substr(0, 11) + '/store/' + parseInt($('#storeBrandList option:selected').val()) + '/item';
            if (data) {
                var items = '';
                var item = '';
                header = '<div class="card">' +
                    '<div class="card-header"><h2>' +
                    data.name +
                    '</h2></div>';
                var comboItems= data.comboItems;
                if(comboItems.length == 0){
                    items += '<div class="card-body">No item Avaliable</div>';
                }else {
                    console.log(comboItems)
                    item += '<div class="card-body">' +
                              '<div class="row">';
                    $.each(comboItems, function (index, comboItem) {
                        console.log(comboItem);
                        item += '<div class="col-md-3">' +
                            '<div class="product-box">' +
                            '<img src="'+comboItem.imageUrl+'" alt="'+comboItem.name+'" class="img-fluid">' +
                            '<div class="product-text">' +
                            '<h5 class="title">'+comboItem.name+'</h5>' +
                            '<p class="description">'+comboItem.description.substr(0, 20)+'</p>' +
                            '<a class="btn btn-primary" href="/merchant/item_form/edit/'+comboItem.id+'">Edit</a>' +
                            '</div>' +
                            '</div>' +
                            '</div>';
                    });
                    item += '</div>' +
                             '</div>';
                }
                items += item;
            }
            container +=  header + items;
            container += '</div>';
            that.ui.itemListContainer.append(container);
        },
        getItemList: function (storeId) {
            var itemCount = 4;
            var url = '/merchant/get_store_category_item_list';
            var params = {};

            var callback = function (status, data) {
                if (data.success) {
                    var categories = data.params;
                    if (categories.length > 0) {
                        list.doList(categories);
                    } else {
                        list.ui.itemListContainer.html("no item found");
                    }
                } else {
                    Main.popDialog('Error', data.message);
                }
            };
            callback.requestType = "GET";
            Main.request(url, {}, callback, {brandId: Main.getURLvalue(2)});
        }
    };

    var view = {
        init: function (id) {
            if (id) {
                this.ui.itemId = id;
            } else {
                this.ui.itemId = (isNaN(parseInt(Main.getURLvalue(5))) ) ? history.back() : Main.getURLvalue(5);
            }
            var path = window.location.pathname;
            $('#editItemBtn').attr('href', path + '/edit');
            this.getDetails();
        },
        ui: {
            itemId: null,
            itemImages: $('#itemImages'),
            itemName: $('#itemName'),
            itemPrice: $('#itemPrice'),
            itemDescription: $('#itemDescription'),
            itemOrderQty: $('#itemOrderQty'),
            itemAvailability: $('#itemAvailability'),
            itemCategory: $('#itemCategory'),
            itemStrictPref: $('#itemStrictPref'),
            itemOtherPref: $('#itemOtherPref'),
            itemServiceCharge: $('#itemServiceCharge'),
            itemCommission: $('#itemCommission'),
            storeLocations: $('#storeLocations'),
            itemPromoCode: $('#itemPromoCode'),
            itemPromoCodeDetail: $('#itemPromoCodeDetail'),
            itemAttributeDetail: $('#itemAttributeContainer'),
            itemSimilarContainer: $('#itemSimilar'),
            currency: (Main.getFromLocalStorage('currency') !== 'null') ? Main.getFromLocalStorage('currency') : ''
        },
        template: function (data) {
            // var currency = (Main.getFromLocalStorage('currency') !== 'null') ? Main.getFromLocalStorage('currency') : '';
            var name = data.name || '';
            var price = this.ui.currency + ' ' + data.unitPrice || 'N/A';
            var desc = data.description || 'N/A';
            var maxQty = data.maxOrderQuantity || null;
            var minQty = data.minOrderQuantity || null;
            var orderQty = (maxQty !== minQty) ? minQty + ' to ' + maxQty : maxQty;
            var startTime = data.availableStartTime || null;
            var endTime = data.availableEndTime || null;
            var availableTime = (startTime && endTime) ? startTime + ' - ' + endTime : '';
            var strictPref = data.strictItemPreference || 'N/A';
            var otherPref = data.otherItemPreference || 'N/A';
            var serviceCharge = this.ui.currency + ' ' + data.serviceCharge;
            var commission = data.commissionPercentage + ' %';
            var locations = '',
                category = '',
                promoCode = '',
                promoCodeEdit = '',
                promoCodeDetail = '',
                itemAttributes = '',
                image = '',
                i = 0,
                len = 0;

            var getCat = function (cat) {
                category += cat.name;
                if (cat.child.length) {
                    for (i = 0, len = cat.child.length; i < len; i++) {
                        category += ', ';
                        getCat(cat.child[i]);
                    }
                }
            };

            if (data.category) {
                getCat(data.category);
            } else {
                console.log('No Cat');
            }

            if (data.itemsImage.length) {
                var images = data.itemsImage;
                for (i = 0, len = images.length; i < len; i++) {
                    image += '<div class="col-md-3">';
                    image += '<img src="' + images[i].url + '" alt="' + name + '" class="img-responsive img-thumbnail" width="200px" height="200px">';
                    image += "</div>";
                }
            }

            if (data.itemsStores) {
                for (i = 0, len = data.itemsStores.length; i < len; i++) {
                    var store = data.itemsStores[i].store;
                    locations += '<li> <div class="table-cell"> <span class="counter-circle">' + (i + 1) + '</span> </div> <div class="table-cell"> <span>' + store.street + ', ' + store.city + ', ' + store.state + ', ' + store.country + '</span> </div> </li>';
                }
            }

            if (data.promoCodes.length) {
                var promo = data.promoCodes[0];
                promoCode = promo.promoCode;
                var date = new Date(Main.sanitizeDateTime(promo.validTill));
                date = date.toString();
                var vdate = date.substr(0, 16);
                promoCodeEdit = '<button type="button" id="editPromoCodeBtn" class="btn btn-default promo-code-edit" data-toggle="modal" data-target="#promoModal" data-pid="' + promo.id + '"  data-type="item" data-element="' + name + '" data-date=' + promo.validTill + ' data-discount=' + promo.discountPcnt + ' data-count="' + promo.validCount + '" data-promo="' + promoCode + '" data-action="edit"> <i class="kravicon krav-edit"> </button>';
                promoCodeDetail =
                    '<li class="t-right"> <span class="pull-left"> Discount </span> <span class="c-t-light"> ' + promo.discountPcnt + ' % </span> </li>' +
                    '<li class="t-right"> <span class="pull-left"> Validity </span> <span class="c-t-light"> ' + vdate + ' </span> </li>' +
                    '<li class="t-right"> <span class="pull-left"> Count </span> <span class="c-t-light"> ' + promo.currentCount + ' / ' + promo.validCount + ' </span> </li>';
            } else {
                promoCodeDetail = '<li> <button type="button" class="btn btn-outline c-b-pure c-bo-emphasis c-t-emphasis t-uppercase center-h-b" data-toggle="modal" data-target="#promoModal" data-id="' + data.id + '"  data-type="item" data-element="' + name + '" data-action="add"> Add Promo </li>';
            }

            if (data.attributesTypes.length) {
                $.each(data.attributesTypes, function (index, attrType) {
                    itemAttributes += '<dl class="attribute-type list-unstyled">';
                    itemAttributes += '<dt class="c-b-emphasis c-t-pure subtitle">' + attrType.type + '</dt>';
                    if (attrType.itemsAttribute.length) {
                        $.each(attrType.itemsAttribute, function (attrIndex, attrItem) {
                            itemAttributes += '<dd class="clearfix c-b-shade"> <span class="pull-left"> <i class="glyphicon';
                            if (attrType.multiSelect) {
                                itemAttributes += ' glyphicon-check"> </i>';
                            } else {
                                itemAttributes += ' glyphicon-record"> </i>';
                            }
                            itemAttributes += attrItem.attribute + ' </span> <span class="pull-right">' + Main.getFromLocalStorage('currency') + " " + attrItem.unitPrice + '</span> </dd>';
                        });
                    } else {
                        itemAttributes += '<dd> No attributes on this type </dd>';
                    }
                    itemAttributes += '</dl>';
                });

            } else {
                itemAttributes = "No attributes selected";
            }

            this.ui.itemImages.html(image);
            this.ui.itemName.html(name);
            this.ui.itemPrice.html(price);
            this.ui.itemDescription.html(desc);
            this.ui.itemOrderQty.html(orderQty);
            this.ui.itemAvailability.html(availableTime);
            this.ui.itemCategory.html(category);
            this.ui.itemStrictPref.html(strictPref);
            this.ui.itemOtherPref.html(otherPref);
            this.ui.itemServiceCharge.html(serviceCharge);
            this.ui.itemCommission.html(commission);
            this.ui.storeLocations.html(locations);
            this.ui.itemPromoCode.html(promoCode);
            this.ui.itemPromoCode.closest('.promo-header').append(promoCodeEdit);
            this.ui.itemPromoCodeDetail.html(promoCodeDetail);
            this.ui.itemAttributeDetail.html(itemAttributes);

        },
        getDetails: function () {
            var that = this;
            var url = '/merchant/get_items_detail';
            var header = {};
            header.id = Main.getURLvalue(3);

            var callback = function (status, data) {
                if (data.success) {

                    var item = data.params.item;
                    var params = {};
                    params.storeId = item.id;
                    params.catId = item.category.id;

                    document.title = item.name + ' | Krave';
                    that.template(item);
                    that.getCatItems(params);
                } else {
                    Main.popDialog('Error', data.message, null, 'error');
                }
            };
            callback.requestType = 'GET';
            Main.request(url, {}, callback, {id: Main.getURLvalue(3)});
        },

        buildSimilar: function (data) {
            if (data.length) {

                var itemGrid = '',
                    item = '',
                    itemData = {},
                    itemImage = '',
                    url = window.location.pathname.substr(0, 24);

                itemGrid = '<div class="grid">';

                for (var i = 0; i < data.length; i++) {
                    itemData = data.item[i];
                    itemImage = (itemData.itemsImage.length) ? itemData.itemsImage[0].url : "#";
                    item = '';
                    item = '<div class="grid-cell">' +
                        '<div class="item">' +
                        '<div class="item-head">' +
                        '<img src="' + itemImage + '" alt="' + itemData.name + '" class="img-responsive" width="200px" height="200px">' +
                        '</div>' +
                        '<div class="item-body">' +
                        '<a href="' + url + '/' + itemData.id + '" class="item-link"> <strong> ' + itemData.name + ' </strong> </a>' +
                        '<span> ' + currency + ' ' + itemData.unitPrice + ' </span>' +
                        '</div>' +
                        '</div>' +
                        '</div>';
                    itemGrid += item;
                }

                itemGrid += '</div>';

                this.ui.itemSimilarContainer.html(itemGrid);

            } else {
                this.ui.itemSimilarContainer.html('No similar items available.');
            }

        },

        getCatItems: function (params) {
            var that = this;
            var url = '/merchant/get_stores_category_item';
            var headers = {};
            headers.storeBrandId = params.storeId;
            headers.parentCategoryId = params.catId;
            var callback = function (status, data) {
                if (data.success) {
                    console.log('Successful to fetch similar items');
                    var params = data.params.itemList;
                    that.buildSimilar(params);
                } else {
                    console.log('Failed to fetch similar items.');
                }
            };
            Main.request(url, {}, callback, headers);
        },
    };

    return {
        add: function () {
            form.init();
        },
        list: function () {
            list.init();
        },
        view: function (id) {
            view.init(id);
        },
    };

})(jQuery);