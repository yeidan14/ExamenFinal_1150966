<%-- 
    Document   : index
    Created on : 10/11/2019, 01:29:58 PM
    Author     : Daniel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
   <head>

    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Meta -->
    <meta name="description" content="Responsive Bootstrap 4 Dashboard and Admin Template">
    <meta name="author" content="ThemePixels">

    <!-- Favicon -->
    <link rel="shortcut icon" type="image/x-icon" href="Librerias/assets/img/favicon.png">

    <title>Login</title>

    <!-- vendor css -->
    <link href="Librerias/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">
    <link href="Librerias/ionicons/css/ionicons.min.css" rel="stylesheet">

    <!-- template css -->
    <link rel="stylesheet" href="Librerias/assets/css/cassie.css">

  </head>
  <body>

    <div class="signin-panel">
       <img src="Librerias/assets/img/fondo.jpg" width="100%">
      <div class="signin-sidebar">
        <div class="signin-sidebar-body">
            <a href="index.jsp" class="sidebar-logo mg-b-40"><span>EXAMEN FINAL</span></a>
          <h4 class="signin-title">Bienvenido</h4>
          <h5 class="signin-subtitle">Inicia Session para continuar</h5>

          <div class="signin-form">
              <form name="login" action="Login.do" method="_POST">
            <div class="form-group">
              <label>Usuario</label>
              <input type="text" name="user" class="form-control" placeholder="Ingrese su usuario" value="Usuario">
            </div>

            <div class="form-group">
              <label class="d-flex justify-content-between">
                <span>Pass</span>
                <a href="" class="tx-13">Olvido su contraseña?</a>
              </label>
              <input name="pass" type="password" class="form-control" placeholder="Enter your password" value="yourpassword">
            </div>

            <div class="form-group d-flex mg-b-0">
              <button class="btn btn-brand-01 btn-uppercase flex-fill">Ingresar</button>
              <a href="Registro.jsp" class="btn btn-white btn-uppercase flex-fill mg-l-10">Registrar</a>
            </div>
              </form>
            <div class="divider-text mg-y-30"></div>
<img src="Librerias/assets/img/logosistemas.png" width="100%">
          </div>
          
         </div><!-- signin-sidebar-body -->
      </div><!-- signin-sidebar -->
    </div><!-- signin-panel -->

    <script src="Librerias/jquery/jquery.min.js"></script>
    <script src="Librerias/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="Librerias/feather-icons/feather.min.js"></script>
    <script src="Librerias/perfect-scrollbar/perfect-scrollbar.min.js"></script>

    <script>
      $(function(){

        'use strict'

        feather.replace();

        new PerfectScrollbar('.signin-sidebar', {
          suppressScrollX: true
        });

      })
    </script>
    <script src="Librerias/assets/js/svg-inline.js"></script>
  </body>
</html>
