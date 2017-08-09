(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('PersistentIdentifierController', PersistentIdentifierController);

    PersistentIdentifierController.$inject = ['PersistentIdentifier', 'Principal'];

    function PersistentIdentifierController(PersistentIdentifier, Principal) {

        var vm = this;

        vm.persistentIdentifiers = [];
        vm.isAuthenticated = Principal.isAuthenticated;

        loadAll();

        function loadAll() {
            PersistentIdentifier.query(function(result) {
                vm.persistentIdentifiers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
