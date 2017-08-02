(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('GroupDialogController', GroupDialogController);

    GroupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Group', 'GroupMember', 'Namespace'];

    function GroupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Group, GroupMember, Namespace) {
        var vm = this;

        vm.group = entity;
        vm.clear = clear;
        vm.save = save;
        vm.groupmembers = GroupMember.query();
        vm.namespaces = Namespace.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.group.id !== null) {
                Group.update(vm.group, onSaveSuccess, onSaveError);
            } else {
                Group.save(vm.group, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pidmsApp:groupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
