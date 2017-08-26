(function() {
    'use strict';
    angular
        .module('pidmsApp')
        .factory('Identifier', Identifier);

    Identifier.$inject = ['$resource', 'DateUtils'];

    function Identifier ($resource, DateUtils) {
        var resourceUrl =  'api/identifiers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.beginLifespanVersion = DateUtils.convertDateTimeFromServer(data.beginLifespanVersion);
                        data.endLifespanVersion = DateUtils.convertDateTimeFromServer(data.endLifespanVersion);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
