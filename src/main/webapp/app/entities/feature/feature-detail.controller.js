(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('FeatureDetailController', FeatureDetailController);

    FeatureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Feature', 'Namespace', 'Principal'];

    function FeatureDetailController($scope, $rootScope, $stateParams, previousState, entity, Feature, Namespace, Principal) {
        var vm = this;

        vm.isAuthenticated = Principal.isAuthenticated;
        vm.feature = entity;
        vm.previousState = previousState.name + '(' + JSON.stringify(previousState.params) + ')'

        var unsubscribe = $rootScope.$on('pidmsApp:featureUpdate', function(event, result) {
            vm.feature = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
