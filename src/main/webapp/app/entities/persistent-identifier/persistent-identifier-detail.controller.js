(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('PersistentIdentifierDetailController', PersistentIdentifierDetailController);

    PersistentIdentifierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PersistentIdentifier'];

    function PersistentIdentifierDetailController($scope, $rootScope, $stateParams, previousState, entity, PersistentIdentifier) {
        var vm = this;

        vm.persistentIdentifier = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pidmsApp:persistentIdentifierUpdate', function(event, result) {
            vm.persistentIdentifier = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
