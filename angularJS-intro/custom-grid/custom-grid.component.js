'use strict';

angular
    .module('gridModule')
    .component('customGrid', {
        templateUrl: 'custom-grid/custom-grid.template.html',
        controller: function (customGridService) {
            customGridService.retrieveData().then(response=>{
                this.columns = response.data.columns;
                this.frameworks = response.data.frameworks;
            });

            this.reset = function () {
                this.filterValue = '';
            };

            this.isFilterValueEmpty = function () {
                return this.filterValue === '' || this.filterValue === undefined;
            }
        }
    });