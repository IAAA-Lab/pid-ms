(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('persistent-identifier', {
            parent: 'entity',
            url: '/persistent-identifier?page&sort&search',
            data: {
                authorities: [],
                pageTitle: 'pidmsApp.persistentIdentifier.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/persistent-identifier/persistent-identifiers.html',
                    controller: 'PersistentIdentifierController',
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
                    $translatePartialLoader.addPart('persistentIdentifier');
                    $translatePartialLoader.addPart('resourceType');
                    $translatePartialLoader.addPart('processStatus');
                    $translatePartialLoader.addPart('itemStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('persistent-identifier-detail', {
            parent: 'persistent-identifier',
            url: '/{id}',
            data: {
                authorities: [],
                pageTitle: 'pidmsApp.persistentIdentifier.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/persistent-identifier/persistent-identifier-detail.html',
                    controller: 'PersistentIdentifierDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('persistentIdentifier');
                    $translatePartialLoader.addPart('resourceType');
                    $translatePartialLoader.addPart('processStatus');
                    $translatePartialLoader.addPart('itemStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PersistentIdentifier', function($stateParams, PersistentIdentifier) {
                    return PersistentIdentifier.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'persistent-identifier',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('persistent-identifier-detail.edit', {
            parent: 'persistent-identifier-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/persistent-identifier/persistent-identifier-dialog.html',
                    controller: 'PersistentIdentifierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PersistentIdentifier', function(PersistentIdentifier) {
                            return PersistentIdentifier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('persistent-identifier.new', {
            parent: 'persistent-identifier',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/persistent-identifier/persistent-identifier-dialog.html',
                    controller: 'PersistentIdentifierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                externalUrn: null,
                                feature: null,
                                resolverProxyMode: null,
                                namespace: null,
                                localId: null,
                                versionId: null,
                                beginLifespanVersion: null,
                                endLifespanVersion: null,
                                alternateId: null,
                                resourceType: null,
                                locator: null,
                                processStatus: null,
                                itemStatus: null,
                                lastChangeDate: null,
                                registrationDate: null,
                                lastRevisionDate: null,
                                nextRenewalDate: null,
                                annullationDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('persistent-identifier', null, { reload: 'persistent-identifier' });
                }, function() {
                    $state.go('persistent-identifier');
                });
            }]
        })
        .state('persistent-identifier.edit', {
            parent: 'persistent-identifier',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/persistent-identifier/persistent-identifier-dialog.html',
                    controller: 'PersistentIdentifierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PersistentIdentifier', function(PersistentIdentifier) {
                            return PersistentIdentifier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('persistent-identifier', null, { reload: 'persistent-identifier' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('persistent-identifier.delete', {
            parent: 'persistent-identifier',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/persistent-identifier/persistent-identifier-delete-dialog.html',
                    controller: 'PersistentIdentifierDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PersistentIdentifier', function(PersistentIdentifier) {
                            return PersistentIdentifier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('persistent-identifier', null, { reload: 'persistent-identifier' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
