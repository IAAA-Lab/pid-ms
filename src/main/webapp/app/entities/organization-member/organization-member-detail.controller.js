(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('OrganizationMemberDetailController', OrganizationMemberDetailController);

    OrganizationMemberDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OrganizationMember', 'User', 'Organization'];

    function OrganizationMemberDetailController($scope, $rootScope, $stateParams, previousState, entity, OrganizationMember, User, Organization) {
        var vm = this;

        vm.organizationMember = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pidmsApp:organizationMemberUpdate', function(event, result) {
            vm.organizationMember = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
