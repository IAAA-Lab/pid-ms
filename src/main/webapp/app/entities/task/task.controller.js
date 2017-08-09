(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('TaskController', TaskController);

    TaskController.$inject = ['Task', 'Principal'];

    function TaskController(Task, Principal) {

        var vm = this;

        vm.tasks = [];
        vm.isAuthenticated = Principal.isAuthenticated;

        loadAll();

        function loadAll() {
            Task.query(function(result) {
                vm.tasks = result;
                vm.searchQuery = null;
            });
        }
    }
})();
