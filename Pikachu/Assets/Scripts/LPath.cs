using UnityEngine;
using System.Collections;

public class LPath 
{
    public int N;
    public Vec2[] PATH;
    public LPath(Vec2 v0,Vec2 v1)
    {
        N = 2;
        PATH = new Vec2[2];
        PATH[0] = v0;
        PATH[1] = v1;
    }
    public LPath(Vec2 v0, Vec2 v1, Vec2 v2)
    {
        N = 3;
        PATH = new Vec2[N];
        PATH[0] = v0;
        PATH[1] = v1;
        PATH[2] = v2;
        Trim();
    }
    public LPath(Vec2 v0, Vec2 v1,Vec2 v2,Vec2 v3)
    {
        N = 4;
        PATH = new Vec2[N];
        PATH[0] = v0;
        PATH[1] = v1;
        PATH[2] = v2;
        PATH[3] = v3;
        Trim();
    }
    public string Print()
    {
        string h="lpath=";
        for (int i = 0; i < N; i++)
            h += PATH[i].Print();
        return h;
    }
    public void Trim()
    {
        if(N>=4)
        if (PATH[1].R == PATH[2].R && PATH[2].R == PATH[3].R)
        {
            Remove(1);
            Trim();
        }
        if (N >= 4)
        if (PATH[1].C == PATH[2].C && PATH[2].C == PATH[3].C)
        {
            Remove(1);
            Trim();
        }

        if (N >= 3)
        if (PATH[0].R == PATH[1].R && PATH[2].R == PATH[1].R)
        { 
            Remove(1);
            Trim();
        }
        if (N >= 3)
        if (PATH[0].C == PATH[1].C && PATH[2].C == PATH[1].C)
        {
            Remove(1);
            Trim();
        }
    }
    public void Remove(int index)
    {
        if(index==0)
        {
            PATH[0] = PATH[1];
            PATH[1] = PATH[2];
            PATH[2] = PATH[3];
            N--;
        }
        else if (index == 1)
        {
            PATH[1] = PATH[2];
            PATH[2] = PATH[3];
            N--;
        }
        else if (index == 2)
        {
            PATH[2] = PATH[3];
            N--;
        }
        else if (index == 3)
        {
            N--;
        }
    }
}
