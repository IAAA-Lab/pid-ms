(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('FeatureDeleteController',FeatureDeleteController);

    FeatureDeleteController.$inject = ['$uibModalInstance', 'entity', 'Feature'];

    function FeatureDeleteController($uibModalInstance, entity, Feature) {
        var vm = this;

        vm.feature = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Feature.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
