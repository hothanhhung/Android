angular.module('myApp.services', []).service('DataService', function(){
	this.getDirectories = function(path){
  var data = [{
				Name :'hung ',
				FullPath:'hung dep trai',
				Size:"",
				isFile: false ,
				LastModified:'10-1-2003',
			  },
			  {
				Name :'hung 2',
				FullPath:'hung dep trai',
				Size:"14G",
				isFile: false ,
				LastModified:'23-2-2013',
			  },
			  {
				Name :'hung 231',
				FullPath:'hung dep trai',
				Size:"134B",
				isFile: false ,
				LastModified:'10-11-2014',
			  },
			  {
				Name :'hung ',
				FullPath:'hung dep trai',
				Size:"",
				isFile: false ,
				LastModified:'10-1-2003',
			  },
			  {
				Name :'hung 2',
				FullPath:'hung dep trai',
				Size:"14G",
				isFile: false ,
				LastModified:'23-2-2013',
			  },
			  {
				Name :'hung 231',
				FullPath:'hung dep trai',
				Size:"134B",
				isFile: true ,
				LastModified:'10-11-2014',
			  },
			  {
				Name :'có con chim là chim chich chè',
				FullPath:'hung dep trai',
				Size:"",
				isFile: false ,
				LastModified:'10-1-2003',
			  },
			  {
				Name :'hung 2',
				FullPath:'hung dep trai',
				Size:"14G",
				isFile: true ,
				LastModified:'23-2-2013',
			  },
			  {
				Name :'hung 231',
				FullPath:'hung dep trai',
				Size:"134B",
				isFile: true ,
				LastModified:'10-11-2014',
			  }];
  return data;
  }
});