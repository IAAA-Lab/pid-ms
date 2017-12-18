(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('feature', {
            parent: 'entity',
            url: '/feature',
            data: {
                authorities: [],
                pageTitle: 'pidmsApp.feature.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/feature/features.html',
                    controller: 'FeatureController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('feature');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('feature-detail', {
            parent: 'feature',
            url: '/{id}',
            data: {
                authorities: [],
                pageTitle: 'pidmsApp.feature.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/feature/feature-detail.html',
                    controller: 'FeatureDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('feature');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Feature', function($stateParams, Feature) {
                    return Feature.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'feature',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('feature-detail.edit', {
            parent: 'feature-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/feature/feature-dialog.html',
                    controller: 'FeatureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Feature', function(Feature) {
                            return Feature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('feature.new', {
            parent: 'feature',
            url: '',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/feature/feature-dialog.html',
                    controller: 'FeatureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                namespace: null,
                                srsName: null,
                                schemaUri: null,
                                schemaUriGML: null,
                                schemaUriBase: null,
                                schemaPrefix: null,
                                featureType: null,
                                geometryProperty: null,
                                beginLifespanVersionProperty: null,
                                featuresThreshold: null,
                                hitsRequest: false,
                                factorK: null,
                                xpath: null,
                                nameItem: null,
                                minX: null,
                                minY: null,
                                maxX: null,
                                maxY: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('feature', null, { reload: 'feature' });
                }, function() {
                    $state.go('feature');
                });
            }]
        })
        .state('feature.edit', {
            parent: 'feature',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/feature/feature-dialog.html',
                    controller: 'FeatureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Feature', function(Feature) {
                            return Feature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('feature', null, { reload: 'feature' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('feature.delete', {
            parent: 'feature',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/feature/feature-delete-dialog.html',
                    controller: 'FeatureDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Feature', function(Feature) {
                            return Feature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('feature', null, { reload: 'feature' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
