package com.example.ooprpgproto;

import java.io.Serializable;

public class Consumable extends Item implements Serializable
{

	private int HealthBoost;
	public final int getHealthBoost()
	{
		return HealthBoost;
	}
	public final void setHealthBoost(int value)
	{
		HealthBoost = value;
	}
	private int TempCritBoost;
	public final int getTempCritBoost()
	{
		return TempCritBoost;
	}
	public final void setTempCritBoost(int value)
	{
		TempCritBoost = value;
	}
	private int TempDodgeBoost;
	public final int getTempDodgeBoost()
	{
		return TempDodgeBoost;
	}
	public final void setTempDodgeBoost(int value)
	{
		TempDodgeBoost = value;
	}
	private int TempAttackBoost;
	public final int getTempAttackBoost()
	{
		return TempAttackBoost;
	}
	public final void setTempAttackBoost(int value)
	{
		TempAttackBoost = value;
	}
	private int TempDefenseBoost;
	public final int getTempDefenseBoost()
	{
		return TempDefenseBoost;
	}
	public final void setTempDefenseBoost(int value)
	{
		TempDefenseBoost = value;
	}
	private int TempInitBoost;
	public final int getTempInitBoost()
	{
		return TempInitBoost;
	}
	public final void setTempInitBoost(int value)
	{
		TempInitBoost = value;
	}
	private int NumOfBattles;
	public final int getNumOfBattles()
	{
		return NumOfBattles;
	}
	public final void setNumOfBattles(int value)
	{
		NumOfBattles = value;
	}

	public Consumable(String n, int sl, boolean tb, int hb, int tcb, int tab, int tdb, int tdeb, int tib, int nub, int val)
	{
		Name = n;
		StackLimit = sl;
		TempBoost = tb;
		setHealthBoost(hb);
		setTempCritBoost(tcb);
		setTempAttackBoost(tab);
		setTempDodgeBoost(tdb);
		setTempDefenseBoost(tdeb);
		setTempInitBoost(tib);
		setNumOfBattles(nub);
		Value = val;
		Equipable = false;
	}
}