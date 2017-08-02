(function() {
    'use strict';
    angular
        .module('pidmsApp')
        .factory('Group', Group);

    Group.$inject = ['$resource'];

    function Group ($resource) {
        var resourceUrl =  'api/groups/:id';

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
