(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('namespace', {
            parent: 'entity',
            url: '/namespace?page&sort&search',
            data: {
                authorities: [],
                pageTitle: 'pidmsApp.namespace.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/namespace/namespaces.html',
                    controller: 'NamespaceController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('namespace');
                    $translatePartialLoader.addPart('renewalPolicy');
                    $translatePartialLoader.addPart('namespaceStatus');
                    $translatePartialLoader.addPart('processStatus');
                    $translatePartialLoader.addPart('itemStatus');
                    $translatePartialLoader.addPart('methodType');
                    $translatePartialLoader.addPart('sourceType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('namespace-detail', {
            parent: 'namespace',
            url: '/{id}',
            data: {
                authorities: [],
                pageTitle: 'pidmsApp.namespace.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/namespace/namespace-detail.html',
                    controller: 'NamespaceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('namespace');
                    $translatePartialLoader.addPart('renewalPolicy');
                    $translatePartialLoader.addPart('namespaceStatus');
                    $translatePartialLoader.addPart('processStatus');
                    $translatePartialLoader.addPart('itemStatus');
                    $translatePartialLoader.addPart('methodType');
                    $translatePartialLoader.addPart('sourceType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Namespace', function($stateParams, Namespace) {
                    return Namespace.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'namespace',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('namespace-detail.edit', {
            parent: 'namespace-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/namespace/namespace-dialog.html',
                    controller: 'NamespaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Namespace', function(Namespace) {
                            return Namespace.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('namespace.new', {
            parent: 'namespace',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/namespace/namespace-dialog.html',
                    controller: 'NamespaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                namespace: null,
                                title: null,
                                publicNamespace: false,
                                renewalPolicy: null,
                                namespaceStatus: null,
                                processStatus: null,
                                itemStatus: null,
                                lastChangeDate: null,
                                registrationDate: null,
                                lastRevisionDate: null,
                                nextRenewalDate: null,
                                annullationDate: null,
                                methodType: null,
                                sourceType: null,
                                endpointLocation: null,
                                srsName: null,
                                schemaUri: null,
                                schemaUriGML: null,
                                schemaUriBase: null,
                                schemaPrefix: null,
                                featureType: null,
                                geometryProperty: null,
                                beginLifespanVersionProperty: null,
                                featuresThreshold: null,
                                resolverProxyMode: null,
                                hitsRequest: null,
                                factorK: null,
                                xpath: null,
                                nameItem: null,
                                maxNumRequest: null,
                                minX: null,
                                minY: null,
                                maxX: null,
                                maxY: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('namespace', null, { reload: 'namespace' });
                }, function() {
                    $state.go('namespace');
                });
            }]
        })
        .state('namespace.edit', {
            parent: 'namespace',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/namespace/namespace-dialog.html',
                    controller: 'NamespaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Namespace', function(Namespace) {
                            return Namespace.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('namespace', null, { reload: 'namespace' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('namespace.delete', {
            parent: 'namespace',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/namespace/namespace-delete-dialog.html',
                    controller: 'NamespaceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Namespace', function(Namespace) {
                            return Namespace.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('namespace', null, { reload: 'namespace' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
