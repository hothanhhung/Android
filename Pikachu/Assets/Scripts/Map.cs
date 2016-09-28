using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Collections.Generic;

public class Map : MonoBehaviour 
{
	private const string SOUND_ON ="SOUND : ON";
	private const string SOUND_OFF ="SOUND : OFF";
	private const string COUNT_DOWN_ON ="COUNT DOWN : ON";
	private const string COUNT_DOWN_OFF ="COUNT DOWN : OFF";

	private bool isEnableSound = true;
	private const int  MAX_NUMBER_ITEM = 36;
	private int  NUMBER_ITEM = 0;
	private bool stopGame = false;
	public GameObject startPanel;
	public GameObject helpPanel;
	public GameObject winPanel;
	public GameObject gameoverPanel;
	public GameObject pausePanel;
	public GameObject headerPanel;
	public Button hintButton;
	public Button pauseButton;

	private GameObject objectPos1;
	private GameObject objectPos2;

	public Object prefap_pikachu;
	int [][]ROW_COL = { new int[]{6, 5, 15}, new int[]{8, 5, 15}, new int[]{7, 6, 20}, new int[]{8, 6, 20} ,new int[]{9, 6, 40} ,
						new int[]{10, 6, 60}, new int[]{10, 7, 100}, new int[]{10, 8, 140}, new int[]{11, 8, 200},	new int[]{10, 9, 240} ,
						new int[]{10, 10, 280}, new int[]{11, 10, 320}, new int[]{12, 10, 360} /*, new int[]{13, 10}, new int[]{14, 10} */};
	int ROW = 0, COL = 0;
	public int[][] MAP;
	public bool[][] SHIT;
	public Vec2[][] SHIT_ROOT_POS;
	public Vec2 POS1;
	public Vec2 POS2;
	public Vector3[][] POS;
	public static int MIN_X;
	public static int MIN_Y;
	public static int CELL_WIDH = 28;
	public static int CELL_HEIGHT = 32;
	private float CAM_SIZE=320f;
	private float CAM_GAME_X = 0f;
	private float CAM_GAME_Y = 0f;
	private int OFFSET_Y_TOP=30;
	private int OFFSET_Y_BOTTOM=10;

	public Text timeText;
	public Text notifyHideSomeItemsText;
	public Text starsText;
	public Text soundText;
	public Text soundMainMenuText;
	public Text numberHintText;
	public Text countDownText;
	private int numberHint = 0;
	private int starsLevel = 0;
	private float totalTime=0;
	private AudioSource source;
	public AudioClip shootSound;
	public AudioClip changeMapSound;
	public AudioClip gameoverSound;
	public AudioClip wrongSelectionSound;
	private float volLowRange = .5f;
	private float volHighRange = 1.0f;
	private LineRenderer lineRenderer1;
	public GameObject objectLineRenderer2;
	private LineRenderer lineRenderer2;
	public GameObject objectLineRenderer3;
	private LineRenderer lineRenderer3;

	private float lastShowAds = 0;
	private long timeToOpenApp=0;

	void Awake () {		
		lineRenderer1 = this.GetComponent<LineRenderer> ();
		lineRenderer2 = objectLineRenderer2.GetComponent<LineRenderer> ();
		lineRenderer3 = objectLineRenderer3.GetComponent<LineRenderer> ();

		source = GetComponent<AudioSource>();
		if (isEnableSound) {
			var backgroundSound = GameObject.Find ("BackgroundSound");
			var backgroundSoundAudioSource = backgroundSound.GetComponent<AudioSource> ();
			backgroundSoundAudioSource.Play ();
		}
	}

	void Start () 
	{
		lastShowAds = Time.realtimeSinceStartup;
		timeToOpenApp = System.DateTime.Now.Ticks;
		stopGame = true;
		SaveLoad.Load ();
		isCountDownMode = SaveLoad.SavedData.IsCountDown;
		isEnableSound = SaveLoad.SavedData.IsSound;
		UpdateForUI ();
		Screen.orientation = ScreenOrientation.Portrait;
		AdmobService.RequestBanner ();
	}

	void StartPlay (bool isNewLevel) 
	{

		startPanel.SetActive (false);
		headerPanel.SetActive (true);
		ChangeEnableHeaderButton (true);
		stopGame = false;
		prefap_pikachu = Resources.Load ("item");
		if (prefap_pikachu == null)
			Debug.Log ("123");
		//LMap(10, 15);
		if (isNewLevel) {
			starsLevel = SaveLoad.SavedData.StarsLevel;
			numberHint = SaveLoad.SavedData.NumberOfHint;
			NUMBER_ITEM = SaveLoad.SavedData.NumberOfItem;

			starsLevel++;
			if (NUMBER_ITEM == 0)
				NUMBER_ITEM = 15;
			else 
				NUMBER_ITEM = NUMBER_ITEM + 5;
			if (NUMBER_ITEM > MAX_NUMBER_ITEM)
				NUMBER_ITEM = MAX_NUMBER_ITEM;
		}

		if (starsLevel < 1)
			starsLevel = 1;

		if(starsLevel<ROW_COL.Length)
		{
			LMap(ROW_COL[starsLevel-1][0], ROW_COL[starsLevel-1][1]);
			if(isCountDownMode)
			{
				totalTime = ROW_COL[starsLevel-1][2];
			}else {
				totalTime = 0;
			}
		}
		else{

			LMap(ROW_COL[ROW_COL.Length - 1][0], ROW_COL[ROW_COL.Length - 1][1]);
			if(isCountDownMode)
			{
				if(starsLevel < LEVEL_FOR_HIDE_SOME_ITEMS){
					totalTime = ROW_COL[ROW_COL.Length - 1][2] + 60;
				}else{
					var numberItems = ROW_COL[ROW_COL.Length - 1][0] * ROW_COL[ROW_COL.Length - 1][1];
					var numberHideItems = starsLevel - LEVEL_FOR_HIDE_SOME_ITEMS + NUMBER_HIDE_ITEM_IN_THE_FIRST;
					if(numberHideItems > numberItems/2) numberHideItems = numberItems/2;
					totalTime = ROW_COL[ROW_COL.Length - 1][2] + 120;
					if(numberHideItems>NUMBER_HIDE_ITEM_IN_THE_FIRST){
						totalTime += (numberHideItems - NUMBER_HIDE_ITEM_IN_THE_FIRST) * 20;
					}
				}
			}else {
				totalTime = 0;
			}
		}
		RandomMap();
		CheckAndSwapThings (false); // make sure has a couple
		starsText.text = "" + starsLevel;
		numberHintText.text = "" + numberHint;
		if(starsLevel < LEVEL_FOR_HIDE_SOME_ITEMS){
			notifyHideSomeItemsText.gameObject.SetActive(false);
		}
		else{
			timeForHideSomeItems = TIME_FOR_HIDE_SOME_ITEMS;
			notifyHideSomeItemsText.gameObject.SetActive(true);
		}
	}
	void FixedUpdate()
	{
		if (stopGame)
			return;
		if(isCountDownMode) totalTime -= Time.deltaTime;
		else
			totalTime += Time.deltaTime;
		timeForHideSomeItems -= Time.deltaTime;
		timeText.text = "" + Mathf.RoundToInt(totalTime);
		notifyHideSomeItemsText.text = "Hide In " + Mathf.RoundToInt(timeForHideSomeItems);
		 
	}

	void Update () 
	{
		if (stopGame)
			return;
		if(totalTime < 0)
		{
			ShowInterstitialAds();
			if (isEnableSound) {
				float vol = Random.Range (volLowRange, volHighRange);
				source.PlayOneShot (gameoverSound, vol);
			}
			stopGame = true;
			this.enabled = false;
			gameoverPanel.SetActive(true);
			return;
		}
		if(Input.GetMouseButtonDown(0))
		{
			float x = (Input.mousePosition.x - Screen.width / 2) / Screen.width * (Screen.width * 1.0f / Screen.height);
			float y = ((Input.mousePosition.y - Screen.height / 2) / Screen.height)  ;
			x *= CAM_SIZE;
			y *= CAM_SIZE;
			int mouse_col = (int)((x-MIN_X)/CELL_WIDH);
			int mouse_row = (int)((y - MIN_Y) / CELL_HEIGHT);
			Debug.Log(mouse_col + " " + mouse_row);
			if(mouse_col>-1 && mouse_col<COL && mouse_row>-1 && mouse_row < ROW && MAP[mouse_row][mouse_col] != -1){
				if(POS1 !=null && POS1.C == mouse_col && POS1.R == mouse_row)
				{
					DeSelect();
				}
				else
					if(POS1 == null)
				{
					Select(new Vec2(mouse_row, mouse_col));
				}
				else
				{
					CheckPair(new Vec2(mouse_row, mouse_col));
				}
			}
		}
		HideSomeItem (false);
	}
	
	private GameObject GetGameObjectFromPos(Vec2 pos)
	{
		return GameObject.Find(GetNameOfGameObjectFromPos(pos));
	}

	private string GetNameOfGameObjectFromPos(Vec2 pos)
	{
		return "PKC_R" + pos.R+ "C" +pos.C;
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
	void Select(Vec2 pos)
	{
		if(pos!=null && MAP[pos.R][pos.C] != -1){
			POS1 = new Vec2(pos.R, pos.C);
			SetColorForSprite (POS1, Color.green);
			Debug.Log("selected " + POS1.Print());
		}
	}


	void DeSelect()
	{
		SetColorForSprite (POS1, Color.white);		
		SetColorForSprite (POS2, Color.white);
		POS1 = null;
		POS2 = null;
		Debug.Log("DeSelect");
	}

	void CheckPair(Vec2 pos)
	{
		POS2 = new Vec2(pos.R, pos.C);
		//Debug.Log("CheckPair " + POS1.Print() +" and " + POS2.Print());
		
		
		// Debug.Log(MAP[POS1.R][POS1.C] + " " + MAP[POS2.R][POS2.C]);
		if(MAP[POS1.R][POS1.C] != MAP[POS2.R][POS2.C])
		{
			Debug.Log("KHONG PHAI LA 1 CAP");
			DeSelect ();
		}
		else{ 
			var path = CheckpairOnly(POS1, POS2);
			if ( path != null)
			{
				Debug.Log("la 1 cap");
				renderLine(path);

				objectPos1 = GetGameObjectFromPos(POS1);
				objectPos2 = GetGameObjectFromPos(POS2);
				MAP[POS1.R][POS1.C] = -1;
				MAP[POS2.R][POS2.C] = -1;

				Invoke("DeleteSameItems", 0.5f);
			}
			else{
				if (isEnableSound) {
					float vol = Random.Range (volLowRange, volHighRange);
					source.PlayOneShot (wrongSelectionSound, vol);
				}
				Debug.Log("khong thay dương di ");
				DeSelect ();
			}
		}
	}
	void AddPikachu(int type,Vector3 pos,int width,int height, string objectName)
	{
		GameObject g = Instantiate(prefap_pikachu) as GameObject;
		g.name = objectName;
		g.transform.parent = this.transform;
		g.transform.position = pos;
		Sprite sprite = Resources.Load("Images/item/item"+type, typeof(Sprite)) as Sprite;
		g.GetComponent<SpriteRenderer>().sprite = sprite;
		g.transform.localScale = new Vector3(Mathf.Abs(width * 1.0f / sprite.bounds.size.x), Mathf.Abs (- height * 1.0f / sprite.bounds.size.y), 1);
		if(ROW_COL.Length < starsLevel){
			var val = Random.Range(0, 3);
			if(val==1) g.transform.Rotate(0, 180, 0);
			else if(val==2) g.transform.Rotate(180, 0, 0);
		}else{
			if(Random.Range(0, 3) == 1) g.transform.Rotate(0, 180, 0);
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
		CELL_WIDH = (int)(CELL_HEIGHT * 0.9f);
		
		ROW = row;
		COL = col;
		MAP = new int[ROW][];
		SHIT = new bool[ROW][];
		SHIT_ROOT_POS = new Vec2[ROW][];
		POS = new Vector3[ROW][];
		
		MIN_X = -COL * CELL_WIDH / 2;
		//MIN_Y = -1 * (int)CAM_SIZE / 2 + OFFSET_Y_BOTTOM;//-(ROW) * CELL_HEIGHT / 2 - OFFSET_Y_TOP + OFFSET_Y_BOTTOM;
		MIN_Y = -(ROW) * CELL_HEIGHT / 2;

		Debug.Log(MIN_X + " " + MIN_Y );
		
		for (int i = 0; i < ROW; i++)
		{
			MAP[i] = new int[COL];
			SHIT[i] = new bool[COL];
			SHIT_ROOT_POS[i] = new Vec2[COL];
			POS[i] = new Vector3[COL];
			for (int j = 0; j < COL; j++)
			{
				SHIT[i][j] = false;
				MAP[i][j] = -1;
				SHIT_ROOT_POS[i][j] = new Vec2();
				
				
				POS[i][j] = new Vector3(0, 0, 0);
				POS[i][j].x = MIN_X + j * CELL_WIDH + CELL_WIDH / 2;
				POS[i][j].y = MIN_Y + i * CELL_HEIGHT + CELL_HEIGHT / 2;
				POS[i][j].z = i / 10.0f;
				
				//Debug.Log("a  " + POS[i][j]);
			}
		}
		//PrintMap();
		//RandomMap();
		//CheckAndSwapThings();
	}
	
	void RandomMap()
	{	
		/*
		int dem = 0;
		for (int i = 1; i < ROW - 1; i++)
			for (int j = 1; j < COL - 1; j++)
				if (MAP[i][j] == 1)
					dem++;
		*/
		int dem = (ROW-2)*(COL - 2);
		if (dem % 2 == 1)
		{
			Debug.LogError("than gnu");
			return;
		}

		/// random limiting repeat number
		List<int[]> itemList = new List<int[]>();
		for(int i = 0; i<NUMBER_ITEM; i++)
		{
			int [] itemArray = new int[2];
			itemArray[0] = i;
			itemArray[1] = 2;
			itemList.Add(itemArray); 
		}

		int[] pool = new int[dem];
		
		for (int i = 0; i < dem / 2; i++)
		{
			if(itemList.Count > 0)
			{
				int index = Random.Range(0, itemList.Count);
				var item = itemList[index];
				pool[i] = item[0];
				item[1] = item[1] - 1;
				if(item[1] == 0) itemList.RemoveAt(index);
			}else{
				pool[i] = Random.Range(0, NUMBER_ITEM);
			}
		}

		///////////
		for (int i = dem / 2; i < dem; i++)
			pool[i] = pool[dem - 1 - i];
		
		

		for (int i = 0; i < dem / 2; i++)
		{
			int index1 = Random.Range(0, dem);
			int index2 = Random.Range(0, dem);
			int temp = pool[index1];
			pool[index1] = pool[index2];
			pool[index2] = temp;
		}

		for (int i = 1; i < ROW - 1; i++)
		{
			for (int j = 1; j < COL - 1; j++)
			{
				//if(i==1 && j==1)
				int type = pool[(i-1)*(COL - 2) + j - 1];// Random.Range(0, 36);
				AddPikachu(type, POS[i][j], CELL_WIDH, CELL_HEIGHT, GetNameOfGameObjectFromPos(new Vec2(i,j)));
				MAP[i][j] = type;
			}
		}
	}
	
	//bai 4
	public Vec2 POS1_SAVE;
	static int[] DX = { 0, 0, -1, 1 };
	static int[] DY = { -1, 1, 0, 0 };
	static int[] D = { -1, 1 };
	Vec2 CheckShit_Fast_temp;
	public int POS1_SAVE_INDEX;
	public Vec2 POS_SAVE_MIDDLE;
	public LPath CheckpairOnly(Vec2 v0, Vec2 v1)
	{
		ResetShit();
		SetShit(v0, true);
		return CheckShit(v1, true);
	}
	
	
	
	public void ResetShit()
	{
		for (int i = 0; i < ROW; i++)
			for (int j = 0; j < COL; j++)
				if (SHIT[i][j] == true) SHIT[i][j] = false;
	}

	public void SetShit(Vec2 v)
	{
		SetShit (v, false);
	}

	public void SetShit(Vec2 v, bool isneedtotrack)
	{
		POS1_SAVE = new Vec2(v.R, v.C);
		SHIT[v.R][v.C] = true;
		//Debug.Log("(" + v.R + "," + v.C);
		int nc, nr;
		for (int k = 0; k < 2; k++)
		{
			for (int l = 1; l < ROW; l++)
			{
				nr = v.R + l * D[k];
				if (nr < 0 || nr >= ROW) break;
				if (MAP[nr][v.C] != -1) break;
				// if (SHIT[nr][v.C]==false)
				{
					SHIT[nr][v.C] = true;
					if (isneedtotrack)
						SHIT_ROOT_POS[nr][v.C].R = -1;
					//Debug.Log("(" + nr + "," + v.C );
					SetShitHorizontal(new Vec2(nr, v.C), isneedtotrack);
				}
			}
		}
		
		for (int k = 0; k < 2; k++)
		{
			for (int l = 1; l < COL; l++)
			{
				nc = v.C + l * D[k];
				if (nc < 0 || nc >= COL) break;
				if (MAP[v.R][nc] != -1) break;
				//if (SHIT[v.R][nc]==false)
				{
					SHIT[v.R][nc] = true;
					if (isneedtotrack)
						SHIT_ROOT_POS[v.R][nc].R = -1;
					//Debug.Log("(" + v.R + "," + nc );
					SetShitVerticle(new Vec2(v.R, nc), isneedtotrack);
				}
			}
		}
	}
	public void SetShitHorizontal(Vec2 v)
	{
		SetShitHorizontal (v, false);
	}
	public void SetShitHorizontal(Vec2 v, bool isneedtotrack)
	{
		int nc;
		for (int k = 0; k < 2; k++)
		{
			for (int l = 1; l < COL; l++)
			{
				nc = v.C + l * D[k];
				if (nc < 0 || nc >= COL) break;
				if (MAP[v.R][nc] != -1) break;
				if (SHIT[v.R][nc] == false)
				{
					SHIT[v.R][nc] = true;
					//Debug.Log("(" + v.R + "," + nc +"_ horifrom :" + v.Print());
					if (isneedtotrack)
					{
						SHIT_ROOT_POS[v.R][nc].R = v.R;
						SHIT_ROOT_POS[v.R][nc].C = v.C;
					}
				}
			}
		}
	}
	public void SetShitVerticle(Vec2 v)
	{
		SetShitVerticle(v, false);
	}

	public void SetShitVerticle(Vec2 v, bool isneedtotrack)
	{
		int nr;
		for (int k = 0; k < 2; k++)
		{
			for (int l = 1; l < ROW; l++)
			{
				nr = v.R + l * D[k];
				if (nr < 0 || nr >= ROW) break;
				if (MAP[nr][v.C] != -1) break;
				if (SHIT[nr][v.C] == false)
				{
					SHIT[nr][v.C] = true;
					//Debug.Log("(" + nr + "," + v.C + "_ verfrom :" + v.Print());
					if (isneedtotrack)
					{
						SHIT_ROOT_POS[nr][v.C].R = v.R;
						SHIT_ROOT_POS[nr][v.C].C = v.C;
					}
				}
			}
		}
	}

	public LPath CheckShit(Vec2 v)
	{
		return CheckShit(v, false);
	}

	public LPath CheckShit(Vec2 v, bool is_slow)
	{
		//Debug.Log("asd");
		bool b;
		if (is_slow) b = CheckShit_Slow(v);
		else b = (CheckShit_Fast(v));
		if (b)
		{
			if (POS1_SAVE.R == v.R && POS_SAVE_MIDDLE.R == v.R)
				return new LPath(new Vec2(POS1_SAVE.R, POS1_SAVE.C), new Vec2(v.R, v.C));
			if (POS1_SAVE.C == v.C && POS_SAVE_MIDDLE.C == v.C)
				return new LPath(new Vec2(POS1_SAVE.R, POS1_SAVE.C), new Vec2(v.R, v.C));
			
			if (POS1_SAVE.R == POS_SAVE_MIDDLE.R || POS1_SAVE.C == POS_SAVE_MIDDLE.C)
				return new LPath(new Vec2(POS1_SAVE.R, POS1_SAVE.C), new Vec2(POS_SAVE_MIDDLE.R, POS_SAVE_MIDDLE.C), new Vec2(v.R, v.C));
			
			
			return new LPath(new Vec2(POS1_SAVE.R, POS1_SAVE.C),
			                 SHIT_ROOT_POS[POS_SAVE_MIDDLE.R][POS_SAVE_MIDDLE.C],
			                 new Vec2(POS_SAVE_MIDDLE.R, POS_SAVE_MIDDLE.C),
			                 new Vec2(v.R, v.C));
			//if (POS_SAVE_MIDDLE.R != POS1_SAVE.R && POS_SAVE_MIDDLE.C != POS1_SAVE.C)
			//{ 
			
			// return new LPath(new Vec2(POS1_SAVE.R, POS1_SAVE.C), new Vec2(POS1_SAVE.R, POS_SAVE_MIDDLE.C), new Vec2(POS_SAVE_MIDDLE.R, POS_SAVE_MIDDLE.C), new Vec2(v.R, v.C));
			//}
			//if (POS_SAVE_MIDDLE.C == POS1_SAVE.C)
			//return new LPath(new Vec2(POS1_SAVE.R, POS1_SAVE.C), new Vec2(POS_SAVE_MIDDLE.R, POS1_SAVE.C), new Vec2(POS_SAVE_MIDDLE.R, POS_SAVE_MIDDLE.C), new Vec2(v.R, v.C));
			
			
		}
		return null;
	}
	public bool CheckShit_Slow(Vec2 v)
	{
		int dx = 0;
		int dy = 0;
		//bool b = false;
		if (POS1.R == v.R)
		{
			//Debug.Log("11111111111111111111");
			dx = 0; dy = 1;
			if (CheckShit_Slow_Child(v, dx, dy)) return true;
			dx = 0; dy = -1;
			if (CheckShit_Slow_Child(v, dx, dy)) return true;
			dx = 1; dy = 0;
			if (CheckShit_Slow_Child(v, dx, dy)) return true;
			dx = -1; dy = 0;
			if (CheckShit_Slow_Child(v, dx, dy)) return true;
		}
		else if (POS1.C == v.C)
		{
			//Debug.Log("222222222222222222222");
			dx = 1; dy = 0;
			if (CheckShit_Slow_Child(v, dx, dy)) return true;
			dx = -1; dy = 0;
			if (CheckShit_Slow_Child(v, dx, dy)) return true;
			dx = 0; dy = 1;
			if (CheckShit_Slow_Child(v, dx, dy)) return true;
			dx = 0; dy = -1;
			if (CheckShit_Slow_Child(v, dx, dy)) return true;
		}
		else
			return CheckShit_Fast(v, true);
		return false;
	}

	public bool CheckShit_Fast(Vec2 v)
	{
		return CheckShit_Fast(v, false);
	}

	public bool CheckShit_Fast(Vec2 v, bool is_find_best_way)
	{
		int nr, nc;
		CheckShit_Fast_temp = null;
		int maxIndex = ROW;
		if (maxIndex < COL)
			maxIndex = COL;
		for (int k = 0; k < 4; k++)
		{
			for (int l = 1; l < maxIndex; l++)
			{
				nr = v.R + l * DY[k];
				nc = v.C + l * DX[k];
				//Debug.Log(nr + " " + nc);
				if (nr < 0 || nr >= ROW || nc < 0 || nc >= COL) break;
				
				if (MAP[nr][nc] != -1) break;
				if (SHIT[nr][nc] == true)
				{
					if (is_find_best_way == false || SHIT_ROOT_POS[nr][nc].R == -1)
					{
						POS_SAVE_MIDDLE = new Vec2(nr, nc);
						return true;
					}
					else if (CheckShit_Fast_temp == null)
					{
						CheckShit_Fast_temp = new Vec2(nr, nc);
					}
				}
			}
		}
		if (CheckShit_Fast_temp != null)
		{
			POS_SAVE_MIDDLE = new Vec2(CheckShit_Fast_temp.R, CheckShit_Fast_temp.C);
			return true;
		}
		return false;
	}
	public bool CheckShit_Slow_Child(Vec2 v, int dx, int dy)
	{
		int maxIndex = ROW;
		if (maxIndex < COL)
			maxIndex = COL;
		int nr, nc;
		for (int l = 1; l < maxIndex; l++)
		{
			nr = v.R + l * dx;
			nc = v.C + l * dy;
			if (nr < 0 || nr >= ROW || nc < 0 || nc >= COL) break;
			if (nr == POS1.R && nc == POS1.C)
			{
				POS_SAVE_MIDDLE = new Vec2(POS1.R, POS1.C);
				return true;
			}
			if (MAP[nr][nc] != -1) break;
			if (SHIT[nr][nc] == true)
			{
				POS_SAVE_MIDDLE = new Vec2(nr, nc);
				return true;
			}
		}
		return false;
	}
	
	
	//bài 5
	public void CheckAndSwapThings(bool hasSound)
	{
		bool changed = false;
		//LLSound.I.playWrong();
		while (CheckIsAvailable() == false)
		{
			changed = true;
			Debug.Log("CheckAndSwapThings(): ResetMap here");
			ResetMap();
		}
		if(changed && isEnableSound && hasSound)
		{			
			float vol = Random.Range (volLowRange, volHighRange);
			source.PlayOneShot(changeMapSound,vol);
		}
		if(changed)
		{
			HideSomeItem(true);
		}
	}
	Vec2 HINT_POS0;
	Vec2 HINT_POS1;
	int tcount;
	public bool CheckIsAvailable()
	{
		tcount = 0;
		// CoundAndLog();
		HINT_POS0 = null;
		HINT_POS1 = null;
		for (int i = 1; i < ROW - 1; i++)
			for (int j = 1; j < COL - 1; j++)
		{
			if (MAP[i][j] != -1)
			{
				tcount++;
				//tcount++;
				ResetShit();
				SetShit(new Vec2(i, j), true);
				
				for (int i2 = 1; i2 < ROW - 1; i2++)
					for (int j2 = 1; j2 < COL - 1; j2++)
				{
					if (i2 <= i && j2 <= j) continue;
					if (MAP[i2][j2] == MAP[i][j])
						if (CheckShit_Fast(new Vec2(i2, j2)))
					{
						HINT_POS0 = new Vec2(i, j);
						HINT_POS1 = new Vec2(i2, j2);
						return true;
					}
				}
			}
		}
		//Debug.Log("tcount=" + tcount);
		if (tcount == 0)
		{
			//ItemManager.I.YouWin();
			MeetWinner();
			return true;
		}
		return false;
	}
	public void ResetMap()
	{
		for (int i = 1; i < ROW - 1; i++)
			for (int j = 1; j < COL - 1; j++)
				if (MAP[i][j] != -1)
			{
				bool is_swaped = false;
				for (int i2 = 1; i2 < ROW - 1; i2++)
				{
					for (int j2 = 1; j2 < COL - 1; j2++)
					{
						if (i2 <= i && j2 <= j) continue;
						if (MAP[i2][j2] != -1)
						{
							if (Random.Range(0, 2) == 0)
							{
								Swap(new Vec2(i, j), new Vec2(i2, j2));
							}
							is_swaped = true;
							break;
						}
					}
					if (is_swaped) break;
				}
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

	public void Hint()
	{
		if(numberHint > 0){
			if(CheckIsAvailable()){			
				SetColorForSprite (HINT_POS0, Color.yellow);
				SetColorForSprite (HINT_POS1, Color.yellow);
				Invoke("ResetHint", 1.0f);
			}
			numberHint--;
			numberHintText.text = "" + numberHint;
		}
		if(numberHint < 1)
		{
			hintButton.enabled = false;
		}
	}

	private void ResetHint()
	{
		if(HINT_POS0!=null){
			var obj = GetGameObjectFromPos(HINT_POS0);
			if(obj != null && obj.GetComponent<SpriteRenderer> ().color == Color.yellow){
				obj.GetComponent<SpriteRenderer> ().color = Color.white;
			}
		}
		if(HINT_POS1!=null){
			var obj1 = GetGameObjectFromPos(HINT_POS1);
			if(obj1 != null && obj1.GetComponent<SpriteRenderer> ().color == Color.yellow){
				obj1.GetComponent<SpriteRenderer> ().color = Color.white;
			}
		}
	}

	private void DeleteSameItems()
	{
		if (isEnableSound) {
			float vol = Random.Range (volLowRange, volHighRange);
			source.PlayOneShot (shootSound, vol);
		}
		if(objectPos1!=null) Destroy(objectPos1);
		if(objectPos2!=null) Destroy(objectPos2);
		objectPos1 = null;
		objectPos2 = null;
		DeSelect();
		if(lineRenderer1!=null) lineRenderer1.SetVertexCount(0);
		if(lineRenderer2!=null) lineRenderer2.SetVertexCount(0);
		if(lineRenderer3!=null) lineRenderer3.SetVertexCount(0);
		CheckAndSwapThings(true);
	}

	private void MeetWinner()
	{
		numberHint++;
		ShowInterstitialAds ();
		SaveLoad.SetData(starsLevel, numberHint, NUMBER_ITEM);
		stopGame = true;
		winPanel.SetActive (true);
		ChangeEnableHeaderButton (false);
	}

	public void ContinueGame()
	{
		if(numberHint > 0)
		{
			hintButton.enabled = true;
		}
		else
		{
			hintButton.enabled = false;
		}
		numberHintText.text = "" + numberHint;
		winPanel.SetActive (false);
		StartPlay (true);
	}

	public void PauseGame()
	{
		stopGame = true;
		this.enabled = false;
		pausePanel.SetActive (true);
		ChangeEnableHeaderButton (false);
	}
	
	public void ResumeGame()
	{
		stopGame = false;
		this.enabled = true;
		pausePanel.SetActive (false);
		ChangeEnableHeaderButton (true);
	}
	
	public void ResetLevelGame()
	{
		stopGame = false;
		this.enabled = true;
		pausePanel.SetActive (false);
		gameoverPanel.SetActive (false);
		ChangeEnableHeaderButton (true);
		foreach (Transform child in this.transform) {
			Destroy(child.gameObject);
		}
		StartPlay (false);
	}

	private void UpdateForUI()
	{
		var backgroundSound = GameObject.Find ("BackgroundSound");
		var backgroundSoundAudioSource = backgroundSound.GetComponent<AudioSource> ();
		if (isEnableSound) {
			soundText.text = SOUND_ON;
			soundMainMenuText.text = SOUND_ON;
			backgroundSoundAudioSource.Play ();
		} else {
			soundText.text = SOUND_OFF;
			soundMainMenuText.text = SOUND_OFF;
			backgroundSoundAudioSource.Stop();
		}

		if (isCountDownMode) {
			countDownText.text = COUNT_DOWN_ON;
		} else {
			countDownText.text = COUNT_DOWN_OFF;
		}
	}
	
	public void SoundChangeGame()
	{
		isEnableSound = !isEnableSound;
		UpdateForUI ();
		SaveLoad.SetDataConfig (isEnableSound, isCountDownMode);
	}

	public void StartGame()
	{
		starsLevel = 0;
		numberHint = 0;
		stopGame = false;
		this.enabled = true;
		StartPlay (true);
	}

	private bool isCountDownMode = true;
	public void CoundownModeGame()
	{
		isCountDownMode = !isCountDownMode;
		if (isCountDownMode) {
			countDownText.text = COUNT_DOWN_ON;
		} else {
			countDownText.text = COUNT_DOWN_OFF;
		}
		SaveLoad.SetDataConfig (isEnableSound, isCountDownMode);
	}

	public void GotoMainMenuGame()
	{

		stopGame = true;
		this.enabled = false;
		winPanel.SetActive (false);
		gameoverPanel.SetActive (false);
		pausePanel.SetActive (false);
		headerPanel.SetActive (false);
		ChangeEnableHeaderButton (false);
		foreach (Transform child in this.transform) {
			Destroy(child.gameObject);
		}

		startPanel.SetActive (true);
	}

	public void HelpGame()
	{
		helpPanel.SetActive (true);
	}
	public void CloseHelpGame()
	{
		helpPanel.SetActive (false);
	}
	private int countAdsShow = 2;
	private void ShowInterstitialAds()
	{
		if(countAdsShow < 5) countAdsShow++;
		if(Time.realtimeSinceStartup - lastShowAds > countAdsShow * 60)
		{
			AdmobService.RequestInterstitial (true);
			lastShowAds = Time.realtimeSinceStartup;
		}
		 
	}

	private void ChangeEnableHeaderButton(bool enable)
	{		
		hintButton.enabled = enable;
		pauseButton.enabled = enable;
	}

	private void renderLine(LPath path)
	{
		if(path!=null)
		{
			for(int i =0; i<2;i++){
				if(path.N > 1)
				{
					if(i==0) lineRenderer1.SetVertexCount(2);
					lineRenderer1.SetPosition (i,new Vector3 (CELL_WIDH * path.PATH [i].C + MIN_X + CELL_WIDH/2, CELL_HEIGHT * path.PATH [i].R + MIN_Y + CELL_HEIGHT/2, 0));
				}
				if(path.N > 2)
				{
					if(i==0) lineRenderer2.SetVertexCount(2);
					lineRenderer2.SetPosition (i,new Vector3 (CELL_WIDH * path.PATH [i+1].C + MIN_X + CELL_WIDH/2, CELL_HEIGHT * path.PATH [i+1].R + MIN_Y + CELL_HEIGHT/2, 0));
				}
				if(path.N > 3)
				{
					if(i==0) lineRenderer3.SetVertexCount(2);
					lineRenderer3.SetPosition (i,new Vector3 (CELL_WIDH * path.PATH [i+2].C + MIN_X + CELL_WIDH/2, CELL_HEIGHT * path.PATH [i+2].R + MIN_Y + CELL_HEIGHT/2, 0));
				}
			}
			/*for(int i =0;i<path.N; i++){
				Vector3 vec3 = new Vector3 (CELL_WIDH * path.PATH [i].C + MIN_X + CELL_WIDH/2, CELL_HEIGHT * path.PATH [i].R + MIN_Y + CELL_HEIGHT/2, 0);
				lineRenderer.SetPosition (i, vec3);
			}*/
		}
	}

	private void hideSpriteItem(Vec2 pos)
	{
		var obj = GetGameObjectFromPos (pos);
		var sprite = Resources.Load("Images/item/item", typeof(Sprite)) as Sprite; 
		obj.GetComponent<SpriteRenderer> ().sprite = sprite;

	}

	private void resetSpriteItem(Vec2 pos)
	{
		var type = MAP [pos.R] [pos.C];
		var obj = GetGameObjectFromPos (pos);
		var sprite = Resources.Load("Images/item/item"+type, typeof(Sprite)) as Sprite; 
		obj.GetComponent<SpriteRenderer> ().sprite = sprite;
		obj.transform.localScale = new Vector3(Mathf.Abs(CELL_WIDH * 1.0f / sprite.bounds.size.x), Mathf.Abs (- CELL_HEIGHT * 1.0f / sprite.bounds.size.y), 1);

		
	}

	private const int LEVEL_FOR_HIDE_SOME_ITEMS = 20;
	private const int TIME_FOR_HIDE_SOME_ITEMS = 45;
	private const int NUMBER_HIDE_ITEM_IN_THE_FIRST = 5;
	float timeForHideSomeItems;
	private void HideSomeItem(bool isReset)
	{
		if (stopGame)
			return;
		if(isReset){
			List<Vec2> listLiveItem = new List<Vec2>();
			for (int i = 1; i < ROW - 1; i++)
				for (int j = 1; j < COL - 1; j++)
			{
				if (MAP[i][j] != -1)
				{
					var pos = new Vec2(i, j);
					listLiveItem.Add(pos);
					resetSpriteItem(pos);
				}
			}
			timeForHideSomeItems = TIME_FOR_HIDE_SOME_ITEMS;
		}
		else{
			if(starsLevel > LEVEL_FOR_HIDE_SOME_ITEMS - 1)
			{
				if(timeForHideSomeItems < 0)
				{
					List<Vec2> listLiveItem = new List<Vec2>();
					for (int i = 1; i < ROW - 1; i++)
						for (int j = 1; j < COL - 1; j++)
					{
						if (MAP[i][j] != -1)
						{
							var pos = new Vec2(i, j);
							listLiveItem.Add(pos);
							resetSpriteItem(pos);
						}
					}

					var numberHideItem = starsLevel - LEVEL_FOR_HIDE_SOME_ITEMS + NUMBER_HIDE_ITEM_IN_THE_FIRST;
					if(numberHideItem > listLiveItem.Count / 2) numberHideItem= listLiveItem.Count / 2;
					for(int i = 0; i< numberHideItem; i++)
					{
						int index = Random.Range(0, listLiveItem.Count);
						hideSpriteItem(listLiveItem[index]);
						listLiveItem.RemoveAt(index);
					}

					timeForHideSomeItems = TIME_FOR_HIDE_SOME_ITEMS;
				}
			}
		}
	}
	
	public void GotoMoreGame()
	{
		string lg = "EN";
		if(Application.systemLanguage == SystemLanguage.Vietnamese) lg="VN";
		else if(Application.systemLanguage == SystemLanguage.ChineseSimplified) lg="CN"; 
		else if(Application.systemLanguage == SystemLanguage.ChineseTraditional) lg="TW"; 
		string urlMoreGame = "http://hunght.com/adsweb/?country="+lg+"&os=android&device=" + SystemInfo.deviceUniqueIdentifier + "&open=" + timeToOpenApp + "&version=1.0.5&package=com.hth.animalconnection";
		Debug.Log("GotoMoreGame: " + urlMoreGame);
		Application.OpenURL (urlMoreGame);
	}
}
