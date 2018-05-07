(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('OrganizationDialogController', OrganizationDialogController);

    OrganizationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Organization', 'OrganizationMember', 'Namespace'];

    function OrganizationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Organization, OrganizationMember, Namespace) {
        var vm = this;

        vm.organization = entity;
        vm.clear = clear;
        vm.save = save;
        vm.organizationmembers = OrganizationMember.query();
        vm.namespaces = Namespace.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.organization.id !== null) {
                Organization.update({id : $stateParams.id}, vm.organization, onSaveSuccess, onSaveError);
            } else {
                Organization.save(vm.organization, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pidmsApp:organizationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
