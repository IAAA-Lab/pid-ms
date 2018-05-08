(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('NamespaceUpdateController',NamespaceUpdateController);

    NamespaceUpdateController.$inject = ['$uibModalInstance', 'entity', 'NamespaceCSVUpdate'];

    function NamespaceUpdateController($uibModalInstance, entity, NamespaceCSVUpdate) {
        var vm = this;

        vm.csvData = entity;
        vm.clear = clear;
        vm.update = update;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        
        function update (id) {
        	NamespaceCSVUpdate.updateCSV(vm.csvData,
                    function () {
                        $uibModalInstance.close(true);
                    });
        }
    }
})();
