(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('IdentifierDeleteController',IdentifierDeleteController);

    IdentifierDeleteController.$inject = ['$uibModalInstance', 'entity', 'Identifier'];

    function IdentifierDeleteController($uibModalInstance, entity, Identifier) {
        var vm = this;

        vm.identifier = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Identifier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
