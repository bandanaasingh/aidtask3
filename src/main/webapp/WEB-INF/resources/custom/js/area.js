/**
 * Created by Frank on 5/3/2018.
 */
var listAreaModule = (function () {
    "use strict";
    var GParentId = Main.getNamedParameter('parentId');
    var allAvailableAreas = {};
    var listArea = {
        ui: {
            rootList: $('#rootAreaList'),
            subList: $('#subAreaList'),
            rootAreaLink: $('.root-area-link'),
            addSubAreaBtn: $('#addSubAreaBtn'),
            deleteAreaLink: $('[data-action="delete"]'),
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


            /*$(document).on('click', this.ui.deleteAreaLink.selector, function (e) {
                e.stopPropagation();
                var areaId = $(this).attr('data-areaid');
                if (areaId != "undefined") {
                    that.deleteArea(areaId);
                }
            });*/

        },

        listRootAreas: function () {
            var rootList = "";
            $.each(allAvailableAreas, function (index, area) {
                rootList += '<li data-areaid="' + area.id + '"> ' +
                    '<a href="javascript:" class="root-area-link" id="' + area.id + '" >' + area.name + '</a> ' +
                    //'<a href="#" data-areaid="' + area.areaId + '" data-action="delete" class="pull-right edit"><i class="fa fa-remove"></i></a> ' +
                    '<a href="/organizer/area_form?type=edit&areaId=' + area.id + '" class="pull-right edit mr10"><i class="fa fa-edit"></i></a> ' +
                    '</li>';
            });

            this.ui.rootList.html(rootList);

            if (GParentId != null)
                $('a[id="' + GParentId + '"]').click();
        },

        listSubAreas: function (parentId) {
            //console.log(parentId);
            var subAreas = allAvailableAreas[parentId];
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

        getAvailableAreas: function () {
            var that = this;
            var url = "/smanager/get_area_list";
            var callback = function (status, data) {
                if (data.success) {
                    var areas = data.params;

                    $.each(areas, function (index, data) {
                        allAvailableAreas[data.id] = data;
                    });

                    that.listRootAreas();


                } else {
                    Main.popDialog("Error", data.message, null, 'error');
                }
            };
            callback.requestType = "GET";
            Main.request(url, {}, callback, {});
        },

        deleteArea: function (areaId) {
            var that = this;
            var btnCancel = function () {
                $('#popDialog').modal('hide');
                return;
            };
            btnCancel.text = "Cancel";

            var btnConfirm = function () {

                var url = "organizer/delete_area";
                var callback = function (status, data) {
                    if (data.success) {
                        var button1 = function () {
                            window.location.reload();
                        };
                        button1.text = "Close";
                        var button = [button1];
                        Main.popDialog("Success", data.message, button, 'success', true);


                    } else {
                        Main.popDialog("Error", data.message, null, 'error');
                    }
                };
                callback.requestType = "GET";
                Main.request(url, {}, callback, {id: areaId});

                $('#popDialog').modal('hide');
                return;
            };
            btnConfirm.text = "Yes";

            var button = [btnConfirm, btnCancel];

            Main.popDialog("Confirm", "Are you sure you want to delete this area?", button, null, null);
        }
    };


    return {
        init: function () {
            listArea.init();
        },

        deleteArea: function (areaId) {
            //listArea.deleteArea(areaId);
        }
    }
})();

var addEditAreaModule = (function () {
    "use strict"
    var allAvailableAreas = {};
    var GType = Main.getNamedParameter('type'); // = addRoot, addSub, editRoot, editSub
    var GParentId = Main.getNamedParameter('parentId');
    var GAreaId = Main.getNamedParameter('areaId');

    var addEditArea = {
        ui: {
            rootList: $('#rootAreaList'),
            subList: $('#subAreaList'),
            rootAreaLink: $('.root-area-link'),
            addSubAreaBtn: $('#addSubAreaBtn'),

            formTitle: $('.form-title'),
            areaForm: $('#areaForm'),
            areaName: $("#areaName"),
            areaStatus: $("#areaStatus"),
            latitude: $("#latitude"),
            longitude: $("#longitude"),

            mapsWrapper: $('#googleMapsWrapper'),

            deleteAreaLink: $('[data-action="delete"]'),
        },

        init: function () {
            this.events();
            this.getAvailableAreas();
            this.validation();
            this.googleMapsChange('hide');
        },

        events: function () {
            var that = this;
            this.ui.rootList.on('click', this.ui.rootAreaLink, function (e) {
                that.ui.rootList.find('li').removeClass('active');
                that.ui.rootList.find('li[data-areaid="' + e.target.id + '"]').addClass('active');
                that.listSubAreas(e.target.id);
            });

            $(document).on('click', this.ui.deleteAreaLink.selector, function (e) {
                e.stopPropagation();
                var areaId = $(this).attr('data-areaid');
                if (areaId != "undefined") {
                    //listAreaModule.deleteArea(areaId);
                }
            });
        },

        listRootAreas: function () {
            var rootList = "";
            $.each(allAvailableAreas, function (index, area) {
                rootList += '<li data-areaid="' + area.id + '"> ' +
                    '<a href="/organizer/area_list?parentId=' + area.id + '" class="root-area-link" id="' + area.id + '" >' + area.name + '</a> ' +
                    //'<a href="#" data-areaid="' + area.id + '" data-action="delete" class="pull-right edit"><i class="fa fa-remove"></i></a> ' +
                    '<a href="/organizer/area_form?type=edit&areaId=' + area.id + '" class="pull-right edit mr10"><i class="fa fa-edit"></i></a> ' +
                    '</li>';
            });

            this.ui.rootList.html(rootList);

            this.ui.rootList.find('li[data-areaid="' + GAreaId + '"]').addClass('active');

            if (GType == "addSub") {
                this.ui.rootList.find('li[data-areaid="' + GParentId + '"]').addClass('active');
                this.ui.formTitle.text("Add Sub Area");
                this.googleMapsChange('show');
            }
        },

        googleMapsChange: function (option) {
            if (option == "show") {
                this.ui.mapsWrapper.removeClass('hide');
            } else if (option == "hide") {
                this.ui.mapsWrapper.addClass('hide');
            }

            setTimeout(function () {
                google.maps.event.trigger(map, 'resize')
            }, 200);

        },


        getAvailableAreas: function () {
            var that = this;
            var url = "/smanager/get_area_list";
            var callback = function (status, data) {
                if (data.success) {
                    //console.log(data);
                    var areas = data.params;

                    $.each(areas, function (index, data) {
                        allAvailableAreas[data.id] = data;
                    });

                    that.listRootAreas();

                    if (GType == "edit") {
                        that.getAreaDetails(GAreaId);
                        that.ui.formTitle.text("Edit Area Detail");
                        //console.log(that.ui.areaStatus.parent());
                        that.ui.areaStatus.parent().removeClass('hide');
                    }


                } else {
                    Main.popDialog("Error", data.message, null, 'error');
                }
            };
            callback.requestType = "GET";
            Main.request(url, {}, callback, {});
        },


        getAreaDetails: function (areaId) {
            var that = this;
            var url = "/smanager/get_area";
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
            Main.request(url, {}, callback, {areaId: Main.globalReplace(areaId, '#', '')});
        },


        setDataToFormFields: function (curAreaData) {
            //var curAreaData = allAvailableAreas[GAreaId];
            var that = this;
            that.ui.areaName.val(curAreaData.name).focus().blur();
            that.ui.areaStatus.val(curAreaData.status).focus().blur();


            if (curAreaData.latitude != "" && curAreaData.latitude != "0" && typeof(curAreaData.latitude) !== "undefined") {
                that.ui.latitude.val(curAreaData.latitude);
                that.ui.longitude.val(curAreaData.longitude);

                that.googleMapsChange('show');
                var locations = [{lat: Number(curAreaData.latitude), lng: Number(curAreaData.longitude)}];

                mapsModule.addMarkers(locations);

            } else {
                that.googleMapsChange('hide');
            }
        },

        validation: function () {
            //console.log('validation');
            var that = this;
            this.ui.areaForm.submit(function (e) {
                e.preventDefault();
            }).validate({
                rules: {
                    areaName: {
                        required: true
                    },
                    areaStatus: {
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
                        "name": that.ui.areaName.val(),
                        "latitude": that.ui.latitude.val(),
                        "longitude": that.ui.longitude.val(),
                        "status": that.ui.areaStatus.val(),
                        "parentId":  GParentId
                    };

                    //console.log(that.ui.mapsWrapper.hasClass('hide'));
                    if (!that.ui.mapsWrapper.hasClass('hide')) {
                        if (data.latitude == "" || data.longitude == "") {
                            Main.popDialog('Error', "Please place marker on map.", null, 'error');
                            return;
                        }
                    }


                    if (GType != "edit") {
                        Main.request('/organizer/save_area', data, callback);
                    } else {
                        Main.request('/organizer/update_area', data, callback, {id: GAreaId});
                    }

                }
            });

        },
    };


    return {
        init: function () {
            addEditArea.init();
        }
    }
})();