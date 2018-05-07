(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('PersistentIdentifierDialogController', PersistentIdentifierDialogController);

    PersistentIdentifierDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PersistentIdentifier','Feature'];

    function PersistentIdentifierDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PersistentIdentifier, Feature) {
        var vm = this;

        vm.persistentIdentifier = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.features = Feature.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.persistentIdentifier.id !== null) {
                PersistentIdentifier.update({id : $stateParams.id},vm.persistentIdentifier, onSaveSuccess, onSaveError);
            } else {
                PersistentIdentifier.save(vm.persistentIdentifier, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pidmsApp:persistentIdentifierUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.beginLifespanVersion = false;
        vm.datePickerOpenStatus.endLifespanVersion = false;
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
