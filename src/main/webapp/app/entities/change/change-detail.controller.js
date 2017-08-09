(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('ChangeDetailController', ChangeDetailController);

    ChangeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Change', 'Principal'];

    function ChangeDetailController($scope, $rootScope, $stateParams, previousState, entity, Change, Principal) {
        var vm = this;

        vm.isAuthenticated = Principal.isAuthenticated;
        vm.change = entity;
        vm.previousState = previousState.name + '(' + JSON.stringify(previousState.params) + ')'

        var unsubscribe = $rootScope.$on('pidmsApp:changeUpdate', function(event, result) {
            vm.change = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
