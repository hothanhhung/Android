using System.Collections.Generic;
using System.Linq;

namespace TrueMagic.SudokuGenerator
{
    public class Sudoku
    {
        public int BlockSize { get; private set; }
        public int BoardSize { get; private set; }
        private readonly byte[] possibleValues;
        private readonly IDictionary<int, int> blockIndex = new Dictionary<int, int>();
        private readonly IDictionary<int, int> inBlockIndex = new Dictionary<int, int>();
        private byte[][] rows;
        private byte[][] columns;
        private byte[][] blocks;
        private HashSet<byte>[] rowValues;
        private HashSet<byte>[] columnValues;
        private HashSet<byte>[] blockValues;

        public Sudoku(int blockSize)
        {
            this.BlockSize = blockSize;
            this.BoardSize = blockSize * blockSize;
            this.possibleValues = Enumerable.Range(1, this.BoardSize).Select(value => (byte)value).ToArray();

            this.rows = new byte[this.BoardSize][];
            this.columns = new byte[this.BoardSize][];
            this.blocks = new byte[this.BoardSize][];
            this.rowValues = new HashSet<byte>[this.BoardSize];
            this.columnValues = new HashSet<byte>[this.BoardSize];
            this.blockValues = new HashSet<byte>[this.BoardSize];
            for (var x = 0; x < this.BoardSize; x++)
            {
                this.rows[x] = new byte[this.BoardSize];
                this.columns[x] = new byte[this.BoardSize];
                this.blocks[x] = new byte[this.BoardSize];
                this.rowValues[x] = new HashSet<byte>();
                this.columnValues[x] = new HashSet<byte>();
                this.blockValues[x] = new HashSet<byte>();
            }

            for (var blockX = 0; blockX < this.BlockSize; blockX++)
            {
                for (var blockY = 0; blockY < this.BlockSize; blockY++)
                {
                    for (var x = 0; x < this.BlockSize; x++)
                    {
                        for (var y = 0; y < this.BlockSize; y++)
                        {
                            var itemX = blockX * this.BlockSize + x;
                            var itemY = blockY * this.BlockSize + y;
                            this.blockIndex[itemY * this.BoardSize + itemX] = blockY * this.BlockSize + blockX;
                            this.inBlockIndex[itemY * this.BoardSize + itemX] = y * this.BlockSize + x;
                        }
                    }
                }
            }
        }

        private Sudoku(Sudoku sudoku)
        {
            this.BlockSize = sudoku.BlockSize;
            this.BoardSize = sudoku.BoardSize;
            this.possibleValues = sudoku.possibleValues;
            this.blockIndex = sudoku.blockIndex;
            this.inBlockIndex = sudoku.inBlockIndex;
            this.rows = sudoku.rows.Select(row => row.ToArray()).ToArray();
            this.columns = sudoku.columns.Select(column => column.ToArray()).ToArray();
            this.blocks = sudoku.blocks.Select(block => block.ToArray()).ToArray();
            this.rowValues = sudoku.rowValues.Select(values => new HashSet<byte>(values)).ToArray();
            this.columnValues = sudoku.rowValues.Select(values => new HashSet<byte>(values)).ToArray();
            this.blockValues = sudoku.rowValues.Select(values => new HashSet<byte>(values)).ToArray();
        }

        public byte GetValue(int x, int y)
        {
            return this.rows[x][y];
        }

        public void SetValue(int x, int y, byte value)
        {
            var oldValue = GetValue(x, y);
            this.rows[x][y] = value;
            this.columns[y][x] = value;
            var blockIndex = this.blockIndex[y * this.BoardSize + x];
            this.blocks[blockIndex][this.inBlockIndex[y * this.BoardSize + x]] = value;
            this.rowValues[x].Remove(oldValue);
            this.rowValues[x].Add(value);
            this.columnValues[y].Remove(oldValue);
            this.columnValues[y].Add(value);
            this.blockValues[blockIndex].Remove(oldValue);
            this.blockValues[blockIndex].Add(value);
        }

        public bool CanSetValue(int x, int y, byte value)
        {
            return !this.rowValues[x].Contains(value) && !this.columnValues[y].Contains(value) && !this.blockValues[this.blockIndex[y * this.BoardSize + x]].Contains(value);
        }

        public IList<byte> GetPossibleValues(int x, int y)
        {
            return this.possibleValues.Where(value => CanSetValue(x, y, value)).ToList();
        }

        public Sudoku Clone()
        {
            return new Sudoku(this);
        }
    }
}
