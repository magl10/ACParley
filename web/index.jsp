<!DOCTYPE html>
<html lang="en" data-ng-app="app">
<head>
  <meta charset="utf-8" />
  <title>Administrador de Parlay</title>
  <meta name="description" content="app, web app, responsive, responsive layout, admin, admin panel, admin dashboard, flat, flat ui, ui kit, AngularJS, ui route, charts, widgets, components" />
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
  <link rel="stylesheet" href="asset/css/bootstrap.css" type="text/css" />
  <link rel="stylesheet" href="asset/css/bootstrap-select.min.css" />
  <link rel="stylesheet" href="asset/css/animate.css" type="text/css" />
  <link rel="stylesheet" href="asset/css/font-awesome.min.css" type="text/css" />
  <link rel="stylesheet" href="asset/css/simple-line-icons.css" type="text/css" />
  <link rel="stylesheet" href="asset/css/font.css" type="text/css" />
  <link rel="stylesheet" href="asset/css/app.css" type="text/css" />
  <link rel="stylesheet" href="asset/css/myStyle.css" type="text/css"/>
</head>
<body ng-controller="AppCtrl">
  <div class="app" id="app" ng-class="{'app-header-fixed':app.settings.headerFixed, 'app-aside-fixed':app.settings.asideFixed, 'app-aside-folded':app.settings.asideFolded}" ui-view></div>
  <!-- Format Number Currency -->
  <script src="asset/js/libs/accounting.min.js"></script>
  <!-- jQuery -->
  <script src="asset/js/jquery/jquery.min.js"></script>
  <script src="asset/js/jquery/jquery-ui.js"></script>
   <!-- Notifications -->
   <script src="asset/js/libs/Notifier.js"></script>
  <!-- Bootstrap-->
   <script src="asset/js/libs/bootstrap.min.js"></script>
  <!-- Angular -->
  <script src="asset/js/angular/angular.min.js"></script>
  <script src="asset/js/angular/angular-cookies.min.js"></script>
  <script src="asset/js/angular/angular-animate.min.js"></script>
  <script src="asset/js/angular/angular-ui-router.min.js"></script>
  <script src="asset/js/angular/angular-translate.js"></script>
  <script src="asset/js/angular/ngStorage.min.js"></script>
  <script src="asset/js/angular/ui-load.js"></script>
  <script src="asset/js/angular/ui-jq.js"></script>
  <script src="asset/js/angular/ui-validate.js"></script>
  <script src="asset/js/angular/ui-bootstrap-tpls.min.js"></script>
  <script src="asset/js/angular/ng-google-chart.js"></script>
  
  <!-- App -->
  <script src="app/app.js"></script>
  <script src="app/services.js"></script>
  <script src="app/controllers.js"></script>
  <script src="app/filters.js"></script>
  <script src="app/directives.js"></script>
  <!-- Lazy loading -->
</body>
</html>