'use strict';

describe('Controller Tests', function() {

    describe('Group Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockGroup, MockGroupMember, MockNamespace;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockGroup = jasmine.createSpy('MockGroup');
            MockGroupMember = jasmine.createSpy('MockGroupMember');
            MockNamespace = jasmine.createSpy('MockNamespace');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Group': MockGroup,
                'GroupMember': MockGroupMember,
                'Namespace': MockNamespace
            };
            createController = function() {
                $injector.get('$controller')("GroupDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pidmsApp:groupUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
