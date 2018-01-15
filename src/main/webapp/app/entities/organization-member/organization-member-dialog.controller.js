(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('OrganizationMemberDialogController', OrganizationMemberDialogController);

    OrganizationMemberDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OrganizationMember', 'User', 'Organization'];

    function OrganizationMemberDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OrganizationMember, User, Organization) {
        var vm = this;

        vm.organizationMember = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.organizations = Organization.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.organizationMember.id !== null) {
                OrganizationMember.update({id : $stateParams.id},vm.organizationMember, onSaveSuccess, onSaveError);
            } else {
                OrganizationMember.save(vm.organizationMember, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pidmsApp:organizationMemberUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
