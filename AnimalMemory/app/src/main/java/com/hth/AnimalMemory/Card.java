package com.hth.AnimalMemory;

import android.widget.ImageButton;


public class Card{

	public int x;
	public int y;
	public ImageButton button;
	
	public Card(ImageButton button, int x,int y) {
		this.x = x;
		this.y=y;
		this.button=button;
	}
	

}
