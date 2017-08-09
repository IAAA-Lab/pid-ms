(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('NamespaceDetailController', NamespaceDetailController);

    NamespaceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Namespace', 'Organization', 'Principal'];

    function NamespaceDetailController($scope, $rootScope, $stateParams, previousState, entity, Namespace, Organization, Principal) {
        var vm = this;

        vm.isAuthenticated = Principal.isAuthenticated;
        vm.namespace = entity;
        vm.previousState = previousState.name + '(' + JSON.stringify(previousState.params) + ')'

        var unsubscribe = $rootScope.$on('pidmsApp:namespaceUpdate', function(event, result) {
            vm.namespace = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
