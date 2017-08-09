(function() {
    'use strict';

    angular
        .module('pidmsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('organization-member', {
            parent: 'entity',
            url: '/organization-member?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pidmsApp.organizationMember.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/organization-member/organization-members.html',
                    controller: 'OrganizationMemberController',
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
                    $translatePartialLoader.addPart('organizationMember');
                    $translatePartialLoader.addPart('capacity');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('organization-member-detail', {
            parent: 'organization-member',
            url: '/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pidmsApp.organizationMember.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/organization-member/organization-member-detail.html',
                    controller: 'OrganizationMemberDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('organizationMember');
                    $translatePartialLoader.addPart('capacity');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OrganizationMember', function($stateParams, OrganizationMember) {
                    return OrganizationMember.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'organization-member',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('organization-member-detail.edit', {
            parent: 'organization-member-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/organization-member/organization-member-dialog.html',
                    controller: 'OrganizationMemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrganizationMember', function(OrganizationMember) {
                            return OrganizationMember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('organization-member.new', {
            parent: 'organization-member',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/organization-member/organization-member-dialog.html',
                    controller: 'OrganizationMemberDialogController',
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
                    $state.go('organization-member', null, { reload: 'organization-member' });
                }, function() {
                    $state.go('organization-member');
                });
            }]
        })
        .state('organization-member.edit', {
            parent: 'organization-member',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/organization-member/organization-member-dialog.html',
                    controller: 'OrganizationMemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrganizationMember', function(OrganizationMember) {
                            return OrganizationMember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('organization-member', null, { reload: 'organization-member' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('organization-member.delete', {
            parent: 'organization-member',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/organization-member/organization-member-delete-dialog.html',
                    controller: 'OrganizationMemberDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OrganizationMember', function(OrganizationMember) {
                            return OrganizationMember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('organization-member', null, { reload: 'organization-member' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
