'use strict';

angular.module('gridModule').filter('filterByName', function () {
    return(frameworksList, keyName)=>{
        if (keyName !== undefined) {
            return _.filter(frameworksList, function (framework) {
                return framework.name.toLowerCase().indexOf(keyName.toLowerCase()) !== -1;
            });
        }
        return frameworksList;
    }
});