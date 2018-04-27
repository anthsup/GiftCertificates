import angular from 'angular';

import liveEditorComponent from './live-editor.component';
import liveEditorFilter from './live-editor.filter';

const module = angular.module('app.components.live-editor', [])
    .component('liveEditor', liveEditorComponent)
    .filter('liveEditor', liveEditorFilter);

export default module.name;