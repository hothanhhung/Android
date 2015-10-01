using UnityEngine;
using System.Collections;

public class ButtonAction : MonoBehaviour {

	public GameObject map;
	public void StartGame()
	{
		Map mapScrip = (Map) map.GetComponent(typeof(Map));
		mapScrip.StartGame ();
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

	public void CloseHelpGame()
	{
		Map mapScrip = (Map) map.GetComponent(typeof(Map));
		mapScrip.CloseHelpGame ();
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
	
	public void CountdownModeGame()
	{
		Map mapScrip = (Map) map.GetComponent(typeof(Map));
		mapScrip.CoundownModeGame ();
	}

	
	public void GotoMainMenuGame()
	{
		Map mapScrip = (Map) map.GetComponent(typeof(Map));
		mapScrip.GotoMainMenuGame ();
	}

	public void ResetData()
	{
		SaveLoad.SetData (0, 0, 0);
	}

}
