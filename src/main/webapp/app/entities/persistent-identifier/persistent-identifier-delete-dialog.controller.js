(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('PersistentIdentifierDeleteController',PersistentIdentifierDeleteController);

    PersistentIdentifierDeleteController.$inject = ['$uibModalInstance', 'entity', 'PersistentIdentifier'];

    function PersistentIdentifierDeleteController($uibModalInstance, entity, PersistentIdentifier) {
        var vm = this;

        vm.persistentIdentifier = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PersistentIdentifier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
