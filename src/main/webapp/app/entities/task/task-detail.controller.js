(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('TaskDetailController', TaskDetailController);

    TaskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Task', 'Namespace', 'Principal'];

    function TaskDetailController($scope, $rootScope, $stateParams, previousState, entity, Task, Namespace, Principal) {
        var vm = this;

        vm.task = entity;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.previousState = previousState.name + '(' + JSON.stringify(previousState.params) + ')'

        var unsubscribe = $rootScope.$on('pidmsApp:taskUpdate', function(event, result) {
            vm.task = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
