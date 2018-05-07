/**
 * Created by Pratik on 5/24/2016.
 */

var markers = [];
var marker;
var map;
var arrGeoPoints = {};
var title;
var label;
var icon = {
    url: "",
    size: {
        width: 38,
        height: 52
    }
};
var curMarker;
var curAddressComponent;
var readOnly = true;

var mapsModule = (function (config) {
    "use strict";

    var gMap = {
        config: config,

        ui: {
            formStreetAddress: $('#streetAddress'),
            formStreetAddressTwo: $('#streetAddressTwo'),
            formCity: $('#city'),
            formPostalCode: $('#postalCode'),
            formState: $('#state'),
            formRegion: $('#region'),
            formCountry: $('#country'),
            formlat: $('#latitude'),
            formlng: $('#longitude'),
            formMainArea: $('#mainArea'),
            formSubArea: $('#subArea'),
            contactNumber: $('#contactNumber'),
            formAddressNote: $('#addressNote'),
            formPriority: $('#priority'),
            locationBoxWrapper: $("#locationBoxWrapper")
        },

        init: function (config) {
            this.config = config;
            if (config.hasInput !== undefined || config.hasInput !== null) {
                this.config.hasInput = config.hasInput;
            } else {
                this.config.hasInput = true;
            }

            map = this.config.mapVar;

            icon.url = config.markerIcon;
            this.events();
            this.initGoogleMaps();

            readOnly = this.config.readOnly;
        },

        locationToKey: function (location) {
            var latitude = location.latitude === undefined ? location.lat() : location.latitude;
            var longitude = location.longitude === undefined ? location.lng() : location.longitude;
            return (latitude + '_' + longitude).replace(/[.-]/g, '_');
        },

        clearAllMarkers: function () {
            $.each(markers, function (index, data) {
                data.setMap(null);
            });
            markers.length = 0;
            marker = Boolean(false);
            arrGeoPoints = {};

        },

        setAvailableMarkers: function (locations) {
            this.clearAllMarkers();
            var that = this;
            if (typeof (google) !== 'undefined') {
                $.each(locations, function (index, data) {
                    //console.log(data);
                    that.getAddressComponents(data, true);
                    //that.addMarker(data);
                });
                setTimeout(function () {
                    map.setCenter(locations[locations.length - 1]);
                }, 500);
            } else {
                Main.log("Google is not loaded, trying to load again...", 'warning');
                setTimeout(function () {
                    that.setAvailableMarkers(locations);
                }, 1000);
            }
        },

        setLocationInfoBox: function (addressComponent) {
            var locationBox = '<div class="col-sm-2 location-info-box mb25 active" data-uid="' + label + '"><div>' +
                '<p class="fontbold font18 mb0" data-type="infoLocation">Location ' + label + '</p>' +
                '<p class="mb0 c-t-light" data-type="infoStreetAddress">' + addressComponent.streetAddress + '</p> ' +
                '<p class="mb0 c-t-light" data-type="infoStreetAddressTwo">' + addressComponent.locality + '</p> ' +
                '<p class="mb0 c-t-light" data-type="infoCityCountry">' + addressComponent.city + " " + addressComponent.country + '</p>' +
                '</div></div>';

            this.setActiveLocationBox(true);
            this.ui.locationBoxWrapper.append(locationBox);
            //return locationBox;
        },

        updateLocationInfoBox: function (addressComponent) {
            var activeBox = this.ui.locationBoxWrapper.find('.active');
            activeBox.find("[data-type='infoStreetAddress']").html(addressComponent.streetAddress);
            activeBox.find("[data-type='infoStreetAddressTwo']").html(addressComponent.locality);
            activeBox.find("[data-type='infoCityCountry']").html(addressComponent.city + " " + addressComponent.country);
        },


        events: function () {
            var that = this;
            $('#pac-input').on('keyup keypress', function (e) {
                var keyCode = e.keyCode || e.which;
                if (keyCode === 13) {
                    e.preventDefault();
                    return false;
                }
            });

            $('#removeMarker').click(function () {

                if (!confirm("Are you sure you want to remove this store?")) {
                    return;
                }


                $.each(markers, function (index, data) {
                    if (markers[index] === curMarker) {
                        curMarker.setMap(null); //remove marker from map
                        markers.splice(index, 1); //remove marker data from markers object
                        that.resetAddressFields();
                        that.ui.locationBoxWrapper.find('.active').remove();
                        //console.log(data);
                        delete arrGeoPoints[that.locationToKey({
                            latitude: data.position.lat(),
                            longitude: data.position.lng()
                        })];
                        $(document).trigger('maps.marker.remove');
                    }
                });

                $.each(markers, function (index) {
                    markers[index].setLabel((index + 1) + "");
                });

                $.each(that.ui.locationBoxWrapper.find('.location-info-box'), function (index, data) {
                    //console.log(index);
                    $(this).attr('data-uid', (index + 1));
                    $(this).find("[data-type='infoLocation']").html("Location " + (index + 1));
                });
            });

            $('.marker_nav').click(function () {
                that.removeAllMarkerAnimation();
                var marker_index = $(this).attr('data-index');
                if (marker_index !== undefined && $.isNumeric(marker_index) && marker_index >= 0 && marker_index < markers.length) {
                    //google.maps.event.trigger(markers[marker_index], 'click');
                    curMarker = markers[marker_index];
                    map.panTo(curMarker.position);

                    curMarker.setAnimation(google.maps.Animation.BOUNCE);

                    that.switchMaps(curMarker);
                    //Main.log(curMarker);
                    //that.geoCodeLocation({lat: curMarker.position.lat(), lng: curMarker.position.lng()}, true);
                    that.setActiveLocationBox(true);

                    curAddressComponent = arrGeoPoints[that.locationToKey({
                        latitude: curMarker.position.lat(),
                        longitude: curMarker.position.lng()
                    })];
                    that.setAddressToFields(curAddressComponent);


                }
            });

            /*formSubArea: $('#subArea'),
             formAddressNote: $('#addressNote'),
             formPriority: $('#priority'),*/


            this.ui.formMainArea.change(function () {
                //console.log("Main Area:"+$(this).val());
                if (typeof (curAddressComponent) !== "undefined") {
                    var location = {
                        latitude: curAddressComponent.position.lat,
                        longitude: curAddressComponent.position.lng
                    };
                    arrGeoPoints[that.locationToKey(location)].mainArea = $(this).val();
                }
            });

            this.ui.contactNumber.change(function () {
                //console.log("Main Area:"+$(this).val());
                if (typeof (curAddressComponent) !== "undefined") {
                    var location = {
                        latitude: curAddressComponent.position.lat,
                        longitude: curAddressComponent.position.lng
                    };
                    arrGeoPoints[that.locationToKey(location)].contactNumber = $(this).val();
                }
            });

            this.ui.formSubArea.change(function () {
                //console.log("Sub Area:"+$(this).val());
                if (typeof (curAddressComponent) !== "undefined") {
                    var location = {
                        latitude: curAddressComponent.position.lat,
                        longitude: curAddressComponent.position.lng
                    };
                    arrGeoPoints[that.locationToKey(location)].subArea = $(this).val();
                }
            });

            this.ui.formAddressNote.change(function () {
                //console.log("Area Note:"+$(this).val());
                if (typeof (curAddressComponent) !== "undefined") {
                    var location = {
                        latitude: curAddressComponent.position.lat,
                        longitude: curAddressComponent.position.lng
                    };
                    arrGeoPoints[that.locationToKey(location)].addressNote = $(this).val();
                }
            });

            this.ui.formPriority.change(function () {
                // console.log("Area Priority:"+$(this).val());
                if (typeof (curAddressComponent) !== "undefined") {
                    var location = {
                        latitude: curAddressComponent.position.lat,
                        longitude: curAddressComponent.position.lng
                    };
                    arrGeoPoints[that.locationToKey(location)].priority = $(this).val();
                }
            });

        },

        /**
         * initiated when google maps api is loaded
         * @type {gMap}
         */
        initGoogleMaps: function () {
            var that = this;
            map = new google.maps.Map(document.getElementById('map'), {
                mapTypeId: google.maps.MapTypeId.ROADMAP,
                disableDoubleClickZoom: true
            });

            this.reverseGeoCodeLocation(this.config.curLocation);

            //For Double Click on Maps
            google.maps.event.addListener(map, "dblclick", function (e) {
                if (readOnly)
                    return;

                if (!that.config.multiMarker) {
                    if (!marker) {
                        var location = {
                            lat: e.latLng.lat(),
                            lng: e.latLng.lng()
                        };
                        //that.addMarker(that.getAddressComponents(location));
                        that.getAddressComponents(location, true); //this will get address components(city, streetaddress...) from latlng (1st Param), true (2nd Param) will add marker to map.
                    }
                } else {
                    var location = {
                        lat: e.latLng.lat(),
                        lng: e.latLng.lng()
                    };
                    //that.addMarker(that.getAddressComponents(location));
                    that.getAddressComponents(location, true); //this will get address components(city, streetaddress...) from latlng (1st Param), true (2nd Param) will add marker to map.
                }
            });

            if (that.config.hasInput) {
                //For Search Box
                var input = document.getElementById('pac-input');
                var searchBox = new google.maps.places.SearchBox(input);
                map.controls[google.maps.ControlPosition.TOP_CENTER].push(input);

                map.addListener('bounds_changed', function () {
                    searchBox.setBounds(map.getBounds());
                });

                searchBox.addListener('places_changed', function () {
                    var places = searchBox.getPlaces();
                    if (places.length === 0) {
                        return;
                    }
                    var bounds = new google.maps.LatLngBounds();
                    places.forEach(function (place) {
                        if (!that.config.multiMarker) {
                            if (!marker) {
                                var location = {
                                    lat: place.geometry.location.lat(),
                                    lng: place.geometry.location.lng()
                                };
                                that.getAddressComponents(location, true);
                            }
                        } else {
                            var location = {
                                lat: place.geometry.location.lat(),
                                lng: place.geometry.location.lng()
                            };
                            that.getAddressComponents(location, true);
                        }
                        if (place.geometry.viewport) {
                            bounds.union(place.geometry.viewport);
                        } else {
                            bounds.extend(place.geometry.location);
                        }
                    });
                    map.fitBounds(bounds);
                });
            }
        },

        /**
         * adds new marker to map and events like click, dblclick... in markers
         * @param addressComponent : receive from geocode after providing latlng
         */
        addMarker: function (addressComponent) {
            /*this.resetAddressFields();
             return;*/
            //Main.log(addressComponent);
            var that = this;

            label = (this.config.countLabel) ? (markers.length + 1) + "" : "";

            //this.geoCodeLocation(location, true);

            if (!this.config.multiMarker) {
                if (!marker) {
                    // Create the marker if it doesn't exist
                    marker = new google.maps.Marker({
                        map: map,
                        icon: icon,
                        title: addressComponent.streetAddress,
                        draggable: true,
                        animation: google.maps.Animation.BOUNCE,
                        position: addressComponent.position,
                        label: label
                    });
                }
                // Otherwise, simply update its location on the map.
                else {
                    console.log("ret");
                    //markers[0].setPosition(addressComponent.location);
                    /*  marker.setPosition(addressComponent.location);
                     marker.setAnimation(google.maps.Animation.BOUNCE);*/
                    return;

                }
            } else {
                this.removeAllMarkerAnimation();
                marker = new google.maps.Marker({
                    map: map,
                    icon: icon,
                    title: addressComponent.streetAddress,
                    draggable: true,
                    animation: google.maps.Animation.DROP,
                    position: addressComponent.position,
                    label: label

                });

                curMarker = marker;
            }


            marker.addListener('dragstart', function (e) {
                that.removeAllMarkerAnimation();
                var lastPosition = {
                    latitude: e.latLng.lat(),
                    longitude: e.latLng.lng()
                };
                delete arrGeoPoints[that.locationToKey(lastPosition)];
            });

            marker.addListener('dblclick', function (e) {
                //that.removeAllMarkerAnimation();
                //this.setAnimation(google.maps.Animation.BOUNCE);
                //Main.log("dblclick");

            });

            marker.addListener('dragend', function (e) {
                var location = {
                    lat: e.latLng.lat(),
                    lng: e.latLng.lng()
                };
                curMarker = this;
                that.setActiveLocationBox(true);


                that.getAddressComponents(location, false); //updates current address components for form fields, wont add marker as 2nd param is false
                that.switchMaps(curMarker);


            });

            google.maps.event.addListener(marker, 'click', function () {
                var location = {
                    latitude: this.position.lat(),
                    longitude: this.position.lng()
                };
                that.removeAllMarkerAnimation();
                curMarker = this;
                //that.getAddressComponents(location, false); //updates current address components for form fields, wont add marker as 2nd param is false
                //console.log(arrGeoPoints[that.locationToKey(location)]);

                curAddressComponent = arrGeoPoints[that.locationToKey(location)];
                that.setAddressToFields(curAddressComponent);


                that.switchMaps(this);
                that.setActiveLocationBox(true);

                this.setAnimation(google.maps.Animation.BOUNCE);
            });

            this.switchMaps(marker);

            this.setAddressToFields(addressComponent);
            this.setLocationInfoBox(addressComponent);

            markers.push(marker);
            //console.log(addressComponent);


        },

        /**
         * removes animation from all/provided markers
         * @param customMarkers
         */
        removeAllMarkerAnimation: function (customMarkers) {
            if (customMarkers !== undefined) {
                markers = customMarkers;
            }
            for (var i in markers) {
                if (markers.hasOwnProperty(i)) {
                    markers[i].setAnimation(null);
                }
            }
        },

        /**
         *
         * @param location : latlng which will be used to get address components (city, address...)
         * @param addMarker : boolean which will decide to add new marker in map or not.
         */
        getAddressComponents: function (location, addMarker) {
            var that = this;
            var geocoder = new google.maps.Geocoder();
            //console.log(location);
            geocoder.geocode({
                'latLng': location
            }, function (results, status) {
                Main.log(status);
                if (status == google.maps.GeocoderStatus.OK) {
                    if (results[0]) {
                        var component = results[0];
                        var streetNumber = "",
                            sublocality = "",
                            locality = "",
                            city = "",
                            postalCode = "",
                            state = "",
                            region = "",
                            country = "";
                        var addressComponents = component.address_components;

                        for (var i in addressComponents) {
                            if (addressComponents.hasOwnProperty(i)) {
                                if (addressComponents[i].types[0] == 'country') {
                                    country = addressComponents[i].long_name;
                                    break;
                                }
                            }
                        }

                        for (var i in addressComponents) {
                            if (addressComponents.hasOwnProperty(i)) {
                                for (var j in addressComponents[i].types) {
                                    if (addressComponents[i].types.hasOwnProperty(j)) {
                                        if (addressComponents[i].types[j] == "street_number") {
                                            streetNumber = component.address_components[i].long_name;
                                        }
                                        if (addressComponents[i].types[j] == "route") {
                                            sublocality = component.address_components[i].long_name;
                                        }
                                        if (addressComponents[i].types[j] == "sublocality") {
                                            locality = component.address_components[i].long_name;
                                        }
                                        if (addressComponents[i].types[j] == "locality") {
                                            city = component.address_components[i].long_name;
                                        }
                                        if (addressComponents[i].types[j] == "postal_code") {
                                            postalCode = component.address_components[i].long_name;
                                        }
                                        if (addressComponents[i].types[j] == "administrative_area_level_2") {
                                            state = component.address_components[i].long_name;
                                        }
                                        if (addressComponents[i].types[j] == "administrative_area_level_1") {
                                            region = component.address_components[i].long_name;
                                        }
                                    }

                                }
                            }

                        }

                        if (that.config.curPage == "add_store") {
                            var streetAddress = ((streetNumber == "") ? "" : streetNumber + ", ") + ((sublocality == "") ? "" : sublocality);
                        } else {
                            var streetAddress = ((streetNumber == "") ? "" : streetNumber + ", ") + ((sublocality == "") ? "" : sublocality + ", ") + ((locality == "") ? "" : locality + " ");
                        }

                        curAddressComponent = {
                            "streetnumber": streetNumber,
                            "streetAddress": streetAddress,
                            "sublocality": sublocality,
                            "locality": locality,
                            "city": city,
                            "postalCode": postalCode,
                            "state": state,
                            "region": region,
                            "country": country,
                            "position": location,
                            "contactNumber": "",
                            "mainArea": "",
                            "subArea": "", //(that.ui.formSubArea.val() != "") ? that.ui.formSubArea.val() : "",
                            "addressNote": "", //(that.ui.formAddressNote.val() != "") ? that.ui.formAddressNote.val() : "",
                            "priority": "" //(that.ui.formPriority.val() != "") ? that.ui.formAddressNote.val() : "",
                        };


                        if (addMarker) {
                            that.addMarker(curAddressComponent);

                        }

                        that.setAddressToFields(curAddressComponent);
                        that.updateLocationInfoBox(curAddressComponent);

                    }
                }
            });
        },

        /**
         * updates form fields in the view
         * @param addressComponent
         */
        setAddressToFields: function (addressComponent) {
            // console.log(addressComponent);
            //console.log(typeof(addressComponent.position.lat));

            arrGeoPoints[this.locationToKey({
                latitude: addressComponent.position.lat,
                longitude: addressComponent.position.lng
            })] = addressComponent;


            //console.log(addressComponent);
            if (this.ui.formStreetAddress.length) {
                this.ui.formStreetAddress.val(addressComponent.streetAddress);
            }
            if (this.ui.formStreetAddressTwo.length) {
                this.ui.formStreetAddressTwo.val(addressComponent.locality);
            }
            if (this.ui.formCity.length) {
                this.ui.formCity.val(addressComponent.city);
            }
            if (this.ui.formPostalCode.length) {
                this.ui.formPostalCode.val(addressComponent.postalCode);
            }
            if (this.ui.formState.length) {
                this.ui.formState.val(addressComponent.state);
            }
            if (this.ui.formRegion.length) {
                this.ui.formRegion.val(addressComponent.region);
            }
            if (this.ui.formCountry.length) {
                this.ui.formCountry.val(addressComponent.country);
            }
            if (this.ui.formlat.length) {
                this.ui.formlat.val(addressComponent.position.lat);
            }
            if (this.ui.formlng.length) {
                this.ui.formlng.val(addressComponent.position.lng);
            }

            if (this.ui.formMainArea.length) {
                this.ui.formMainArea.val(addressComponent.mainArea).trigger('change');
            }

            //addStoreModule.getSubArea(addressComponent.mainArea);
            var that = this;
            setTimeout(function () {
                that.ui.formSubArea.val(addressComponent.subArea);
                var $selectpicker = $('.select-picker');
                if ($selectpicker.length) {
                    $selectpicker.selectpicker('refresh');
                }
            }, 1000);

            //console.log(addressComponent);
            if (this.ui.formAddressNote.length) {
                this.ui.formAddressNote.val(addressComponent.addressNote);
            }
            if (this.ui.contactNumber.length) {
                this.ui.contactNumber.val(addressComponent.contactNumber);
            }
            if (this.ui.formPriority.length) {
                this.ui.formPriority.val(addressComponent.priority);
            }

            $(':input').blur();
            if ($('.select-picker').length) {
                $('.select-picker').selectpicker('refresh');
            }


        },

        /**
         * clear up the form fields in the view
         */
        resetAddressFields: function () {
            this.ui.formStreetAddress.val("");
            this.ui.formStreetAddressTwo.val("");
            this.ui.formCity.val("");
            this.ui.formPostalCode.val("");
            this.ui.formState.val("");
            this.ui.formRegion.val("");
            this.ui.formCountry.val("");
            this.ui.formlat.val("");
            this.ui.formlng.val("");


            this.ui.formMainArea.val("");
            this.ui.formSubArea.val("");
            this.ui.contactNumber.val("");
            this.ui.formPriority.val("");
            this.ui.formAddressNote.val("");

            $(':input').blur();
            $('.select-picker').selectpicker('refresh');
        },

        setActiveLocationBox: function (setActive) {
            if (this.config.curPage == "add_store") {
                this.ui.locationBoxWrapper.find('.active').removeClass('active');
                if (setActive) {
                    this.ui.locationBoxWrapper.find('.location-info-box[data-uid="' + curMarker.getLabel() + '"]').addClass("active");
                }
            }
        },


        /**
         * converts name of places to latlng
         * @param address : name of city or place
         * @param noBounds
         * @param noCenter
         * @returns {boolean}
         */
        reverseGeoCodeLocation: function (address, noBounds, noCenter) {
            if (address == undefined) return false;
            var geocoder = new google.maps.Geocoder();
            geocoder.geocode({
                'address': address
            }, function (results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                    if (typeof map != 'undefined') {
                        if (noCenter !== true)
                            map.setCenter(results[0].geometry.location);

                        if (noBounds !== true)
                            map.fitBounds(results[0].geometry.bounds);

                        map.setZoom(13);
                    }
                } else {
                    Main.popDialog('', "Geocode was not successful for the following reason: " + status);
                }
            });
        },

        /**
         * changes the marker indexes for next and prev marker feature in map
         * @param marker
         */
        switchMaps: function (marker) {
            var current_index = markers.indexOf(marker);
            var markers_length = markers.length;
            //Main.log(current_index + " length " + markers_length);
            var prev = $("#prevMarker");
            var next = $("#nextMarker");
            prev.attr('data-index', current_index - 1);
            next.attr('data-index', current_index + 1);
            if (markers_length > 1) {
                if (current_index === 0) {
                    prev.attr('disabled', 'disabled');
                } else {
                    prev.removeAttr('disabled', 'disabled');
                }
                if (current_index == markers_length - 1) {
                    next.attr('disabled', 'disabled');
                } else {
                    next.removeAttr('disabled', 'disabled');
                }
            } else {
                prev.attr('disabled', 'disabled');
                next.attr('disabled', 'disabled');
            }
        }
    };

    return {
        init: function (config) {
            gMap.init(config);
        },

        addMarkers: function (location) {
            gMap.setAvailableMarkers(location);
        },

        clearMarkers: function () {
            gMap.clearAllMarkers();
        },

        getKeyByLocation: function (location) {
            //location = {latitude:"", longitude:""}
            return gMap.locationToKey(location);
        }
    };

})();


var soloMarkersModule = (function () { //view only map with markers.
    "use strict";
    var markers = {};
    var selectors = [];
    var map, canvas, mapConfig;

    var showmarkers = {
        init: function (config) {
            canvas = config.mapCanvas;
            mapConfig = {
                zoom: config.zoom,
                center: config.center,
                mapTypeId: config.mapTypeId
            };
            map = new google.maps.Map(document.getElementById(canvas), mapConfig);
            setTimeout(function () {
                $(document).trigger('maps.ready');
            }, 500);

            return map;
        },

        setMarkers: function (map, inMarkers) {
            var that = this;
            var bounds = new google.maps.LatLngBounds();
            $.each(inMarkers, function (index, data) {

                if (typeof(data) == "undefined") {
                    return;
                }

                if (!data.lat || !data.lng) {
                    return;
                }

                var latlng = new google.maps.LatLng(Number(data.lat), Number(data.lng));
                var icon = {};
                icon.url = data.icon || "http://www.luxpopuli.com/es/wp-content/uploads/2015/10/map-marker.png";
                icon.scaledSize = new google.maps.Size(50, 50);

                var marker = new google.maps.Marker({
                    position: latlng,
                    map: map,
                    icon: icon,
                    type: data.type
                });

                bounds.extend(latlng)

                map.fitBounds(bounds);

                var infowindow = new google.maps.InfoWindow({
                    content: data.title
                });

                marker.addListener('mouseover', function () {
                    infowindow.open(map, this);
                });

                marker.addListener('mouseout', function () {
                    infowindow.close();
                });

                marker.addListener('click', function () {
                    that.removeAllMarkerAnimation(markers);
                    this.setAnimation(google.maps.Animation.BOUNCE);
                });

                var locationKey = newMapModule.getKeyByLocation(latlng);
                markers[locationKey] = marker;
                //$(document).trigger('maps.marker.add', [locat]);

            });
        },

        removeAllMarkerAnimation: function (customMarkers) {
            if (customMarkers !== undefined) markers = customMarkers;
            for (var i in markers) {
                if (markers.hasOwnProperty(i)) {
                    markers[i].setAnimation(null);
                }
            }
        },
    };

    return {
        init: function (config) {
            return showmarkers.init(config);
        },

        setMarkers: function (map, points, removeOld) {
            if (removeOld || points.length == 0) {
                $.each(markers, function (index, data) {
                    if (data.type === "Driver") {
                        data.setMap(null); //remove marker from map
                    }
                });
            }

            showmarkers.setMarkers(map, points);
        },

        getMarkers: function () {
            showmarkers.removeAllMarkerAnimation(markers);
            return markers;
        }
    };
})();


var linkedMarkersModule = (function () { // view only maps with linked lines
    "use strict";
    var global_markers = [];
    var selectors = [];
    var map, canvas, mapConfig, flightPath;
    var directionsService, directionsDisplay;

    var showmarkers = {
        init: function (config) {

            map = config.mapVar;
            canvas = config.mapCanvas;
            mapConfig = {
                zoom: config.zoom,
                center: config.center,
                mapTypeId: config.mapTypeId
            };
            map = new google.maps.Map(document.getElementById(canvas), mapConfig);


            directionsService = new google.maps.DirectionsService;
            directionsDisplay = new google.maps.DirectionsRenderer({
                suppressMarkers: true,
                suppressInfoWindows: true,
                /*polylineOptions: {
                 strokeColor: 'red'
                 }*/
            });

            directionsDisplay.setMap(map);
            return map;
        },

        setMarkers: function (markers, locmap) {
            var that = this;
            if (!locmap) {
                locmap = map;
            }

            var bounds = new google.maps.LatLngBounds();
            $.each(markers, function (index, data) {
                var driver, store, customer;
                var driverlat, driverlng, storelat, storelng, customerlat, customerlng;
                var flightPlanCoordinates = [];
                var origin = null;
                var destination = null;
                var waypoint = null;

                if (!$.isEmptyObject(data.driver)) {
                    driver = data.driver;
                    driverlat = driver.lat;
                    driverlng = driver.lng;
                    if (driverlat != "undefined" && driverlng != "undefined") {
                        flightPlanCoordinates.push({
                            lat: driverlat,
                            lng: driverlng
                        });
                        origin = driverlat + "," + driverlng;
                        bounds.extend(new google.maps.LatLng(driverlat, driverlng));
                    }

                }

                if (!$.isEmptyObject(data.store)) {
                    store = data.store;
                    storelat = store.lat;
                    storelng = store.lng;
                    if (storelat != "undefined" && storelng != "undefined") {
                        flightPlanCoordinates.push({
                            lat: storelat,
                            lng: storelng
                        });
                        waypoint = {
                            location: storelat + "," + storelng,
                            stopover: false,
                        };
                        bounds.extend(new google.maps.LatLng(storelat, storelng));
                    }

                }

                if (!$.isEmptyObject(data.customer)) {
                    customer = data.customer;
                    customerlat = customer.lat;
                    // || (!$.isEmptyObject(customer.area)) ? customer.area.latitude : undefined;
                    customerlng = customer.lng;
                    //|| (!$.isEmptyObject(customer.area)) ? customer.area.longitude : undefined;

                    if (!customerlat && !customerlng) {
                        if (!$.isEmptyObject(customer.area)) {
                            customerlat = customer.area.latitude || null;
                            customerlng = customer.area.longitude || null;
                        }
                    }

                    if (customerlat != "undefined" && customerlng != "undefined") {
                        flightPlanCoordinates.push({
                            lat: customerlat,
                            lng: customerlng
                        });
                        destination = customerlat + "," + customerlng;
                        bounds.extend(new google.maps.LatLng(customerlat, customerlng));
                    }

                }

                that.placeMarkerOnMap([driver, store, customer]);

                /*flightPath = new google.maps.Polyline({
                 path: flightPlanCoordinates,
                 geodesic: true,
                 strokeColor: '#FF0000',
                 strokeOpacity: 1.0,
                 strokeWeight: 2
                 });

                 flightPath.setMap(locmap);*/
                /* console.log(origin);
                 console.log(waypoint.location);
                 console.log(destination);*/

                if (destination === "0,0") {
                    destination = null;
                } else if (origin === "0,0") {
                    origin = null;
                } else if (waypoint.location == "0,0") {
                    waypoint = null;
                }
                /*console.log(origin);
                 console.log(waypoint.location);
                 console.log(destination);


                 console.log({
                 origin: origin || waypoint.location,
                 destination: destination,
                 waypoints: [waypoint],
                 optimizeWaypoints: true,
                 travelMode: 'DRIVING'
                 });*/


                try {
                    directionsService.route({
                        origin: origin || waypoint.location,
                        destination: destination || waypoint.location,
                        waypoints: [waypoint],
                        optimizeWaypoints: true,
                        travelMode: 'DRIVING'
                    }, function (response, status) {
                        if (status === 'OK') {
                            //console.log(response);
                            directionsDisplay.setDirections(response);


                        } else {


                            if (status === "ZERO_RESULTS" || status === "NOT_FOUND") {
                                Main.popDialog('Error', 'No directions available for these locations.', null, 'error');
                            } else {
                                $('#closeLinkedMapsBtn').click();
                                Main.popDialog('Error', 'Directions request failed due to ' + status, null, 'error');
                            }

                        }
                    });

                    locmap.fitBounds(bounds);
                } catch (e) {
                    $('.loader').remove();
                    Main.popDialog('Error', "Can't fetch the required location data.", null, 'error');
                }

            });
        },

        placeMarkerOnMap: function (markers) {
            var bound = new google.maps.LatLngBounds();

            $.each(global_markers, function (index, marker) {
                marker.setMap(null);
            });
            if (flightPath) {
                flightPath.setMap(null);
            }


            $.each(markers, function (index, data) {
                if (typeof (data) == "undefined") {
                    return;
                }
                var latlng = new google.maps.LatLng(Number(data.lat), Number(data.lng));
                bound.extend(latlng);
                var icon = {};
                icon.url = data.icon || "http://www.luxpopuli.com/es/wp-content/uploads/2015/10/map-marker.png";
                icon.scaledSize = new google.maps.Size(50, 50);

                var marker = new google.maps.Marker({
                    position: latlng,
                    map: map,
                    icon: icon
                });


                var infowindow = new google.maps.InfoWindow({
                    content: data.title
                });

                marker.addListener('mouseover', function () {
                    infowindow.open(map, this);
                });

                marker.addListener('mouseout', function () {
                    infowindow.close();
                });

                global_markers.push(marker);
            });

            setTimeout(function () {
                google.maps.event.trigger(map, 'resize');
                map.setCenter(bound.getCenter());
            }, 200);


        }
    };

    return {
        init: function (config) {
            showmarkers.init(config);
        },

        setMarkers: function (markers, map) {
            showmarkers.setMarkers(markers, map);
        }
    };
})();


var newMapModule = (function () { //view only map with markers.
    "use strict";
    var marker, markerIcon, curMarker;
    var markers = [];
    var custMarkerDetailArr = {};
    var map, canvas, mapConfig, readOnly, isMultiMarker, disableDoubleClickZoom, searchBox, searchInput;

    var showmarkers = {
        ui: {
            markerNavs: $('.marker_nav'),
            removeMarkerBtn: $('#removeMarker')
        },
        init: function (config) {

            //map = lineConfig.mapVar;
            canvas = config.mapCanvas;
            readOnly = config.readOnly;
            isMultiMarker = config.isMultiMarker;
            markerIcon = config.markerIcon;
            disableDoubleClickZoom = config.disableDoubleClickZoom;
            searchInput = config.searchInput;
            mapConfig = {
                zoom: config.zoom,
                center: config.center,
                mapTypeId: config.mapTypeId,
                disableDoubleClickZoom: disableDoubleClickZoom,
                mapMaker: false
            };
            map = new google.maps.Map(document.getElementById(canvas), mapConfig);

            var input = document.getElementById(searchInput);
            searchBox = new google.maps.places.SearchBox(input);
            map.controls[google.maps.ControlPosition.TOP_CENTER].push(input);


            $('#' + searchInput).on('keyup keypress', function (e) {
                var keyCode = e.keyCode || e.which;
                if (keyCode === 13) {
                    e.preventDefault();
                    return false;
                }
            });

            this.configAddMarker(map);
            this.events();
            return map;
        },

        events: function () {
            var that = this;
            this.ui.markerNavs.click(function () {
                var marker_index = $(this).attr('data-index');
                console.log(marker_index);
                if (marker_index !== undefined && $.isNumeric(marker_index) && marker_index >= 0 && marker_index < markers.length) {

                    //google.maps.event.trigger(markers[marker_index], 'click');
                    that.removeAllMarkerAnimation();
                    curMarker = markers[marker_index];
                    map.panTo(curMarker.position);

                    curMarker.setAnimation(google.maps.Animation.BOUNCE);

                    that.switchMaps(curMarker);

                    var locationKey = that.locationToKey({
                        latitude: curMarker.position.lat(),
                        longitude: curMarker.position.lng()
                    });

                    $(document).trigger('maps.marker.active', [locationKey]);
                }
            });

            this.ui.removeMarkerBtn.click(function () {
                if (readOnly) {
                    return;
                }


                var btnCancel = function () {
                    $('#popDialog').modal('hide');
                    return;
                };
                btnCancel.text = "Cancel";

                var btnConfirm = function () {
                    $.each(markers, function (index, data) {
                        if (markers[index] === curMarker) {
                            curMarker.setMap(null); //remove marker from map
                            markers.splice(index, 1); //remove marker data from markers object

                            var locationKey = that.locationToKey({
                                latitude: data.position.lat(),
                                longitude: data.position.lng()
                            });
                            delete custMarkerDetailArr[locationKey];

                            $(document).trigger('maps.marker.remove', [locationKey]);
                        }
                    });


                    $('#popDialog').modal('hide');
                    return;
                };
                btnConfirm.text = "Yes";

                var button = [btnConfirm, btnCancel];

                Main.popDialog("Confirm", "Are you sure you want to remove this store?", button, null, null);


            });
        },

        configAddMarker: function (map) {

            var that = this;
            google.maps.event.addListener(map, "dblclick", function (e) {
                if (readOnly) {
                    return;
                }
                //console.log(isMultiMarker);
                //console.log(marker);
                if (!isMultiMarker) {
                    var location = {
                        lat: e.latLng.lat(),
                        lng: e.latLng.lng()
                    };
                    if (marker) {
                        marker.setMap(null);
                        $(document).trigger('maps.marker.remove');
                    }
                } else {
                    var location = {
                        lat: e.latLng.lat(),
                        lng: e.latLng.lng()
                    };
                }
                that.addMarker(map, location, true);
            });

            searchBox.addListener('places_changed', function () {
                //console.log('hrere');
                var places = searchBox.getPlaces();
                if (places.length === 0) {
                    return;
                }
                var bounds = new google.maps.LatLngBounds();

                places.forEach(function (place) {
                    if (readOnly) {
                        return;
                    }

                    if (!isMultiMarker) {
                        var location = {
                            lat: place.geometry.location.lat(),
                            lng: place.geometry.location.lng()
                        };
                        if (marker) {
                            marker.setMap(null);
                            $(document).trigger('maps.marker.remove');
                        }
                    } else {
                        var location = {
                            lat: place.geometry.location.lat(),
                            lng: place.geometry.location.lng()
                        };
                    }


                    that.addMarker(map, location, true);

                    if (place.geometry.viewport) {
                        bounds.union(place.geometry.viewport);
                    } else {
                        bounds.extend(place.geometry.location);
                    }
                });
                map.fitBounds(bounds);
            });
        },

        addMarker: function (map, location, updateLoc) {
            var that = this;
            that.removeAllMarkerAnimation();

            marker = new google.maps.Marker({
                map: map,
                icon: markerIcon,
                draggable: false, //!readOnly
                position: location,
                animation: google.maps.Animation.BOUNCE
            });

            map.setCenter(location);


            marker.addListener("rightclick", function (e) {
                if (readOnly) {
                    return;
                }

                var that_m = this;

                var btnCancel = function () {
                    $('#popDialog').modal('hide');
                    return;
                };
                btnCancel.text = "Cancel";

                var btnConfirm = function () {

                    that_m.setMap(null);
                    var locationKey = that.locationToKey({
                        latitude: e.latLng.lat(),
                        longitude: e.latLng.lng()
                    });
                    delete custMarkerDetailArr[locationKey];

                    $(document).trigger('maps.marker.remove', [locationKey]);

                    marker = null;


                    $('#popDialog').modal('hide');
                    return;
                };
                btnConfirm.text = "Yes";

                var button = [btnConfirm, btnCancel];

                Main.popDialog("Confirm", "Are you sure you want to remove this store?", button, null, null);


            });

            marker.addListener("click", function (e) {
                that.removeAllMarkerAnimation();
                curMarker = this;
                curMarker.setAnimation(google.maps.Animation.BOUNCE);
                var locationKey = that.locationToKey({
                    latitude: e.latLng.lat(),
                    longitude: e.latLng.lng()
                });

                $(document).trigger('maps.marker.active', [locationKey]);

                that.switchMaps(this);
            });

            that.switchMaps(marker);

            markers.push(marker);
            if (updateLoc) {
                this.getDetailsByLocation(location);
            }
        },

        getDetailsByLocation: function (location) {
            var that = this;
            var geocoder = new google.maps.Geocoder();
            geocoder.geocode({
                'latLng': location
            }, function (results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                    if (results[0]) {
                        var component = results[0];
                        var streetNumber = "",
                            sublocality = "",
                            locality = "",
                            city = "",
                            postalCode = "",
                            state = "",
                            region = "",
                            country = "";
                        var addressComponents = component.address_components;

                        for (var i in addressComponents) {
                            if (addressComponents.hasOwnProperty(i)) {
                                if (addressComponents[i].types[0] == 'country') {
                                    country = addressComponents[i].long_name;
                                    break;
                                }
                            }
                        }

                        for (var i in addressComponents) {
                            if (addressComponents.hasOwnProperty(i)) {
                                for (var j in addressComponents[i].types) {
                                    if (addressComponents[i].types.hasOwnProperty(j)) {
                                        if (addressComponents[i].types[j] == "street_number") {
                                            streetNumber = component.address_components[i].long_name;
                                        }
                                        if (addressComponents[i].types[j] == "route") {
                                            sublocality = component.address_components[i].long_name;
                                        }
                                        if (addressComponents[i].types[j] == "sublocality") {
                                            locality = component.address_components[i].long_name;
                                        }
                                        if (addressComponents[i].types[j] == "locality") {
                                            city = component.address_components[i].long_name;
                                        }
                                        if (addressComponents[i].types[j] == "postal_code") {
                                            postalCode = component.address_components[i].long_name;
                                        }
                                        if (addressComponents[i].types[j] == "administrative_area_level_2") {
                                            state = component.address_components[i].long_name;
                                        }
                                        if (addressComponents[i].types[j] == "administrative_area_level_1") {
                                            region = component.address_components[i].long_name;
                                        }
                                    }

                                }
                            }

                        }

                        var addressObj = {
                            "streetnumber": streetNumber,
                            "sublocality": sublocality,
                            "locality": locality,
                            "city": city,
                            "postalCode": postalCode,
                            "state": state,
                            "region": region,
                            "country": country,
                            "position": location
                        };

                        var loc = {
                            latitude: location.lat,
                            longitude: location.lng
                        };

                        var locationKey = that.locationToKey(loc);
                        custMarkerDetailArr[locationKey] = addressObj;
                        addressObj.key = locationKey;
                        //console.log(addressObj);
                        $(document).trigger('maps.marker.add', [addressObj]);

                        $(document).trigger('maps.marker.active', [locationKey]);
                    }
                }
            });
        },

        locationToKey: function (location) {
            //console.log(location);
            var latitude = location.latitude === undefined ? location.lat() : location.latitude;
            var longitude = location.longitude === undefined ? location.lng() : location.longitude;
            latitude = Number(latitude).toFixed(7);
            longitude = Number(longitude).toFixed(7);
            return (latitude + '_' + longitude).replace(/[.-]/g, '_');
        },

        removeAllMarkerAnimation: function (customMarkers) {
            if (customMarkers !== undefined) markers = customMarkers;
            for (var i in markers) {
                if (markers.hasOwnProperty(i)) {
                    markers[i].setAnimation(null);
                }
            }
        },

        changeMapStatus: function (read, multiMarker) {
            readOnly = read;
            isMultiMarker = multiMarker;
        },

        switchMaps: function (marker) {
            var current_index = markers.indexOf(marker);
            var markers_length = markers.length;
            //Main.log(current_index + " length " + markers_length);
            var prev = $("#prevMarker");
            var next = $("#nextMarker");
            prev.attr('data-index', current_index - 1);
            next.attr('data-index', current_index + 1);
            if (markers_length > 1) {
                if (current_index === 0) {
                    prev.attr('disabled', 'disabled');
                } else {
                    prev.removeAttr('disabled', 'disabled');
                }
                if (current_index == markers_length - 1) {
                    next.attr('disabled', 'disabled');
                } else {
                    next.removeAttr('disabled', 'disabled');
                }
            } else {
                prev.attr('disabled', 'disabled');
                next.attr('disabled', 'disabled');
            }
        },

        addContent: function (config) {
            var content = "",
                contentEl = "",
                position = 3;

            if (!config) {
                return;
            }

            if (config.msg) {
                content = config.msg;
            }

            if (config.placement) {
                switch (config.placement) {
                    case 'TOP_LEFT':
                        position = 1;
                        break;
                    case 'TOP_CENTER':
                    case 'TOP':
                        position = 2;
                        break;
                    case 'TOP_RIGHT':
                        position = 3;
                        break;
                    case 'LEFT_CENTER':
                        position = 4;
                        break;
                    case 'LEFT_TOP':
                    case 'LEFT':
                        position = 5;
                        break;
                    case 'LEFT_BOTTOM':
                        position = 6;
                        break;
                    case 'RIGHT_TOP':
                    case 'RIGHT':
                        position = 7;
                        break;
                    case 'RIGHT_CENTER':
                        position = 8;
                        break;
                    case 'RIGHT_BOTTOM':
                        position = 9;
                        break;
                    case 'BOTTOM_LEFT':
                        position = 10;
                        break;
                    case 'BOTTOM_CENTER':
                    case 'BOTTOM':
                        position = 11;
                        break;
                    case 'BOTTOM_RIGHT':
                        position = 12;
                        break;
                    case 'CENTER':
                        position = 13;
                        break;
                    default:
                        position = 3;
                }
            }

            if (document.getElementById('customContent')) {
                document.getElementById('customContent').innerHTML = content;
            } else {
                contentEl = document.createElement('div');
                contentEl.id = "customContent";
                contentEl.innerHTML = content;
                contentEl.className = 'custom-map-content';
            }

            if (config.prop && typeof config.prop === 'function') {
                contentEl.addEventListener('click', config.prop);
            }

            map.controls[position].push(contentEl);
        }
    };

    return {
        init: function (config) {
            return showmarkers.init(config);
        },

        getKeyByLocation: function (location) {
            //location = {latitude:"", longitude:""}
            return showmarkers.locationToKey(location);
        },

        addMarkersToMap: function (map, location, updateLoc) {
            return showmarkers.addMarker(map, location, updateLoc);
        },

        updateMapStatus: function (readOnly, isMultiMarker) {
            showmarkers.changeMapStatus(readOnly, isMultiMarker);
        },

        showContent: function (config) {
            if (config && typeof config === 'object') {
                showmarkers.addContent(config);
            }
        }
    };
})();