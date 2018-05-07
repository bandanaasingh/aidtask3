<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <link rel="apple-touch-icon" sizes="76x76" href="./assets/img/apple-icon.png">
    <link rel="icon" type="image/png" href="./assets/img/favicon.ico">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>Office Catering Made Easy | Powered by ANYORDER</title>
    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no' name='viewport' />
    <!--     Fonts and icons     -->
    <link href="https://fonts.googleapis.com/css?family=Open+Sans+Condensed:300|Open+Sans:300,400,700|Oswald:400,500,600" rel="stylesheet">
    <link rel="stylesheet" href="/resources/custom/css/client/font-awesome.min.css" />
    <link rel="stylesheet" href="/resources/custom/css/client/bootstrap-select.min.css">
    <!-- CSS Files -->

    <link href="/resources/custom/css/client/bootstrap.min.css" rel="stylesheet" />
    <link href="/resources/custom/css/client/main.css?v=1.1.0" rel="stylesheet" />
    <link href="/resources/custom/css/client/added-styles.css" rel="stylesheet" />

</head>

<body class="index-page sidebar-collapse">
<!-- Navbar -->
<nav class="navbar navbar-expand-lg bg-primary fixed-top navbar-transparent " color-on-scroll="400">
    <div class="container">
        <div class="navbar-translate">
            <a class="navbar-brand" href="index.html" rel="tooltip" title="" data-placement="bottom" target="_blank">
                ANYORDER Services
            </a>
            <button class="navbar-toggler navbar-toggler" type="button" data-toggle="collapse" data-target="#navigation" aria-controls="navigation-index" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-bar bar1"></span>
                <span class="navbar-toggler-bar bar2"></span>
                <span class="navbar-toggler-bar bar3"></span>
            </button>
        </div>
        <div class="collapse navbar-collapse justify-content-end" id="navigation" data-nav-image="/resources/custom/images/client/blurred-image-1.jpg">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="javascript:void(0)" onclick="scrollToDownload()">
                        <p>About</p>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" target="_blank">
                        <p>Deliveries</p>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/signup" target="_blank">
                        <p>Signup</p>
                    </a>
                </li>

            </ul>
        </div>
    </div>
</nav>
<!-- End Navbar -->
<div class="wrapper">
    <div class="page-header clear-filter" filter-color="black">
        <div class="page-header-image" data-parallax="true" style="background-image: url('/resources/custom/images/client/header.jpg');">
        </div>
        <div class="container">
            <div class="content-center brand" id="start">
                <img class="n-logo" src="/resources/custom/images/client/logo.svg" alt="">
                <h1 class="h1-seo">Corporate Meal Delivery. Easy.</h1>
                <h5 class="h5-seo">Bring your office together with family style meals curated for your cravings.</h5>
            </div>
            <div class="off-top col-lg-12 col-md-12">

                <form class="form" method="POST" action="/items" id="item_search">
                    <div class="selected-item justify-content-md-center">
                        <p><span>Get Started</span></p>
                    </div>
                    <div id="container" class="row">
                        <div class="col-sm-6 col-lg-3">
                            <div class="form-group">
                                <select class="form-control filter-option pull-left" id="event" name="event" data-style="btn-info">
                                    <option>Choose Event</option>
                                    <option value="2">IFTAR</option>
                                    <option value="3">breakfast</option>
                                    <option value="4">lunch</option>
                                    <option value="5">snacks</option>
                                    <option value="6">dinner</option>
                                    <option value="7">birthday</option>
                                    <option value="8">others</option>
                                </select>

                            </div>
                        </div>
                        <div class="col-sm-6 col-lg-3">
                            <div class="form-group has-success">
                                <select class="form-control" id="location" name="location">
                                    <option>Your Location</option>
                                    <option value="2">Downtown Dubai</option>
                                    <option value="3">Business Bay</option>
                                    <option value="4">DIFC</option>
                                    <option value="5">Dubai Marina</option>
                                    <option value="6">JLT</option>
                                    <option value="7">Other Category</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-sm-6 col-lg-3">
                            <div class="form-group has-danger">
                                <input type="text" class="form-control date-picker" id="date" name="date" data-datepicker-color="info">
                            </div>
                        </div>
                        <div class="col-sm-6 col-lg-2">
                            <div class="form-group">
                                <div class="quantity">

                                    <input type="number" step="1" max="99" min="1" value="1" id="number" name="number" class="qty form-control"
                                           size="4">
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6 col-lg-1">
                            <div class="form-group">
                                <button type="submit" href="" class="btn btn-success" id="searchBtn">GO</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="main">
        <div class="section">
            <div class="container text-center">
                <div class="row justify-content-md-center">
                    <div class="col-md-12 col-lg-8">
                        <h6 class="title">What we Deliver</h6>
                        <h3 class="section-description">We partnered with restaurants and caterers to deliver meals for those breaking bread together in the office</h3>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <div class="row justify-content-md-center">
                            <div class="col-md-10">
                                <div class="portfolio-3__projects__column">
                                    <a class="portfolio-3__projects__item" href="recommended-meals.html" data-aos="fade-up">
                                            <span> 
                                                <img src="/resources/custom/images/client/projects/project-4.jpg" alt="Thumbnail Image" class="img-fluid portfolio-3__projects__item__image">
                                            </span>
                                        <span class="portfolio-3__projects__item__content">
                                                <span class="portfolio-3__projects__item__tag tag-2">ARABIC / MIDDLE EASTERN</span>
                                                <span class="portfolio-3__projects__item__title h2">Halla Mandi</span>
                                                <span class="portfolio-3__projects__item__text">For those lovin' mandi and more from the newest hotspot of Arabic cuisine in Dubai</span>
                                            </span>
                                    </a>
                                </div>
                            </div>
                            <div class="col-md-5">
                                <div class="portfolio-3__projects__column">
                                    <a class="portfolio-3__projects__item" href="recommended-meals.html" data-aos="fade-up">
                                            <span> 
                                                <img src="/resources/custom/images/client/projects/project-1.jpg" alt="Thumbnail Image" class="img-fluid portfolio-3__projects__item__image">
                                            </span>
                                        <span class="portfolio-3__projects__item__content">
                                                <span class="portfolio-3__projects__item__tag tag-2">SUSHI / JAPANESE</span>
                                                <span class="portfolio-3__projects__item__title h2">Sushi Nation</span>
                                                <span class="portfolio-3__projects__item__text">For those Sushi and Japanese food-lovers out there</span>
                                            </span>
                                    </a>
                                </div>
                            </div>
                            <div class="col-md-5">
                                <div class="portfolio-3__projects__column">
                                    <a class="portfolio-3__projects__item" href="recommended-meals.html" data-aos="fade-up">
                                            <span> 
                                                <img src="/resources/custom/images/client/projects/project-2.jpg" alt="Thumbnail Image" class="img-fluid portfolio-3__projects__item__image">
                                            </span>
                                        <span class="portfolio-3__projects__item__content">
                                                <span class="portfolio-3__projects__item__tag tag-2">BRAZILIAN / HEALTHY</span>
                                                <span class="portfolio-3__projects__item__title h2">Brazilian Cafe</span>
                                                <span class="portfolio-3__projects__item__text">Want a taste of Brazil? This is the joint for you</span>
                                            </span>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="section-gray">
            <div class="container text-center">
                <div class="row">
                    <div class="col-md-12 col-lg-12 col-xs-12">
                        <div class="row justify-content-md-center">
                            <div class="col-md-12 col-lg-8">
                                <h2 class="title">Curated Meals for Your Team</h2>
                                <h5 class="description">We bring fresh delicious, healthy meals from locally owned restaurants and neighborhood favorites for any event you're planning. Morning, day or night, you're supporting local businesses when you order with us.</h5>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4">
                                <h4 class="trifold-headers">WE DELIVER</h4>
                                <p>Our service is stress free, on time and easy to customize</p>
                            </div>
                            <div class="col-md-4">
                                <h4 class="trifold-headers">WE SUPPORT LOCAL</h4>
                                <p>Our partner restaurants favor quality, variety and sustainability</p>
                            </div>
                            <div class="col-md-4">
                                <h4 class="trifold-headers">WE LET YOU ENJOY CULTURE</h4>
                                <p>Our meals strengthen company culture by bringing teams together</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="section">
            <div class="container text-center">
                <div class="row justify-content-md-center">
                    <div class="col-md-12 col-lg-8">
                        <h2 class="title">Now Who's Hungry?</h2>
                        <h5 class="description">All you need is to choose the event, select your area, the date and time and start ordering team meals. Feed your people now.</h5>

                        <a href="#start" class="btn btn-primary btn-lg btn-round" role="button">Start Now</a>


                    </div>
                </div>
            </div>
        </div>

        <div class="section section-signup" style="background-image: url('/resources/custom/images/client/bg11.jpg'); background-size: cover; background-position: top center; min-height">
            <div class="container">
                <div class="row">
                </div>
            </div>
        </div>
    </div>
    <!-- Sart Modal -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header justify-content-center">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <i class="now-ui-icons ui-1_simple-remove"></i>
                    </button>
                    <h4 class="title title-up">Modal title</h4>
                </div>
                <div class="modal-body">
                    <p>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth.
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default">Nice Button</button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    <!--  End Modal -->
    <!-- Mini Modal -->
    <div class="modal fade modal-mini modal-primary" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header justify-content-center">
                    <div class="modal-profile">
                        <i class="now-ui-icons users_circle-08"></i>
                    </div>
                </div>
                <div class="modal-body">
                    <p>Always have an access to your profile</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-link btn-neutral">Back</button>
                    <button type="button" class="btn btn-link btn-neutral" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    <!--  End Modal -->
    <footer class="footer" data-background-color="black">
        <div class="container">
            <nav>
                <ul>
                    <li>
                        <a href="https://www.creative-tim.com">
                            Creative Tim
                        </a>
                    </li>
                    <li>
                        <a href="http://presentation.creative-tim.com">
                            About Us
                        </a>
                    </li>
                    <li>
                        <a href="http://blog.creative-tim.com">
                            Blog
                        </a>
                    </li>
                    <li>
                        <a href="https://github.com/creativetimofficial/now-ui-kit/blob/master/LICENSE.md">
                            MIT License
                        </a>
                    </li>
                </ul>
            </nav>
            <div class="copyright">
                &copy;
                <script>
                    document.write(new Date().getFullYear())
                </script>, Corporate Meals
                <a href="http://www.anyorder.ae" target="_blank">Delivery</a>. Powered by
                <a href="https://www.anyorder.ae" target="_blank">ANYORDER</a>.
            </div>
        </div>
    </footer>
</div>
</body>
<!--   Core JS Files   -->
<script src="/resources/custom/js/client/core/jquery.3.2.1.min.js" type="text/javascript"></script>
<script src="/resources/custom/js/client/core/popper.min.js" type="text/javascript"></script>
<script src="/resources/custom/js/client/core/bootstrap.min.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/js/bootstrap-select.min.js"></script>
<!--  Plugin for Switches, full documentation here: http://www.jque.re/plugins/version3/bootstrap.switch/ -->
<script src="/resources/custom/js/client/plugins/bootstrap-switch.js"></script>
<!--  Plugin for the Sliders, full documentation here: http://refreshless.com/nouislider/ -->
<script src="/resources/custom/js/client/plugins/nouislider.min.js" type="text/javascript"></script>
<!--  Plugin for the DatePicker, full documentation here: https://github.com/uxsolutions/bootstrap-datepicker -->
<script src="/resources/custom/js/client/plugins/bootstrap-datepicker.js" type="text/javascript"></script>
<!-- Control Center for Now Ui Kit: parallax effects, scripts for the example pages etc -->
<script src="/resources/custom/js/client/now-ui-kit.js?v=1.1.0" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/prefixfree/1.0.7/prefixfree.min.js"></script>

<script src="/resources/custom/js/Main.js" type="text/javascript"></script>

<%@include file="includes/validate.jsp" %>
<%@include file="includes/selectPicker.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        // the body of this function is in assets/js/now-ui-kit.js
        nowuiKit.initSliders();

        $('#item_search').submit(function (e) {
            e.preventDefault();
            console.log('submit');
        }).validate({
            rules: {
                event: {
                    required: true
                },
                location:{
                    required: true
                },
                date: {
                    required: true
                },
                number: {
                    required: true,
                    number: true
                }
            },
            submitHandler: function () {
                console.log('submit handler');
                location.href=$('#item_search').attr('action');
            }
        });
    });

    function scrollToDownload() {
        if ($('.section-download').length != 0) {
            $("html, body").animate({
                scrollTop: $('.section-download').offset().top
            }, 1000);
        }
    }

</script>

</html>