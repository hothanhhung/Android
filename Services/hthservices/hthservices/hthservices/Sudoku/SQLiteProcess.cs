using System;
using System.Collections.Generic;
using System.Data.SQLite;
using System.Linq;
using System.Web;

namespace hthservices.Sudoku
{
    public class SQLiteProcess
    {
        private static string connectString;
        private static string ConnectString
        {
            get
            {
                return @"Data Source=D:\hung\github\android\Sudoku1.db3;Version=3;New=False;Compress=True;UTF8Encoding=True";
                //if (string.IsNullOrWhiteSpace(connectString))
                //{
                //    connectString = "Data Source=" + HttpContext.Current.Server.MapPath("~/Data/TVSchedules.db3") + ";Version=3;New=False;Compress=True;UTF8Encoding=True";
                //}
                //return connectString;
            }
        }
        public static void SaveSudoku(SudokuItem sudokuItem)
        {
            string sqlSaveSudoku = "";
            sqlSaveSudoku = " INSERT INTO Games(OriginalMap, Difficulty) " +
                                 "    SELECT @OriginalMap, @Difficulty " +
                                 " WHERE NOT EXISTS (SELECT * FROM Games WHERE OriginalMap= @OriginalMap)";
            using (var sql_con = new SQLiteConnection(ConnectString))
            {
                sql_con.Open();
                using (var sql_cmd = sql_con.CreateCommand())
                {
                    sql_cmd.CommandText = sqlSaveSudoku;
                    SQLiteParameterCollection myParameters = sql_cmd.Parameters;
                    myParameters.AddWithValue("@OriginalMap", sudokuItem.OriginalMap);
                    myParameters.AddWithValue("@Difficulty", sudokuItem.Difficulty);
                    sql_cmd.ExecuteNonQuery();
                }
                sql_con.Close();
            }
        }

    }
}