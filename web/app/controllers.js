'use strict';

/* Controllers */
angular.module('app.controllers', ['pascalprecht.translate', 'ngCookies'])
.controller('AppCtrl', ['$rootScope','$scope', '$translate', '$localStorage', '$window','AuthenticationService','$location','$modal', 
  function($rootScope ,$scope,   $translate,   $localStorage,   $window, AuthenticationService,$location,$modal ) {
    // add 'ie' classes to html
    var isIE = !!navigator.userAgent.match(/MSIE/i);
    isIE && angular.element($window.document.body).addClass('ie');
    isSmartDevice( $window ) && angular.element($window.document.body).addClass('smart');
    // config
    $scope.production = true;
    $scope.app = {
      name: 'AC-Parlay',
      version: '0.1',
      // for chart colors
      color: {
        primary: '#7266ba',
        info:    '#23b7e5',
        success: '#27c24c',
        warning: '#fad733',
        danger:  '#f05050',
        light:   '#e8eff0',
        dark:    '#3a3f51',
        black:   '#1c2b36'
      },
      settings: {
        themeID: 1,
        navbarHeaderColor: 'bg-black',
        navbarCollapseColor: 'bg-white-only',
        asideColor: 'bg-black',
        headerFixed: true,
        asideFixed: false,
        asideFolded: false
      },
      user:{
          name: "",
          username: "",
          office: ""
      }
    };
    if($rootScope.globals){
        if ($rootScope.globals.currentUser) {
            $scope.app.user.name =  $rootScope.globals.currentUser.userdat.nombre;
            $scope.app.user.username =  $rootScope.globals.currentUser.userdat.username;
            $scope.app.user.office = $rootScope.globals.currentUser.userdat.officeid;
        }
    }
    // save settings to local storage
    if ( angular.isDefined($localStorage.settings) ) {
      $scope.app.settings = $localStorage.settings;
    } else {
      $localStorage.settings = $scope.app.settings;
    }
    $scope.$watch('app.settings', function(){ $localStorage.settings = $scope.app.settings; }, true);

    // angular translate
    $scope.lang = { isopen: false };
    $scope.langs = {en:'English', es: "Español"};
    $scope.selectLang = $scope.langs[$translate.proposedLanguage()] || "English";
    $scope.setLang = function(langKey, $event) {
      // set the current lang
      $scope.selectLang = $scope.langs[langKey];
      // You can change the language during runtime
      $translate.use(langKey);
      $scope.lang.isopen = !$scope.lang.isopen;
    };

    function isSmartDevice( $window )
    {
        // Adapted from http://www.detectmobilebrowsers.com
        var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
        // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
        return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
    }
    $scope.logout = function(){
        AuthenticationService.ClearCredentials();
         $location.path('/access/signin');
    };
    $rootScope.$on('logonChange', function (event, next, current) {
          if ($rootScope.globals.currentUser) {
             $scope.app.user.name =  $rootScope.globals.currentUser.userdat.nombre;
             $scope.app.user.username =  $rootScope.globals.currentUser.userdat.username;
             $scope.app.user.office = $rootScope.globals.currentUser.userdat.officeid;
          }
      });
      $scope.createGamer = function(){
          $modal.open({
            templateUrl: "view/gamer/create.html",
            controller: "CreateGamerController",
            size: "lg",
            resolve:{
                user: function(){
                  return   null;
                }
            }
        });
      };
}])
.controller('GeneralMonitorStat', ['$scope','ServiceMonitor','$timeout', function($scope,ServiceMonitor,$timeout) {
      $scope.stat =  {
      };
      $scope.pieCharts = {
              type:'PieChart',
              data: [ ['type', 'value'],["Parlay",0],["derecho",0]],
              options : {
                  displayExactValues: true,
                  width: 120,
                  height: 90,
                  legend: 'none',
                  is3D: false,
                  chartArea: {left:20,top:-20,bottom:0,height:"100%"} 
              },
             formatters: {
                number : [{
                columnNum: 1
                }]
             }
        };
      $scope.formato = function(value){
        return accounting.formatNumber(value,2,".",",");  
      };
      $scope.inicilizeGraphit = function(){
        $scope.chart = $scope.pieCharts;
      };
      $scope.getGeneralStat =  function(){
          ServiceMonitor.getMonitorGeneral(function(stat){
              $scope.stat = stat;
              $scope.chart.data[1][1] =stat.ticketsP;
              $scope.chart.data[2][1] =stat.ticketsD;
              $timeout(function(){
              $scope.getGeneralStat();
          },5000);
          });
         
      };
      
}])
 // Flot Chart controller 
.controller('DashboardStadisticSale', ['$scope','ServiceMonitor','$timeout', function($scope,ServiceMonitor,$timeout) {

    $scope.areaCharts = {
              type:'AreaChart',
              data: 
                  {
                    "cols": [
                    {id: "hour", label: "Hora", type: "string"},
                    {id: "sale", label: "Venta", type: "number"}
                    ],
                    "rows": []
                }
                ,
              options : {
                  "isStacked": "true",
                  "fill": 20,
                  "height": 350,
                  "displayExactValues": true,
                  "vAxis": {
                        "title": "Ventas", "gridlines": {"count": 10}
                   },
                   "hAxis": {
                        "title": "Hora"
                    }
              }
            };
    $scope.lineCharts = {
              type:'LineChart',
              displayed: true,
              data: 
                  {
                    "cols": [
                    {id: "date", label: "Fecha", type: "string"},
                    {id: "sale", label: "Venta", type: "number"},
                    {id: "win", label: "Pagado", type: "number"}
                    ],
                    "rows": []
                }
                ,
              options : {
                  "isStacked": "true",
                  "fill": 20,
                  "height": 246,
                  "displayExactValues": true,
                  "vAxis": {
                        "title": "Ventas", "gridlines": {"count": 10}
                   },
                   "hAxis": {
                        "title": "Fecha"
                    }
              }
            };
    $scope.sale = [];
    $scope.saleWeek = [];
    $scope.iniciliceGraphift = function(){
        $scope.chart = $scope.areaCharts;
        $scope.getLastSale();
    };
    $scope.existsSale = function(sale){
      for(var i =0; i<$scope.sale.length;i++){
          if(sale.hour ==$scope.sale[i].hour && sale.min == $scope.sale[i].min  ){
              $scope.sale[i]  =sale;
              return true;
          }
       }
       return false;
    };
    $scope.getLastSale = function(){
      ServiceMonitor.getHourLastSale(function(lastSale){
        for(var i = 0; i<lastSale.length;i++){
            if(!$scope.existsSale(lastSale[i])){
                $scope.sale.push(lastSale[i]);
                $scope.chart.data.rows.push({c: 
                    [
                        {v: lastSale[i].hour+":"+((lastSale[i].min<10)?"0"+lastSale[i].min:lastSale[i].min)},
                        {v: lastSale[i].sale}
                    ]
                });
            }
        }
        $timeout(function(){
            $scope.getLastSale(); 
        },30000);
    });
    };
     $scope.iniciliceGraphiftWeek = function(){
        $scope.chartLine = $scope.lineCharts;
        $scope.getLastSaleWeek();
    };
    $scope.existDateSale = function(sale){
        for(var i =0;i<$scope.saleWeek.length;i++){
            var saleTemp = $scope.saleWeek[i];
            if(sale.date == saleTemp.date){
                $scope.saleWeek[i] = sale;
                return true;
            }
        }
        return false;
    };
     $scope.getLastSaleWeek = function(){
      ServiceMonitor.getWeekLastSale(function(lastSaleweek){
        for(var i = 0; i<lastSaleweek.length;i++){
                if(!$scope.existDateSale(lastSaleweek[i])){
                $scope.saleWeek.push(lastSaleweek[i]);
                $scope.chartLine.data.rows.push({c: 
                    [
                        {v: lastSaleweek[i].date},
                        {v: lastSaleweek[i].sale},
                        {v: lastSaleweek[i].winPaid}
                    ]
                });
            }
        }
        $timeout(function(){
            $scope.getLastSaleWeek(); 
        },30000);
    });
    };
        
  

    $scope.d1_3 = [ [10, 80],  [20, 40], [30, 30],  [40, 20] ];

    $scope.d2 = [];

    for (var i = 0; i < 20; ++i) {
      $scope.d2.push([i, Math.sin(i)]);
    }   

    $scope.d3 = [ 
      { label: "iPhone5S", data: 40 }, 
      { label: "iPad Mini", data: 10 },
      { label: "iPad Mini Retina", data: 20 },
      { label: "iPhone4S", data: 12 },
      { label: "iPad Air", data: 18 }
    ];

    $scope.getRandomData = function() {
      var data = [],
      totalPoints = 150;
      if (data.length > 0)
        data = data.slice(1);
      while (data.length < totalPoints) {
        var prev = data.length > 0 ? data[data.length - 1] : 50,
          y = prev + Math.random() * 10 - 5;
        if (y < 0) {
          y = 0;
        } else if (y > 100) {
          y = 100;
        }
        data.push(y);
      }
      // Zip the generated y values with the x values
      var res = [];
      for (var i = 0; i < data.length; ++i) {
        res.push([i, data[i]])
      }
      return res;
    }

    $scope.d4 = $scope.getRandomData();
  }])
.controller('CreateGamerController',['$scope','$modalInstance','user','GamerService','AgentService',function($scope,$modalInstance,user,GamerService,AgentService){
  $scope.user = (user==null)?{}:user;   
  if(user!=null){
      $scope.passConfirm =  $scope.user.pass;
  }
  $scope.agents = [];
  AgentService.getListAgent(function(agents){
    $scope.agents = agents;
    for(var i =0 ; i<agents.length;i++){
        if($scope.agents[i].idagente == $scope.user.idAgent){
            $scope.user.idAgent =$scope.agents[i];
            break;
        }
    }
  });
  $scope.update = function () {
     var user =  $scope.user;
     user.idAgent =  $scope.user.idAgent.idagente;
     GamerService.update(user,function(resp){
         Notifier.info("Respuesta",resp);
     });
    $modalInstance.dismiss('cancel');
  };

       
}])
// bootstrap controller
.controller('TypeaheadDemoCtrl', ['$scope', '$http', function($scope, $http) {
  $scope.selected = undefined;
  $scope.states = ['Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut', 'Delaware', 'Florida', 'Georgia', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana', 'Maine', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico', 'New York', 'North Dakota', 'North Carolina', 'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania', 'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont', 'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'];
  // Any function returning a promise object can be used to load values asynchronously
  $scope.getLocation = function(val) {
    return $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
      params: {
        address: val,
        sensor: false
      }
    }).then(function(res){
      var addresses = [];
      angular.forEach(res.data.results, function(item){
        addresses.push(item.formatted_address);
      });
      return addresses;
    });
  };
}])
// signin controller
.controller('SigninFormController', ['$scope', '$state','$location','AuthenticationService', function($scope, $state,$location,AuthenticationService) {
  $scope.user = {};
  $scope.authError = null;
  $scope.loading  = false;
  $scope.login = function() {
    $scope.authError = null;
    $scope.loading  = true;
    // Try to login
    AuthenticationService.Login($scope.user.email, $scope.user.password,function(resp){
$scope.loading  = false;
      if ( !resp.success ) {
        $scope.authError = resp.message;
      }else{
         AuthenticationService.SetCredentials(resp.login);
         $state.go('app.player');

      }
    });
  };
}])
// Gamer Controler
.controller('showPlayerController',['$scope','$sce','$modal','GamerService','CommonUtil',function($scope,$sce,$modal,GamerService,CommonUtil){
        $scope.admin = {};
        $scope.search =  "";
        $scope.pageShow = {value:5,view:5};
        $scope.currentPage = 0;
        $scope.showview = 5;
        $scope.totalPagination = 0;
        $scope.optionPageShow = [{value:5, view: 5},{value: 10 , view: 10},{value:20,view:20},{value:50,view:50},{value:100,view:100}];
        $scope.getItemTotal = function(){
          var pagination  = new Array();
          for(var i =0;i<$scope.players.length/$scope.pageShow.value;i++){
              pagination.push(i+1);
          }
          $scope.totalPagination = pagination.length;
          return pagination;  
        };
        $scope.changePage = function(index){
            $scope.currentPage = index;
        };
        $scope.showItem = function(index){
            return index>=($scope.currentPage*$scope.pageShow.value)&&index<($scope.currentPage*$scope.pageShow.value)+$scope.pageShow.value;
        };
        $scope.prevPage = function(){
            if($scope.currentPage>0){
                $scope.currentPage--;
            }
        };
        $scope.nextPage = function(){
            if($scope.currentPage <  $scope.totalPagination){
                $scope.currentPage++;
            }
        };
        $scope.searchGamer = function(item){
            if($scope.search .length>0){
                
                if(item.username.toLowerCase().replace($scope.search.toLowerCase(),"").length != item.username.length || item.nameagencia.toLowerCase().replace($scope.search.toLowerCase(),"").length != item.nameagencia.length) {
                    return true;
                } else{
                    return false;
                }
            }else{
                return true;
            }
        };
        $scope.update= function(player){
            $modal.open({
            templateUrl: "view/gamer/create.html",
            controller: "CreateGamerController",
            size: "lg",
            resolve:{
                user: function(){
                  return   player;
                }
            }
        });
        };
        $scope.summary = {
            player: {
                taq: 0,
                onl: 0
            }
        };
        $scope.iconPlus = $sce.trustAsHtml(CommonUtil.iconplus);
        $scope.iconLock = $sce.trustAsHtml(CommonUtil.iconLock);
        $scope.iconKey = $sce.trustAsHtml(CommonUtil.iconKey);
        $scope.iconToggleOn = $sce.trustAsHtml(CommonUtil.iconToggleOn);
        $scope.players = [];
        $scope.isLoadingData=true;
        $scope.free = function(player){
            GamerService.playerFree(player.idagencia,function(resp){
               if(eval(resp)){
                   Notifier.info("Agencia Liberada Exitosamente","Aviso");
               } else{
                   Notifier.error("Error Liberando Agencia","Err");
               }
            });
        };
        $scope.reset = function(player){
             GamerService.playerResetPass(player.idagencia,function(resp){
               if(eval(resp)){
                   Notifier.info("Reseteando de Contraseña Agencia Exitoso","Aviso");
               } else{
                   Notifier.error("Error Reseteando Contraseña Agencia","Err");
               }
            });
         };
         $scope.changeStat = function(player){
             GamerService.playerChangeStat(player.idagencia,function(resp){
               if(eval(resp)){
                   Notifier.info("Cambio de Status de  Agencia Exitoso","Aviso");
               } else{
                   Notifier.error("Error Cambiando el Status de la Agencia","Err");
               }
            });
         };
        $scope.getPlayer = function(){
            GamerService.getPlayersActive(function(players){
                $scope.isLoadingData=false;
                $scope.players = players;
                $scope.summary.player.taq =0;
                $scope.summary.player.onl =0;
                $scope.players.map(function(player){
                    if(player.istaquilla)
                        $scope.summary.player.taq += 1;
                    else    
                        $scope.summary.player.onl += 1;
                });
            });
        };
  }])
.controller('ticketPlayerController',['$scope','$modal','$sce','$timeout','TicketService','GamerService','CommonUtil',function($scope,$modal,$sce,$timeout,TicketService,GamerService,CommonUtil){
    $scope.admin = {};
    $scope.summary = {
        ticket: {
            der: 0,
            par: 0
        }
    };
    $scope.isLoadingData=true;
    $scope.message = "";
    $scope.iconPlus = $sce.trustAsHtml(CommonUtil.iconplus);
    $scope.iconDelete = $sce.trustAsHtml(CommonUtil.iconDelete);
    $scope.iconMoney = $sce.trustAsHtml(CommonUtil.iconMoney);
    $scope.tickets = [];
    $scope.search ="";
    $scope.agencias = [];
    $scope.status = CommonUtil.statusGame();
    $scope.from = new Date();
    $scope.to = new Date();
    $scope.hiddenmenu = false;
    $scope.stat = null;
    $scope.openedfrom = false;
    $scope.openedto = false;
          $scope.pageShow = {value:5,view:5};
        $scope.currentPage = 0;
        $scope.showview = 5;
        $scope.totalPagination = 0;
        $scope.optionPageShow = [{value:5, view: 5},{value: 10 , view: 10},{value:20,view:20},{value:50,view:50},{value:100,view:100}];
        $scope.getItemTotal = function(){
          var pagination  = new Array();
          for(var i =0;i<$scope.tickets.length/$scope.pageShow.value;i++){
              pagination.push(i+1);
          }
          $scope.totalPagination = pagination.length;
          return pagination;  
        };
        $scope.changePage = function(index){
            $scope.currentPage = index;
        };
        $scope.showItem = function(index){
            return index>=($scope.currentPage*$scope.pageShow.value)&&index<($scope.currentPage*$scope.pageShow.value)+$scope.pageShow.value;
        };
        $scope.prevPage = function(){
            if($scope.currentPage>0){
                $scope.currentPage--;
            }
        };
        $scope.nextPage = function(){
            if($scope.currentPage <  $scope.totalPagination){
                $scope.currentPage++;
            }
        };
    $scope.getPlayers = function(){
        GamerService.getPlayersActive(function(players){
             $scope.updatePlayers(players);
        });
    };
    $scope.checkSelectedAgencia = function(ticket){
        if($scope.agencia==null){
            return true
        }else{
            if($scope.agencia.length==0){
                return true;
            }else{
                for(var i = 0;i<$scope.agencia.length;i++){
                    if(ticket.agencia.toLowerCase()==$scope.agencia[i].nameagencia.toLowerCase())
                        return true;
                }
                return false;
            }
        }
    };
    $scope.formato = function(value){
      return accounting.formatNumber(value,2,".",",");
    };
    $scope.checkSelectedStatus = function(ticket){
        if($scope.stat==null){
            return true
        }else{
            if($scope.stat.length==0){
                return true;
            }else{
                for(var i = 0;i<$scope.stat.length;i++){
                    if(ticket.status.toLowerCase()==$scope.stat[i].name.toLowerCase())
                        return true;
                }
                return false;
            }
        }
    };
    $scope.updatePlayers = function(players){
         if(!$scope.$$phase) {
                $scope.$apply(
                    function(){
                        $scope.agencias = players;
                        $timeout(function(){
                            if($('.selectpicker').length>0)
                            $('.selectpicker').selectpicker('refresh');
                        },100);
                    }
                );
            }else{
                $timeout(function(){
                     $scope.updatePlayers(players);
                },100);
            }
     };
    $scope.openFilter = function(){
        if($("#filters").is(":visible")){
            $("#filters").fadeOut("slow");
        }else{
            $("#filters").fadeIn("slow");
        }
    };
    $scope.press =function(){
        $("#filters").fadeOut("slow");
         $scope.tickets =[];
         if($scope.search===""){
             $scope.getTickets();
         }else{
             if($scope.search.split(",").length==1){
                  $scope.isLoadingData=true;
                 TicketService.find($scope.search,function(tickets){
                      $scope.isLoadingData=false;
                     $scope.tickets = tickets;
                 });
             }else{
                 $scope.checkTypeSearch();
                 $scope.updateSearch();
                 $scope.getTickets();
             }
         }
    };
    $scope.openDialogConfirm = function(ticket,option){
        var modalInstance = $modal.open({
            templateUrl: "view/gamer/ticketDetail.html",
            controller: "TicketDialogController",
            size: "sm",
            resolve:{
                parameter: function(){
                  return   {ticket: ticket,option: option};
                }
            }
        });
         modalInstance.result.then(function (success) {
             if(success){
                 if(option==0){
                 $scope.deletedTicket(ticket);
                 Notifier.info("Ticket Borrado Exitosamente","Aviso");
                }else{
                    Notifier.info("Ticket Premiado Exitosamente","Aviso");
                }
         }else{
             Notifier.error("Error Procesando Solicitud","Err");
         }
        }, function () {
          
        });

    };
    $scope.deletedTicket = function(ticket){
        for(var i = 0; i<$scope.tickets.length;i++){
            if(ticket.idticket ==$scope.tickets[i].idticket){
                $scope.tickets.slice(i,1);
                break;
            }
        }
    };
    $scope.updateSearch = function(){
            var agencia = "["+$scope.agencias.map(function(agenc){
                return agenc.username+",";
            })+"]";
            var status =$scope.status.length==0?"":agencia==""?",":"" +","+$scope.status;
            $scope.search =$scope.from+","+$scope.to+agencia+status;
    };
    $scope.checkTypeSearch=function(){
        var tokens = $scope.search.split(",");
        if(tokens.length>1)
        $scope.from = $scope.getDate(tokens[0]);
        if(tokens.length>=2)
        $scope.to = $scope.getDate(tokens[1]);
        if(tokens.length>=3){
            $scope.agencia = [];
            $scope.agencia.push( tokens[2]); 
        }
        if(tokens.length>=4){
            $scope.statu = [];
            $scope.statu.push(tokens[3]);
        }
    };
    $scope.open = function($event,orig) {
        $event.preventDefault();
        $event.stopPropagation();
        if(orig===1){
            $scope.openedfrom = !$scope.openedfrom;
            
        }else{
            $scope.openedto = !$scope.openedto;
            
        }
    };
    
    $scope.getTickets = function(){
            $scope.isLoadingData=true;
            $scope.message = "Obteniendo Tickets";
            var parameter  = {from: $scope.from,to: $scope.to};
            TicketService.getTickets(parameter,function(tickets){
                $scope.isLoadingData=false;
            $scope.message = "Procesando Tickets";
            $scope.tickets = tickets;
            $scope.summary.ticket.der =0;
            $scope.summary.ticket.par =0;
            $scope.tickets.map(function(ticket){
                if(ticket.typebet!="Parlay")
                    $scope.summary.ticket.der += 1;
                else    
                    $scope.summary.ticket.par += 1;
            });
            if($scope.tickets.length==0){
                $scope.message = "Sin Tickets";
            }else{
                $scope.message = "Mostrando Tickets";
            }
        });
    };
    $scope.openDetails = function(index){
        $modal.open({
            templateUrl: "view/gamer/ticketDetail.html",
            controller: "TicketDetailsController",
            size: "sm",
            resolve:{
                ticket: function(){
                  return   $scope.tickets[index];
                }
            }
        });
    };
    
}])
.controller('gameTodayController',['$scope','$sce','$modal','PlayService','CommonUtil',function($scope,$sce,$modal,PlayService,CommonUtil){
    $scope.admin = {};
    $scope.iconPlus = $sce.trustAsHtml(CommonUtil.iconplus);
    $scope.games = [];
    $scope.search ="";
    $scope.status = CommonUtil.statusGame();
    $scope.from = new Date();
    $scope.hiddenmenu = false;
    $scope.stat = null;
    $scope.openedfrom = false;
    $scope.isLoadingData = true;
    $scope.pageShow = {value:5,view:5};
        $scope.currentPage = 0;
        $scope.showview = 5;
        $scope.totalPagination = 0;
        $scope.optionPageShow = [{value:5, view: 5},{value: 10 , view: 10},{value:20,view:20},{value:50,view:50},{value:100,view:100}];
        $scope.getItemTotal = function(){
          var pagination  = new Array();
          for(var i =0;i<$scope.games.length/$scope.pageShow.value;i++){
              pagination.push(i+1);
          }
          $scope.totalPagination = pagination.length;
          return pagination;  
        };
        $scope.changePage = function(index){
            $scope.currentPage = index;
        };
        $scope.showItem = function(index){
            return index>=($scope.currentPage*$scope.pageShow.value)&&index<($scope.currentPage*$scope.pageShow.value)+$scope.pageShow.value;
        };
        $scope.prevPage = function(){
            if($scope.currentPage>0){
                $scope.currentPage--;
            }
        };
        $scope.nextPage = function(){
            if($scope.currentPage <  $scope.totalPagination){
                $scope.currentPage++;
            }
        };
    $scope.date = function(value){
      return CommonUtil.formatDate(new Date(value));
    };
    $scope.time=function(value){
      return CommonUtil.formatTime(new Date(value))  ;
    };
    $scope.showDetails =  function(game){
        
       
    };
    $scope.openDetails = function(game){
        $modal.open({
            templateUrl: "view/gamer/gameDetails.html",
            controller: "GameDetailsController",
            size: "lg",
            resolve:{
                game: function(){
                  return   game;
                }
            }
        });
    };
    $scope.openFilter = function(){
         if($("#filters").is(":visible")){
             $("#filters").fadeOut("slow");
         }else{
             $("#filters").fadeIn("slow");
         }
     };
     $scope.getSport = function(sport){
       return CommonUtil.getSport(sport)  ;
     };
     $scope.open = function($event,orig) {
        $event.preventDefault();
        $event.stopPropagation();
        if(orig===1){
            $scope.openedfrom = !$scope.openedfrom;
            
        }else{
            $scope.openedto = !$scope.openedto;
            
        }
    };
    $scope.getGames = function(){
        $scope.isLoadingData = true;
        PlayService.getGames($scope.from,function(games){
            $scope.isLoadingData = false;
            $scope.games = games;
        });
    };
}])
.controller('GameDetailsController',['$scope','$modalInstance','game','$sce','PlayService','CommonUtil',function($scope,$modalInstance,game,$sce,PlayService,CommonUtil){
  $scope.game = game;
  $scope.tickets = [];
  $scope.iconPlus = $sce.trustAsHtml(CommonUtil.iconplus);
      $scope.isLoadingData = true;
  PlayService.getDetailsPlayGame($scope.game.idgame,function(tickets){
          $scope.isLoadingData = false;
      $scope.tickets = tickets;
      
  });

  $scope.ok = function () {
     $modalInstance.dismiss('cancel');
  };

       
}])
.controller('GameDetailsController',['$scope','$modalInstance','game','$sce','PlayService','CommonUtil',function($scope,$modalInstance,game,$sce,PlayService,CommonUtil){
  $scope.game = game;
  $scope.tickets = [];
  $scope.iconPlus = $sce.trustAsHtml(CommonUtil.iconplus);
      $scope.isLoadingData = true;
  PlayService.getDetailsPlayGame($scope.game.idgame,function(tickets){
          $scope.isLoadingData = false;
      $scope.tickets = tickets;
      
  });

  $scope.ok = function () {
     $modalInstance.dismiss('cancel');
  };

       
}])
.controller('TicketDetailsController',['$scope','$modalInstance','ticket','$timeout','$sce','TicketService','CommonUtil',function($scope,$modalInstance,ticket,$timeout,$sce,TicketService,CommonUtil){
  $scope.idModal = ticket.idticket;
  $scope.ticket = ticket;
  $scope.details = [];
  TicketService.getDetails(ticket.idticket,function(resp){
    $scope.details = resp;
  });
  $scope.ok = function () {
    $modalInstance.close($scope.selected.item);
  };
  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
       
  $timeout(function(){
      $(".modal-backdrop").remove();
      $("body").removeClass("modal-open");
      var modal = $("#"+$scope.idModal).parents(".modal");
      $(modal).draggable(); 
      $(modal).css("width","300px"); 
      $(modal).css("left","0px"); 
      $(modal).css("right","10px"); 
  },0);
}])
.controller('TicketDialogController',['$scope','$modalInstance','parameter','TicketService',function($scope,$modalInstance,parameter,TicketService){
  $scope.option = parameter.option;
  $scope.ticket = parameter.ticket;
  $scope.details = [];
  TicketService.getDetails($scope.ticket.idticket,function(resp){
    $scope.details = resp;
  });
  $scope.ok = function () {
    if($scope.option==0){
        TicketService.deleted($scope.ticket.idticket,function(resp){
             $modalInstance.close(eval(resp));
        });
    }else{
        TicketService.paid($scope.ticket.idticket,function(resp){
             $modalInstance.close(eval(resp));
        });
    }
   
  };
  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };     
}])
.controller('BalancePlayerController',['$scope','$timeout','$modal','ReportService','CommonUtil','GamerService','AgentService',function($scope,$timeout,$modal,ReportService,CommonUtil,GamerService,AgentService){
   $scope.from = new Date();
   $scope.to = new Date;
   $scope.tickets = [];
   $scope.agencias = [];
   $scope.agencia = null;
   $scope.agents = [];
   $scope.summary = [];
   $scope.oneAtATime = true;
   $scope.isLoadingData = true;
   $scope.detail_hidden = false;
   $scope.status = {
    isFirstOpen: true,
    isFirstDisabled: false
  };
   $scope.formato = function (value){
     return   accounting.formatNumber(value,2,".",",");
   };
   $scope.inicialiceResumen = function(){
       return {
       vtaderecho: 0,
       vtaparlay:0,
       vta:0,
       devderecho:0,
       devparlay: 0,
       dev: 0,
       tickets: 0,
       premioderecho:0,
       premioparlay:0,
       premio: 0,
       comisionderecho:0,
       comisionparlay:0,
       comision: 0,
       participacion: 0,
       subtotal: 0,
       ganancia:0,
       saldo:0
   };
   };
   AgentService.getListAgent(function(resp){
       for(var i =0;i<resp.length;i++){
           resp[i].summary = [];
           resp[i].resumeAgent = $scope.inicialiceResumen();
           resp[i].hiddenDetails = false;
           resp[i].open = true;
       }
       $timeout(function(){
           $('.selectpicker').selectpicker('refresh');
       },100);
       $scope.agents = resp;
   });
   $scope.filterAgent = function(agent){
       for(var i =0;i<$scope.summary.length;i++){
           if(agent.nameagente==$scope.summary[i].agente)
               return true;
       }
       return false;
   };
   $scope.getAgenciabyAgent = function(agent){
       var index = $scope.agents.indexOf(agent);
       var agencias  =  new Array();
       for(var i =0;i<$scope.summary.length;i++){
           if($scope.agents[index].nameagente==$scope.summary[i].agente)
               agencias.push($scope.summary[i]);
       }
       $scope.agents[index].summary = agencias;
       $scope.processSummarybyAgent(index);
       return agencias;
   };
   GamerService.getPlayersActive(function(resp){
       for(var i =0;i<resp.length;i++){
          var _agencia= resp[i];
          _agencia.rslt = {};
       }
       $scope.agencias = resp;
       $timeout(function(){
           $('.selectpicker').selectpicker('refresh');
       },100);
   });
   $scope.getSummaryReport = function(){
       $scope.isLoadingData  = true;
        ReportService.getSummary(CommonUtil.formatDate($scope.from),CommonUtil.formatDate($scope.to),
        function(resp){
            $scope.isLoadingData = false;
                $scope.summary = resp;
                $scope.processSummary();
                CommonUtil.hideMessage();
        });
   };
    $scope.$watch('agencia',function(newvalue,oldvalue){
            if(newvalue != oldvalue){
                 $scope.processSummary();
            }
    });
    $scope.$watch('from',function(newvalue,oldvalue){
            if(newvalue != oldvalue)
                    $scope.getSummaryReport();    
    });
    $scope.$watch('to',function(newvalue,oldvalue){
            if(newvalue != oldvalue)
                    $scope.getSummaryReport();    
    });
   $scope.open = function($event,orig){
           $event.preventDefault();
           $event.stopPropagation();
           if(orig===1){
            $scope.openedfrom = !$scope.openedfrom;
           }else{
            $scope.openedto = !$scope.openedto ;
           }
   };
   $scope.processSummarybyAgent=function(index){
       var summary = $scope.agents[index].summary;
       $scope.agents[index].resumeAgent = $scope.inicialiceResumen();
       for(var i=0;i<summary.length;i++){
         if( $scope.checkSelected(summary[i]) || $scope.agencia === null){             
            $scope.updateDatabyAgent(index,summary[i]);
          }
       }    
       
   };
    $scope.updateDatabyAgent = function(index,_summary){
       $scope.agents[index].resumeAgent.vta += _summary.sale;
       $scope.agents[index].resumeAgent.tickets +=_summary.canttickets;
       $scope.agents[index].resumeAgent.vtaderecho +=_summary.salederecho;
       $scope.agents[index].resumeAgent.vtaparlay +=_summary.saleparlay;
       $scope.agents[index].resumeAgent.dev += _summary.devolucion;
       $scope.agents[index].resumeAgent.devderecho +=_summary.devolucionderecho;
       $scope.agents[index].resumeAgent.devparlay +=_summary.devolucionparlay;
       $scope.agents[index].resumeAgent.comisionderecho +=_summary.comisionderecho;
       $scope.agents[index].resumeAgent.comisionparlay +=_summary.comisionparlay;
       $scope.agents[index].resumeAgent.comision +=_summary.comision;
       $scope.agents[index].resumeAgent.premioderecho +=_summary.premioderecho;
       $scope.agents[index].resumeAgent.premioparlay +=_summary.premioparlay;
       $scope.agents[index].resumeAgent.participacion +=_summary.participacion;
       $scope.agents[index].resumeAgent.premio +=_summary.premio;
       $scope.agents[index].resumeAgent.subtotal+=_summary.subtotal;
       $scope.agents[index].resumeAgent.ganancia+=_summary.ganancia;
       $scope.agents[index].resumeAgent.saldo+=_summary.saldo;
   };
   $scope.processSummary=function(){
       var summary = $scope.summary;
       $scope.resumeAgent = $scope.inicialiceResumen();
       for(var i=0;i<summary.length;i++){
         if( $scope.checkSelected(summary[i]) || $scope.agencia === null){             
            $scope.updateDataAgent(summary[i]);
          }
       }    
       
   };
   $scope.checkSelected = function(obj){
       if($scope.agencia!==null){
           if($scope.agencia.length!==0){
            for(var i = 0; i<$scope.agencia.length;i++){
                if(obj.username===$scope.agencia[i].username)
                    return true;
            }
            return false;
        }else
            return true;
       }else 
        return true;
   };
   $scope.updateDataAgent = function(_summary){
       $scope.resumeAgent.vta += _summary.sale;
      $scope.resumeAgent.tickets +=_summary.canttickets;
       $scope.resumeAgent.vtaderecho +=_summary.salederecho;
       $scope.resumeAgent.vtaparlay +=_summary.saleparlay;
       $scope.resumeAgent.dev += _summary.devolucion;
       $scope.resumeAgent.devderecho +=_summary.devolucionderecho;
       $scope.resumeAgent.devparlay +=_summary.devolucionparlay;
       $scope.resumeAgent.comisionderecho +=_summary.comisionderecho;
       $scope.resumeAgent.comisionparlay +=_summary.comisionparlay;
       $scope.resumeAgent.comision +=_summary.comision;
       $scope.resumeAgent.premioderecho +=_summary.premioderecho;
       $scope.resumeAgent.premioparlay +=_summary.premioparlay;
       $scope.resumeAgent.premio +=_summary.premio;
       $scope.resumeAgent.subtotal+=_summary.subtotal;
       $scope.resumeAgent.ganancia+=_summary.ganancia;
       $scope.resumeAgent.saldo+=_summary.saldo;
   };
   $scope.resumeAgent = $scope.inicialiceResumen();
   $scope.getSummaryReport();
   $scope.resetFilter= function(){
        $scope.agencia = null;
        $scope.processSummary();
   };
   $scope.getDetails = function(user,premio){
        if(premio>0){
            $modal.open({
               templateUrl: 'app/view/report/detailsTicket.html',
               controller: 'TicketDetailsController',
               size: 'lg',
               resolve:{
                   param: function(){
                       return {user: user,from: CommonUtil.formatDate($scope.from),to:CommonUtil.formatDate($scope.to), type:$scope.user.cierre};
                   }
               } 
            });
        }else{
            Notifier.info("No Hay Premios a Mostrar","Mensaje");
        }
    };
}])
.controller('BalancePlayerControllerDetails',['$scope','$timeout','$modal','ReportService','CommonUtil','GamerService','AgentService',function($scope,$timeout,$modal,ReportService,CommonUtil,GamerService,AgentService){
   $scope.from = new Date();
   $scope.to = new Date;
   $scope.tickets = [];
   $scope.agencias = [];
   $scope.agencia = null;
   $scope.agents = [];
   $scope.summary = [];
   $scope.oneAtATime = true;
   $scope.isLoadingData = true;
   $scope.detail_hidden = false;
   $scope.status = {
    isFirstOpen: true,
    isFirstDisabled: false
  };
   $scope.formato = function (value){
     return   accounting.formatNumber(value,2,".",",");
   };
   $scope.inicialiceResumen = function(){
       return {
       vtaderecho: 0,
       vtaparlay:0,
       vta:0,
       devderecho:0,
       devparlay: 0,
       dev: 0,
       tickets: 0,
       premioderecho:0,
       premioparlay:0,
       premio: 0,
       comisionderecho:0,
       comisionparlay:0,
       comision: 0,
       participacion: 0,
       subtotal: 0,
       ganancia:0,
       saldo:0
   };
   };
   AgentService.getListAgent(function(resp){
       for(var i =0;i<resp.length;i++){
           resp[i].summary = [];
           resp[i].resumeAgent = $scope.inicialiceResumen();
           resp[i].hiddenDetails = false;
           resp[i].open = true;
       }
       $timeout(function(){
           $('.selectpicker').selectpicker('refresh');
       },100);
       $scope.agents = resp;
   });
   $scope.filterAgent = function(agent){
       for(var i =0;i<$scope.summary.length;i++){
           if(agent.nameagente==$scope.summary[i].agente)
               return true;
       }
       return false;
   };
   $scope.getAgenciabyAgent = function(agent){
       var index = $scope.agents.indexOf(agent);
       var agencias  =  new Array();
       for(var i =0;i<$scope.summary.length;i++){
           if($scope.agents[index].nameagente==$scope.summary[i].agente)
               agencias.push($scope.summary[i]);
       }
       $scope.agents[index].summary = agencias;
       $scope.processSummarybyAgent(index);
       return agencias;
   };
   GamerService.getPlayersActive(function(resp){
       for(var i =0;i<resp.length;i++){
          var _agencia= resp[i];
          _agencia.rslt = {};
       }
       $scope.agencias = resp;
       $timeout(function(){
           $('.selectpicker').selectpicker('refresh');
       },100);
   });
   $scope.getSummaryReport = function(){
       $scope.isLoadingData  = true;
        ReportService.getSummaryAgentTaqDetails(CommonUtil.formatDate($scope.from),CommonUtil.formatDate($scope.to),
        function(resp){
            $scope.isLoadingData = false;
                $scope.summary = resp;
                $scope.processSummary();
                CommonUtil.hideMessage();
        });
   };
    $scope.$watch('agencia',function(newvalue,oldvalue){
            if(newvalue != oldvalue){
                 $scope.processSummary();
            }
    });
    $scope.$watch('from',function(newvalue,oldvalue){
            if(newvalue != oldvalue)
                    $scope.getSummaryReport();    
    });
    $scope.$watch('to',function(newvalue,oldvalue){
            if(newvalue != oldvalue)
                    $scope.getSummaryReport();    
    });
   $scope.open = function($event,orig){
           $event.preventDefault();
           $event.stopPropagation();
           if(orig===1){
            $scope.openedfrom = !$scope.openedfrom;
           }else{
            $scope.openedto = !$scope.openedto ;
           }
   };
   $scope.processSummarybyAgent=function(index){
       var summary = $scope.agents[index].summary;
       $scope.agents[index].resumeAgent = $scope.inicialiceResumen();
       for(var i=0;i<summary.length;i++){
         if( $scope.checkSelected(summary[i]) || $scope.agencia === null){             
            $scope.updateDatabyAgent(index,summary[i]);
          }
       }    
       
   };
    $scope.updateDatabyAgent = function(index,_summary){
       $scope.agents[index].resumeAgent.vta += _summary.sale;
       $scope.agents[index].resumeAgent.tickets +=_summary.canttickets;
       $scope.agents[index].resumeAgent.vtaderecho +=_summary.salederecho;
       $scope.agents[index].resumeAgent.vtaparlay +=_summary.saleparlay;
       $scope.agents[index].resumeAgent.dev += _summary.devolucion;
       $scope.agents[index].resumeAgent.devderecho +=_summary.devolucionderecho;
       $scope.agents[index].resumeAgent.devparlay +=_summary.devolucionparlay;
       $scope.agents[index].resumeAgent.comisionderecho +=_summary.comisionderecho;
       $scope.agents[index].resumeAgent.comisionparlay +=_summary.comisionparlay;
       $scope.agents[index].resumeAgent.comision +=_summary.comision;
       $scope.agents[index].resumeAgent.premioderecho +=_summary.premioderecho;
       $scope.agents[index].resumeAgent.premioparlay +=_summary.premioparlay;
       $scope.agents[index].resumeAgent.participacion +=_summary.participacion;
       $scope.agents[index].resumeAgent.premio +=_summary.premio;
       $scope.agents[index].resumeAgent.subtotal+=_summary.subtotal;
       $scope.agents[index].resumeAgent.ganancia+=_summary.ganancia;
       $scope.agents[index].resumeAgent.saldo+=_summary.saldo;
   };
   $scope.processSummary=function(){
       var summary = $scope.summary;
       $scope.resumeAgent = $scope.inicialiceResumen();
       for(var i=0;i<summary.length;i++){
         if( $scope.checkSelected(summary[i]) || $scope.agencia === null){             
            $scope.updateDataAgent(summary[i]);
          }
       }    
       
   };
   $scope.checkSelected = function(obj){
       if($scope.agencia!==null){
           if($scope.agencia.length!==0){
            for(var i = 0; i<$scope.agencia.length;i++){
                if(obj.username===$scope.agencia[i].username)
                    return true;
            }
            return false;
        }else
            return true;
       }else 
        return true;
   };
   $scope.updateDataAgent = function(_summary){
       $scope.resumeAgent.vta += _summary.sale;
      $scope.resumeAgent.tickets +=_summary.canttickets;
       $scope.resumeAgent.vtaderecho +=_summary.salederecho;
       $scope.resumeAgent.vtaparlay +=_summary.saleparlay;
       $scope.resumeAgent.dev += _summary.devolucion;
       $scope.resumeAgent.devderecho +=_summary.devolucionderecho;
       $scope.resumeAgent.devparlay +=_summary.devolucionparlay;
       $scope.resumeAgent.comisionderecho +=_summary.comisionderecho;
       $scope.resumeAgent.comisionparlay +=_summary.comisionparlay;
       $scope.resumeAgent.comision +=_summary.comision;
       $scope.resumeAgent.premioderecho +=_summary.premioderecho;
       $scope.resumeAgent.premioparlay +=_summary.premioparlay;
       $scope.resumeAgent.premio +=_summary.premio;
       $scope.resumeAgent.subtotal+=_summary.subtotal;
       $scope.resumeAgent.ganancia+=_summary.ganancia;
       $scope.resumeAgent.saldo+=_summary.saldo;
   };
   $scope.resumeAgent = $scope.inicialiceResumen();
   $scope.getSummaryReport();
   $scope.resetFilter= function(){
        $scope.agencia = null;
        $scope.processSummary();
   };
   $scope.getDetails = function(user,premio){
        if(premio>0){
            $modal.open({
               templateUrl: 'app/view/report/detailsTicket.html',
               controller: 'TicketDetailsController',
               size: 'lg',
               resolve:{
                   param: function(){
                       return {user: user,from: CommonUtil.formatDate($scope.from),to:CommonUtil.formatDate($scope.to), type:$scope.user.cierre};
                   }
               } 
            });
        }else{
            Notifier.info("No Hay Premios a Mostrar","Mensaje");
        }
    };
}])
.controller('showAgentController',['$scope','AgentService',function($scope,AgentService){
        $scope.agents = [];
        $scope.pageShow = {value:5,view:5};
        $scope.currentPage = 0;
        $scope.showview = 5;
        $scope.totalPagination = 0;
        $scope.optionPageShow = [{value:5, view: 5},{value: 10 , view: 10},{value:20,view:20},{value:50,view:50},{value:100,view:100}];
        $scope.getItemTotal = function(){
          var pagination  = new Array();
          for(var i =0;i<$scope.agents.length/$scope.pageShow.value;i++){
              pagination.push(i+1);
          }
          $scope.totalPagination = pagination.length;
          return pagination;  
        };
        $scope.checkSearch = function(agent){
            if($scope.search==null || $scope.search==""){
                return true;
            }
            if(agent!=null){
                if(agent.nameagente.toLowerCase().replace($scope.search.toLowerCase(),'').length!=agent.nameagente.toLowerCase().length ||
                        agent.username.toLowerCase().replace($scope.search.toLowerCase(),'').length!=agent.username.toLowerCase().length){
                    return true;
                }
            }
            return false;
        };
        $scope.changePage = function(index){
            $scope.currentPage = index;
        };
        $scope.showItem = function(index){
            return index>=($scope.currentPage*$scope.pageShow.value)&&index<($scope.currentPage*$scope.pageShow.value)+$scope.pageShow.value;
        };
        $scope.prevPage = function(){
            if($scope.currentPage>0){
                $scope.currentPage--;
            }
        };
        $scope.nextPage = function(){
            if($scope.currentPage<  $scope.getItemTotal().length){
                $scope.currentPage++;
            }
        };
        
        AgentService.getListAgent(function(agents){
            agents.map(function(elem){
                elem.editing = false;
                return elem;
            });
            $scope.agents =agents;
        });
        $scope.edit= function(agent){
            agent.editing = false;
            AgentService.edit(agent,function(msg){
                Notifier.info("Info",msg);
            });
        };
    }])
.controller('BalanceAgentController',['$scope','$timeout','$modal','ReportService','CommonUtil','GamerService','AgentService',function($scope,$timeout,$modal,ReportService,CommonUtil,GamerService,AgentService){
   $scope.from = new Date();
   $scope.to = new Date;
   $scope.tickets = [];
   $scope.agentSelected = null;
   $scope.agents = [];
   $scope.summary = [];
   $scope.agentsFilter = [];
   $scope.resumenOffice = {};
   $scope.oneAtATime = true;
   $scope.status = {
    isFirstOpen: true,
    isFirstDisabled: false
  };
  $scope.isLoadingData = true;
   $scope.formato = function (value){
     return    accounting.formatNumber(value,2,".",",");
   };
   $scope.inicialiceResumen = function(){
       return {
       vtaderecho: 0,
       vtaparlay:0,
       vta:0,
       tickets:0,
       devderecho:0,
       devparlay: 0,
       dev: 0,
       premioderecho:0,
       premioparlay:0,
       participacion:0,
       premio: 0,
       comisionderecho:0,
       comisionparlay:0,
       comision: 0,
       subtotal: 0,
       ganancia:0,
       saldo:0
   };
   };
   AgentService.getListAgent(function(resp){
       for(var i =0;i<resp.length;i++){
           resp[i].summary = [];
           resp[i].resumeAgent = $scope.inicialiceResumen();
           resp[i].hiddenDetails = false;
           resp[i].open = false;
       }
       $timeout(function(){
           $('.selectpicker').selectpicker('refresh');
       },100);
       $scope.agents = resp;
   });
   $scope.filterAgent = function(agent){
       for(var i =0;i<$scope.summary.length;i++){
           if(agent.nameagente==$scope.summary[i].agente)
               return true;
       }
       return false;
   };
   $scope.getAgenciabyAgent = function(agent){
       var index = $scope.agents.indexOf(agent);
       var agencias  =  new Array();
       for(var i =0;i<$scope.summary.length;i++){
           if($scope.agents[index].nameagente==$scope.summary[i].agente)
               agencias.push($scope.summary[i]);
       }
       $scope.agents[index].summary = agencias;
       $scope.processSummarybyAgent(index);
       return agencias;
   };
   $scope.getSummaryReport = function(){
       $scope.isLoadingData = true;
        ReportService.getSummaryAgent(CommonUtil.formatDate($scope.from),CommonUtil.formatDate($scope.to),
        function(resp){
            $scope.isLoadingData = false;
                $scope.summary = resp;
                $scope.processSummary();
                CommonUtil.hideMessage();
        });
   };
    $scope.$watch('from',function(newvalue,oldvalue){
            if(newvalue != oldvalue)
                    $scope.getSummaryReport();    
    });
    $scope.$watch('to',function(newvalue,oldvalue){
            if(newvalue != oldvalue)
                    $scope.getSummaryReport();    
    });
   $scope.open = function($event,orig){
           $event.preventDefault();
           $event.stopPropagation();
           if(orig===1){
            $scope.openedfrom = !$scope.openedfrom;
           }else{
            $scope.openedto = !$scope.openedto ;
           }
   };
   $scope.processSummarybyAgent=function(index){
       var summary = $scope.agents[index].summary;
       $scope.agents[index].resumeAgent = $scope.inicialiceResumen();
       for(var i=0;i<summary.length;i++){
         if( $scope.checkSelected(summary[i]) || $scope.agentSelected === null){             
            $scope.updateDatabyAgent(index,summary[i]);
          }
       }    
       
   };
    $scope.updateDatabyAgent = function(index,_summary){
       $scope.agents[index].resumeAgent.vta += _summary.sale;
       $scope.agents[index].resumeAgent.partipacion+= _summary.participacion; 
       $scope.agents[index].resumeAgent.vtaderecho +=_summary.salederecho;
       $scope.agents[index].resumeAgent.vtaparlay +=_summary.saleparlay;
       $scope.agents[index].resumeAgent.tickets +=_summary.canttickets;
       $scope.agents[index].resumeAgent.dev += _summary.devolucion;
       $scope.agents[index].resumeAgent.devderecho +=_summary.devolucionderecho;
       $scope.agents[index].resumeAgent.devparlay +=_summary.devolucionparlay;
       $scope.agents[index].resumeAgent.comisionderecho +=_summary.comisionderecho;
       $scope.agents[index].resumeAgent.comisionparlay +=_summary.comisionparlay;
       $scope.agents[index].resumeAgent.comision +=_summary.comision;
       $scope.agents[index].resumeAgent.premioderecho +=_summary.premioderecho;
       $scope.agents[index].resumeAgent.premioparlay +=_summary.premioparlay;
       $scope.agents[index].resumeAgent.premio +=_summary.premio;
       $scope.agents[index].resumeAgent.subtotal+=_summary.subtotal;
       $scope.agents[index].resumeAgent.ganancia+=_summary.ganancia;
       $scope.agents[index].resumeAgent.saldo+=_summary.saldo;
   };
   $scope.$watch('agentSelected',function(newvalue,oldvalue){
            $scope.processSummary();
    });
   $scope.processSummary=function(){
       var summary = $scope.summary;
       $scope.resumenOffice = $scope.inicialiceResumen();
       for(var i=0;i<summary.length;i++){
         if( $scope.checkSelected(summary[i]) || $scope.agentSelected === null){             
            $scope.updateDataOffice(summary[i]);
          }
       }    
       
   };
   $scope.checkSelected = function(obj){
       if($scope.agentSelected!==null){
           if($scope.agentSelected.length!==0){
            for(var i = 0; i<$scope.agentSelected.length;i++){
                if(obj.idAgent===$scope.agentSelected[i].idagente)
                    return true;
            }
            return false;
        }else
            return true;
       }else 
        return true;
   };
   $scope.updateDataOffice = function(_summary){
       $scope.resumenOffice.vta += _summary.sale;
       $scope.resumenOffice.participacion+= _summary.participacion; 
       $scope.resumenOffice.vtaderecho +=_summary.salederecho;
       $scope.resumenOffice.vtaparlay +=_summary.saleparlay;
       $scope.resumenOffice.tickets +=_summary.canttickets;
       $scope.resumenOffice.dev += _summary.devolucion;
       $scope.resumenOffice.devderecho +=_summary.devolucionderecho;
       $scope.resumenOffice.devparlay +=_summary.devolucionparlay;
       $scope.resumenOffice.comisionderecho +=_summary.comisionderecho;
       $scope.resumenOffice.comisionparlay +=_summary.comisionparlay;
       $scope.resumenOffice.comision +=_summary.comision;
       $scope.resumenOffice.premioderecho +=_summary.premioderecho;
       $scope.resumenOffice.premioparlay +=_summary.premioparlay;
       $scope.resumenOffice.premio +=_summary.premio;
       $scope.resumenOffice.subtotal+=_summary.subtotal;
       $scope.resumenOffice.ganancia+=_summary.ganancia;
       $scope.resumenOffice.saldo+=_summary.saldo;
   };
   $scope.resumenOffice = $scope.inicialiceResumen();
   $scope.getSummaryReport();
   $scope.resetFilter= function(){
        $scope.agent = null;
        $scope.processSummary();
   };
   $scope.getDetails = function(user,premio){
        if(premio>0){
            $modal.open({
               templateUrl: 'app/view/report/detailsTicket.html',
               controller: 'TicketDetailsController',
               size: 'lg',
               resolve:{
                   param: function(){
                       return {user: user,from: CommonUtil.formatDate($scope.from),to:CommonUtil.formatDate($scope.to), type:$scope.user.cierre};
                   }
               } 
            });
        }else{
            Notifier.info("No Hay Premios a Mostrar","Mensaje");
        }
    };
}])
.controller('BalanceAgentTaqController',['$scope','$timeout','$modal','ReportService','CommonUtil','GamerService','AgentService',function($scope,$timeout,$modal,ReportService,CommonUtil,GamerService,AgentService){
   $scope.from = new Date();
   $scope.to = new Date;
   $scope.tickets = [];
   $scope.agentSelected = null;
   $scope.agents = [];
   $scope.summary = [];
   $scope.agentsFilter = [];
   $scope.resumenOffice = {};
   $scope.oneAtATime = true;
   $scope.status = {
    isFirstOpen: true,
    isFirstDisabled: false
  };
  $scope.isLoadingData = true;
   $scope.formato = function (value){
     return    accounting.formatNumber(value,2,".",",");
   };
   $scope.inicialiceResumen = function(){
       return {
       vtaderecho: 0,
       vtaparlay:0,
       vta:0,
       tickets:0,
       devderecho:0,
       devparlay: 0,
       dev: 0,
       premioderecho:0,
       premioparlay:0,
       premio: 0,
       participacion:0,
       comisionderecho:0,
       comisionparlay:0,
       comision: 0,
       subtotal: 0,
       ganancia:0,
       saldo:0
   };
   };
   AgentService.getListAgent(function(resp){
       for(var i =0;i<resp.length;i++){
           resp[i].summary = [];
           resp[i].resumeAgent = $scope.inicialiceResumen();
           resp[i].hiddenDetails = false;
           resp[i].open = false;
       }
       $timeout(function(){
           $('.selectpicker').selectpicker('refresh');
       },100);
       $scope.agents = resp;
   });
   $scope.filterAgent = function(agent){
       for(var i =0;i<$scope.summary.length;i++){
           if(agent.nameagente==$scope.summary[i].agente)
               return true;
       }
       return false;
   };
   $scope.getAgenciabyAgent = function(agent){
       var index = $scope.agents.indexOf(agent);
       var agencias  =  new Array();
       for(var i =0;i<$scope.summary.length;i++){
           if($scope.agents[index].nameagente==$scope.summary[i].agente)
               agencias.push($scope.summary[i]);
       }
       $scope.agents[index].summary = agencias;
       $scope.processSummarybyAgent(index);
       return agencias;
   };
   $scope.getSummaryReport = function(){
       $scope.isLoadingData = true;
        ReportService.getSummaryAgentTaq(CommonUtil.formatDate($scope.from),CommonUtil.formatDate($scope.to),
        function(resp){
            $scope.isLoadingData = false;
                $scope.summary = resp;
                $scope.processSummary();
                CommonUtil.hideMessage();
        });
   };
    $scope.$watch('from',function(newvalue,oldvalue){
            if(newvalue != oldvalue)
                    $scope.getSummaryReport();    
    });
    $scope.$watch('to',function(newvalue,oldvalue){
            if(newvalue != oldvalue)
                    $scope.getSummaryReport();    
    });
   $scope.open = function($event,orig){
           $event.preventDefault();
           $event.stopPropagation();
           if(orig===1){
            $scope.openedfrom = !$scope.openedfrom;
           }else{
            $scope.openedto = !$scope.openedto ;
           }
   };
   $scope.processSummarybyAgent=function(index){
       var summary = $scope.agents[index].summary;
       $scope.agents[index].resumeAgent = $scope.inicialiceResumen();
       for(var i=0;i<summary.length;i++){
         if( $scope.checkSelected(summary[i]) || $scope.agentSelected === null){             
            $scope.updateDatabyAgent(index,summary[i]);
          }
       }      
   };
    $scope.updateDatabyAgent = function(index,_summary){
       $scope.agents[index].resumeAgent.vta += _summary.sale;
       $scope.agents[index].resumeAgent.participacion += _summary.participacion;
       $scope.agents[index].resumeAgent.vtaderecho +=_summary.salederecho;
       $scope.agents[index].resumeAgent.vtaparlay +=_summary.saleparlay;
       $scope.agents[index].resumeAgent.tickets +=_summary.canttickets;
       $scope.agents[index].resumeAgent.dev += _summary.devolucion;
       $scope.agents[index].resumeAgent.devderecho +=_summary.devolucionderecho;
       $scope.agents[index].resumeAgent.devparlay +=_summary.devolucionparlay;
       $scope.agents[index].resumeAgent.comisionderecho +=_summary.comisionderecho;
       $scope.agents[index].resumeAgent.comisionparlay +=_summary.comisionparlay;
       $scope.agents[index].resumeAgent.comision +=_summary.comision;
       $scope.agents[index].resumeAgent.premioderecho +=_summary.premioderecho;
       $scope.agents[index].resumeAgent.premioparlay +=_summary.premioparlay;
       $scope.agents[index].resumeAgent.premio +=_summary.premio;
       $scope.agents[index].resumeAgent.subtotal+=_summary.subtotal;
       $scope.agents[index].resumeAgent.ganancia+=_summary.ganancia;
       $scope.agents[index].resumeAgent.saldo+=_summary.saldo;
   };
   $scope.$watch('agentSelected',function(newvalue,oldvalue){
            $scope.processSummary();
    });
   $scope.processSummary=function(){
       var summary = $scope.summary;
       $scope.resumenOffice = $scope.inicialiceResumen();
       for(var i=0;i<summary.length;i++){
         if( $scope.checkSelected(summary[i]) || $scope.agentSelected === null){             
            $scope.updateDataOffice(summary[i]);
          }
       }    
       
   };
   $scope.checkSelected = function(obj){
       if($scope.agentSelected!==null){
           if($scope.agentSelected.length!==0){
            for(var i = 0; i<$scope.agentSelected.length;i++){
                if(obj.idAgent===$scope.agentSelected[i].idagente)
                    return true;
            }
            return false;
        }else
            return true;
       }else 
        return true;
   };
   $scope.updateDataOffice = function(_summary){
       $scope.resumenOffice.vta += _summary.sale;
        $scope.resumenOffice.participacion += _summary.participacion;
       $scope.resumenOffice.vtaderecho +=_summary.salederecho;
       $scope.resumenOffice.vtaparlay +=_summary.saleparlay;
       $scope.resumenOffice.tickets +=_summary.canttickets;
       $scope.resumenOffice.dev += _summary.devolucion;
       $scope.resumenOffice.devderecho +=_summary.devolucionderecho;
       $scope.resumenOffice.devparlay +=_summary.devolucionparlay;
       $scope.resumenOffice.comisionderecho +=_summary.comisionderecho;
       $scope.resumenOffice.comisionparlay +=_summary.comisionparlay;
       $scope.resumenOffice.comision +=_summary.comision;
       $scope.resumenOffice.premioderecho +=_summary.premioderecho;
       $scope.resumenOffice.premioparlay +=_summary.premioparlay;
       $scope.resumenOffice.premio +=_summary.premio;
       $scope.resumenOffice.subtotal+=_summary.subtotal;
       $scope.resumenOffice.ganancia+=_summary.ganancia;
       $scope.resumenOffice.saldo+=_summary.saldo;
   };
   $scope.resumenOffice = $scope.inicialiceResumen();
   $scope.getSummaryReport();
   $scope.resetFilter= function(){
        $scope.agent = null;
        $scope.processSummary();
   };
   $scope.getDetails = function(user,premio){
        if(premio>0){
            $modal.open({
               templateUrl: 'app/view/report/detailsTicket.html',
               controller: 'TicketDetailsController',
               size: 'lg',
               resolve:{
                   param: function(){
                       return {user: user,from: CommonUtil.formatDate($scope.from),to:CommonUtil.formatDate($scope.to), type:$scope.user.cierre};
                   }
               } 
            });
        }else{
            Notifier.info("No Hay Premios a Mostrar","Mensaje");
        }
    };
}])
.controller('StatePlayController',['$scope','$timeout','ReportService','CommonUtil',function($scope,$timeout,ReportService,CommonUtil){
   $scope.games = [];
   $scope.configSport  = CommonUtil.configSport;
   $scope.showSpline = true;
   $scope.isLoadingData = true;
   $scope.sports =  CommonUtil.sports;
   $scope.sport = [];
         $scope.pageShow = {value:5,view:5};
        $scope.currentPage = 0;
        $scope.showview = 5;
        $scope.totalPagination = 0;
        $scope.optionPageShow = [{value:5, view: 5},{value: 10 , view: 10},{value:20,view:20},{value:50,view:50},{value:100,view:100}];
        $scope.getItemTotal = function(){
          var pagination  = new Array();
          for(var i =0;i<$scope.games.length/$scope.pageShow.value;i++){
              pagination.push(i+1);
          }
          $scope.totalPagination = pagination.length;
          return pagination;  
        };
        $scope.changePage = function(index){
            $scope.currentPage = index;
        };
        $scope.showItem = function(index){
            return index>=($scope.currentPage*$scope.pageShow.value)&&index<($scope.currentPage*$scope.pageShow.value)+$scope.pageShow.value;
        };
        $scope.prevPage = function(){
            if($scope.currentPage>0){
                $scope.currentPage--;
            }
        };
        $scope.nextPage = function(){
            if($scope.currentPage<  $scope.getItemTotal().length){
                $scope.currentPage++;
            }
        };
        
   $scope.existLabel = function(config,result,label){
       var resultLabel =  "";
       for(var i =0;i<result.length;i++){
           if(result[i].type === label){
               resultLabel =config.odds[label];
           }
       }
       return resultLabel;
   };
   $scope.checkSport = function(item){
       if($scope.sport.length>0){
       for(var i =0;i<$scope.sport.length;i++){
           if($scope.sport[i].id==item.sport){
               return true;
           }
       }
       return false;
      }else{
          return true;
      }
   };
   $scope.getLabel = function(game,label){
       var config  = $scope.configSport[game.sport];
       var result ="";
       var participant = game.visitor;
       result = $scope.existLabel(config,participant.playsRisk,label[0]);
       if(result==""){
             result = $scope.existLabel(config,participant.playsWin,label[0]);    
       }
       participant = game.home;
       if(result==""){
            result = $scope.existLabel(config,participant.playsRisk,label[1]);    
       }
       if(result==""){
            result = $scope.existLabel(config,participant.playsWin,label[1]);    
       }
       return result;
   };
   $scope.getMount= function(participant,label){
       var result =  "";
       
       if($scope.showSpline){
           var mount  = participant.playsWin;
       }else{
           var mount  = participant.playsRisk;
       }
       for(var i =0;i<mount.length;i++){
           if(mount[i].type === label){
               result =mount[i].value;
           }
       }
       if(result!=""){
          result =   accounting.formatNumber(result,2,".",",");
       }
       return result;
   };
   $scope.getPlay = function(){
       $scope.isLoadingData = true;
        ReportService.getPlayRisk(function(games){
            $scope.games = games;
            $scope.isLoadingData = false;
            $timeout(function(){
                $scope.getPlay();
            },60000);
        });
   };
}]);