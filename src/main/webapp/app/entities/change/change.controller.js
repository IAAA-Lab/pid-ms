(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('ChangeController', ChangeController);

    ChangeController.$inject = ['Change'];

    function ChangeController(Change) {

        var vm = this;

        vm.changes = [];

        loadAll();

        function loadAll() {
            Change.query(function(result) {
                vm.changes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
