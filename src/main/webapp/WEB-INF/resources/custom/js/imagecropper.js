
var imageCropperModule = (function () {
    "use strict";
    var imgsrc; // for base64 URL of uploaded image
    var cropPic; // for cropper object
    var inputSel;
    var imageSel;
    var curImageSel;
    var fromParams = false;
    var imageCropper = {
        ui: {
            inputImage: $('[data-upload="image"], .image_input'),
            btnGetCropped: $('#btnSetImage'),
            imageCropperModal: $('#picCropModal'),
            imageCropperWrapper: $('#cropWrapper'),
            imagePreview: $(".fileinput-preview img")
            //uploadImagePreview : $(".fileinput-preview img"),
        },

        init: function (inputSelector, ImageSelector) {
            if (typeof(inputSelector) != "undefined" && typeof(ImageSelector) != "undefined") {
                inputSel = $(inputSelector);
                imageSel = $(ImageSelector);
                fromParams = true;
            } else {
                inputSel = this.ui.inputImage;
                imageSel = this.ui.imagePreview;
            }

            this.events();

        },

        events: function () {
            var that = this;
            inputSel.on('change', function () {
                if (fromParams) {
                    curImageSel = $('#' + $(this).attr('id').replace('_input', ''));
                } else {
                    curImageSel = imageSel;
                }

                var FR = new FileReader();
                FR.onload = function (e2) {
                    //console.log(e2.target.result);
                    imgsrc = e2.target.result;
                    that.ui.imageCropperModal.modal('show');

                    cropPic = that.ui.imageCropperWrapper.croppie({
                        url: imgsrc,
                        viewport: {
                            width: 300,
                            height: 300
                        },
                        boundary: {
                            width: "100%",
                            height: 400
                        }
                    });

                };
                FR.readAsDataURL(this.files[0]);

                that.modalEventHandlers();

                that.ui.btnGetCropped.one('click', function () {
                    //console.log('click btn');
                    that.getUploadedDataURL();
                    that.ui.imageCropperModal.modal("hide");
                });

            });


        },

        getUploadedDataURL: function () {
            //console.log('ss');
            //var that = this;
            cropPic.croppie('result', 'canvas').then(function (resp) {
                //console.log(that.ui.imagePreview.selector);
                $(curImageSel.selector).attr("src", resp).trigger('submitPref');

            });
        },


        modalEventHandlers: function () {
            this.ui.imageCropperModal.on('shown.bs.modal', function () {
                cropPic.croppie('bind');
            }).on('hidden.bs.modal', function () {
                cropPic.croppie('destroy');
            });
        }


    };

    return {
        init: function (inputSelector, ImageSelector) {
            imageCropper.init(inputSelector, ImageSelector);
        }
    };


})();