(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('ChangeDetailController', ChangeDetailController);

    ChangeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Change', 'Task'];

    function ChangeDetailController($scope, $rootScope, $stateParams, previousState, entity, Change, Task) {
        var vm = this;

        vm.change = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pidmsApp:changeUpdate', function(event, result) {
            vm.change = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
