(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('OrganizationMemberController', OrganizationMemberController);

    OrganizationMemberController.$inject = ['OrganizationMember'];

    function OrganizationMemberController(OrganizationMember) {

        var vm = this;

        vm.organizationMembers = [];

        loadAll();

        function loadAll() {
            OrganizationMember.query(function(result) {
                vm.organizationMembers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
