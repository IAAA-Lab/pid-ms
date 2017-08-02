(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('NamespaceDetailController', NamespaceDetailController);

    NamespaceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Namespace', 'Group'];

    function NamespaceDetailController($scope, $rootScope, $stateParams, previousState, entity, Namespace, Group) {
        var vm = this;

        vm.namespace = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pidmsApp:namespaceUpdate', function(event, result) {
            vm.namespace = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
