import angular from 'angular';
import AppComponent from './app.component';
import Components from './components/components.module';

angular.module('app', [
    Components
])
.component('app', AppComponent);