using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Web;

namespace hthservices.Sudoku
{
    public class HtmlHelper
    {

        static public SudokuItem GenSudoku(int le)
        {
            for (int level = 1; level < 5; level++)
            {
                for (int i = 0; i < 1000; i++)
                {
                    var gen = new TrueMagic.SudokuGenerator.Generator();
                    TrueMagic.SudokuGenerator.Sudoku s = null;
                    switch (level)
                    {
                        case 1:
                            s = gen.Generate(3, TrueMagic.SudokuGenerator.Level.Easy);
                            break;
                        case 2:
                            s = gen.Generate(3, TrueMagic.SudokuGenerator.Level.Medium);
                            break;
                        case 3:
                            s = gen.Generate(3, TrueMagic.SudokuGenerator.Level.Hard);
                            break;
                        case 4:
                            s = gen.Generate(3, TrueMagic.SudokuGenerator.Level.VeryHard);
                            break;
                    }
                    if (s != null)
                    {
                        var str = new StringBuilder();
                        for (var x = 0; x < 9; x++)
                            for (var y = 0; y < 9; y++)
                            {
                                str.Append(s.GetValue(x, y));
                            }
                        if (str.Length == 81)
                        {
                            SQLiteProcess.SaveSudoku(new SudokuItem() { OriginalMap = str.ToString(), Difficulty = level });
                        }
                    }
                }
            }
            return null;
        }
        //http://www.menneske.no/sudoku/eng/random.html?diff=9
        static public List<SudokuItem> GetSudoku(string url,int difficulty)
        {

            List<SudokuItem> guideItems = new List<SudokuItem>();
            try
            {
                HttpClient http = new HttpClient();
                var response = http.GetByteArrayAsync(url).Result;
                String source = Encoding.GetEncoding("utf-8").GetString(response, 0, response.Length - 1);
                source = WebUtility.HtmlDecode(source);
                HtmlDocument resultat = new HtmlDocument();
                resultat.LoadHtml(source);

                var chanelDetails = resultat.DocumentNode.SelectNodes("//div[@class='grid']");
                if (chanelDetails != null && chanelDetails.Count > 0)
                {
                    StringBuilder map = new StringBuilder();
                    // get start time
                    var tdTags = chanelDetails.FirstOrDefault().SelectNodes("table//td");
                    foreach (var td in tdTags)
                    {
                        map.Append(td.InnerText.Trim());
                    }
                    if (map.Length == 91)
                    {
                        var sudokuItem = new SudokuItem() { OriginalMap = map.ToString(), Difficulty = difficulty };
                        guideItems.Add(sudokuItem);
                    }
                }
            }
            catch (Exception ex)
            {

            }

            return guideItems;
        }
    }
}