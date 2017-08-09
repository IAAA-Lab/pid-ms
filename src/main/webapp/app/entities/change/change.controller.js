(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('ChangeController', ChangeController);

    ChangeController.$inject = ['Change', 'Principal'];

    function ChangeController(Change, Principal) {

        var vm = this;

        vm.changes = [];
        vm.isAuthenticated = Principal.isAuthenticated;


        loadAll();

        function loadAll() {
            Change.query(function(result) {
                vm.changes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
