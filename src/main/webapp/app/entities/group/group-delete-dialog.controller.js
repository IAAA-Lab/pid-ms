(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('GroupDeleteController',GroupDeleteController);

    GroupDeleteController.$inject = ['$uibModalInstance', 'entity', 'Group'];

    function GroupDeleteController($uibModalInstance, entity, Group) {
        var vm = this;

        vm.group = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Group.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
