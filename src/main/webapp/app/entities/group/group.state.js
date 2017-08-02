(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('group', {
            parent: 'entity',
            url: '/group',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pidmsApp.group.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/group/groups.html',
                    controller: 'GroupController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('group');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('group-detail', {
            parent: 'group',
            url: '/group/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pidmsApp.group.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/group/group-detail.html',
                    controller: 'GroupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('group');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Group', function($stateParams, Group) {
                    return Group.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'group',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('group-detail.edit', {
            parent: 'group-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group/group-dialog.html',
                    controller: 'GroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Group', function(Group) {
                            return Group.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('group.new', {
            parent: 'group',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group/group-dialog.html',
                    controller: 'GroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                organization: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('group', null, { reload: 'group' });
                }, function() {
                    $state.go('group');
                });
            }]
        })
        .state('group.edit', {
            parent: 'group',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group/group-dialog.html',
                    controller: 'GroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Group', function(Group) {
                            return Group.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('group', null, { reload: 'group' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('group.delete', {
            parent: 'group',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group/group-delete-dialog.html',
                    controller: 'GroupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Group', function(Group) {
                            return Group.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('group', null, { reload: 'group' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
