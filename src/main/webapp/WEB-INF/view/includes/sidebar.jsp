<%--
  Created by IntelliJ IDEA.
  User: Frank
  Date: 4/25/2018
  Time: 10:33 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Side Navbar -->
<nav class="side-navbar">
    <!-- Sidebar Header-->
    <div class="sidebar-header d-flex align-items-center">
        <div class="avatar"><img src="/resources/custom/images/avatar-1.jpg" alt="..." class="img-fluid rounded-circle"></div>
        <div class="title">
            <h1 class="h4">Admin BigOrder</h1>
        </div>
    </div>
    <!-- Sidebar Navidation Menus--><!--<span class="heading">Main</span>-->
    <ul class="list-unstyled">
        <li class="active"><a href="/organizer/dashboard"> <i class="fa fa-home"></i>Dashboard </a></li>
        <li><a href="/organizer/merchant"> <i class="fa fa-users"></i>Merchant </a></li>
        <%--<sec:authorize access="hasRole('ROLE_MERCHANT')">--%>
            <li><a href="/merchant/store_list"> <i class="fa fa-bank"></i>Stores </a></li>
        <%--</sec:authorize>
        <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')">--%>
            <li><a href="/organizer/area_form?type=addRoot"> <i class="fa fa-location-arrow"></i>Area </a></li>
            <li><a href="/organizer/category"> <i class="fa fa-location-arrow"></i>Category </a></li>
        <%--</sec:authorize>--%>
        <!--<li><a href="#exampledropdownDropdown" aria-expanded="false" data-toggle="collapse"> <i class="icon-interface-windows"></i>Example dropdown </a>
          <ul id="exampledropdownDropdown" class="collapse list-unstyled ">
            <li><a href="#">Page</a></li>
            <li><a href="#">Page</a></li>
            <li><a href="#">Page</a></li>
          </ul>
        </li>-->
        <li><a href="/smanager/order"> <i class="fa fa-file-text"></i>Order </a></li>
        <li><a href="/organizer/notification"> <i class="fa-paper-plane"></i>Notification </a></li>
    </ul>
    <!--<span class="heading">Extras</span>
    <ul class="list-unstyled">
      <li> <a href="#"> <i class="icon-flask"></i>Demo </a></li>
      <li> <a href="#"> <i class="icon-screen"></i>Demo </a></li>
      <li> <a href="#"> <i class="icon-mail"></i>Demo </a></li>
      <li> <a href="#"> <i class="icon-picture"></i>Demo </a></li>
    </ul>-->
</nav>
