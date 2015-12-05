angular.module('myApp', [])
.controller("listObjectControl", function ($scope){
	var country ="vn";
	$scope.gameAndAppItems = [];
	var url = 'http://api.adflex.vn/api/?action=campaign&refcode=thanhhung1012&country='+country+'&os=android';
	$scope.gameAndAppItems = [{
	                            name :'hung ',
                                desc:'hung dep trai',
                                type:"Google play",
                                isActive: true ,
                                link:'#',
                                imageUrl:'/images/exampleimage.png'
	                          },
	                          {
                                  name :'hung 2',
                                  desc:'hung dep trai 1',
                                  type:"Google play",
                                  isActive: true ,
                                  link:'#',
                                  imageUrl:'https://lh6.ggpht.com/bPkgcQgGsJW9sdrXe3VfY4KrnnZpzkmLuEidN2YLNw2z6tA0IPBfieBPeU199tMNes4f=w300'
                              },
                              {
                                  name :'hung qua dep',
                                  desc:'hung dep trai dep qua',
                                  type:"Google play",
                                  isActive: true ,
                                  link:'#',
                                  imageUrl:'https://lh6.ggpht.com/bPkgcQgGsJW9sdrXe3VfY4KrnnZpzkmLuEidN2YLNw2z6tA0IPBfieBPeU199tMNes4f=w300'
                              }];
                              /*
		$.ajax({
			url: url,
			type: 'GET',
			dataType: 'jsonp',
			timeout: 10000,
			error: function(){
			},
			success: function(jsonData){
				if(jsonData && jsonData)
				{
					var campaigns = [];
					for(var i = 0; i < jsonData.length; i++)
					{
						var camp = jsonData[i];						
						if(camp.incentive == "non-incentive")
						{
							var campObj = 	{
											name :camp.title, 
											desc:camp.desc,
											type:"Google play", 
											isActive: true , 
											link:camp.link, 
											imageUrl:camp.icon.full
										};
							if(camp.type == "gplay") campObj.type = "Google play";
							else if(camp.type == "apk") campObj.type = "File APK";
							campaigns.push(campObj);
						}
					}
					$scope.gameAndAppItems = campaigns;
				}
			}
		});*/
});
