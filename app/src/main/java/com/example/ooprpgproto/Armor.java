package com.example.ooprpgproto;

import java.io.Serializable;

public class Armor extends Item implements Serializable
{
	private int Defense;
	public final int getDefense()
	{
		return Defense;
	}
	public final void setDefense(int value)
	{
		Defense = value;
	}
	private int Slot;
	public final int getSlot()
	{
		return Slot;
	}
	public final void setSlot(int value)
	{
		Slot = value;
	}
	private int DodgeMod;
	public final int getDodgeMod()
	{
		return DodgeMod;
	}
	public final void setDodgeMod(int value)
	{
		DodgeMod = value;
	}

	public Armor(String n, int d, int s, int dm, int val)
	{
		Name = n;
		setDefense(d);
		setSlot(s);
		setDodgeMod(dm);
		Value = val;
		Equipable = true;
	}

	public Armor()
	{
	}
}