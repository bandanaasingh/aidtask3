<!-- jQuery -->
<script src="${pageContext.request.contextPath}/resources/vendors/jquery/dist/jquery.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/vendors/moment/min/moment.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/vendors/popper/popper.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/vendors/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/vendors/jasny-bootstrap/dist/js/jasny-bootstrap.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/custom/js/Main.js"></script>
<script src="${pageContext.request.contextPath}/resources/custom/js/admin.js"></script>
<script>

    $(function () {

        // Loader
        $('body').addClass('loader_div clearfix').append('<div class="loader"></div>');

        // Form
        $('.form-group.floating .form-control').on('focus blur', function (e) {
            $(this).parents('.form-group.floating').toggleClass('focused', (e.type === 'focus' || this.value.length > 0 ));
        }).trigger('blur');

        $('.nav-sidebar').children('li').tooltip({container: 'body'}).tooltip('disable');

        // Sidebar
        $('[data-toggle="menu"]').click(function () {
            if ($(this).attr('data-status') == "true") {
                $(this).attr('data-status', 'false');
                $('#sidebar').attr('data-menu', 'sm');
                $('#content').attr('data-content', 'lg');
                $('.nav-sidebar').children('li').tooltip('enable');
                // $('#aoLogo').attr('src', $('#aoLogo').attr('src').replace('logo-lg.png', 'logo-sm.png'));
                if (!$('li[data-menu="orders"]').hasClass('closed')) {
                    $('.nav-sidebar-list-clone').addClass('move-right');
                }
            } else {
                $(this).attr('data-status', 'true');
                $('#sidebar').attr('data-menu', 'lg');
                $('#content').attr('data-content', 'sm');
                $('.nav-sidebar').children('li').tooltip('disable');
                $('.nav-sidebar-list-clone').removeClass('move-right');
                //$('#aoLogo').attr('src', $('#aoLogo').attr('src').replace('logo-sm.png', 'logo-lg.png'));
            }
            Main.saveInLocalStorage('isMenuLarge', $(this).attr('data-status'));
            $(window).trigger('resize');
        });

        var isMenuLargeStorage = Main.getFromLocalStorage('isMenuLarge');
        var isMenuLargeView = $('[data-toggle="menu"]').attr('data-status');

        if (isMenuLargeStorage !== isMenuLargeView) {
            $('[data-toggle="menu"]').click();
        }

        $('li[data-menu="orders"]').on('click', function (event) {
            event.stopPropagation();
            $(this).toggleClass('closed').blur();

            if ($(this).closest('#sidebar').attr('data-menu') === 'sm') {
                if (!$('.nav-sidebar-list-clone').length) {
                    var topValue = $(this).position().top + 2;
                    var leftValue = $('#sidebar').width();
                    var elem = $(this).find('.nav-sidebar-list').clone();
                    elem.removeClass('nav-sidebar-list').addClass('nav-sidebar-list-clone');
                    elem.css({
                        position: 'absolute',
                        top: topValue + 'px',
                        left: leftValue,
                    });
                    $('body').append(elem);
                }
                if ($(this).hasClass('closed')) {
                    $('.nav-sidebar-list-clone').removeClass('move-right');
                } else {
                    $('.nav-sidebar-list-clone').addClass('move-right');
                }
            }
        });

        $('.collapse').on('hidden.bs.collapse', function () {
            $(this).parent().find('.nav-collapse .fa-chevron-down').removeClass('fa-chevron-down').addClass('fa-chevron-right');
        });

        $('.collapse').on('shown.bs.collapse', function () {
            $(this).parent().find('.nav-collapse .fa-chevron-right').removeClass('fa-chevron-right').addClass('fa-chevron-down');
        });

        // Notification List
        /*if (Main.userRole === "ROLE_MERCHANT") {
         if ($('#notificationTogglr').length) {
         adminModule.notification();
         $('#notificationTogglr').on('click', function (event) {
         event.preventDefault();
         $('#notificationListSidebar').toggleClass('in');
         });
         }
         }*/

        // Modal
        $('.modal').on('hidden.bs.modal', function (e) {
            $('.modal-backdrop.in').remove();
        });


        if (Main.getFromLocalStorage('userRole') === "ROLE_MERCHANT") {
            $('#userRoleTop').text(Main.getFromLocalStorage('businessName'));
        } else if (Main.getFromLocalStorage('userRole') === "ROLE_ADMIN") {
            $('#userRoleTop').text(Main.ucfirst(Main.getFromLocalStorage('userRole').replace('ROLE_', '').replace('_', ' ')));
        } else {
            if (Main.getFromLocalStorage('userTitle') != undefined)
                $('#userRoleTop').text(Main.ucfirst(Main.getFromLocalStorage('userTitle')));
        }

        if ($('.dataTable').length) {
            $(window).resize();
        }

        //adminModule.orderCount();

        $('body').removeClass('loader_div clearfix');
        $('.loader').remove();
        $('[data-toggle="tooltip"]').tooltip();

    });

    function colorAndScroll(e) {
        var searchBar = $(e.target);
        var searchKey = searchBar.val();
        var wrapper = $('#servingAreaWrapper');
        if (searchKey === '') {
            wrapper.find('span.c-t-success').removeClass('c-t-success');
            wrapper.parent().animate({
                scrollTop: 0
            }, 500);
            return;
        }
        $.each(wrapper.find('div.s-area-box-header span.fontbold.font16'), function (index, data) {
            var that = $(this);
            var text = $(this).html();
            if (text.toLowerCase().indexOf(searchKey.toLowerCase()) > -1 && text != "Select All") {
                that.addClass('c-t-success');
                wrapper.parent().animate({
                    scrollTop: (that.parents().eq(2).position().top)
                }, 500);
                return false;
            }
        });
        var modal = $('#servingAreaModal');
        if (modal.length) {
            modal.one('hidden.bs.modal', function () {
                if (searchBar.val().length) {
                    searchBar.val('').trigger('keyup');
                    return;
                }
            });
        }
    }

    function searchAndScroll(e) {
        var searchBar = $(e.target);
        var searchKey = searchBar.val();
        var wrapper = $('#storeWrapper');
        if (searchKey === '') {
            wrapper.find('span.c-t-success').removeClass('c-t-success');
            wrapper.parent().animate({
                scrollTop: 0
            }, 500);
            return;
        }
        console.log('test');
        $.each(wrapper.find('div.pt5 label'), function (index, data) {
            var that = $(this);
            var text = $(this).html();
            if (text.toLowerCase().indexOf(searchKey.toLowerCase()) > -1 && text != "Select All") {
                that.addClass('c-t-success');
                wrapper.parent().animate({
                    scrollTop: (that.parents().eq(2).position().top)
                }, 500);
                return false;
            }
        });
        var modal = $('#assignDeliveryZoneModal');
        if (modal.length) {
            modal.one('hidden.bs.modal', function () {
                if (searchBar.val().length) {
                    searchBar.val('').trigger('keyup');
                    return;
                }
            });
        }
    }

</script>