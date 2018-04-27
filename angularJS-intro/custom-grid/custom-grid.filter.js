'use strict';

angular.module('gridModule').filter('filterByName', function () {
    return(frameworksList, keyName) => {
        return keyName ? _.filter(frameworksList, framework =>
            framework.name.toLowerCase().includes(keyName.toLowerCase())) : frameworksList;
    }
});