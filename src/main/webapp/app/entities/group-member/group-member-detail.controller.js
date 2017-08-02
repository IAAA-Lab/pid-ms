(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('GroupMemberDetailController', GroupMemberDetailController);

    GroupMemberDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GroupMember', 'User', 'Group'];

    function GroupMemberDetailController($scope, $rootScope, $stateParams, previousState, entity, GroupMember, User, Group) {
        var vm = this;

        vm.groupMember = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pidmsApp:groupMemberUpdate', function(event, result) {
            vm.groupMember = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
