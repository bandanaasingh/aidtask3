/**
 * Created by Pratik on 7/22/2016.
 */

var Preferences = function () {
    return {
        secSettings: undefined,
        secId: undefined,
        prepSelect: function (prefTitle, prefKey, prefValue, options) {
            var elem = $('.form_select_template').clone();
            $('label', elem).attr('for', prefKey).html(prefTitle);
            $('.info_display', elem).html(options[prefValue]);
            $('select', elem).attr({
                id: prefKey,
                name: prefKey
            });
            for (var i in options) {
                $('select', elem).append('<option value="' + i + '" ' + (prefValue == i ? 'selected="selected"' : '') + '>' + options[i] + '</option>');
            }
            return elem;
        },
        loadSettings: function (secSettings) {
            var callback = function (status, data) {
                //console.log(data);
                Preferences.secSettings = data;
                var form_sections = '';
                var sections = data.params.section;
                for (var j = 0; j < sections.length; j++) {
                    var section = sections[j];
                    var sectionName = section.section;
                    var preferences = section.preference;
                    var form_fields = '';
                    if(preferences) {
                        if (preferences.length <= 0) continue;
                        for (var i = 0; i < preferences.length; i++) {
                            var pref = preferences[i];
                            var prefKey = pref.prefKey;
                            var prefValue = pref.value;
                            var prefTitle = pref.prefTitle;
                            var elem = "";
                            if (prefKey == 'SWITCH_DRIVER_ALGORITHM') {
                                elem = Preferences.prepSelect(prefTitle, prefKey, prefValue, {
                                    "ENABLE": 'Enable',
                                    "DISABLE": 'Disable'
                                });
                                elem.selectpicker('refresh');
                            } else if (prefKey == 'AIR_OR_ACTUAL_DISTANCE_SWITCH') {
                                elem = Preferences.prepSelect(prefTitle, prefKey, prefValue, {
                                    "AIR_DISTANCE": 'Air Distance',
                                    "ACTUAL_DISTANCE": 'Actual Distance'
                                });
                                elem.selectpicker('refresh');
                            } else if (prefKey == 'SMS_PROVIDER') {
                                // elem = Preferences.prepSelect(prefTitle, prefKey, prefValue, {
                                //     1: 'Sparrow SMS',
                                //     2: 'Twilio SMS'
                                // });
                                // elem.selectpicker('refresh');
                                elem = $('.form_field_template').clone();
                                $('label', elem).attr('for', prefKey).html(prefTitle);
                                $('.info_display', elem).html(prefValue);
                                $('input.form-control', elem).attr({
                                    id: prefKey,
                                    name: prefKey,
                                    value: prefValue,
                                }).prop({
                                    readonly: true
                                });
                            } else if (prefKey === "ASSIGN_DRIVER_AUTOMATICALLY") {
                                elem = Preferences.prepSelect(prefTitle, prefKey, prefValue, {
                                    0: 'Disable',
                                    1: 'Enable'
                                });
                                elem.selectpicker('refresh');
                            } else if (prefKey === "GOOGLE_TRAFFIC_MODEL") {
                                elem = Preferences.prepSelect(prefTitle, prefKey, prefValue, {
                                    "BEST_GUESS": 'Best Guess',
                                    "OPTIMISTIC": 'Optimistic',
                                    "PESSIMISTIC": 'Pessimistic'
                                });
                                //console.log(elem);
                                ///elem.selectpicker('refresh');
                            } else if (prefKey === "GOOGLE_TRAVEL_MODE") {
                                elem = Preferences.prepSelect(prefTitle, prefKey, prefValue, {
                                    "Car": 'Car',
                                    "Bike": 'Bike'
                                });
                                elem.selectpicker('refresh');
                            } else if (prefKey == 'COMPANY_LOGO' || prefKey == 'DEFAULT_IMG_ITEM' || prefKey == 'DEFAULT_IMG_SEARCH' || prefKey == 'LOGO_FOR_PDF_EMAIL') {
                                elem = $('.image_template').clone();
                                var imgWidth = '';
                                var imgHeight = '';
                                if (prefKey == 'COMPANY_LOGO') {
                                    imgWidth = '120';
                                    imgHeight = '120';
                                } else if (prefKey == 'DEFAULT_IMG_ITEM') {
                                    imgWidth = '400';
                                    imgHeight = '400';
                                } else if (prefKey == 'DEFAULT_IMG_SEARCH') {
                                    imgWidth = '400';
                                    imgHeight = '400';
                                } else if (prefKey == 'LOGO_FOR_PDF_EMAIL') {
                                    imgWidth = '720';
                                    imgHeight = '185';
                                } else {
                                    imgWidth = '750';
                                    imgHeight = '500';
                                }
                                var imgSize = imgWidth + 'x' + imgHeight;
                                $('label', elem).attr('for', prefKey).html(prefTitle);
                                $('.image_container', elem).attr({
                                    'mr-width': imgWidth,
                                    'mr-height': imgHeight
                                });
                                $('.image_input', elem).attr({
                                    id: prefKey + '_input',
                                    name: prefKey + '_input',
                                    "data-height": imgHeight,
                                    "data-width": imgWidth
                                });
                                if (prefValue != "") {
                                    $('.image_container', elem).addClass('no_bg_img').html('<img id="' + prefKey + '" data-height="' + imgHeight + '" data-width="' + imgWidth + '" src="' + prefValue + '" style="height: 100%;" class="img-responsive" />');
                                } else {
                                    $('.drop_info .image_size', elem).html(imgSize);
                                }
                            } else {
                                elem = $('.form_field_template').clone();
                                $('label', elem).attr('for', prefKey).html(prefTitle);
                                $('.info_display', elem).html(prefValue);
                                $('input.form-control', elem).attr({
                                    id: prefKey,
                                    name: prefKey,
                                    value: prefValue
                                });
                            }
                            form_fields += elem.html();
                        }

                        var elem_section = $('.form_section_template').clone();
                        if (sectionName == "Default Image") $('.form_head .detail_options', elem_section).remove();
                        $('.form_head .section_title', elem_section).html(sectionName);
                        $('.form_content .row-ao', elem_section).html(form_fields);
                        form_sections += elem_section.html();
                    }
                }
                $('.display_settings').html(form_sections);
                if ($('.main_tabs li.active').index() == 0) {

                    imageCropperModule.init($('#COMPANY_LOGO_input'), $('#COMPANY_LOGO'));
                    imageCropperModule.init($('#DEFAULT_IMG_ITEM_input'), $('#DEFAULT_IMG_ITEM'));
                    imageCropperModule.init($('#DEFAULT_IMG_SEARCH_input'), $('#DEFAULT_IMG_SEARCH'));
                    imageCropperModule.init($('#LOGO_FOR_PDF_EMAIL_input'), $('#LOGO_FOR_PDF_EMAIL'));


                    //imageCropperModule.init()

                    $('#COMPANY_LOGO, #DEFAULT_IMG_ITEM, #DEFAULT_IMG_SEARCH, #LOGO_FOR_PDF_EMAIL').bind('submitPref', function () {
                        Preferences.uploadPrefImage({
                            prefKey: $(this).attr('id'),
                            imageString: $(this).attr('src')
                        });
                    });
                    //Main.elemRatio();
                }


            };
            if (typeof secSettings != 'object') {
                callback.requestType = "GET";
                callback.loaderDiv = "body";
                Preferences.secId = secSettings;
                Main.request('/admin/get_group_preferences', {}, callback, {
                    id: secSettings
                });
            } else {
                callback('', secSettings);
            }
        },
        loadEditSettings: function () {
            $('.form_content .selectpicker').on('change', function () {
                $(this).parent().siblings('.none_editable').html($('option:selected', this).html());
            });
            $('.main_content').on('click', '.edit_btn', function () {
                var parent = $(this).parents('.form_group').eq(0);
                $(".editable", parent).removeClass('hidden');
                $(".none_editable", parent).addClass('hidden');
            });
            $('.main_content').on('click', '.cancel_btn', function () {
                var parent = $(this).parents('.form_group').eq(0);
                $(".none_editable", parent).removeClass('hidden');
                $(".editable", parent).addClass('hidden');
                Preferences.loadSettings(Preferences.secSettings);
            });
            $('.main_content').on('click', '.save_btn', function () {
                var __this = $(this);
                var parent = __this.parents('.form_group').eq(0);

                if(parent[0] == $('#WEB_APP_VER_NO').parents('.form_group')[0]){
                    var app_version = parseFloat($('#WEB_APP_VER_NO').val()).toFixed(1);
                    $('#WEB_APP_VER_NO').val(app_version);
                }
                if(parent[0] == $('#ANDROID_APP_VER_NO').parents('.form_group')[0]){
                    var app_version = parseFloat($('#ANDROID_APP_VER_NO').val()).toFixed(1);
                    $('#ANDROID_APP_VER_NO').val(app_version);
                }
                if(parent[0] == $('#IOS_APP_VER_NO').parents('.form_group')[0]){
                    var app_version = parseFloat($('#IOS_APP_VER_NO').val()).toFixed(1);
                    $('#IOS_APP_VER_NO').val(app_version);
                }

                var button1 = function () {
                    var preferences = [];
                    $('input.form-control, select', parent).each(function () {
                        var data = {
                            prefKey: $(this).attr('id'),
                            value: $(this).val()
                        };
                        preferences.push(data);
                    });
                    Preferences.updateSettings({
                        preferences: preferences
                    }, parent);
                };

                button1.text = "Yes";
                var button2 = "No";
                var buttons = [button1, button2];
                Main.popDialog('', 'Are you sure you want to update Settings?', buttons);
            });
        },
        updateSettings: function (updatedData, parent) {
            var callback = function (status, data) {
               /* Main.popDialog('', data.message, function () {
                    if (data.success) {
                        $(".none_editable", parent).removeClass('hidden');
                        $(".editable", parent).addClass('hidden');
                        Preferences.loadSettings(Preferences.secId);
                    }
                });*/
                if (data.success) {
                    var button1 = function () {
                        $('#popDialog').modal('hide');
                        $(".none_editable", parent).removeClass('hidden');
                        $(".editable", parent).addClass('hidden');
                        Preferences.loadSettings(Preferences.secId);
                    };
                    button1.text = "Close";
                    var button = [button1];
                    Main.popDialog("Success", data.message, button, 'success', true);
                } else {
                    Main.popDialog('Error', data.message, null, 'error');
                }
            };
            callback.loaderDiv = "body";
            callback.requestType = "PUT";
            Main.request('/admin/update_preferences', updatedData, callback);
        },
        uploadPrefImage: function (imgData, img_container) {
            var callback = function (status, data) {
               /* Main.popDialog('', data.message, function () {
                    if (data.success) {
                        $(".none_editable", parent).removeClass('hidden');
                        $(".editable", parent).addClass('hidden');
                        Preferences.loadSettings(Preferences.secId);
                    }
                });*/

                if (data.success) {
                    var button1 = function () {
                        $('#popDialog').modal('hide');
                        $(".none_editable", parent).removeClass('hidden');
                        $(".editable", parent).addClass('hidden');
                        Preferences.loadSettings(Preferences.secId);
                    };
                    button1.text = "Close";
                    var button = [button1];
                    Main.popDialog("Success", data.message, button, 'success', true);
                } else {
                    Main.popDialog('Error', data.message, null, 'error');
                }


            };
            console.log();
            callback.loaderDiv = img_container;
            Main.request('/admin/update_default_images', imgData, callback);
        }
    };
}();

var preferencesModule = (function () {
    var preferences = {
        type: null,
        noSection: [],
        noSectionPreference: [],
        init: function (id) {
            this.type = id;
        },
        events: function () {},
        getTextField: function(id, key, label, value) {
            if (!(id && key && label)) {
                return;
            }
            var inputValue = value || "";
            var labelField = '<label for="'+key+'" class="col-md-4 floated_label">' + label + '</label>';
            var inputField = '<input type="text" class="form-control" id="'+key+'" name="'+key+'" value="'+inputValue+'">';
            var outputField = '<output for="'+key+'" name="'+key+'_output" class="form-control"> '+inputValue+' </output>';
            var formGroup = '<div class="form-group clearfix">';
                formGroup += labelField;
                formGroup += '<div class="col-md-8">';
                    formGroup += outputField;
                    formGroup += '<div class="info_edit editable hidden">';
                    formGroup += inputField;
                    formGroup += '</div>';
                formGroup += '</div>';
            formGroup += '</div>';
            return formGroup;
        },
        getSelectField: function (id, key, label, value, options) {
            if (!(id && key && label && options)) {
                return;
            }
            var inputValue = value || "";
            var labelField = '<label for="'+key+'" class="col-md-4"> '+label+' </label>';
            var outputField = '<output for="'+key+'" name="'+key+'_output" class="form-control"> '+inputValue+' </output>';
            var selectField = '<select class="col-md-12 p0 m0 selectpicker" data-style="form-control">';
                var optionField = "";
                $.each(options, function(index, val) {
                    optionField += '<option value="'+index+'" '+(val === value) ? "selected" : "" +'>'+val+'</option>';
                });
                selectField += optionField;
            selectField += '</select>';
            var formGroup = '<div class="form-group clearfix">';
                formGroup += labelField;
                formGroup += '<div class="col-lg-8">';
                    formGroup += outputField;
                    formGroup += '<div class="info_edit editable hidden">';
                        formGroup += selectField;
                    formGroup += '</div>';
                formGroup += "</div>";
            formGroup += '</div>';
            return formGroup;
        },
        getImageField: function (id, key, label, value) {
            if (!(id && key && label)) {
                return;
            }
            var labelField = '',
                imageContainer = '',
                buttonContainer = '',
                formGroup = '';

            labelField += '<label class="col-md-4">'+label+'</label>';

            buttonContainer += '<span class="btn btn-default btn-file">';
                buttonContainer += '<span class="fileinput-new">Select image</span>';
                buttonContainer += '<span class="fileinput-exists">Change</span>';
                buttonContainer += '<input type="file" id="imgDriver" name="imgDriver" data-upload="image" accept="image/*">';
            buttonContainer += '</span>';
            buttonContainer += '<a href="#" class="btn btn-default fileinput-exists" data-dismiss="fileinput">Remove</a>';

            imageContainer += '<div class="col-md-8">';
                imageContainer += '<div class="class="fileinput ' + (value) ? 'fileinput-exists' : 'fileinput-new';
                imageContainer += 'data-provides="fileinput">';
                    imageContainer += '<div class="center-me positionabsolute text-center"> <i class="fa fa-image fa-4x"></i> </div>';
                    imageContainer += '<div class="fileinput-preview fileinput-exists thumbnail" style="max-width: 200px; max-height: 150px;" id="driverImageContainer"></div>';
                    imageContainer += '<div>';
                        imageContainer += buttonContainer;
                    imageContainer += '</div>';
                imageContainer += '</div>';
            imageContainer += '</div>';

            formGroup += '<div class="form-group clearfix">';
                formGroup += labelField;
                formGroup += imageContainer;
            formGroup += '</div>';

            return formGroup;
        },
        getSectionPreferences: function (preferenceDetail) {
            if ($.isEmptyObject(preferenceDetail)) {
                return;
            }

            $.each(preferenceDetail, function(index, val) {
                if ($.inArray(val.prefKey, that.noSectionPreference) > 0) {

                }
            });

        },
        getSection: function (sectionDetails) {
            if ($.isEmptyObject(sectionDetails)) {
                return;
            }

            if (sectionDetails.preference && sectionDetails.preference.length) {
                return;
            }

            var sections = '',
                section = '',
                sectionHead = '',
                sectionOptions = '',
                sectionContent = '',
                that = this;

            sectionOptions += '<div class="detail_options pull-right">';
                sectionOptions += '<a class="p0 btn edit_btn none_editable glyphicon glyphicon-edit"></a>';
                sectionOptions += '<div class="action_buttons editable hidden">';
                    sectionOptions += '<a class="p0 btn save_btn glyphicon glyphicon-floppy-disk"></a>';
                    sectionOptions += '<a class="p0 btn cancel_btn glyphicon glyphicon-remove"></a>';
                sectionOptions += '</div>';
            sectionOptions += '</div>';

            $.each(sectionDetails, function(index, val) {
                if ($.inArray(val.section, that.noSection) > 0) {
                    section = "";
                    sectionHead += '<div class="form_head">';
                        sectionHead += '<span class="section_title">' + val.section + '</span>';
                        sectionHead += sectionOptions;
                    sectionHead += '</div>';

                    section += '<section class="form_group" data-id="'+val.id+'">';
                        section += sectionHead;
                        section += '<div class="form_content">';
                            $.each(val.preference, function(index, val) {
                                that.getSectionPreferences(val);
                            });
                        section += '</div>';
                    section += '</section>';
                    sections += section;
                }
            });

            return sections;
        },
        getPreferences: function (id) {
            var prefId = id || this.type || null;
            if (!prefId) {
                return;
            }
        }
    };
    return {
        init: function (prefId) {
            if (prefId && typeof prefId === 'number') {
                preferences.init(prefId);
            }
            return;
        }
    };
})();