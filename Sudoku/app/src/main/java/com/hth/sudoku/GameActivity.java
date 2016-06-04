package com.hth.sudoku;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private int puzzle[];
    private PuzzleView puzzleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        int diff = getIntent().getIntExtra(Data.DIFFICULTY_KEY, Data.DIFFICULTY_EASY);
        puzzle = getPuzzle(diff);
        calculateUsedTiles();
        puzzleView = (PuzzleView) findViewById(R.id.puzzleView);// new PuzzleView(this);
        //setContentView(puzzleView);
        puzzleView.requestFocus();
    }

    boolean setTileIfValid(int x, int y, int value){
        int tiles[] = getUsedTiles(x,y);
        if (value != 0) {
            for(int tile:tiles)
            {
                if(tile==value){return false;}
            }
        }
        setTile(x,y,value);
        calculateUsedTiles();
        return true;
    }

    private final int used[][][] = new int[9][9][];

    protected int[] getUsedTiles(int x, int y)
    {
        return used[x][y];
    }

    private void calculateUsedTiles()
    {
        for(int x=0; x<9;x++)
        {
            for(int y=0;y<9;y++)
            {
                used[x][y] = calculateUsedTiles(x,y);
            }
        }
    }

    private int[] calculateUsedTiles(int x, int y){
        int c[] = new int[9];

        for(int i=0; i<9; i++)
        {
            if(i==y){continue;}
            int t=getTile(x,i);
            if (t != 0) {
                c[t-1]=t;
            }
        }

        int startx = x/3*3;
        int starty = y/3*3;

        for(int i = startx; i<startx+3; i++){
            for(int j = starty; j<starty+3; j++){
                if(i==x && j==y)
                {
                    continue;
                }
                int t=getTile(i,j);
                if(t!=0){
                    c[t-1] = t;
                }
            }
        }

        int nused = 0;
        for(int t:c){
            if(t!=0){nused++;}
        }

        int c1[] = new int [nused];
        nused=0;
        for(int t:c){
            if(t!=0){
                c1[nused++] = t;
            }
        }

        return c1;
    }

    private int[] getPuzzle(int diff)
    {
        String puz;
        switch (diff)
        {
            case Data.DIFFICULTY_HARD:
                puz = Data.HarPuzzle;
                break;
            case Data.DIFFICULTY_MEDIUM:
                puz = Data.MediumPuzzle;
                break;
            case Data.DIFFICULTY_EASY:
            default:
                puz = Data.EasyPuzzle;
                break;
        }

        return fromPuzzleString(puz);
    }

    protected int[] fromPuzzleString(String str)
    {
        int [] puz = new int[str.length()];
        for(int i =0; i<puz.length; i++)
        {
            puz[i] = str.charAt(i)-'0';
        }
        return puz;
    }

    public String getTileString(int x, int y){
        if(puzzle[y*9+x]==0) return "";
        return puzzle[y*9+x]+"";
    }

    private int getTile(int x, int y){
        return puzzle[y*9+x];
    }

    private void setTile(int x, int y, int value){
        puzzle[y*9+x] = value;
    }

    void showKeypadOrError(int x, int y){
        int tiles[] = getUsedTiles(x, y);
        if(tiles.length == 9)
        {
            Toast toast = Toast.makeText(this, R.string.no_move, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            Dialog v = new KeyPad(this, tiles, puzzleView);
            v.show();
        }
    }
}
