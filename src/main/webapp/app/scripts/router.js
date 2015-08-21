
/**
 * @ngdoc here we are configuring the module exposed through the FirstApp
 *        variable. The method receives an array that has a function as a last
 *        argument. Here, the angular inject the dependencies defined as strings
 *        in the array to the corresponding elements in the function. <br/> The
 *        $routeProvider is used to configure the routes. It maps templateUrl
 *        and optionally a controller to a given path. This is used by the
 *        ng-view directive. It replaces the content of the defining element
 *        with the content of the templateUrl, and connects it to the controller
 *        through the $scope.
 * @see https://docs.angularjs.org/guide/di
 */
ProjecManagerApp.config([ '$stateProvider', '$urlRouterProvider', 
  function($stateProvider, $urlRouterProvider) {
	
	$urlRouterProvider.otherwise("/");
	
	$stateProvider
    .state('login', {
      url: "/login",
      templateUrl: "app/views/login.html",
	  controller: "LoginCtrl",
	  data: {
		  css: ["app/css/login.css"]
	  }  
    })
  
    
    .state('pm', {
      url: "/",
      templateUrl: "app/views/index.html",
      controller: "IndexCtrl",
	  data: {
		  css: ["app/css/index.css"]
	  }  
    })
    
    .state('pm.main', {
      url: "main",
      templateUrl: "app/views/main.html",
      controller: "IndexCtrl"
    })
    
    
	.state('pm.dashboard', {
	  url: "dashboard",
      templateUrl: "app/views/dashboard.html",
      controller: "DashboardCtrl",
      data: {
		  css: ["app/css/dashboard.css"]
	  }  
	 
    })
	
	.state('pm.project', {
		  url: "project/:projectId",
	      templateUrl: "app/views/project.html",
	      controller: "ProjectCtrl",
	      data: {
			  css: ["app/css/project.css","app/css/angucomplete-alt.css"]
		  }  
		 
	    });
	
	
	
} ])

.run( function($rootScope, $state) {
    // register listener to watch route changes
	$rootScope.$on('$stateChangeStart', 
		function(event, toState, toParams, fromState, fromParams){ 
		    // do something
			console.log("state change",$rootScope.user,toState);

			if(toState.name == 'login'){
				if($rootScope.user){
					$state.go('pm.dashboard');
				}
			}else{
				if(angular.isUndefined($rootScope.user)){
					$state.go('login');
				}
			}
			
		
		})
		
 });
