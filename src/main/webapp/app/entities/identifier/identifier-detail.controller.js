(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('IdentifierDetailController', IdentifierDetailController);

    IdentifierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Identifier'];

    function IdentifierDetailController($scope, $rootScope, $stateParams, previousState, entity, Identifier) {
        var vm = this;

        vm.identifier = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pidmsApp:identifierUpdate', function(event, result) {
            vm.identifier = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
