/*global defineSuite*/
defineSuite([
        'Core/TaskProcessor',
        'Specs/waitsForPromise'
    ], function(
        TaskProcessor,
        waitsForPromise) {
    "use strict";
    /*global jasmine,describe,xdescribe,it,xit,expect,beforeEach,afterEach,beforeAll,afterAll,spyOn,runs,waits,waitsFor*/

    var taskProcessor;

    beforeEach(function() {
        TaskProcessor._workerModulePrefix = '../Specs/TestWorkers/';
    });

    afterEach(function() {
        TaskProcessor._workerModulePrefix = TaskProcessor._defaultWorkerModulePrefix;

        if (taskProcessor && !taskProcessor.isDestroyed()) {
            taskProcessor = taskProcessor.destroy();
        }
    });

    it('works with a simple worker', function() {
        taskProcessor = new TaskProcessor('returnParameters');

        var parameters = {
            prop : 'blah',
            obj : {
                val : true
            }
        };
        var promise = taskProcessor.scheduleTask(parameters);

        waitsForPromise(promise).then(function(result) {
            expect(result).toEqual(parameters);
        });
    });

    it('can be destroyed', function() {
        taskProcessor = new TaskProcessor('returnParameters');

        expect(taskProcessor.isDestroyed()).toEqual(false);

        taskProcessor.destroy();

        expect(taskProcessor.isDestroyed()).toEqual(true);
    });

    it('can transfer array buffer', function() {
        taskProcessor = new TaskProcessor('returnByteLength');

        var byteLength = 100;
        var parameters = new ArrayBuffer(byteLength);
        expect(parameters.byteLength).toEqual(byteLength);

        var promise = taskProcessor.scheduleTask(parameters, [parameters]);

        // array buffer should be neutered when transferred
        expect(parameters.byteLength).toEqual(0);

        // the worker should see the array with proper byte length
        waitsForPromise(promise).then(function(result) {
            expect(result).toEqual(byteLength);
        });
    });

    it('can transfer array buffer back from worker', function() {
        taskProcessor = new TaskProcessor('transferArrayBuffer');

        var byteLength = 100;
        var parameters = {
            byteLength : byteLength
        };

        var promise = taskProcessor.scheduleTask(parameters);

        // the worker should see the array with proper byte length
        waitsForPromise(promise).then(function(result) {
            expect(result.byteLength).toEqual(100);
        });
    });

    it('rejects promise if worker throws', function() {
        taskProcessor = new TaskProcessor('throwError');

        var message = 'foo';
        var parameters = {
            message : message
        };

        var promise = taskProcessor.scheduleTask(parameters);

        waitsForPromise(promise, {
            expectRejection : true
        }).then(undefined, function(error) {
            expect(error.message).toEqual(message);
        });
    });

    it('rejects promise if worker returns a non-clonable result', function() {
        taskProcessor = new TaskProcessor('returnNonCloneable');

        var message = 'foo';
        var parameters = {
            message : message
        };

        var promise = taskProcessor.scheduleTask(parameters);

        waitsForPromise(promise, {
            expectRejection : true
        }).then(undefined, function(error) {
            expect(error).toContain('postMessage failed');
        });
    });
});