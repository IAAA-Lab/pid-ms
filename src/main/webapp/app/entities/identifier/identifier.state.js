(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('identifier', {
            parent: 'entity',
            url: '/identifier',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pidmsApp.identifier.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/identifier/identifiers.html',
                    controller: 'IdentifierController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('identifier');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('identifier-detail', {
            parent: 'identifier',
            url: '/identifier/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pidmsApp.identifier.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/identifier/identifier-detail.html',
                    controller: 'IdentifierDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('identifier');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Identifier', function($stateParams, Identifier) {
                    return Identifier.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'identifier',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('identifier-detail.edit', {
            parent: 'identifier-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/identifier/identifier-dialog.html',
                    controller: 'IdentifierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Identifier', function(Identifier) {
                            return Identifier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('identifier.new', {
            parent: 'identifier',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/identifier/identifier-dialog.html',
                    controller: 'IdentifierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                namespace: null,
                                localId: null,
                                versionId: null,
                                beginLifespanVersion: null,
                                endLifespanVersion: null,
                                alternateId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('identifier', null, { reload: 'identifier' });
                }, function() {
                    $state.go('identifier');
                });
            }]
        })
        .state('identifier.edit', {
            parent: 'identifier',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/identifier/identifier-dialog.html',
                    controller: 'IdentifierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Identifier', function(Identifier) {
                            return Identifier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('identifier', null, { reload: 'identifier' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('identifier.delete', {
            parent: 'identifier',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/identifier/identifier-delete-dialog.html',
                    controller: 'IdentifierDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Identifier', function(Identifier) {
                            return Identifier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('identifier', null, { reload: 'identifier' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
