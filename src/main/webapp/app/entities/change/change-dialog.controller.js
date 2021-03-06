(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('ChangeDialogController', ChangeDialogController);

    ChangeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Change', 'Task', 'Feature'];

    function ChangeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Change, Task, Feature) {
        var vm = this;

        vm.change = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.tasks = Task.query();
        vm.features = Feature.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.change.id !== null) {
                Change.update({id : $stateParams.id}, vm.change, onSaveSuccess, onSaveError);
            } else {
                Change.save(vm.change, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pidmsApp:changeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.changeTimestamp = false;
        vm.datePickerOpenStatus.beginLifespanVersion = false;
        vm.datePickerOpenStatus.endLifespanVersion = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
