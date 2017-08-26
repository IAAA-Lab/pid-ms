(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('change', {
            parent: 'entity',
            url: '/change?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pidmsApp.change.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/change/changes.html',
                    controller: 'ChangeController',
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
                    $translatePartialLoader.addPart('change');
                    $translatePartialLoader.addPart('changeAction');
                    $translatePartialLoader.addPart('resourceType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('change-detail', {
            parent: 'change',
            url: '/change/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pidmsApp.change.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/change/change-detail.html',
                    controller: 'ChangeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('change');
                    $translatePartialLoader.addPart('changeAction');
                    $translatePartialLoader.addPart('resourceType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Change', function($stateParams, Change) {
                    return Change.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'change',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('change-detail.edit', {
            parent: 'change-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/change/change-dialog.html',
                    controller: 'ChangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Change', function(Change) {
                            return Change.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('change.new', {
            parent: 'change',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/change/change-dialog.html',
                    controller: 'ChangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                changeTimestamp: null,
                                action: null,
                                feature: null,
                                namespace: null,
                                localId: null,
                                versionId: null,
                                beginLifespanVersion: null,
                                endLifespanVersion: null,
                                alternateId: null,
                                resourceType: null,
                                locator: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('change', null, { reload: 'change' });
                }, function() {
                    $state.go('change');
                });
            }]
        })
        .state('change.edit', {
            parent: 'change',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/change/change-dialog.html',
                    controller: 'ChangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Change', function(Change) {
                            return Change.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('change', null, { reload: 'change' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('change.delete', {
            parent: 'change',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/change/change-delete-dialog.html',
                    controller: 'ChangeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Change', function(Change) {
                            return Change.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('change', null, { reload: 'change' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
