using hthservices.Utils;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Text;
using System.IO;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.Mvc;

namespace hthservices.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            return Redirect("Programming");
        }

        public ActionResult About()
        {
            ViewBag.Message = "Your app description page.";

            return View();
        }

        public class ContactModel
        {
            public string FullName { get; set; }
            public string PhoneNumber { get; set; }
            public string Email { get; set; }
            public string Content { get; set; }
            public bool IsEmpty { get { return string.IsNullOrWhiteSpace(FullName) || string.IsNullOrWhiteSpace(Email) || string.IsNullOrWhiteSpace(Content); } }
        }

        [ValidateInput(false)]
        public ActionResult Contact(ContactModel contact = null)
        {
            if (contact != null && !contact.IsEmpty)
            {
                SendEmailHelpers.SendEmail(contact.FullName, contact.PhoneNumber, contact.Email, contact.Content);
                ViewBag.Message = "Cảm bạn đã liên hệ, chúng tôi sẽ trả lời sớm nhất có thể.";
            }
            else
            {
                ViewBag.Message = string.Empty;
            }
            return View();
        }

        [System.Web.Http.Route("Common/GetImage/{text:string}")]
        public FileResult GenerateImage(string text)
        {
            var image = GenerateImage(text, 0);
            using (var ms = new MemoryStream())
            {
                image.Save(ms, System.Drawing.Imaging.ImageFormat.Png);

                return File(ms.ToArray(), "image/png");
            }
        }

        public FileResult Capcha()
        {
            var ran = new Random();
            var strBuilder = new StringBuilder();
            for(int i =0; i< 5; i++)
            {
                strBuilder.Append((char)ran.Next('A', 'Z'));
            }
            Session["Capcha"] = strBuilder.ToString();
            var image = GenerateImage(Session["Capcha"].ToString(), 1);
            using (var ms = new MemoryStream())
            {
                image.Save(ms, System.Drawing.Imaging.ImageFormat.Png);

                return File(ms.ToArray(), "image/png");
            }
        }

        private Bitmap GenerateImage(string text, int kind = 0)
        {
            int height = 250, width = 250;
            FontFamily fontFamily = new FontFamily("Arial");
            Color textColor = Color.DarkCyan;
            switch (kind)
            {
                case 0:
                    //Color.DarkCyan, Color.Brown, Color.DarkBlue, Color.DarkGreen, Color.Purple, Color.Navy, Color.DarkViolet, Color.DeepSkyBlue
                    var colors = new Color[] { Color.DarkCyan, Color.Brown, Color.DarkBlue, Color.DarkGreen, Color.Purple, Color.Navy, Color.DarkViolet, Color.DeepSkyBlue };
                    PrivateFontCollection pfc = new PrivateFontCollection();
                    pfc.AddFontFile(Server.MapPath("~/fonts/grease__.ttf"));
                    fontFamily = pfc.Families[0];
                    height = 250;
                    width = 250;
                    var ran = new Random();
                    textColor = colors[ran.Next(colors.Length)];
                    break;
                case 1:
                    height = 35;
                    width = 100;
                    break;
            }


            var image = MethodHelpers.ConvertTextToImage(text, fontFamily, Color.WhiteSmoke, textColor, width, height);
            return image;
        }
		
 		private static int numbRow = 3, numbCol = 3, block = 1;
        public ActionResult Create(int row = 3, int col = 3, int bl = 0)
        {
            numbRow = row;
            numbCol = col; 
            block = bl;

            ViewBag.Message = "Your contact page.";
            var values = create(numbRow, numbCol, block);
            ViewBag.ArrayPositions = values;
            return View("Contact");
        }

        private int[,] create(int row, int col, int block)
        {
            List<int> blocks = new List<int>();
            for (int i = 0; i < col * row; i++)
            {
                blocks.Add(i);
            }

            int[] rs = new int[col * row];

            int[,] array = new int[row, col];

            for (int x = 0; x < row; x++)
            {
                for (int y = 0; y < col; y++)
                {
                    array[x, y] = -1;
                }
            }

            Random rand = new Random();
            int start = rand.Next(col * row);
            blocks.Remove(start);

            for (int i = 0; i < block; i++ )
            {
                if (blocks.Count > 0)
                {
                    int blockIndex = rand.Next(blocks.Count);
                    array[blocks[blockIndex] / numbRow, blocks[blockIndex] % numbCol] = 0;
                    blocks.Remove(blockIndex);
                }
            }

           repeart(array, 1, start / numbRow, start % numbCol);

            return array;
        }

        private bool repeart(int[,] array, int number, int x, int y)
        {
            array[x, y] = number;
            //print(array);
            if (number < numbRow * numbCol - block)
            {
                var positions = GetListPosition(array, x, y);
                if (positions == null || positions.Count == 0)
                {
                    return false;
                }
                Random rand = new Random();
                while (positions.Count > 0)
                {
                    int index = rand.Next(positions.Count);
                    var position = positions.ElementAt(index);
                    if (repeart(array, number + 1, position.Key, position.Value))
                    {
                        return true;
                    }
                    else
                    {
                        array[position.Key, position.Value] = -1;
                    }
                    positions.RemoveAt(index);
                }

                //if (!isFull(array))
                //{
                //    return false;
                //}
                //else
                {
                    return false;
                }

            }
            else
            {
                return true;
            }
        }

        private List<KeyValuePair<int, int>> GetListPosition(int[,] array, int x, int y)
        {
            List<KeyValuePair<int, int>> rs = new List<KeyValuePair<int, int>>();

            // top
            if (x - 1 >= 0 && array[x - 1, y] == -1)
            {
                rs.Add(new KeyValuePair<int, int>(x - 1, y));
            }

            //left
            if (y - 1 >= 0 && array[x, y - 1] == -1)
            {
                rs.Add(new KeyValuePair<int, int>(x, y - 1));
            }

            //below
            if (x + 1 < numbRow && array[x + 1, y] == -1)
            {
                rs.Add(new KeyValuePair<int, int>(x + 1, y));
            }

            //right
            if (y + 1 < numbCol && array[x, y + 1] == -1)
            {
                rs.Add(new KeyValuePair<int, int>(x, y + 1));
            }

            //top - left
            if (x - 1 >= 0 && y - 1 >= 0 && array[x - 1, y - 1] == -1)
            {
                rs.Add(new KeyValuePair<int, int>(x - 1, y - 1));
            }

            //top - right
            if (x - 1 >= 0 && y + 1 < numbCol && array[x - 1, y + 1] == -1)
            {
                rs.Add(new KeyValuePair<int, int>(x - 1, y + 1));
            }

            //below - left
            if (x + 1 < numbRow && y - 1 >= 0 && array[x + 1, y - 1] == -1)
            {
                rs.Add(new KeyValuePair<int, int>(x + 1, y - 1));
            }

            //below - right
            if (x + 1 < numbRow && y + 1 < numbCol && array[x + 1, y + 1] == -1)
            {
                rs.Add(new KeyValuePair<int, int>(x + 1, y + 1));
            }

            return rs;
        }

        private bool isFull(int[,] array)
        {
            for(int x=0; x<numbRow; x++)
            {
                for (int y = 0; y < numbCol; y++)
                {
                    if (array[x, y] == -1) return false;
                }
            }
            return true;
        }

        private void print(int[,] array)
        {
            for (int x = 0; x < numbRow; x++)
            {
                for (int y = 0; y < numbCol; y++)
                {
                    System.Diagnostics.Debug.Write(String.Format("{0:3}", array[x, y]));
                }
                System.Diagnostics.Debug.WriteLine("");
            }
        }
    }
}
