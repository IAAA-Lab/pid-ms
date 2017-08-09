(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('OrganizationDetailController', OrganizationDetailController);

    OrganizationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Organization', 'OrganizationMember', 'Namespace', 'Principal'];

    function OrganizationDetailController($scope, $rootScope, $stateParams, previousState, entity, Organization, OrganizationMember, Namespace, Principal) {
        var vm = this;

        vm.isAuthenticated = Principal.isAuthenticated;
        vm.organization = entity;
        vm.previousState = previousState.name + '(' + JSON.stringify(previousState.params) + ')'

        var unsubscribe = $rootScope.$on('pidmsApp:organizationUpdate', function(event, result) {
            vm.organization = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
