(function() {
    'use strict';
    angular
        .module('pidmsApp')
        .factory('PersistentIdentifier', PersistentIdentifier);

    PersistentIdentifier.$inject = ['$resource', 'DateUtils'];

    function PersistentIdentifier ($resource, DateUtils) {
        var resourceUrl =  'api/persistent-identifiers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.beginLifespanVersion = DateUtils.convertDateTimeFromServer(data.beginLifespanVersion);
                        data.endLifespanVersion = DateUtils.convertDateTimeFromServer(data.endLifespanVersion);
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
