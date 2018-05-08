(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('NamespaceDialogController', NamespaceDialogController);

    NamespaceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Namespace', 'Organization'];

    function NamespaceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Namespace, Organization) {
        var vm = this;

        vm.namespace = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.organizations = Organization.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.namespace.id !== null) {
                Namespace.update({id : $stateParams.id},vm.namespace, onSaveSuccess, onSaveError);
            } else {
                Namespace.save(vm.namespace, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pidmsApp:namespaceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.lastChangeDate = false;
        vm.datePickerOpenStatus.registrationDate = false;
        vm.datePickerOpenStatus.lastRevisionDate = false;
        vm.datePickerOpenStatus.nextRenewalDate = false;
        vm.datePickerOpenStatus.annullationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
