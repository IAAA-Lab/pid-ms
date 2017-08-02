(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('PersistentIdentifierController', PersistentIdentifierController);

    PersistentIdentifierController.$inject = ['PersistentIdentifier'];

    function PersistentIdentifierController(PersistentIdentifier) {

        var vm = this;

        vm.persistentIdentifiers = [];

        loadAll();

        function loadAll() {
            PersistentIdentifier.query(function(result) {
                vm.persistentIdentifiers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
