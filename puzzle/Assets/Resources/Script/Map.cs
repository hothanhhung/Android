using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Collections.Generic;

public class Map : MonoBehaviour {

	private int EMPTY_TYPE = -1;
	private Object prefap_piece;
	private Texture2D full_Image_texture;
	private int[][] MAP;
	private Vector3[][] POS;

	private static int MIN_X;
	private static int MIN_Y;
	private static int CELL_WIDTH = 28;
	private static int CELL_HEIGHT = 32;
	private float CAM_SIZE=320f;
	private float CAM_GAME_X = 0f;
	private float CAM_GAME_Y = 0f;
	private int OFFSET_Y_TOP=30;
	private int OFFSET_Y_BOTTOM=10;
	private int ROW = 0, COL = 0;

	private Vec2 OLD_POS;
	private float Scale_Image = 1.0f;

	// Use this for initialization
	void Start () {
		prefap_piece = Resources.Load ("Piece");
		if (prefap_piece == null)
			Debug.Log ("123");
		LMap (9, 7);
		MatchSizeImage ();
		PieceImage ();
	}
	
	// Update is called once per frame
	void Update () {
		if(Input.GetMouseButtonDown(0))
		{
			float x = (Input.mousePosition.x - Screen.width / 2) / Screen.width * (Screen.width * 1.0f / Screen.height);
			float y = ((Input.mousePosition.y - Screen.height / 2) / Screen.height)  ;
			x *= CAM_SIZE;
			y *= CAM_SIZE;
			int mouse_col = (int)((x-MIN_X)/CELL_WIDTH);
			int mouse_row = (int)((y - MIN_Y) / CELL_HEIGHT);
			Debug.Log(mouse_col + " " + mouse_row);
			if(mouse_col>-1 && mouse_col<COL && mouse_row>-1 && mouse_row < ROW && MAP[mouse_row][mouse_col] != -1){
				Vec2 pos = new Vec2(mouse_row, mouse_col);
				Select(pos);
				ProcessMovePiece(pos);
			}

		}
	}

	private void ProcessMovePiece(Vec2 pos)
	{
		Vec2 empty = GetPOSOfEmptyTypeBasedPos(pos);
		if(empty != null)
		{
			if(empty.R == pos.R)
			{
				if(empty.C > pos.C)
				{
					for(int i = empty.C; i > pos.C; i--)
					{
						Swap(new Vec2(empty.R, i), new Vec2(empty.R, i-1));
					}
				}
				else
				{
					for(int i = empty.C; i < pos.C; i++)
					{
						Swap(new Vec2(empty.R, i), new Vec2(empty.R, i+1));
					}
				}
			}else
			{
				{
					if(empty.R > pos.R)
					{
						for(int i = empty.R; i > pos.R; i--)
						{
							Swap(new Vec2(i, empty.C), new Vec2(i-1, empty.C));
						}
					}
					else
					{
						for(int i = empty.R; i < pos.R; i++)
						{
							Swap(new Vec2(i, empty.C), new Vec2(i+1, empty.C));
						}
					}
				}
			}
		}
		
	}

	private Vec2 GetPOSOfEmptyTypeBasedPos(Vec2 pos)
	{
		for (int i = 0; i < ROW; i++)
			if (MAP [i] [pos.C] == EMPTY_TYPE)
				return new Vec2 (i, pos.C);
		
		for (int i = 0; i < COL; i++)
			if (MAP [pos.R] [i] == EMPTY_TYPE)
				return new Vec2 (pos.R, i);

		return null;
	}

	void Select(Vec2 pos)
	{
		if(pos!=null && MAP[pos.R][pos.C] != -1){
			DeSelect(OLD_POS);
			OLD_POS = new Vec2(pos.R, pos.C);
			SetColorForSprite (OLD_POS, Color.green);
		}
	}
	
	
	void DeSelect(Vec2 pos)
	{
		SetColorForSprite (pos, Color.white);		
	}

	void SetColorForSprite(Vec2 pos, Color color)
	{
		if(pos != null){
			var obj = GetGameObjectFromPos(pos);
			if(obj != null){
				obj.GetComponent<SpriteRenderer> ().color = color;
			}
		}
	}

	public void LMap(int row, int col)
	{
		CAM_GAME_Y = CAM_SIZE - OFFSET_Y_TOP - OFFSET_Y_BOTTOM;
		ROW = row;
		COL = col;
		CELL_HEIGHT = (int)(CAM_GAME_Y / (ROW));//(int)(CAM_GAME_Y / (ROW - 2));
		if (CELL_HEIGHT > 30)
			CELL_HEIGHT = 30;
		//Debug.Log(CELL_HEIGHT);
		CELL_WIDTH = (int)(CELL_HEIGHT * 0.9f);
		
		ROW = row;
		COL = col;
		MAP = new int[ROW][];
		POS = new Vector3[ROW][];
		
		MIN_X = -COL * CELL_WIDTH / 2;
		MIN_Y = -(ROW) * CELL_HEIGHT / 2;
		//MAX_Y = (ROW) * CELL_HEIGHT / 2;

		for (int i = 0; i < ROW; i++)
		{
			MAP[i] = new int[COL];
			POS[i] = new Vector3[COL];
			for (int j = 0; j < COL; j++)
			{
				MAP[i][j] = -1;				
				POS[i][j] = new Vector3(0, 0, 0);
				POS[i][j].x = MIN_X + j * CELL_WIDTH;// + CELL_WIDTH / 2;
				POS[i][j].y = MIN_Y + i * CELL_HEIGHT;// + CELL_HEIGHT / 2;
				POS[i][j].z = i / 10.0f;

			}
		}

	}

	void PieceImage()
	{
		
		for(var i=0; i < ROW; i++)
		{
			for(var j = 0; j< COL; j++)
			{
				MAP[i][j] = i*COL+j;
				if(i*COL+j == ROW * COL - 1) EMPTY_TYPE = MAP[i][j];
				AddPiece(MAP[i][j], POS[i][j], CELL_WIDTH, CELL_HEIGHT, GetNameOfGameObjectFromPos(new Vec2(i, j)));
			}
		}
	}

	void AddPiece(int type,Vector3 pos,int width,int height, string objectName)
	{
		GameObject g = Instantiate(prefap_piece) as GameObject;
		g.name = objectName;
		g.transform.parent = this.transform;
		g.transform.position = pos;
		//Sprite sprite = Resources.Load(, typeof(Sprite)) as Sprite;
		Sprite sprite = GetSpriteFromType(type);
		g.GetComponent<SpriteRenderer>().sprite = sprite;
		g.transform.localScale = new Vector3(Mathf.Abs(width * 1.0f / sprite.bounds.size.x), Mathf.Abs (- height * 1.0f / sprite.bounds.size.y), 1);
		if(type == EMPTY_TYPE) g.GetComponent<SpriteRenderer> ().color = Color.grey;
		/*if(ROW_COL.Length < starsLevel){
			var val = Random.Range(0, 3);
			if(val==1) g.transform.Rotate(0, 180, 0);
			else if(val==2) g.transform.Rotate(180, 0, 0);
		}else{
			if(Random.Range(0, 3) == 1) g.transform.Rotate(0, 180, 0);
		}*/
	}

	private void MatchSizeImage()
	{
		full_Image_texture = Resources.Load("Images/Items/SwanSunset",typeof(Texture2D)) as Texture2D;
		int width = CELL_WIDTH * COL;
		int height = CELL_HEIGHT * ROW;
		if(full_Image_texture.height * 1.0f/height > full_Image_texture.width * 1.0f/width){
			Scale_Image= full_Image_texture.width/width;
		}else{
			Scale_Image= full_Image_texture.height/height;
		}
	}

	public void Swap(Vec2 v0, Vec2 v1)
	{
		int index0 = MAP[v0.R][v0.C];
		int index1 = MAP[v1.R][v1.C];
		MAP[v0.R][v0.C] = index1;
		MAP[v1.R][v1.C] = index0;
		SwapSpriteOfGameObject (v0, v1);
		//ItemManager.I.Swap(v0, v1);
	}
	
	private void SwapSpriteOfGameObject(Vec2 v0, Vec2 v1)
	{
		var obj0 = GetGameObjectFromPos(v0);
		var obj1 = GetGameObjectFromPos(v1);
		if(obj0 != null && obj1 != null)
		{
			var temptSprite = obj0.GetComponent<SpriteRenderer> ().sprite;
			obj0.GetComponent<SpriteRenderer> ().sprite = obj1.GetComponent<SpriteRenderer> ().sprite;
			obj1.GetComponent<SpriteRenderer> ().sprite = temptSprite;
		}
	}

	private GameObject GetGameObjectFromPos(Vec2 pos)
	{
		return GameObject.Find(GetNameOfGameObjectFromPos(pos));
	}
	
	private string GetNameOfGameObjectFromPos(Vec2 pos)
	{
		return "PKC_R" + pos.R+ "C" +pos.C;
	}

	private Sprite GetSpriteFromType(int type)
	{
		if (type < 0)
			return null;
		else {
			int row = type / COL;
			int col = type % COL;
			Sprite sprite = Sprite.Create(full_Image_texture, new Rect(POS[row][col].x - MIN_X, POS[row][col].y + CELL_HEIGHT - MIN_Y, CELL_WIDTH, CELL_HEIGHT), new Vector2(0f, 0f), 100);
			return sprite;
		}
	}
	/*
	void SetColorForSprite(Vec2 pos, Color color)
	{
		if(pos != null){
			var obj = GetGameObjectFromPos(pos);
			if(obj != null){
				obj.GetComponent<SpriteRenderer> ().color = color;
			}
		}
	}
	void Select(Vec2 pos)
	{
		if(pos!=null && MAP[pos.R][pos.C] != -1){
			POS1 = new Vec2(pos.R, pos.C);
			SetColorForSprite (POS1, Color.green);
			Debug.Log("selected " + POS1.Print());
		}
	}*/
}
