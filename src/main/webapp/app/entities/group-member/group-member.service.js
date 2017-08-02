(function() {
    'use strict';
    angular
        .module('pidmsApp')
        .factory('GroupMember', GroupMember);

    GroupMember.$inject = ['$resource'];

    function GroupMember ($resource) {
        var resourceUrl =  'api/group-members/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
