(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('ChangeDeleteController',ChangeDeleteController);

    ChangeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Change'];

    function ChangeDeleteController($uibModalInstance, entity, Change) {
        var vm = this;

        vm.change = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Change.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
