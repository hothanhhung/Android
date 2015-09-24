using UnityEngine;
using System.Collections;

public class Vec2 
{
	public int R;
	public int C;
	
	public Vec2()
	{
		R = 0;
		C = 0;
	}
	
	public Vec2(int r,int c)
	{
		R = r;
		C = c;
	}
	public Vec2(Vector2 vec)
	{
		R = (int)vec.y;
		C = (int)vec.x;
	}
	public Vec2(Vec2 vec)
	{
		R = (int)vec.R;
		C = (int)vec.C;
	}
	static int r, c;
	static public int FastDistance(Vec2 v1, Vec2 v2)
	{
		r = Mathf.Abs (v1.R - v2.R);
		c = Mathf.Abs (v1.C - v2.C);
		if(r>c) return r;
		return c;
	}
	
	public string Print()
	{
		string r = "(" + R + "," + C + ")";
		return r;
	}
}
