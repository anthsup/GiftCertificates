import angular from 'angular';
import LiveEditor from './live-editor/live-editor.module';

const module = angular.module('app.components', [
    LiveEditor
]);

export default module.name;