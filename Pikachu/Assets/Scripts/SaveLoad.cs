using UnityEngine;
using System.Collections;
using System.Runtime.Serialization.Formatters.Binary;
using System.IO;

[System.Serializable]
public class GameData{

	public int StarsLevel;
	public int NumberOfHint;
	public int NumberOfItem;

	public bool IsSound;
	public bool IsCountDown;
	/*public SaveData(){}
	public SaveData(int level, int hint, int item){
		StarsLevel = level;
		NumberOfHint = hint;
		NumberOfItem = item;
	}*/
}

public static class SaveLoad {

	public static GameData SavedData = new GameData();
	public static string localSavedDataString = Application.persistentDataPath + "/gamedata.gd";

	public static void SetData(int level, int hint, int item){
		SavedData.StarsLevel = level;
		SavedData.NumberOfHint = hint;
		SavedData.NumberOfItem = item;
		Save ();
	}

	private static void Save(){
		BinaryFormatter bf = new BinaryFormatter ();
		FileStream file = File.Create(localSavedDataString);
		bf.Serialize (file, SavedData);
		file.Close ();
	}

	public static void Load(){
		if(File.Exists(localSavedDataString))
		{
			try{
				BinaryFormatter bf = new BinaryFormatter ();
				FileStream file = File.Open(localSavedDataString, FileMode.Open);
				SavedData = (GameData) bf.Deserialize (file);
				file.Close ();
			}catch{}
		}
	}

	public static void SetDataConfig(bool sound, bool countDown){
		SavedData.IsSound = sound;
		SavedData.IsCountDown = countDown;
		Save ();
	}

}
