package com.example.ooprpgproto;

import java.io.Serializable;

public abstract class Item implements Serializable
{
	public Item() {

	}

	public String Name;
	public final String getName()
	{
		return Name;
	}
	public final void setName(String value)
	{
		Name = value;
	}
	public int StackLimit;
	public final int getStackLimit()
	{
		return StackLimit;
	}
	public final void setStackLimit(int value)
	{
		StackLimit = value;
	}
	public boolean TempBoost;
	public final boolean getTempBoost()
	{
		return TempBoost;
	}
	public final void setTempBoost(boolean value)
	{
		TempBoost = value;
	}
	public boolean Equipable;
	public final boolean getEquipable()
	{
		return Equipable;
	}
	public final void setEquipable(boolean value)
	{
		Equipable = value;
	}
	public int Value;
	public final int getValue()
	{
		return Value;
	}
	public final void setValue(int value)
	{
		Value = value;
	}

}