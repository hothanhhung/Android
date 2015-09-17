using UnityEngine;
using System.Collections;

public class ButtonAction : MonoBehaviour {

	public GameObject map;
	public void StartGame()
	{
		var btStartGame = GameObject.Find("btStartGame");
		btStartGame.SetActive (false);
		map.SetActive (true);
	}

	public void PauseGame()
	{
		Map mapScrip = (Map) map.GetComponent(typeof(Map));
		mapScrip.PauseGame ();
	}

	public void ResumeGame()
	{
		Map mapScrip = (Map) map.GetComponent(typeof(Map));
		mapScrip.ResumeGame ();
	}

	
	public void ResetLevelGame()
	{
		Map mapScrip = (Map) map.GetComponent(typeof(Map));
		mapScrip.ResetLevelGame ();
	}

	
	public void ChangeSoundGame()
	{
		Map mapScrip = (Map) map.GetComponent(typeof(Map));
		mapScrip.SoundChangeGame ();
	}

	
	public void HelpGame()
	{
		Map mapScrip = (Map) map.GetComponent(typeof(Map));
		mapScrip.HelpGame ();
	}

	public void HintGame()
	{
		Map mapScrip = (Map) map.GetComponent(typeof(Map));
		mapScrip.Hint ();
	}

	public void ContinueGame()
	{
		Map mapScrip = (Map) map.GetComponent(typeof(Map));
		mapScrip.ContinueGame ();
	}
}
