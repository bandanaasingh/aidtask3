/**
 * Created by Frank on 5/3/2018.
 */
/*var listCategoryModule = (function () {
    "use strict";
    var GParentId = Main.getNamedParameter('parentId');
    var allAvailableCategories = {};
    var listCategory = {
        ui: {
            rootList: $('#rootAreaList'),
            subList: $('#subAreaList'),
            rootAreaLink: $('.root-area-link'),
            addSubAreaBtn: $('#addSubAreaBtn')
        },

        init: function () {
            this.events();
            this.getAvailableAreas();
        },

        events: function () {
            var that = this;

            this.ui.rootList.on('click', this.ui.rootAreaLink.selector, function (e) {
                that.ui.rootList.find('li').removeClass('active');
                that.ui.rootList.find('li[data-areaid="' + e.target.id + '"]').addClass('active');
                that.listSubAreas(e.target.id);
            });

        },

        listRootCategories: function () {
            var rootList = "";
            $.each(allAvailableCategories, function (index, category) {
                rootList += '<li data-categoryid="' + category.id + '" data-type="edit"> ' +
                    '<span href="javascript:" class="root-area-link" id="' + category.id + '" >' + category.name + '</span> ' +
                    '<span class="pull-right edit mr10"><i class="fa fa-edit"></i></span> ' +
                    '</li>';
            });

            this.ui.rootList.html(rootList);

            if (GParentId != null)
                $('a[id="' + GParentId + '"]').click();
        },

        listSubAreas: function (parentId) {
            //console.log(parentId);
            var subAreas = allAvailableCategories[parentId];
            if (typeof(subAreas) == "undefined")
                return;

            var subList = "";
            if (typeof (subAreas.child) != "undefined") {
                if (subAreas.child.length > 0) {
                    $.each(subAreas.child, function (index, area) {
                        subList += '<li class=""> ' +
                            '<a href="javascript:" class="sub-area-link" id="' + area.id + '" >' + area.name + '</a> ' +
                            //'<a href="#" data-areaid="' + area.areaId + '" data-action="delete" class="pull-right edit"><i class="fa fa-remove"></i></a> ' +
                            '<a href="/organizer/area_form?type=edit&areaId=' + area.id + '" class="pull-right edit mr10"><i class="fa fa-edit"></i></a> ' +
                            '</li>';
                    });

                } else {
                    subList += "<li>No sub areas available.</li>";
                }
                this.ui.subList.html(subList);
                this.ui.addSubAreaBtn.removeClass('hide').attr('href', '/organizer/area_form?type=addSub&parentId=' + parentId)
            }
        },

        getCategories: function () {
            var that = this;
            var url = "/smanager/get_area_list";
            var callback = function (status, data) {
                if (data.success) {
                    var areas = data.params;
                    $.each(areas, function (index, data) {
                        allAvailableCategories[data.id] = data;
                    });
                    that.listRootCategories();
                } else {
                    Main.popDialog("Error", data.message, null, 'error');
                }
            };
            callback.requestType = "GET";
            Main.request(url, {}, callback, {});
        }
    };


    return {
        init: function () {
            listCategory.init();
        },
    }
})();*/

var addEditCategoryModule = (function () {
    "use strict"
    var allAvailableCategories = {};
    var GType = "Add";
    var GParentId = Main.getNamedParameter('parentId');
    var GCategoryId = "";

    var addEditCategory = {
        ui: {
            rootList: $('#rootCategoryList'),
            rootAreaLink: $('.root-area-link'),

            formTitle: $('.form-title'),
            addCategory: $('#addCategory'),
            categoryForm: $('#categoryForm'),
            categoryId: $("#categoryId"),
            categoryName: $("#categoryName"),
            categoryStatus: $("#categoryStatus"),
            categoryList: $("#category-list"),
            submitButton: $("#submitButton"),
            catStatus: $(".cat-status"),
        },


        init: function () {
            this.events();
            this.getCategories();
            this.validation();
        },

        events: function () {
            var that = this;
            $('#addCategory').on('click', function (e) {
                that.ui.rootList.find('li').removeClass('active');
                $('#categoryForm')[0].reset();
                that.ui.formTitle.html("Add Category");
                that.ui.submitButton.html("Save");
                GType = "Add";
                that.ui.catStatus.addClass('hide');
            });

            $('#rootCategoryList').on('click', "li", function (e) {
                var id = $(this).data("id");
                GCategoryId = id;
                GType = "Edit";

                that.ui.categoryId.val(id);
                that.ui.categoryName.val($(this).data("name"));
                that.ui.categoryStatus.val($(this).data("status"));
                that.ui.formTitle.html("Edit Category");
                that.ui.submitButton.html("Update");
                that.ui.rootList.find('li').removeClass('active');
                $(this).addClass('active');
                that.ui.catStatus.removeClass('hide');
            });


        },

        listRootCategories: function () {
            var rootList = "";
            $.each(allAvailableCategories, function (index, category) {
                rootList += '<li data-id="' + category.id + '" data-name="' + category.name + '" data-status="' + category.status + '" class="category-list"> ' +
                    '<span class="root-area-link">' + category.name + '</span> ' +
                    '<span class="pull-right edit mr10"><i class="fa fa-edit"></i></span> ' +
                    '</li>';
            });

            this.ui.rootList.html(rootList);
        },

        getCategories: function () {
            var that = this;
            var url = "/merchant/get_parent_categories";
            var callback = function (status, data) {
                if (data.success) {
                    //console.log(data);
                    var areas = data.params;

                    $.each(areas, function (index, data) {
                        allAvailableCategories[data.id] = data;
                    });

                    that.listRootCategories();
                } else {
                    Main.popDialog("Error", data.message, null, 'error');
                }
            };
            callback.requestType = "GET";
            Main.request(url, {}, callback, {});
        },


        getCategoryDetails: function (categoryId) {
            var that = this;
            var url = "/smanager/get_category";
            var callback = function (status, data) {
                if (data.success) {
                    that.setDataToFormFields(data.params);
                    if (typeof(data.params.parent) !== "undefined") {
                        that.ui.rootList.find('li[data-areaid="' + data.params.id + '"]').addClass('active');
                        that.googleMapsChange('show');
                    }
                } else {
                    Main.popDialog("Error", data.message, null, 'error');
                }
            };
            callback.requestType = "GET";
            Main.request(url, {}, callback, {categoryId: Main.globalReplace(categoryId, '#', '')});
        },


        setDataToFormFields: function (curAreaData) {
            var that = this;
            that.ui.areaName.val(curAreaData.name).focus().blur();
            that.ui.areaStatus.val(curAreaData.status).focus().blur();
        },

        validation: function () {
            //console.log('validation');
            var that = this;
            this.ui.categoryForm.submit(function (e) {
                e.preventDefault();
            }).validate({
                rules: {
                    categoryName: {
                        required: true
                    },
                    categoryStatus: {
                        required: true,
                    },
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

                    var data = {
                        "name": that.ui.categoryName.val(),
                        "status": that.ui.categoryStatus.val(),
                    };

                    if (GType == "Add") {
                        Main.request('/organizer/save_category', data, callback);
                    } else {
                        Main.request('/organizer/update_category', data, callback, {id: GCategoryId});
                    }

                }
            });

        },
    };


    return {
        init: function () {
            addEditCategory.init();
        }
    }
})();