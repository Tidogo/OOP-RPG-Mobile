package com.example.ooprpgproto;

import java.io.Serializable;

public class Weapon extends Item implements Serializable
{
	public int AttkPower;
	public final int getAttkPower()
	{
		return AttkPower;
	}
	public final void setAttkPower(int value)
	{
		AttkPower = value;
	}
	public boolean TwoHanded;
	public final boolean getTwoHanded()
	{
		return TwoHanded;
	}
	public final void setTwoHanded(boolean value)
	{
		TwoHanded = value;
	}
	public int Initiative;
	public final int getInitiative()
	{
		return Initiative;
	}
	public final void setInitiative(int value)
	{
		Initiative = value;
	}
	public int CritChance;
	public final int getCritChance()
	{
		return CritChance;
	}
	public final void setCritChance(int value)
	{
		CritChance = value;
	}
	public int CritDmgMod;
	public final int getCritDmgMod()
	{
		return CritDmgMod;
	}
	public final void setCritDmgMod(int value)
	{
		CritDmgMod = value;
	}

	public Weapon(String n, int ap, boolean th, int init, int cc, int cdm, int val)
	{
		Equipable = true;
		Name = n;
		setAttkPower(ap);
		setTwoHanded(th);
		setInitiative(init);
		setCritChance(cc);
		setCritDmgMod(cdm);
		Value = val;
	}
}