/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
    // config.uiColor = '#AADC6E';
    config.skin = 'icy_orange';
    //copy or drop image
    //config.extraPlugins = 'uploadimage';
    //config.extraPlugins = 'uploadwidget';
    //config.extraPlugins = 'widget';
    //config.extraPlugins = 'lineutils';
    //config.extraPlugins = 'filetools';
    //config.extraPlugins = 'notificationaggregator';
    //config.extraPlugins = 'notification';
    //config.extraPlugins = 'toolbar';
    //config.extraPlugins = 'button';

    ////uploader
    //config.extraPlugins = 'imageuploader';
    //config.baseFloatZIndex = 100001;

    //config.extraPlugins = 'codesnippet';
    //config.extraPlugins = 'dialog';
    //config.extraPlugins = 'dialogui';
    //config.extraPlugins = 'codeTag';
    config.extraPlugins = 'uploadimage,uploadwidget,widget,lineutils,filetools,notificationaggregator,notification,toolbar,button,imageuploader,codesnippet,dialog,dialogui,codeTag';
    config.baseFloatZIndex = 100001;
    config.imageUploadUrl = '/CKEditorUpload.ashx';

};
