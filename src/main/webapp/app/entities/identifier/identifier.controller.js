(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('IdentifierController', IdentifierController);

    IdentifierController.$inject = ['Identifier'];

    function IdentifierController(Identifier) {

        var vm = this;

        vm.identifiers = [];

        loadAll();

        function loadAll() {
            Identifier.query(function(result) {
                vm.identifiers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
