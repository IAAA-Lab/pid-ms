(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('OrganizationMemberDeleteController',OrganizationMemberDeleteController);

    OrganizationMemberDeleteController.$inject = ['$uibModalInstance', 'entity', 'OrganizationMember'];

    function OrganizationMemberDeleteController($uibModalInstance, entity, OrganizationMember) {
        var vm = this;

        vm.organizationMember = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OrganizationMember.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
