'use strict';

describe('Controller Tests', function() {

    describe('GroupMember Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockGroupMember, MockUser, MockGroup;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockGroupMember = jasmine.createSpy('MockGroupMember');
            MockUser = jasmine.createSpy('MockUser');
            MockGroup = jasmine.createSpy('MockGroup');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'GroupMember': MockGroupMember,
                'User': MockUser,
                'Group': MockGroup
            };
            createController = function() {
                $injector.get('$controller')("GroupMemberDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pidmsApp:groupMemberUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
