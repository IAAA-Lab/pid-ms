(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('OrganizationDetailController', OrganizationDetailController);

    OrganizationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Organization', 'OrganizationMember', 'Namespace'];

    function OrganizationDetailController($scope, $rootScope, $stateParams, previousState, entity, Organization, OrganizationMember, Namespace) {
        var vm = this;

        vm.organization = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pidmsApp:organizationUpdate', function(event, result) {
            vm.organization = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
