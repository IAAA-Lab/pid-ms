(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('FeatureDialogController', FeatureDialogController);

    FeatureDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Feature', 'Namespace'];

    function FeatureDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Feature, Namespace) {
        var vm = this;

        vm.feature = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.save = save;
        vm.namespaces = Namespace.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.feature.id !== null) {
                Feature.update({id : $stateParams.id}, vm.feature, onSaveSuccess, onSaveError);
            } else {
                Feature.save(vm.feature, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pidmsApp:featureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
