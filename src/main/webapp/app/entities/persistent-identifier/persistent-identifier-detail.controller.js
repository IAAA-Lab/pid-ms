(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('PersistentIdentifierDetailController', PersistentIdentifierDetailController);

    PersistentIdentifierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PersistentIdentifier', 'Principal'];

    function PersistentIdentifierDetailController($scope, $rootScope, $stateParams, previousState, entity, PersistentIdentifier, Principal) {
        var vm = this;

        vm.persistentIdentifier = entity;
        vm.previousState = previousState.name + '(' + JSON.stringify(previousState.params) + ')'
        vm.isAuthenticated = Principal.isAuthenticated;


        var unsubscribe = $rootScope.$on('pidmsApp:persistentIdentifierUpdate', function(event, result) {
            vm.persistentIdentifier = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
