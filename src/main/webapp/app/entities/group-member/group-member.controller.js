(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .controller('GroupMemberController', GroupMemberController);

    GroupMemberController.$inject = ['GroupMember'];

    function GroupMemberController(GroupMember) {

        var vm = this;

        vm.groupMembers = [];

        loadAll();

        function loadAll() {
            GroupMember.query(function(result) {
                vm.groupMembers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
