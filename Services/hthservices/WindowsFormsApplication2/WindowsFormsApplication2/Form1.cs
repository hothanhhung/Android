using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApplication2
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }
        Thread thread;
        private void button1_Click(object sender, EventArgs e)
        {
            button1.Enabled = false;
            button2.Enabled = true;
            button3.Enabled = false;
            if (thread != null && thread.IsAlive)
            {
                thread.Abort();
                thread = null;
            }

            if(int.TryParse(txtRows.Text, out numbRow) &&
                int.TryParse(txtColumns.Text, out numbCol) &&
                int.TryParse(txtBlock.Text, out block))
            {
                thread = new Thread(Create);
                thread.Start();
            }
            progressBar1.Style = ProgressBarStyle.Marquee;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            if(thread != null && thread.IsAlive)
            {
                thread.Abort();
                thread = null;
            }
            button1.Enabled = true;
            button2.Enabled = false;
            progressBar1.Style = ProgressBarStyle.Blocks;
        }

        private void viewCurrent(int[,] array)
        {
            if (needView)
            {
                String str = "";
                StringBuilder st = new StringBuilder();
                for (int i = 0; i < array.GetLength(0); i++)
                {
                    for (int j = 0; j < array.GetLength(1); j++)
                    {
                       // if (array[i, j] == 0) { ArrayPositions[i, j] = -1; }
                        st.Append(String.Format("<span style='width:25px'> {0} </span>", array[i, j]));
                        str += "|" + array[i, j];
                    }
                    st.AppendLine("<br/>");
                }
                //st.Append(str);
                webBrowser3.Invoke(new Action(() => webBrowser3.DocumentText = st.ToString()));
            }
            needView = false;
        }

        private void button3_Click(object sender, EventArgs e)
        {
            Random ran = new Random();
            String str = "";
            StringBuilder st = new StringBuilder();
            int numberOfHidden = 0;
            for (int i = 0; i < ArrayPositions.GetLength(0); i++)
            {
                for (int j = 0; j < ArrayPositions.GetLength(1); j++)
                {
                    if (ArrayPositions[i, j] == 0) { ArrayPositions[i, j] = -1; }
                    if (ran.Next(2) == 1 || ArrayPositions[i, j] < 0)
                    {
                        st.Append(String.Format("<span style='width:25px'> {0} </span>", ArrayPositions[i, j]));
                        str += "|" + ArrayPositions[i, j];
                    }
                    else
                    {
                        st.Append(String.Format("<span style='width:25px'> {0} </span>", 0));
                        str += "|" + 0;
                        numberOfHidden++;
                    }
                }
                st.AppendLine("<br/>");
            }
            st.AppendLine(String.Format("<br/><span style='width:25px'> {0} </span>", numberOfHidden));
            //st.Append(str);
            webBrowser2.DocumentText = st.ToString();
            textBox2.Text = str.Trim().Trim('|');
        }

        int[,] ArrayPositions;
        private static int numbRow = 3, numbCol = 3, block = 1, tryTimes = 0;
        public void Create()//int row = 3, int col = 3, int bl = 0)
        {
            //numbRow = row;
            //numbCol = col;
            //block = bl;
            ArrayPositions = create(numbRow, numbCol, block);
            String str = "";
            StringBuilder st = new StringBuilder();
            for (int i = 0; i < ArrayPositions.GetLength(0); i++)
            {
                for (int j = 0; j < ArrayPositions.GetLength(1); j++)
                {
                    if (ArrayPositions[i, j] == 0) { ArrayPositions[i, j] = -1; }
                    st.Append(String.Format("<span style='width:25px'> {0} </span>", ArrayPositions[i, j]));
                    str += "|" + ArrayPositions[i, j];
                }
                st.AppendLine("<br/>");
            }
            //st.Append(str);
            textBox1.Invoke(new Action(() => textBox1.Text = str.Trim().Trim('|')));
            webBrowser1.Invoke(new Action(() => webBrowser1.DocumentText = st.ToString()));
            button1.Invoke(new Action(() => button1.Enabled = true));
            button2.Invoke(new Action(() => button2.Enabled = false));
            button3.Invoke(new Action(() => button3.Enabled = true));
            progressBar1.Invoke(new Action(() => progressBar1.Style = ProgressBarStyle.Blocks));
            
        }

        private int[,] create(int row, int col, int block)
        {
            tryTimes = 0;
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

            for (int i = 0; i < block; i++)
            {
                if (blocks.Count > 0)
                {
                    int blockIndex = rand.Next(blocks.Count);
                    array[blocks[blockIndex] / numbCol, blocks[blockIndex] % numbCol] = 0;
                    blocks.Remove(blockIndex);
                }
            }

            repeart(array, 1, start / numbCol, start % numbCol);

            return array;
        }

        private bool repeart(int[,] array, int number, int x, int y)
        {
            tryTimes++;
            label1.Invoke(new Action(() => label1.Text = number.ToString()));
            label2.Invoke(new Action(() => label2.Text = tryTimes.ToString("D10")));
            array[x, y] = number;
            //print(array);
            viewCurrent(array);
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
                    if (havePathForAll(array, position.Key, position.Value))
                    {
                        if (repeart(array, number + 1, position.Key, position.Value))
                        {
                            return true;
                        }
                        else
                        {
                            array[position.Key, position.Value] = -1;
                        }
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
            for (int x = 0; x < numbRow; x++)
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
        private List<KeyValuePair<int, int>> GetListPositionNeedCheck(int[,] array)
        {
            List<KeyValuePair<int, int>> rs = new List<KeyValuePair<int, int>>();

            for (int i = 0; i < array.GetLength(0); i++)
            {
                for (int j = 0; j < array.GetLength(1); j++)
                {
                    if (array[i, j] == -1)
                    {
                        rs.Add(new KeyValuePair<int, int>(i, j));
                    }
                }
            }
            return rs;
        }

        private bool havePathForAll(int[,] array, int targetX, int targetY)
        {
            int numberOnePath = 0;
            var positions = GetListPositionNeedCheck(array);
            if (positions != null && positions.Count > 1)
            {
                var position = positions[0];
                if (position.Key == targetX && targetY == position.Value)
                {
                    position = positions[1];
                }
                int[,] array1 = tryFillPath(array, position.Key, position.Value, targetX, targetY);

                foreach (var pos in positions)
                {
                    if (pos.Key != targetX || pos.Value != targetY)
                    {
                        if (array1[pos.Key, pos.Value] != 0)
                        {
                            return false;
                        }
                        int temp = array[targetX, targetY];
                        array[targetX, targetY] = 0;
                        var p = GetListPosition(array, pos.Key, pos.Value);
                        array[targetX, targetY] = temp;
                        if (p == null || p.Count < 2)
                        {
                            numberOnePath++;
                        }
                        if (numberOnePath > 2) return false;
                    }
                }
            }
            return true;
        }
        private int[,] tryFillPath(int[,] array, int x1, int y1, int x2, int y2)
        {
            int[,] array1 = new int[array.GetLength(0), array.GetLength(1)];
            for (int i = 0; i < array.GetLength(0); i++)
            {
                for (int j = 0; j < array.GetLength(1); j++)
                {
                    if (i == x2 && j == y2)
                    {
                        array1[i, j] = 1;
                    }
                    else
                        if (array[i, j] == -1)// && )
                        {
                            array1[i, j] = -1;
                        }
                        else
                        {
                            array1[i, j] = 1;
                        }
                }
            }
            array1[x1, y1] = 0;
            fillPath(array1, x1, y1);
            return array1;
        }

        private void fillPath(int[,] array1, int x1, int y1)
        {
            //array1[x1, y1] = 0;
            var positions = GetListPosition(array1, x1, y1);
            if (positions == null || positions.Count == 0)
            {
                return ;
            }
            foreach(var position in positions)
            {
                array1[position.Key, position.Value] = 0;
            }
            while (positions.Count > 0)
            {
                var position = positions.ElementAt(0);
                fillPath(array1, position.Key, position.Value);
                positions.RemoveAt(0);
            }
        
        }

        private bool isNear(int x, int y, int x2, int y2)
        {
            // same
            if (x == x2 && y == y2)
            {
                return true;
            }

            // top
            if (x - 1 == x2 && y == y2)
            {
                return true;
            }

            //left
            if (y - 1 == y2 && x == x2)
            {
                return true;
            }

            //below
            if (x + 1 == x2 && y == y2)
            {
                return true;
            }

            //right
            if (y + 1 == y2 && x == x2)
            {
                return true;
            }

            //top - left
            if (x - 1 == x2 && y - 1 == y2)
            {
                return true;
            }

            //top - right
            if (x - 1 == x2 && y + 1 == y2)
            {
                return true;
            }

            //below - left
            if (x + 1 == x2 && y - 1 == y2)
            {
                return true;
            }

            //below - right
            if (x + 1 == x2 && y + 1 == y2)
            {
                return true;
            }

            return false;
        }

        private void Form1_FormClosed(object sender, FormClosedEventArgs e)
        {
            if (thread != null && thread.IsAlive)
            {
                thread.Abort();
            }
            thread = null;
        }
        bool needView = false;
        private void button4_Click(object sender, EventArgs e)
        {
            needView = true;
        }


    }
}
