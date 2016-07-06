using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Ads
{
    public class OwnerAds
    {

        public static readonly List<AdItem> OWNER_ADITEMS = new List<AdItem>(){
            new AdItem(){
		        NameVN = "Ủng hộ tác giả bằng cách down và cài đặt Game và ứng dụng",
		        DescVN = "Đối với file APK vui lòng bật chế độ cho phép cài đặt nguồn ngoài Google play \n Settings > Security > ☑ Unknown Sources.",
		        Name = "Download and install Games and application to support us",
		        Desc = "For APK, Please allow install from Unknown Sources \n Settings > Security > ☑ Unknown Sources.",
		      },
            new AdItem(){
                Type ="Google play",
		        Name = "Lines",
		        Desc = "Lines game is the same to Color Lines game and lines 98 on window",
		        Link = "https://play.google.com/store/apps/details?id=com.hth.lines",
		        UrlImage ="https://lh3.googleusercontent.com/W_8ALgD9xzuMB4AvT2L7GM2d6xCNYt5T2am8xBMv3P0gG2kLZ0dHW9POLKwJdoHVV2Xu=w300",
            },new AdItem(){
                Type ="Google play",
		        Name = "Sudoku",
		        Desc = "The rule is simple. However, it is not easy to find solution",
		        Link = "https://play.google.com/store/apps/details?id=com.hth.sudoku",
		        UrlImage ="https://lh3.googleusercontent.com/7BFgI4j1PX7f-J6B47BqzuW2C5DGqQ-WpbcRcZQjSI6pQO7CXNQSprjOXHEDilXaY9Q=w300",
            },
            new AdItem(){
                Type ="Google play",
		        Name = "Đọc Truyện Cổ",
		        Desc = "Với ứng dụng như một sách truyện giúp bạn có thể đọc offline các truyện",
		        Link = "https://play.google.com/store/apps/details?id=com.hth.doctruyenco",
		        UrlImage ="https://lh3.ggpht.com/wk8buIq8ybv9vjZ1GPQ1q6NiClaZDJJ1KzQ3EBCAhxretCryfJRsXaLxrXjLSdLjTw=w300",
                InCountries="VN"
            },
		    new AdItem()
            {
		        Type="Google play",
		        Name="Hài Tổng Hợp",
		        Desc="Ứng dụng hài tổng hợp có các kênh hài, video hài, và clip hài đang được yêu thích trên youtube: Hài tuyển chọn, 5S Online, YEAH1TV, DAM tv, BB&BG Entertainment,...",
		        Link="https://play.google.com/store/apps/details?id=com.hth.haitonghop",
		        UrlImage="https://lh3.googleusercontent.com/IJA3lu0hYrH4ZkSqe3b92dHs5NZfiribIWBguyh-HB5ISFFoo4aOEMVogmbhe7UEcw=w300",
                InCountries="VN"
            },

		    new AdItem(){
		        Type="Google play",
		        Name="Animal Connection",
		        DescVN="Game Pikachu mới, càng chơi càng thú vị",
		        Desc="Pikachu Game, more play more fun",
		        Link="https://play.google.com/store/apps/details?id=com.hth.animalconnection",
		        UrlImage="https://lh3.googleusercontent.com/UifZjCgWDSnWQ6c-9ukm8e8osK9VthxPvgAqEaLBpZJOWYad8ncIl-AL0Sokn5yRLOU=w300",
		    },

            new AdItem(){
		        Type="Google play",
		        Name="Quà Tặng Cuộc Sống",
		        Desc="Ứng dụng xem lại video Quà Tặng Cuộc sống phát sóng trên VTV3 lúc 22:15 hàng ngày",
		        Link="https://play.google.com/store/apps/details?id=com.hth.qtcs",
		        UrlImage="https://lh3.googleusercontent.com/kJ4kA4PS2GSNYKkLjW9UC3s2FTTvi6nacJOKSkXlL1Nm-VWgxtnb2geGWfqjh94dCg=w300",
		        InCountries="VN"
            },

		    new AdItem(){
		        Type="Google play",
		        Name="Fruit Link",
		        Desc="Pikachu Game, more play more fun",
		        Link="https://play.google.com/store/apps/details?id=com.hth.fruitlink",
		        UrlImage="https://lh3.googleusercontent.com/xBiot__H7a2OBnjijQXxS5xAEhLrbM6hQ2FdUhrP4ukS69n7_FlBR3RIu_L_FtHEZg=w300",
		    },
        
            new AdItem(){
		        Type="Google play",
		        Name="Photo Puzzle",
		        Desc="Puzzle Game, more play more fun",
		        Link="https://play.google.com/store/apps/details?id=com.hth.photopuzzle",
		        UrlImage="https://lh3.googleusercontent.com/5MBrxlD_Ct1D9QEQbqRVw0dxajJXGfsnk2JhkejsfchVKK6CcFDSOuewfixqV9oycw=w300",
		    },
        
            new AdItem(){
		        Type="Google play",
		        Name="Files Transfer",
		        DescVN="Truyền và quản lý các tập tin trên Android của bạn từ các thiết bị khác thông qua WiFi.",
		        Desc="Transfer and manage files on Android via WiFi.",
		        Link="https://play.google.com/store/apps/details?id=com.hth.filestransfer",
		        UrlImage="https://lh3.googleusercontent.com/EJvOjPkghTWeg_a-eH3fuimTpjKMmL5kp7ummS_FBPbhAoddB_Ur9tmZfzJTl_Zl32A=w300",
		    },

		    new AdItem(){
		        Type="Google play",
		        Name="Learn English By Videos",
		        DescVN="Tổng hợp các kênh học tiếng anh hay trên Youtube",
		        Desc="Collection of the best channels to learn English on Youtube",
		        Link="https://play.google.com/store/apps/details?id=com.hth.learnenglishbyvideos",
		        UrlImage="https://lh3.ggpht.com/jPHpzWNuoFd9M0fcKb3gkIUAuXllrJ58vabIPQiWpqRGA-toG3YEl-BczsTTbAjbK58=w300"
            }
        };

        public static List<AdItem> GetOwnerAds(string country)
        {
            List<AdItem> data = new List<AdItem>();
            var rs = OwnerAds.OWNER_ADITEMS.Where(p => p.IsAllowForCountry(country));

            rs = rs.Select(p =>
                new AdItem()
                {
                    Name = p.GetName(country),
                    Desc = p.GetDesc(country),
                    Link = p.Link,
                    UrlImage = p.UrlImage,
                    Type = p.Type
                }
                );
            if (rs != null) 
            { 
                data.AddRange(rs); 
            }
            return data;
        }
    }
}
