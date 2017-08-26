(function() {
    'use strict';
    angular
        .module('pidmsApp')
        .factory('OrganizationMember', OrganizationMember);

    OrganizationMember.$inject = ['$resource'];

    function OrganizationMember ($resource) {
        var resourceUrl =  'api/organization-members/:id';

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
