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
}
