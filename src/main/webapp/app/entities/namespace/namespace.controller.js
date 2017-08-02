(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('NamespaceController', NamespaceController);

    NamespaceController.$inject = ['Namespace'];

    function NamespaceController(Namespace) {

        var vm = this;

        vm.namespaces = [];

        loadAll();

        function loadAll() {
            Namespace.query(function(result) {
                vm.namespaces = result;
                vm.searchQuery = null;
            });
        }
    }
})();
