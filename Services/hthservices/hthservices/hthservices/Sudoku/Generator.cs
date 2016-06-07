using System;
using System.Collections.Generic;
using System.Linq;

namespace TrueMagic.SudokuGenerator
{
    public class Generator
    {
        private readonly Random random = new Random();

        private const int MaxTries = 10000;
        private const int RandomCells = 15;
        private static readonly IDictionary<Level, Tuple<int, int>> LevelToClearCells = new Dictionary<Level, Tuple<int, int>>() 
        { 
            { Level.VeryEasy, new Tuple<int, int>(20, 30) },
            { Level.Easy, new Tuple<int, int>(30, 45) },
            { Level.Medium, new Tuple<int, int>(46, 54) },
            { Level.Hard, new Tuple<int, int>(55, 65) },
            { Level.VeryHard, new Tuple<int, int>(66, 77) },
        };

        public Sudoku Generate(int blockSize, Level level)
        {
            for (var count = 0; count < MaxTries; count++)
            {
                var sudoku = new Sudoku(blockSize);
                if (GenerateSolution(sudoku))
                {
                    if (CreateBoard(sudoku, level))
                    {
                        return sudoku;
                    }
                }
            }
            return null;
        }

        private bool GenerateSolution(Sudoku sudoku)
        {
            return GenerateRandomValues(sudoku) && SolveSolution(sudoku);
        }

        private bool GenerateRandomValues(Sudoku sudoku)
        {
            var indexes = Enumerable.Range(0, sudoku.BoardSize * sudoku.BoardSize).ToList();
            for (int i = 0; i < RandomCells; i++)
            {
                var index = indexes[this.random.Next(indexes.Count)];
                indexes.Remove(index);
                var x = index / sudoku.BoardSize;
                var y = index % sudoku.BoardSize;
                var possibleValues = sudoku.GetPossibleValues(x, y);
                if (possibleValues.Count == 0)
                {
                    return false;
                }
                sudoku.SetValue(x, y, possibleValues[random.Next(possibleValues.Count)]);
            }
            return true;
        }

        public bool SolveSolution(Sudoku sudoku)
        {
            var range = Enumerable.Range(0, sudoku.BoardSize);
            var solutions = 
                range
                .SelectMany(x => range.Select(y => new Tuple<int, int>(x, y)))
                .Where(item => sudoku.GetValue(item.Item1, item.Item2) == 0)
                .OrderBy(item => sudoku.GetPossibleValues(item.Item1, item.Item2).Count)
                .ToList();
            var index = 0;
            var possibleValuesCache = new Dictionary<int, IList<byte>>();
            int count = 0;
            while (index >= 0 && index < solutions.Count)
            {
                count++;
                if (count > 10338434) break;
                var x = solutions[index].Item1;
                var y = solutions[index].Item2;
                if (!possibleValuesCache.ContainsKey(index))
                {
                    possibleValuesCache[index] = sudoku.GetPossibleValues(x, y);
                }
                var possibleValues = possibleValuesCache[index];
                if (possibleValues.Count == 0)
                {
                    sudoku.SetValue(x, y, 0);
                    possibleValuesCache.Remove(index);
                    index--;
                }
                else
                {
                    var value = possibleValues[random.Next(0, possibleValues.Count)];
                    possibleValues.Remove(value);
                    sudoku.SetValue(x, y, value);
                    index++;
                }
            }
            return index >= solutions.Count;
        }

        /// <summary>
        /// Empties cells from the solved sudoku board to create playable game board. 
        /// The result has a number of empty cells dependant on the level and only one correct solution.
        /// </summary>
        private bool CreateBoard(Sudoku sudoku, Level level)
        {
            // All indexes
            var cells = Enumerable.Range(0, sudoku.BoardSize).SelectMany(x => Enumerable.Range(0, sudoku.BoardSize).Select(y => new Tuple<int, int>(x, y))).ToList();
            // List of already veryfied, cleared cell indexes
            var clearedCells = new List<Tuple<int, int>>();
            // Number/index of the cell under try 
            var cellNumber = 0;
            // How many cleared cells are required for the given level
            var clearedCellsRange = LevelToClearCells[level];

            // As long as there are more cells to try and the number of cleared cells is not higher then required maximum.
            while (cells.Count > 0 && clearedCells.Count < clearedCellsRange.Item2)
            {
                // Cell index to try
                var cell = GetNextCell(cells, level, cellNumber++);
                var currentValue = sudoku.GetValue(cell.Item1, cell.Item2);
                // Make sure that any value different from the current one disables the solution.
                // Otherwise more than 1 solution would be possible after clearing this cell.
                var valuesToCheck = sudoku.GetPossibleValues(cell.Item1, cell.Item2).ToList();
                valuesToCheck.Remove(currentValue);
                var correct = true;
                foreach (var valueToCheck in valuesToCheck)
                {
                    sudoku.SetValue(cell.Item1, cell.Item2, valueToCheck);
                    if (SolveSolution(sudoku))
                    {
                        // Other solution possible - this cell cannot be cleared.
                        correct = false;
                        break;
                    }
                }
                // If no other solution is possible we can clear this cell.
                if (correct)
                {
                    clearedCells.Add(new Tuple<int, int>(cell.Item1, cell.Item2));
                }
                // Make sure the cleared cells are really clear.
                foreach (var clearedCell in clearedCells)
                {
                    sudoku.SetValue(clearedCell.Item1, clearedCell.Item2, 0);
                }

                if (!correct)
                {
                    // Other solution possible - this cell cannot be cleared.
                    // Set it to its original value.
                    sudoku.SetValue(cell.Item1, cell.Item2, currentValue);
                }
            }
            return clearedCells.Count >= clearedCellsRange.Item1;
        }

        /// <summary>
        /// Gets the next cell to try to clear it.
        /// For the easiest levels it is a random cell.
        /// For medium the method returns every other cell.
        /// For hardest levels it returns the first not tried cell.
        /// </summary>
        private Tuple<int, int> GetNextCell(IList<Tuple<int, int>> cells, Level level, int cellNumber)
        {
            var index = 0;
            if (level == Level.VeryEasy || level == Level.Easy)
            {
                index = this.random.Next(cells.Count);
            }
            if (level == Level.Medium)
            {
                index = cellNumber % cells.Count;
            }
            var cell = cells[index];
            cells.RemoveAt(index);
            return cell;
        }
    }
}
