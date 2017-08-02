(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('GroupMemberDeleteController',GroupMemberDeleteController);

    GroupMemberDeleteController.$inject = ['$uibModalInstance', 'entity', 'GroupMember'];

    function GroupMemberDeleteController($uibModalInstance, entity, GroupMember) {
        var vm = this;

        vm.groupMember = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GroupMember.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
