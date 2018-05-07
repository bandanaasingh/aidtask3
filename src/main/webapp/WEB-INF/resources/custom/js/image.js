var imageModule = (function () {

    'use strict';

    var
        // sets the active image being selected
        imageActive,
        // sets the image output element
        imageOutput,
        // sets the required image height
        imageHeight,
        // sets the required image width
        imageWidth,
        // caches the cropper modal
        imageCropperModal,
        // caches the cropper image
        imageToCrop,
        // stores the cropped image
        croppedImage,
        // stores the cropped image canvas
        croppedImageCanvas,
        //array of input and output
        imageArray = {},
        //get current image
        currentImage;

    var image = {

        init: function (element, output) {
            if(image.supports) {
                imageArray[element.attr("id")] = output.attr("id");
                imageActive = $(element);
                imageOutput = $(output);
                imageHeight = imageOutput.data('height');
                imageWidth = imageOutput.data('width');
                imageCropperModal = $('#cropperModal');
                imageToCrop = $('#cropImage');
                image.events();
                Main.log("Image Upload is supported");
            } else {
                Main.log("Image Upload is not supported", "error");
            }
        },

        supports: function () {
            if (window.File && window.FileReader && window.FileList && window.Blob) {
                return true;
            } else {
                return false;
            }
        },

        events: function () {
            imageActive.on('change', image.readFile);
            imageCropperModal.on('shown.bs.modal', image.cropInit);
            imageCropperModal.on('hidden.bs.modal', image.destroyCrop);
        },

        readFile: function (evt) {
            Main.log("READ FILE CALLED!");
            evt.stopPropagation();
            evt.preventDefault();
            // var dt = evt.dataTransfer || (evt.originalEvent && evt.originalEvent.dataTransfer);
            currentImage = evt.target.id;
            imageWidth = $("#"+imageArray[currentImage]).data("width");
            imageHeight = $("#"+imageArray[currentImage]).data("height");

            var files = evt.target.files[0] /*|| (dt[0] && dt.files[0])*/;
            var reader = new FileReader();
            reader.readAsDataURL(files);
            reader.onload = function (loadEvt) {
                var srcUrl = loadEvt.target.result;
                var image = new Image();
                image.src = srcUrl;
                image.onload = function() {
                    var
                        // Width of the input image
                        fileWidth = this.width,
                        // Height of the input image
                        fileHeight = this.height;

                    console.log(fileWidth + "*" + fileHeight);
                    console.log(imageWidth + "*" + imageHeight);
                    if (fileWidth !== imageWidth || fileHeight !== imageHeight) {
                        if (fileWidth >= imageWidth && fileHeight >= imageHeight) {
                            imageToCrop.attr('src', srcUrl);
                            imageToCrop.on('load',function() {
                                imageCropperModal.modal('show');
                            });
                        } else {
                            Main.popDialog('Error', 'Please upload an image of size greater than ' + fileWidth + 'px ' + fileHeight + 'px ');
                            return false;
                        }
                    } else {
                        $("#"+imageArray[currentImage]).attr('src', srcUrl);
                        $("#"+imageArray[currentImage]).trigger('submitPref');
                    }
                };
                Main.log("File read");
            };
            //reader.readAsDataURL(files);
        },
        cropInit: function () {
            $.fn.cropper.setDefaults({
                viewMode: 1,
                dragMode: 'move',
                aspectRatio: 1 / 1,
                toggleDragModeOnDblclick: false,
                cropBoxMovable: false,
                cropBoxResizable: false,
                autoCrop: true
            });

            croppedImage = imageToCrop.cropper('crop');
        },
        cropIt: function () {
            if (croppedImage) {
                croppedImageCanvas = croppedImage.cropper('getCroppedCanvas', {height: imageHeight, width: imageWidth});
                if (croppedImageCanvas) {
                    if (imageActive.eq(0).data('item') === true) {
                        image.addExtraPixel.call(this,croppedImageCanvas);
                        $("#"+imageArray[currentImage]).attr('src', croppedImageCanvas.toDataURL('image/jpeg',0.6)); // to add compression toDataURL('image/jpeg',0.7)
                    } else {
                        $("#"+imageArray[currentImage]).attr('src', croppedImageCanvas.toDataURL()); // to add compression toDataURL('image/jpeg',0.7)
                    }
                    $("#"+imageArray[currentImage]).trigger('submitPref');
                    imageCropperModal.modal('hide');
                }
            } else {
                Main.log("No image cropped");
            }
        },
        destroyCrop: function () {
            imageToCrop.cropper('destroy');
        },
        addExtraPixel: function (croppedImageCanvas) {
            var context = croppedImageCanvas.getContext('2d');
            context.fillStyle = 'transparent';
            var imageData = context.getImageData(1,1,croppedImageCanvas.width - 1,croppedImageCanvas.height - 1);
            context.clearRect(0,0,croppedImageCanvas.width,croppedImageCanvas.height);
            context.putImageData(imageData,0,0,1,1,croppedImageCanvas.width - 1,croppedImageCanvas.height - 1);
        }
    };

    return {
        init: function (input, output) {
            image.init(input, output);
        },
        bindCropComplete: function(){
            $('body').on('click', '#cropComplete', image.cropIt);
        }
    };

})();