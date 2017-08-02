(function() {
    'use strict';
    angular
        .module('pidmsApp')
        .factory('Namespace', Namespace);

    Namespace.$inject = ['$resource', 'DateUtils'];

    function Namespace ($resource, DateUtils) {
        var resourceUrl =  'api/namespaces/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.lastChangeDate = DateUtils.convertDateTimeFromServer(data.lastChangeDate);
                        data.registrationDate = DateUtils.convertDateTimeFromServer(data.registrationDate);
                        data.lastRevisionDate = DateUtils.convertDateTimeFromServer(data.lastRevisionDate);
                        data.nextRenewalDate = DateUtils.convertDateTimeFromServer(data.nextRenewalDate);
                        data.annullationDate = DateUtils.convertDateTimeFromServer(data.annullationDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
