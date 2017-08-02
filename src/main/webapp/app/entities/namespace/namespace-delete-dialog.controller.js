(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('NamespaceDeleteController',NamespaceDeleteController);

    NamespaceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Namespace'];

    function NamespaceDeleteController($uibModalInstance, entity, Namespace) {
        var vm = this;

        vm.namespace = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Namespace.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
