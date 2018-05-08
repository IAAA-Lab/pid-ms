(function() {
    'use strict';
    angular
        .module('pidmsApp')
        .factory('Feature', Feature);

    Feature.$inject = ['$resource', 'DateUtils'];

    function Feature ($resource, DateUtils) {
        var resourceUrl =  'api/features/:id';

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
