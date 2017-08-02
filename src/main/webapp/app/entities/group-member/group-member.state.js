(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('group-member', {
            parent: 'entity',
            url: '/group-member',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pidmsApp.groupMember.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/group-member/group-members.html',
                    controller: 'GroupMemberController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('groupMember');
                    $translatePartialLoader.addPart('capacity');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('group-member-detail', {
            parent: 'group-member',
            url: '/group-member/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pidmsApp.groupMember.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/group-member/group-member-detail.html',
                    controller: 'GroupMemberDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('groupMember');
                    $translatePartialLoader.addPart('capacity');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GroupMember', function($stateParams, GroupMember) {
                    return GroupMember.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'group-member',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('group-member-detail.edit', {
            parent: 'group-member-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group-member/group-member-dialog.html',
                    controller: 'GroupMemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GroupMember', function(GroupMember) {
                            return GroupMember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('group-member.new', {
            parent: 'group-member',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group-member/group-member-dialog.html',
                    controller: 'GroupMemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                capacity: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('group-member', null, { reload: 'group-member' });
                }, function() {
                    $state.go('group-member');
                });
            }]
        })
        .state('group-member.edit', {
            parent: 'group-member',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group-member/group-member-dialog.html',
                    controller: 'GroupMemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GroupMember', function(GroupMember) {
                            return GroupMember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('group-member', null, { reload: 'group-member' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('group-member.delete', {
            parent: 'group-member',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group-member/group-member-delete-dialog.html',
                    controller: 'GroupMemberDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GroupMember', function(GroupMember) {
                            return GroupMember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('group-member', null, { reload: 'group-member' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
