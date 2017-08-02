(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('GroupController', GroupController);

    GroupController.$inject = ['Group'];

    function GroupController(Group) {

        var vm = this;

        vm.groups = [];

        loadAll();

        function loadAll() {
            Group.query(function(result) {
                vm.groups = result;
                vm.searchQuery = null;
            });
        }
    }
})();
