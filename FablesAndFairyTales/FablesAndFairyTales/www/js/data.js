angular.module('starter.appDataProcess', []).
	factory('appDataProcess', function(){
		  return {
		    all: function() {
		      var storiesString = window.localStorage['hthfavoriteFables'];
		      if(storiesString) {
		        return angular.fromJson(storiesString);
		      }
		      return [];
		    },
		    save: function(stories) {
		      window.localStorage['hthfavoriteFables'] = angular.toJson(stories);
		    },
		    getLastId: function(categoryId) {
		      var storiesString = window.localStorage['' + categoryId];
		      if(storiesString) {
		        return storiesString;
		      }
		      return "";
		    },
		    saveLastId: function(categoryId, id) {
		      window.localStorage[''+categoryId] = '' + id;
		    }
		  }
	});
angular.module('starter.appData', []).
    factory('appData', function () {

        var appData = {};
        appData.lstCategories = [
        							{"id":1, "title":"Aesop Fables", "link":"worldoftales/aesop-stories.json"},
        							{"id":2, "title":"Grimm's Fairy Tales", "link":"worldoftales/grimm-fairy-tales.json"},
        							{"id":3, "title":"Indian Fairy Tales", "link":"worldoftales/indian-fairy-tales.json"},
        							{"id":4, "title":"US Folktales", "link":"worldoftales/us-folktales.json"},
        							{"id":5, "title":"Arabian Nights (1001 Nights)", "link":"worldoftales/arabian-nights.json"},
        							{"id":6, "title":"Canadian Fairy Tales", "link":"worldoftales/canadian-fairy-tales.json"},
        							{"id":7, "title":"English Fairy Tales", "link":"worldoftales/english-fairy-tales.json"},
        							{"id":8, "title":"Brazil Fairy Tales", "link":"worldoftales/fairy-tales-brazil.json"},
        							{"id":9, "title":"Germany Fairy Tales", "link":"worldoftales/folklore-legends-germany.json"},
        							{"id":10, "title":"Japanese Fairy Tales", "link":"worldoftales/japanese-fairy-tales.json"},
        							{"id":11, "title":"Nigerian Fairy Tales", "link":"worldoftales/nigerian-folktales.json"},
        							{"id":12, "title":"Spanish Fairy Tales", "link":"worldoftales/spanish-folktales.json"},
        							{"id":13, "title":"Andersen Fairy Tales", "link":"worldoftales/andersen-fairy-tales.json"},
        							{"id":14, "title":"Forty Modern Fables", "link":"worldoftales/forty-modern-fables.json"},
        							{"id":15, "title":"Funny Stories", "link":"worldoftales/funny-stories.json"}
        						]
        return appData;
    });