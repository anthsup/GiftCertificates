'use strict';

angular.module('gridModule').service('customGridService', function ($http) {
    this.retrieveData = function () {
        return $http.get('json/frameworks.json');
    }
});