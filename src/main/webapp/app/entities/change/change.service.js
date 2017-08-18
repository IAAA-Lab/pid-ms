(function() {
    'use strict';
    angular
        .module('pidmsApp')
        .factory('Change', Change);

    Change.$inject = ['$resource', 'DateUtils'];

    function Change ($resource, DateUtils) {
        var resourceUrl =  'api/changes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.changeTimestamp = DateUtils.convertDateTimeFromServer(data.changeTimestamp);
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
