(function() {
    'use strict';
    angular
        .module('pidmsApp')
        .factory('NamespaceCSVUpdate', NamespaceCSVUpdate);

    NamespaceCSVUpdate.$inject = ['$resource', 'DateUtils'];

    function NamespaceCSVUpdate ($resource, DateUtils) {
        var resourceUrl =  'api/namespaces/:id/updateCSV';

        return $resource(resourceUrl, {}, {
            'updateCSV': { method:'PUT' }
        });
    }
})();
