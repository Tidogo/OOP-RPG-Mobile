package com.example.ooprpgproto;

import java.io.Serializable;

public class Entity implements Serializable
{

	public String Name;
	public final String getName()
	{
		return Name;
	}
	public final void setName(String value)
	{
		Name = value;
	}
	public int Level;
	public final int getLevel()
	{
		return Level;
	}
	public final void setLevel(int value)
	{
		Level = value;
	}
	public int Strength;
	public final int getStrength()
	{
		return Strength;
	}
	public final void setStrength(int value)
	{
		Strength = value;
	}
	public int Constitution;
	public final int getConstitution()
	{
		return Constitution;
	}
	public final void setConstitution(int value)
	{
		Constitution = value;
	}
	public int Dexterity;
	public final int getDexterity()
	{
		return Dexterity;
	}
	public final void setDexterity(int value)
	{
		Dexterity = value;
	}
	public int Health;
	public final int getHealth()
	{
		return Health;
	}
	public final void setHealth(int value)
	{
		Health = value;
	}
	public int Defense;
	public final int getDefense()
	{
		return Defense;
	}
	public final void setDefense(int value)
	{
		Defense = value;
	}
	public double Dodge;
	public final double getDodge()
	{
		return Dodge;
	}
	public final void setDodge(double value)
	{
		Dodge = value;
	}
	public double Crit;
	public final double getCrit()
	{
		return Crit;
	}
	public final void setCrit(double value)
	{
		Crit = value;
	}
	public int AttPower;
	public final int getAttPower()
	{
		return AttPower;
	}
	public final void setAttPower(int value)
	{
		AttPower = value;
	}
	public double CritDmgMultiplier;
	public final double getCritDmgMultiplier()
	{
		return CritDmgMultiplier;
	}
	public final void setCritDmgMultiplier(double value)
	{
		CritDmgMultiplier = value;
	}

	public final void Calculate_SubStats()
	{
		setDefense(1);
		for (int i = 0; i < getDexterity(); i++)
		{
			setDodge(getDodge() + .05);
			setCrit(getCrit() + .025);
		}
		setCritDmgMultiplier(2);
		setAttPower(getStrength() * 2);
	}


}