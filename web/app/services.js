'use strict';
/* Services */
angular.module('app.services', [])
    .factory('AuthenticationService',['Base64', '$http', '$cookieStore', '$rootScope', '$timeout',function (Base64, $http, $cookieStore, $rootScope) {
                var service = {};
                service.Login = function (username, password, callback) {
                    $http({
                        method: "POST",
                        url: 'api/security/login',
                        data: {"user": username, "pass": password},
                        headers: {
                            "Content-Type": "application/json"
                        }
                    }).
                       success(function (response) {
                          callback(response);
                      }).
                      error(function(){
                            callback({success:false, message: ":( Hubo Un Error Inesperado en el Servidor Intente Nuevamente"})
                        });

                };
                service.changePass = function(pass,newPass,callback){
                  $http.post("api/security/changepass/"+newPass,{"user":$rootScope.globals.currentUser.userdat.idagente,"pass":pass}).success(
                          function(resp){
                              callback(resp);
                          });  
                };
                service.SetCredentials = function (user) {
                    var authdata = Base64.encode(user.username + ':' + user.pass);
                    user.agencias = null;
                    $rootScope.globals = {
                        currentUser:{ 
                            userdat : user,
                            authdata :  authdata
                        }
                    };
                    $rootScope.$broadcast("logonChange");
                    $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata; // jshint ignore:line
                    $cookieStore.put('globals', $rootScope.globals);
                };

                service.ClearCredentials = function () {
                    $rootScope.globals = {};
                    $cookieStore.remove('globals');
                    $http.defaults.headers.common.Authorization = 'Basic ';
                    $rootScope.$broadcast("logonChange");
                };

                return service;
            }])
    .factory('Base64', function () {
        /* jshint ignore:start */
        var keyStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
        return {
            encode: function (input) {
                var output = "";
                var chr1, chr2, chr3 = "";
                var enc1, enc2, enc3, enc4 = "";
                var i = 0;

                do {
                    chr1 = input.charCodeAt(i++);
                    chr2 = input.charCodeAt(i++);
                    chr3 = input.charCodeAt(i++);

                    enc1 = chr1 >> 2;
                    enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                    enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                    enc4 = chr3 & 63;

                    if (isNaN(chr2)) {
                        enc3 = enc4 = 64;
                    } else if (isNaN(chr3)) {
                        enc4 = 64;
                    }

                    output = output +
                        keyStr.charAt(enc1) +
                        keyStr.charAt(enc2) +
                        keyStr.charAt(enc3) +
                        keyStr.charAt(enc4);
                    chr1 = chr2 = chr3 = "";
                    enc1 = enc2 = enc3 = enc4 = "";
                } while (i < input.length);

                return output;
            },

            decode: function (input) {
                var output = "";
                var chr1, chr2, chr3 = "";
                var enc1, enc2, enc3, enc4 = "";
                var i = 0;

                // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
                var base64test = /[^A-Za-z0-9\+\/\=]/g;
                if (base64test.exec(input)) {
                    window.alert("There were invalid base64 characters in the input text.\n" +
                        "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
                        "Expect errors in decoding.");
                }
                input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

                do {
                    enc1 = keyStr.indexOf(input.charAt(i++));
                    enc2 = keyStr.indexOf(input.charAt(i++));
                    enc3 = keyStr.indexOf(input.charAt(i++));
                    enc4 = keyStr.indexOf(input.charAt(i++));

                    chr1 = (enc1 << 2) | (enc2 >> 4);
                    chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                    chr3 = ((enc3 & 3) << 6) | enc4;

                    output = output + String.fromCharCode(chr1);

                    if (enc3 != 64) {
                        output = output + String.fromCharCode(chr2);
                    }
                    if (enc4 != 64) {
                        output = output + String.fromCharCode(chr3);
                    }

                    chr1 = chr2 = chr3 = "";
                    enc1 = enc2 = enc3 = enc4 = "";

                } while (i < input.length);

                return output;
            }
        };

        /* jshint ignore:end */
    })
    .factory('GamerService',['$rootScope','$http',function($rootScope,$http){
        var service = {};
        service.getPlayersActive = function(callback){
        $http.get('api/player')
                    .success(function(resp){
                        return callback(resp);
                    })
                    .error(function(){
                        callback([]);
                    })    ;
        };
        service.playerFree = function(idAgencia,callback){
            $http.post("api/player/free/"+idAgencia)
                    .success(function(resp){
                        callback(resp);
                    })
                    .error(function(){
                        callback(false);
                    });
        };
        service.playerResetPass = function(idAgencia,callback){
            $http.post("api/player/resetpass/"+idAgencia)
                    .success(function(resp){
                        callback(resp);
                    })
                    .error(function(){
                        callback(false);
                    });
        };
        service.playerChangeStat = function(idAgencia,callback){
            $http.post("api/player/changestat/"+idAgencia)
                    .success(function(resp){
                        callback(resp);
                    })
                    .error(function(){
                        callback(false);
                    });
        };
        service.update = function(gamer,callback){
            if(gamer.idagencia!=null || gamer.idagencia>0){
                $http.post('api/player/update',gamer)
                        .success(function(resp){
                            callback(resp);
                        })
                        .error(function(){
                            callback("Err Enviando Peticion al Servidor Intentelo Nuevamente");
                        });
            }else{
                $http.post('api/player/create',gamer)
                   .success(function(resp){
                       callback(resp);
                   })
                   .error(function(){
                       callback("Err Enviando Peticion al Servidor Intentelo Nuevamente");
                   });    
            }
        };
        return service;
    }])
    .factory('TicketService',['$rootScope','$http','CommonUtil',function($rootScope,$http,CommonUtil){
            var service = {};
            service.getTickets = function(parameter,callback){
                $http.get('api/ticket/list/'+CommonUtil.formatDate(parameter.from)+"/"+CommonUtil.formatDate(parameter.to))
                        .success(function(resp){
                            return callback(resp);
                        })
                        .error(function(){
                            callback([]);
                        })    ;
            };
            service.find = function(parameter,callback){
                $http.get('api/ticket/find/'+parameter)
                        .success(function(resp){
                            return callback(resp);
                        })
                        .error(function(){
                            callback([]);
                        })    ;
            };
            service.deleted = function(idTicket,callback){
                $http.delete('api/ticket/delete/'+idTicket)
                        .success(function(resp){
                            return callback(resp);
                        })
                        .error(function(){
                            callback(false);
                        })    ;
            };
            service.paid = function(idTicket,callback){
                $http.delete('api/ticket/paid/'+idTicket)
                        .success(function(resp){
                            return callback(resp);
                        })
                        .error(function(){
                            callback(false);
                        })    ;
            };
            service.getDetails = function(idTicket,callback){
                $http.get('api/ticket/details/'+idTicket)
                        .success(function(resp){
                            callback(resp);
                        })
                        .error(function(){
                            callback([]);
                        });
            };
            return service;
    }])
    .factory('CommonUtil',[function(){
        var service ={};
        service.statusGame = function(){
         return [{"code":0,"name":"En Juego"},{"code":2,"name":"Ganador"},{"code":3,"name":"Perdedor"},{"code":4,"name":"Empate"},{"code":102,"name":"Suspendido"},{"code":199,"name":"-NA"}] ; 
       };
       service.formatDate = function(date){
           try{
                return date.getDate()+"-"+(date.getMonth()+1)+"-"+date.getFullYear();
            }catch(ex){
                return "";
            }
       };
       service.sports = [{id: 0,name:"Beisbol"},{id:1 ,name: "Futbol Americano"},{id:2 , name: "Baloncesto"},{id: 4,name: "Hockey"},{id: 6, name: "Soccer"},{id: 7, name: "Propuestas"}];
       service.lengthInUtf8Bytes=function(str) {
            var m = encodeURIComponent(str).match(/%[89ABab]/g);
            return str.length + (m ? m.length : 0);
        };
       service.formatTime = function(date){
           try{
                return date.getHours()+":"+(date.getMinutes()>9?date.getMinutes():"0"+date.getMinutes());
            }catch(ex){
                return "";
            }
       };
       service.getSport = function(idsport){
           var str = "";
            switch (idsport){
                case 0: str = "Beisbol";break;
                case 1: str = "Futbol Americano";break;
                case 2: str = "Baloncesto";break;
                case 4: str = "Hockey";break;
                case 5: str = "Exoticas";break;
                case 6: str = "Futbol";break;
            }
            return str;
       };
       service.configSport = {
           0: {name: "Beisbol", odds:{A: "MoneyLine",B:"Money Line",C:"Total",D:"Total",E:"RunLine",F:"RunLine" }},
           1: {name: "Futbol Americano", odds:{A: "Spread",B:"Spread",C:"Total",D:"Total",E:"Money Line",F:"Money Line" }},
           2: {name: "Baloncesto", odds:{A: "Spread",B:"Spread",C:"Total",D:"Total",E:"Money Line",F:"Money Line" }},
           3: {},
           4: {name: "Hockey", odds:{A: "MoneyLine",B:"Money Line",C:"Total",D:"Total",E:"RunLine",F:"RunLine" }},
           5: {},
           6: {name: "Soccer", odds:{A: "MoneyLine",B:"Money Line",C:"Total",D:"Total",E:"RunLine",F:"RunLine" }},
           7: {name: "Propuesta", odds:{A: "MoneyLine",B:"Money Line",C:"Total",D:"Total",E:"RunLine",F:"RunLine" }}
       };
       service.iconplus ='<i style=\'cursor:pointer\' class=\'fa fa-plus-circle\'></i>';
       service.iconminus ='<i style=\'cursor:pointer\' class=\'fa fa-minus-circle\'></i>';
       service.iconDelete ='<i style=\'cursor:pointer\' class=\'glyphicon glyphicon-trash\'></i> ';
       service.iconMoney ='<i style=\'cursor:pointer\' class=\'fa fa-money\'></i> ';
       service.iconKey ='<i style=\'cursor:pointer\' class=\'fa fa-key\'></i> ';
       service.iconUnlock ='<i style=\'cursor:pointer\' class=\'fa fa-unlock\'></i>';
       service.iconUnlock ='<i style=\'cursor:pointer\' class=\'fa fa-lock\'></i>';
       service.iconToggleOn ='<i style=\'cursor:pointer\' class=\'fa fa-toggle-on\'></i> ';
       service.iconToggleOff ='<i style=\'cursor:pointer\' class=\'fa fa-toggle-off\'></i> ';
       service.iconloading = $("<img src='data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA=='/>");
       service.showLoading = function(message){
         var base = $("<div class='transparent'></div>");
         var _containt = $("<div class='message'></div>");
         var _loading  = service.iconloading;
         var _message  = $("<span>"+message+"</span>");
         $(_containt).prepend(_loading);
         $(_containt).prepend(_message);
         $(base).append(_containt);
         $("#loading_main").prepend(base);
       };
       service.hideMessage = function(){
           $(".transparent").fadeOut("slow");
           $(".transparent" ).remove();
       };
       return service;
    }])
    .factory('PlayService',['$http','CommonUtil',function($http,CommonUtil){
        var service = {};
        service.getGames = function(from,callback){
            var vFrom = "";
            if(from!=null)
                vFrom = CommonUtil.formatDate(from);
            $http.get('api/game/list/'+vFrom)
                    .success(function(resp){
                        return callback(resp);
                    })
                    .error(function(){
                        callback([]);
                    });    
        };
        service.getDetailsPlayGame = function(idgame,callback){
            $http.get('api/ticket/ticketgame/'+idgame)
                    .success(function(resp){
                        return callback(resp);
                    })
                    .error(function(){
                        callback([]);
                    });    
        };
        return service;   
    }])
    .factory('ReportService',['$http',function($http){
            var service = {};
            service.getSummary = function(from,to,callback){
                 $http.get("api/report/summary/"+from+"/"+to).
                    success(function(resp){
                       callback(resp);
                    }).
                    error(function(){
                       callback([]);
                }); 
            };
            service.getSummaryAgent = function(from,to,callback){
                 $http.get("api/report/summaryagent/"+from+"/"+to).
                    success(function(resp){
                       callback(resp);
                    }).
                    error(function(){
                       callback([]);
                }); 
            };
            service.getSummaryAgentTaq = function(from,to,callback){
                 $http.get("api/report/summaryagentgeneral/"+from+"/"+to).
                    success(function(resp){
                       callback(resp);
                    }).
                    error(function(){
                       callback([]);
                }); 
            };
            service.getSummaryAgentTaqDetails = function(from,to,callback){
                 $http.get("api/report/summarytaqdetails/"+from+"/"+to).
                    success(function(resp){
                       callback(resp);
                    }).
                    error(function(){
                       callback([]);
                }); 
            };
            service.getPlayRisk = function( callback){
              $http.get("api/game/moneyreport").success(function(resp){callback(resp);}).error(function(){});  
            };
            return service;
    }])
    .factory('AgentService',['$http',function($http){
            var service = {};
            service.getListAgent = function(callback){
                $http.get("api/agent/list")
                    .success(function(resp){
                        callback(resp);
                    })
                    .error(function(){
                        callback([]);
                    });
            };
            service.edit =  function(agent,callback){
                $http.post("api/agent/edit",agent)
                        .success(function(resp){callback(resp);})
                        .error(function(){callback(err);});
            };
            return service;
    }])
    .factory('ServiceMonitor',['$http',function($http){
            var service = {};
            service.getHourLastSale = function(callback){
                $http.get("api/ticket/hourlastsale")
                    .success(function(resp){
                        callback(resp);
                    })
                    .error(function(){
                        callback([]);
                    });
            };
            service.getWeekLastSale = function(callback){
                $http.get("api/report/summaryofficegeneralweek")
                    .success(function(resp){
                        callback(resp);
                    })
                    .error(function(){
                        callback([]);
                    });
            };
              service.getMonitorGeneral= function(callback){
                $http.get("api/report/summaryofficegeneral")
                    .success(function(resp){
                        callback(resp);
                    })
                    .error(function(){
                        callback([]);
                    });
            };
            return service;
    }])
    ;