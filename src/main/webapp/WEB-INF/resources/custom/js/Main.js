/**
 * Created by Frank on 11/25/2014.
 */

if (typeof(Main) == "undefined") {
    var Main = {};
    Main.pageContext = "/";
}

(function () {

    'use strict';
    var dialogCallback;
    var dialogTimeout;
    var dataTablesInterval;
    var formSubmit = true;

    $(window).bind('beforeunload', function () {
        if (!formSubmit) {
            return 'Your data will not be saved. Are you sure to continue?';
        }
    });

    $(document).on("click", "button.confirm-button", function () {
        var btnFn = window.dialogButtons[$(this).index()];
        if (typeof btnFn === "function") {
            btnFn();
        } else {
            $('#popDialog').modal("hide");
        }
    });

    (function ($) {

        Main.saveInSessionStorage = function (key, value) {
            sessionStorage.setItem(key, value);
        };

        Main.getFromSessionStorage = function (key) {
            return sessionStorage.getItem(key);
        };

        Main.clearSessionStorage = function (key) {
            if (key == undefined) {
                sessionStorage.clear();
            } else {
                sessionStorage.removeItem(key);
            }
        };

        Main.saveInLocalStorage = function (key, value) {
            localStorage.setItem(key, value);
        };

        Main.getFromLocalStorage = function (key) {
            return localStorage.getItem(key);
        };

        Main.clearLocalStorage = function (key) {
            if (key == undefined) {
                localStorage.clear();
            } else {
                localStorage.removeItem(key);
            }
        };

        Main.countable = function (string) {
            var regexAstralSymbols = /[\uD800-\uDBFF][\uDC00-\uDFFF]/g;
            return string.replace(regexAstralSymbols, '_').length;
        };

        Main.debugMode = true;

        Main.log = function (string, type) {
            if (Main.debugMode) {
                switch (type) {
                    case "warning":
                        console.warn(string);
                        break;
                    case "error":
                        console.error(string);
                        break;
                    case "info":
                        console.info(string);
                        break;
                    case "log":
                    default:
                        console.log(string);
                }
            }
        };

        Main.numberWithCommas = function (x) {
            var parts = x.toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            if (typeof parts[1] === "undefined" || parts[1] === null || parts[1] === "") {
                parts[1] = "00";
            } else if (parts[1].length === 1) {
                parts[1] += "0";
            }
            return parts.join(".");
        };

        Main.country = "United Arab Emirates";
        Main.countryId = "1";
        Main.noStripeCountry = ["Nepal", "United Arab Emirates"];
        Main.docHeight = window.innerHeight;
        Main.docWidth = window.innerWidth;
        Main.ajax = undefined;
        Main.ajaxFile = undefined;
        Main.userRole = Main.getFromLocalStorage('userRole');

        Main.modifyURL = function (url) {
            if (url.indexOf("#") !== 0) {
                if (url.indexOf("://") === -1) {
                    if (url.indexOf("/") !== 0) url = "/" + url;
                    if (Main.pageContext !== "" && Main.pageContext !== undefined) {
                        if (url.indexOf(Main.pageContext) > -1)
                            return url;
                        else
                            return Main.pageContext + url;
                    }

                }
                return url.replace('#', '');
            }
        };

        Main.checkURL = function () {
            $('a[href]').not('[href="#"], [href="javascript:;"]').each(function () {
                if ($(this).attr('href').indexOf(Main.pageContext) < 0)
                    $(this).attr('href', Main.modifyURL($(this).attr('href')));
            });
        };

        Main.addString = function (string, position, value) {
            return string.substr(0, position) + value + string.substr(position);
        }


        Main.request = function (url, parameter, callback, headers) {
            if (headers == undefined) var headers = {};

            if (parameter.stringify != false)
                headers['Content-Type'] = 'application/json';

            var hideLoader = function () {
            };

            if (callback != undefined) {
                var loaderDiv = callback.loaderDiv === undefined ? 'body' : callback["loaderDiv"];
                var requestType = callback["requestType"];
                if (loaderDiv != undefined && $('.loader', loaderDiv).length == 0) {
                    $(loaderDiv).addClass('loader_div clearfix').append('<div class="loader"></div>');
                    $('body').css('overflow', 'hidden');
                }

                hideLoader = function () {
                    if (loaderDiv != undefined) {
                        $(loaderDiv).removeClass('loader_div').children('.loader').hide().remove();
                        $('body').css('overflow', 'unset');
                    }
                };
            }

            Main.ajax = $.ajax({
                url: Main.modifyURL(url),
                type: requestType != undefined ? requestType : "POST",
                data: parameter.stringify == false ? parameter : JSON.stringify(parameter),
                headers: headers,
                async: callback != undefined && callback.async == false ? false : true,
                statusCode: {},
                beforeSend: function (xhr, plain) {
                    if (plain.url.includes('?{}')) {
                        plain.url = plain.url.replace('?{}', '');
                    }
                },
                success: function (data) {
                    if (typeof data === "string") {
                        return window.location.replace("/");
                    }
                    /*if(typeof data === "object")*/
                    if (callback != undefined)
                        return callback("success", data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    if (typeof xmlHttpRequest != "undefined" && (xmlHttpRequest.readyState == 0 || xmlHttpRequest.status == 0)) return;
                    if (callback != undefined && errorThrown != "abort") return callback("error", {
                        success: false,
                        message: XMLHttpRequest.getResponseHeader("errorMessage"),
                        code: XMLHttpRequest.getResponseHeader("errorCode")
                    });
                    setTimeout(hideLoader, 500);
                },
                complete: function (data) {
                    setTimeout(hideLoader, 500);
                }
            });
        };

        Main.requestFile = function (url, formData, callback, headers) {
            if (headers == undefined) var headers = {};

            var hideLoader = function () {
            };
            if (callback != undefined) {
                var loaderDiv = callback.async == false ? 'body' : callback["loaderDiv"];
                var requestType = callback["requestType"];
                if (loaderDiv != undefined && $('.loader', loaderDiv).length == 0) {
                    $(loaderDiv).addClass('loader_div').append('<div class="loader"></div>');
                }

                hideLoader = function () {
                    if (loaderDiv != undefined) {
                        $(loaderDiv).removeClass('loader_div').children('.loader').hide().remove();
                    }
                };
            }

            Main.ajaxFile = $.ajax({
                url: Main.modifyURL(url),
                type: requestType != undefined ? requestType : "POST",
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    if (callback != undefined) return callback("success", data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    console.log(XMLHttpRequest);
                    if (typeof xmlHttpRequest != "undefined" && (xmlHttpRequest.readyState == 0 || xmlHttpRequest.status == 0))
                        return;
                    if (callback != undefined && errorThrown != "abort")
                        return callback("error", {
                            success: false,
                            message: XMLHttpRequest.getResponseHeader("errorMessage"),
                            code: XMLHttpRequest.getResponseHeader("errorCode")
                        });
                },
                complete: function () {
                    setTimeout(hideLoader, 1000);
                }
            });
        };

        Main.doLogout = function (noredir) {
            window.localStorage.clear();
            var callback = function (status, data) {
                console.log("logout");
                //localStorage.clear();
                Main.saveInLocalStorage("logout", "true");
                if (!noredir) {
                    window.location = Main.modifyURL("/");
                } else {
                    window.location = noredir;
                }
            };
            callback.loaderDiv = "body";
            Main.request('/j_spring_security_logout', {}, callback);
        };

        Main.saveMerchants = function (sess_merchants) {

            var callback = function (status, data) {

                if (data.success) {
                    var merchants = data.params.merchants;

                    var sess_merchants = {};
                    for (var i in merchants) {
                        sess_merchants[merchants[i].id] = {
                            businessTitle: merchants[i].businessTitle,
                            status: merchants[i].status
                        };
                    }
                    Main.saveInLocalStorage('merchants', JSON.stringify(sess_merchants));
                }

            };

            if (sess_merchants === undefined) {
                callback.requestType = "GET";
                Main.request('/accountant/get_all_merchants', {}, callback);
            } else {
                callback('', {
                    params: {
                        merchants: sess_merchants
                    },
                    success: true
                });
            }

        };

        Main.assistance = function (data, headers) {

            $("button[type='submit']").attr("disabled", true);

            var callback = function (status, data) {
                $("button[type='submit']").removeAttr("disabled");

                Main.popDialog('', data.message, function () {
                    if (data.success == true) {
                        window.location = Main.modifyURL("/");
                    }
                });
            };

            callback.loaderDiv = ".assist_containers";

            Main.request('/anon/password_assist', data, callback, headers);

        };

        Main.verifyEmail = function (headers) {

            var callback = function (status, data) {
                var message = "";
                if (data.success == true) {
                    message = "Your Email has been verified successfully.";
                } else {
                    message = "Error occured while verifying email.<br> Either the Email is verified already or verification code is wrong";
                    $('#container_verify_email p').addClass("alert-danger");
                }
                $('#container_verify_email p').html(message);
            };

            callback.loaderDiv = "body";

            Main.request('/anon/verify_email', {}, callback, headers);

        };

        Main.changePassword = function (headers) {

            $("button[type='submit']").attr("disabled", true);

            var callback = function (status, data) {
                $("button[type='submit']").removeAttr("disabled");

                Main.popDialog('', data.message, function () {
                    if (data.success == true) {
                        $('#modal_password').modal('hide');
                    }
                });
            };

            callback.loaderDiv = "#modal_password .modal-content";

            Main.request('/anon/change_password', {}, callback, headers);

        };

        Main.createDataTable = function (selector, dataFilter) {
            var headers = {};
            if (dataFilter.headers !== undefined) {
                headers = dataFilter.headers;
            }

            var options = {
                "ajax": {
                    "url": Main.modifyURL(dataFilter.url),
                    "type": dataFilter.requestType || "POST",
                    "headers": headers,
                    "contentType": "application/json",
                    "data": function (data) {
                        // $('body').addClass('loader_div').append('<div class="loader"></div>');
                        var request = {};
                        var page = {};
                        if (typeof(dataFilter.params) == "object") {
                            request = dataFilter.params;
                        }
                        page.pageNumber = parseInt((data.start / data.length) + 1);
                        page.pageSize = dataFilter.pageSize == 0 ? undefined : data.length;
                        page.searchFor = data.search.value;
                        var sortColName = data.columns[data.order[0].column].name;
                        if (sortColName !== "") {
                            page.sortBy = sortColName;
                            page.sortOrder = data.order[0].dir;
                        }
                        request.page = page;
                        return JSON.stringify(request);
                    },
                    "dataFilter": function (data, type) {
                        data = JSON.parse(data);
                        if (data.message == "YSException") {
                            return JSON.stringify({
                                data: [],
                                recordsFiltered: 0,
                                recordsTotal: 0
                            });
                        } else {
                            var jsonData = dataFilter(data, type);
                            return JSON.stringify(jsonData);
                        }
                    },
                    "complete": function () {
                        // $('body').removeClass('loader_div').children('.loader').hide().remove();
                    }
                },
                "buttons": [
                    {
                        extend: 'colvisRestore',
                        text: 'Display All'
                    }, {
                        extend: 'colvis',
                        text: '<span class="caret"></span>',
                        columns: ':gt(1)'
                    }, {
                        extend: 'csv',
                        text: 'Export',
                        exportOptions: {
                            columns: ':visible'
                        }
                    }
                ],
                "lengthMenu": [10, 25, 50, 75, 100, "All"],
                "columns": dataFilter.columns,
                "fnDrawCallback": typeof dataFilter.callback == 'function' ? dataFilter.callback : null,
                "deferRender": true,
                "dom": dataFilter.dom !== undefined ? dataFilter.dom : '<"ao-dt ao-dt-top clearfix"lf>t<"ao-dt ao-dt-bottom clearfix"p>',
                "info": true,
                "autoWidth": dataFilter.autoWidth || true,
                "language": {
                    "info": "_END_ Items of _MAX_",
                    "search": "_INPUT_",
                    "searchPlaceholder": "Search..."
                },
                "order": dataFilter.order || [
                    [0, "desc"]
                ],
                "pagingType": "full_numbers",
                "processing": true,
                "scrollX": true,
                "serverSide": true,
            };
            if (dataFilter.bPaginate != null)
                options.bPaginate = dataFilter.bPaginate;
            if (dataFilter.bFilter != null)
                options.bFilter = dataFilter.bFilter;
            if (dataFilter.bInfo != null)
                options.bInfo = dataFilter.bInfo;


            if (!selector) {
                selector = '.datatable';
            }

            if (dataFilter) {
                $.extend($.fn.dataTable.defaults, options);
            }

            if ($.fn.dataTable.fnIsDataTable(selector)) {
                $(selector).DataTable().clear().destroy();
            }
            var retDatatable = $(selector).DataTable(options);
            $(window).on('resize', function (event) {
                window.setTimeout(function () {
                    retDatatable.columns.adjust().draw();
                }, 500);
            });
            return retDatatable;
        };

        Main.ucfirst = function (word) {
            if (word) {
                return word.substr(0, 1).toUpperCase() + word.substr(1).toLowerCase();
            } else {
                return "";
            }
        };

        Main.globalReplace = function (string, toRemove, toReplace) {
            if (!string && !string.length) {
                return;
            }
            if (!toRemove && !toRemove.length) {
                return;
            }
            var rgxObj = new RegExp(toRemove, 'g');
            return string.replace(rgxObj, toReplace);
        };

        Main.remove_value = function (value, remove) {
            if (value.indexOf(remove) > -1) {
                value.splice(value.indexOf(remove), 1);
                Main.remove_value(value, remove);
            }
            return value;
        };

        Main.getURLhash = function (index) {
            var hash = location.hash;
            var string = hash.slice(1);
            return string.split('/');
        };

        Main.getURLvalue = function (index) {
            if (index == undefined) return false;

            var pathname = window.location.pathname;
            var path_arr = pathname.split('/');
            path_arr = Main.remove_value(path_arr, "");
            if (Main.pageContext != "" && Main.pageContext != undefined)
                path_arr.splice(0, 1);
            return path_arr[index];

        };

        Main.getURLParameter = function (key, keyvalue) {
            if (key == undefined) return false;
            key = key + "_";

            var pathname = window.location.pathname;
            var path_arr = pathname.split('/');
            path_arr = Main.remove_value(path_arr, "");
            var idm_param = undefined;
            for (var i = 0; i < path_arr.length; i++) {
                if (/idm_/g.test(path_arr[i])) {
                    idm_param = path_arr[i];
                    break;
                }
            }
            if (keyvalue)
                return idm_param;
            else
                return idm_param.replace(key, '');
        };

        Main.getNamedParameter = function (key) {
            if (key == undefined) return false;

            var url = window.location.href;
            //console.log(url);
            var path_arr = url.split('?');
            if (path_arr.length === 1) {
                return null;
            }
            path_arr = path_arr[1].split('&');
            path_arr = Main.remove_value(path_arr, "");
            var value = undefined;
            for (var i = 0; i < path_arr.length; i++) {
                var keyValue = path_arr[i].split('=');
                if (keyValue[0] == key) {
                    value = keyValue[1];
                    break;
                }
            }

            return value;
        };

        Main.updateLinks = function (parameter, target, callback) {

            var param = Main.getURLParameter(parameter, true);
            if (param != undefined) {
                $(target).each(function () {
                    var href = $(this).attr('href');
                    $(this).attr('href', href + (href.lastIndexOf('/') == href.length - 1 ? "" : "/") + param);
                });
                if (callback != undefined) callback();
            }

        };

        Main.convertMin = function (valueMin) {

            var min = valueMin % 60;
            var hour = ((valueMin - min) / 60) % 24;
            var days = ((valueMin - (min + hour * 60)) / 60) / 24;
            var time = [];
            if (days > 0) time.push(days + "d");
            if (hour > 0) time.push(hour + "h");
            if (min > 0) time.push(min + "m");
            return time.join(" ");

        };

        Main.sanitizeDateTime = function (dateTime) {
            var dateTimeSanitized = dateTime + "";
            return dateTimeSanitized.replace(/-/g, '/');
        };

        Main.prettifyDateTime = function (dateTime) {
            var dateObj = new Date(Main.sanitizeDateTime(dateTime));
            var output = '';
            output += dateObj.toDateString().substring(4);
            output += " ";
            output += dateObj.toTimeString().substring(0, 5);
            return output;

        };

        Main.fixUndefinedVariable = function (variable, returnText) {
            if (typeof(variable) == "undefined") {
                return returnText || "";
            } else {
                return variable;
            }
        }

        Main.getDropBtn = function (dropArr, btn) {
            var template = '';
            var container = '<div class="dropdown btn-group drop-btn">';
            var content = '';
            var caret = '<button type="button" class="btn btn-krave dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <span class="caret"></span> <span class="sr-only">Toggle Dropdown</span> </button>';
            if (btn) {
                content += btn;
            }
            content += caret;
            var dropdownContainer = '<ul class="dropdown-menu">';
            for (var i = 0; i < dropArr.length; i++) {
                dropdownContainer += '<li>' + dropArr[i] + '</li>';
            }
            dropdownContainer += '</ul>';
            content += dropdownContainer;
            container += content;
            container += '</div>';
            template += container;
            return template;
        }

        Main.popDialog = function (title, content, buttons, type, noHide) {
            var dialogType = type || "null";
            dialogCallback = '';
            if (typeof buttons == 'function') {
                dialogCallback = buttons;
                buttons = undefined;
                if (!buttons.text) {
                    buttons.text = 'Close';
                }
            }

            var modalColor = '';
            switch (type) {
                case 'error':
                    modalColor = ' c-b-nope ';
                    break;
                case 'success':
                default:
                    modalColor = ' c-b-emphasis ';
                    break;
            }

            if (!buttons) {
                buttons = ['Close'];
            }
            if ($('#popDialog').length == 0) {
                $('body').append('\
                    <div class="modal" id="popDialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="z-index: 9999">\
                        <div class="modal-dialog">\
                            <div class="modal-content">\
                                <div class="modal-header ' + modalColor + ' c-t-pure">\
                                    <h4 class="modal-title" id="myModalLabel"></h4>\
                                </div>\
                                <div class="modal-body">\
                                </div>\
                                <div class="modal-footer">\
                                </div>\
                            </div>\
                        </div>\
                    </div>');
            }

            window.dialogButtons = buttons;
            var popElem = $('#popDialog');
            var buttonsElem = '';
            for (var i = 0; i < buttons.length; i++) {
                if (typeof buttons[i] === 'function' && !buttons[i].text) {
                    buttons[i].text = 'Close';
                }
                var button = '<button type="button" class="confirm-button btn btn-outline c-bo-emphasis">' + (typeof buttons[i] == 'string' ? buttons[i] : buttons[i].text) + '</button>';
                buttonsElem += button;
            }
            if (title == '') {
                $('.modal-header', popElem).addClass('hidden').children('.modal-title').html('');
            } else {
                $('.modal-header', popElem).removeClass('hidden').children('.modal-title').html(title);
            }

            $('.modal-body', popElem).html(content);

            if (buttons.length == 0) {
                $('.modal-footer', popElem).addClass('hidden').html('');
            } else {
                $('.modal-footer', popElem).removeClass('hidden').html(buttonsElem);
            }

            if (noHide) {
                popElem.modal({
                    backdrop: 'static',
                    keyboard: false,
                    show: true
                });
            } else {
                popElem.modal('show');
            }


            var closeDialog = function () {
                dialogTimeout = setTimeout(function () {
                    if (typeof dialogCallback === 'function') {
                        dialogCallback();
                    }
                }, 3000);
            }

            //return;

            if (dialogType === 'success') {
                if (buttons.length && typeof buttons[0] == 'function' && buttons[0].text === 'Close') {
                    closeDialog();
                    dialogCallback = buttons[0];
                }
                if (buttons[0] == 'Close') {
                    setTimeout(function () {
                        popElem.modal('hide');
                    }, 3000);
                }
            }

            popElem.on('hide.bs.modal', function () {
                clearTimeout(dialogTimeout);
                $(this).data('bs.modal', null);
            });

        };

    })(jQuery);
})();