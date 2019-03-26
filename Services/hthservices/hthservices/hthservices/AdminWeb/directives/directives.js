var myApp = angular.module('myApp.directives', []);

myApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

myApp.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(){
        })
        .error(function(){
        });
    }
}]);

myApp.directive("dropzone", function() {
    return {
        restrict : "A",
        link: function (scope, elem, attrs) {
			angular.element(elem).attr("draggable", "true")
            elem.bind('drop', function(evt) {
                evt.preventDefault();				
                evt.stopPropagation();
				dataTransfer = event.dataTransfer;
				if(typeof(dataTransfer) === "undefined" || dataTransfer == null)
				{
					dataTransfer = evt.originalEvent.dataTransfer;
				}
                event.preventDefault();
                event.stopPropagation();

                var files = dataTransfer.files;
                for (var i = 0, f; f = files[i]; i++) {
						if(typeof(scope.uploadingFiles) === undefined) scope.uploadingFiles = [];
						scope.uploadingFiles.push(f);
                    //var reader = new FileReader();
                    //reader.readAsArrayBuffer(f);

                    //reader.onload = (function(theFile) {
						//if(typeof(scope.uploadingFiles) === undefined) scope.uploadingFiles = [];
						//scope.uploadingFiles.push(theFile);
                        // return function(e) {
                            // var newFile = { name : theFile.name,
                                // type : theFile.type,
                                // size : theFile.size,
                                // lastModifiedDate : theFile.lastModifiedDate
                            // }

                           ////scope.addfile(newFile);
                        // };
                  //  })(f);
                }			
				scope.$apply();				
				this.className = attrs.class;
            });
			elem.bind('dragover', function(evt) {
                return preventDefaultAction(evt);
			});
			elem.bind('dragleave', function(evt) {
                return preventDefaultAction(evt);
			});
			elem.bind("dragstart", function(evt) {
                 return   preventDefaultAction(evt);
                });
                 
			elem.bind("dragend", function(evt) {
                 return   preventDefaultAction(evt);
			});
			
			function preventDefaultAction(evt)
			{
				evt.preventDefault();
				evt.stopPropagation();
				if(typeof(event) === "undefined")
				{
					event = evt.originalEvent;
				}
				if (event != null) {
					event.preventDefault();
					event.stopPropagation();
				}
				this.className = attrs.class;
				//event.dataTransfer.effectAllowed = 'copy';
				return false;
			}
			
			elem.bind('click', function(evt) {
				if(typeof(scope.fileSelector) === "undefined"){
					scope.fileSelector = document.createElement('input');
					scope.fileSelector.setAttribute('type', 'file');
					scope.fileSelector.setAttribute('multiple', true);
					
					scope.fileSelector.onchange = function(evt1) {
						 for (var i = 0, f; f = this.files[i]; i++) {
							if(typeof(scope.uploadingFiles) === undefined) scope.uploadingFiles = [];
							scope.uploadingFiles.push(f);
						 }
						 scope.$apply();
					};
					scope.$apply();
				}
				scope.fileSelector.click();
				return false;
			});
        }
    }
});

myApp.directive('ngEnter', function () {
        return {
           restrict: 'A',
           link: function (scope, elements, attrs) {
              elements.bind('keydown keypress', function (event) {
                  if (event.which === 13) {
                      scope.$apply(function () {
                            scope.$eval(attrs.ngEnter || attrs.ngClick, {$event:event});
                      });
                      event.preventDefault();
                  }
              });
           }
        };
    });

myApp.directive('lazySrc', ['$window', '$document', function($window, $document){
          var doc = $document[0],
              body = doc.body,
              win = $window,
              $win = angular.element(win),
              uid = 0,
              elements = {};

          function getUid(el){
              return el.__uid || (el.__uid = '' + ++uid);
          }

          function getWindowOffset(){
              var t,
                  pageXOffset = (typeof win.pageXOffset == 'number') ? win.pageXOffset : (((t = doc.documentElement) || (t = body.parentNode)) && typeof t.ScrollLeft == 'number' ? t : body).ScrollLeft,
                  pageYOffset = (typeof win.pageYOffset == 'number') ? win.pageYOffset : (((t = doc.documentElement) || (t = body.parentNode)) && typeof t.ScrollTop == 'number' ? t : body).ScrollTop;
              return {
                  offsetX: pageXOffset,
                  offsetY: pageYOffset
              };
          }

          function isVisible(iElement){
              var elem = iElement[0],
                  elemRect = elem.getBoundingClientRect(),
                  windowOffset = getWindowOffset(),
                  winOffsetX = windowOffset.offsetX,
                  winOffsetY = windowOffset.offsetY,
                  elemWidth = elemRect.width,
                  elemHeight = elemRect.height,
                  elemOffsetX = elemRect.left + winOffsetX,
                  elemOffsetY = elemRect.top + winOffsetY,
                  viewWidth = Math.max(doc.documentElement.clientWidth, win.innerWidth || 0),
                  viewHeight = Math.max(doc.documentElement.clientHeight, win.innerHeight || 0),
                  xVisible,
                  yVisible;

              if(elemOffsetY <= winOffsetY){
                  if(elemOffsetY + elemHeight >= winOffsetY){
                      yVisible = true;
                  }
              }else if(elemOffsetY >= winOffsetY){
                  if(elemOffsetY <= winOffsetY + viewHeight){
                      yVisible = true;
                  }
              }

              if(elemOffsetX <= winOffsetX){
                  if(elemOffsetX + elemWidth >= winOffsetX){
                      xVisible = true;
                  }
              }else if(elemOffsetX >= winOffsetX){
                  if(elemOffsetX <= winOffsetX + viewWidth){
                      xVisible = true;
                  }
              }

              return xVisible && yVisible;
          };

          function checkImage(){
              Object.keys(elements).forEach(function(key){
                  var obj = elements[key],
                      iElement = obj.iElement,
                      $scope = obj.$scope;
                  if(isVisible(iElement)){
                      iElement.attr('src', $scope.lazySrc);
                  }
              });
          }

          $win.bind('scroll', checkImage);
          $win.bind('resize', checkImage);

          function onLoad(){
              var $el = angular.element(this),
                  uid = getUid($el);

              $el.css('opacity', 1);

              if(elements.hasOwnProperty(uid)){
                  delete elements[uid];
              }
          }

          return {
              restrict: 'A',
              scope: {
                  lazySrc: '@'
              },
              link: function($scope, iElement){

                  iElement.bind('load', onLoad);

                  $scope.$watch('lazySrc', function(){
                      if(isVisible(iElement)){
                          iElement.attr('src', $scope.lazySrc);
                      }else{
                          var uid = getUid(iElement);
                          iElement.css({
                              'background-color': '#fff',
                              'opacity': 0,
                              '-webkit-transition': 'opacity 1s',
                              'transition': 'opacity 1s'
                          });
                          elements[uid] = {
                              iElement: iElement,
                              $scope: $scope
                          };
                      }
                  });

                  $scope.$on('$destroy', function(){
                      iElement.unbind('load');
                  });
              }
          };
}]);

myApp.directive('inputMask', [function () {
    return {
        restrict: 'A',
        link: function (scope, el, attrs, ctrl) {
            if (attrs.ngType == 'commonmask' || attrs.ngType == "date") {
                $(el).inputmask(scope.$eval(attrs.inputMask));
            }
            else if (attrs.ngType == 'currency' || attrs.ngType == 'decimal') {
                $(el).inputmask('decimal', {
                    rightAlignNumerics: false
                });
            }
            $(el).on('change', function (e) {
                if (attrs.ngType == 'commonmask') {
                    scope.$eval(attrs.ngModel + "='" + (el.val().replace(/[^\d]/g, '')) + "'");
                }
                else if (attrs.ngType == 'decimal') {
                    scope.$eval(attrs.ngModel + "=" + parseFloat(el.val().replace(/[^\d\.]/g, '')) + "");

                }
                else if (attrs.ngType == 'currency') {
                    var value = el.val().replace(/[^\d\.\-]/g, '');
                    if (value == NaN) { value = 0; }
                    //$(el).val(value);
                    scope.$eval(attrs.ngModel + "=" + value + "");
                }
            });
        }
    };
}])