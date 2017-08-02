(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('GroupDetailController', GroupDetailController);

    GroupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Group', 'GroupMember', 'Namespace'];

    function GroupDetailController($scope, $rootScope, $stateParams, previousState, entity, Group, GroupMember, Namespace) {
        var vm = this;

        vm.group = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pidmsApp:groupUpdate', function(event, result) {
            vm.group = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
