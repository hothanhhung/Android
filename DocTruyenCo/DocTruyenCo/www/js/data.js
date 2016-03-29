angular.module('starter.appDataProcess', []).
	factory('appDataProcess', function(){
		  return {
		    all: function() {
		      var storiesString = window.localStorage['favoriteStories'];
		      if(storiesString) {
		        return angular.fromJson(storiesString);
		      }
		      return [];
		    },
		    save: function(stories) {
		      window.localStorage['favoriteStories'] = angular.toJson(stories);
		    },
		    newProject: function(id, title, link) {
		      // Add a new project
		      return {
		        id: id,
		        title: title,
		        link: link
		      };
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
        							{"id":1, "title":"Truyện cổ tích Việt Nam", "link":"truyenviet/kho-tang-truyen-co-tich-viet-nam.json"},
        							{"id":2, "title":"Truyện cổ", "link":"truyenviet/truyen-co.json"},
        							{"id":3, "title":"Truyện cười dân gian", "link":"truyenviet/truyen-cuoi-dan-gian.json"},
        							{"id":4, "title":"Truyện cười dân gian 2", "link":"truyenviet/truyen-cuoi-dan-gian-2.json"},
        							{"id":5, "title":"Truyện ngụ ngôn", "link":"truyenviet/truyen-ngu-ngon.json"},
        							{"id":6, "title":"Truyện Trạng Quỳnh", "link":"truyenviet/truyen-trang-quynh.json"},
        							{"id":7, "title":"Truyện Xiển Bột", "link":"truyenviet/truyen-xien-bot-2.json"},
        							{"id":8, "title":"Truyện Trạng Lợn", "link":"truyenviet/truyen-trang-lon.json"},
        							{"id":9, "title":"Ba Giai - Tú Xuất", "link":"truyenviet/ba-giai-tu-xuat.json"},
        							{"id":10, "title":"Truyện cổ Grim", "link":"truyenviet/truyen-co-grim.json"},
        							{"id":11, "title":"Truyện 1001 đêm", "link":"truyenviet/nghin-le-mot-dem.json"},
        							{"id":12, "title":"Truyện 1001 ngày", "link":"truyenviet/nghin-le-mot-ngay.json"},
        							{"id":13, "title":"Thần thoại Hy Lạp - La Mã", "link":"truyenviet/than-thoai-hy-lap-la-ma.json"},
        							{"id":14, "title":"Truyện cổ Andersen", "link":"truyenviet/truyen-co-andersen.json"},
        							{"id":15, "title":"Truyện ngụ ngôn thế giới", "link":"truyenviet/truyen-ngu-ngon-the-gioi.json"}
        						]
        return appData;
    });