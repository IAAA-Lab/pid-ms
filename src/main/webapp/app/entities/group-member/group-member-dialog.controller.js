(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('GroupMemberDialogController', GroupMemberDialogController);

    GroupMemberDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GroupMember', 'User', 'Group'];

    function GroupMemberDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GroupMember, User, Group) {
        var vm = this;

        vm.groupMember = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.groups = Group.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.groupMember.id !== null) {
                GroupMember.update(vm.groupMember, onSaveSuccess, onSaveError);
            } else {
                GroupMember.save(vm.groupMember, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pidmsApp:groupMemberUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
