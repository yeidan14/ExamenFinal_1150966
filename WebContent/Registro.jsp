<%-- 
    Document   : Registro
    Created on : 10/11/2019, 12:24:42 PM
    Author     : Daniel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
     <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Meta -->
    <meta name="description" content="Responsive Bootstrap 4 Dashboard and Admin Template">
    <meta name="author" content="ThemePixels">

    <!-- Favicon -->
    <link rel="shortcut icon" type="image/x-icon" href="Librerias/assets/img/favicon.png">

    <title>Segundo Parcial</title>

    <!-- vendor css -->
    <link href="Librerias/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">
    <link href="Librerias/ionicons/css/ionicons.min.css" rel="stylesheet">

    <!-- template css -->
    <link rel="stylesheet" href="Librerias/assets/css/cassie.css">

  </head>
  <body>

    <div class="signup-panel">
        <img src="Librerias/assets/img/fondo.jpg" width="100%">
      <div class="signup-sidebar">
        <div class="signup-sidebar-body">
          <a href="dashboard-one.html" class="sidebar-logo mg-b-40"><span>SEGUNDO PARCIAL</span></a>
          <h4 class="signup-title">REGISTRO</h4>
          <h5 class="signup-subtitle">Registra tu usuario</h5>
          <div class="signup-form">
              
              <form name="registro" action="Registro.do">

            <div class="row">
              <div class="col-sm-6">
                <div class="form-group">
                  <label>Nombres</label>
                  <input type="text" name="nombre" class="form-control" placeholder="Enter your firstname" value="Annie Lee">
                </div>
              </div><!-- col -->
              <div class="col-sm-6">
                <div class="form-group">
                  <label>Apllidos</label>
                  <input type="text" name="apellido" class="form-control" placeholder="Enter your lastname" value="Christensen">
                </div>
              </div><!-- col -->
            </div><!-- row -->

            <div class="row mg-b-5">
              <div class="col-sm-6">
                <div class="form-group">
                  <label>Email address</label>
                  <input type="email" class="form-control" placeholder="Enter your email" value="yourname@yourdomain.com">
                </div>
              </div><!-- col -->
              <div class="col-sm-6">
                <div class="form-group">
                  <label>Password</label>
                  <input type="password" class="form-control" placeholder="Enter your password" value="mypassword">
                </div>
              </div><!-- col -->
            </div><!-- row -->

            <div class="form-group mg-b-30">
              <div class="custom-control custom-checkbox">
                <input type="checkbox" class="custom-control-input" id="agree">
                <label class="custom-control-label tx-sm" for="agree">Acepta Terminos y Conidicones! <a href="">Terms of Use</a> and <a href="">Privacy Policy</a></label>
              </div>
            </div>
               <div class="divider-text mg-y-30"></div>
            <div class="form-group d-flex mg-b-0">
                <button  type="submit" class="btn btn-brand-01 btn-uppercase btn-block">REGISTRAR</button>
            </div>
</form>
         

            
          </div>
          <p class="mg-t-auto mg-b-0 tx-color-03">Ya Tienes una Cuenta ? <a href="index.jsp">Inicia Session</a></p>
        </div><!-- signup-sidebar-body -->
      </div><!-- signup-sidebar -->
    </div><!-- signup-panel -->

    <script src="Librerias/jquery/jquery.min.js"></script>
    <script src="Librerias/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="Librerias/feather-icons/feather.min.js"></script>
    <script src="Librerias/perfect-scrollbar/perfect-scrollbar.min.js"></script>

    <script>
      $(function(){

        'use strict'

        feather.replace();

        new PerfectScrollbar('.signup-sidebar', {
          suppressScrollX: true
        });

      })
    </script>
    <script src="Librerias/assets/js/svg-inline.js"></script>
  </body>
</html>
</html>
