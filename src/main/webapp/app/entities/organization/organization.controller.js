(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('OrganizationController', OrganizationController);

    OrganizationController.$inject = ['Organization', 'Principal'];

    function OrganizationController(Organization, Principal) {

        var vm = this;

        vm.organizations = [];
        vm.isAuthenticated = Principal.isAuthenticated;

        loadAll();

        function loadAll() {
            Organization.query(function(result) {
                vm.organizations = result;
                vm.searchQuery = null;
            });
        }
    }
})();
