(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('NamespaceController', NamespaceController);

    NamespaceController.$inject = ['Namespace', 'Principal'];

    function NamespaceController(Namespace, Principal) {

        var vm = this;

        vm.namespaces = [];
        vm.isAuthenticated = Principal.isAuthenticated;

        loadAll();

        function loadAll() {
            Namespace.query(function(result) {
                vm.namespaces = result;
                vm.searchQuery = null;
            });
        }
    }
})();
