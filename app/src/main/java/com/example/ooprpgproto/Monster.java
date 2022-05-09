package com.example.ooprpgproto;

import java.io.Serializable;

public class Monster extends Entity implements Serializable
{
	public Monster(String n, int diff, int lvl, int str, int con, int dex, int ap, double init)
	{
		Name = n;
		Level = lvl;
		setDifficulty(diff);
		Health = (10 * con) + (lvl * 5);
		Strength = str;
		Constitution = con;
		Dexterity = dex;
		AttPower = ap;
		setInitiative(init);
		setRndCashLoot(lvl + (10 * diff));
		setExperienceValue(lvl * 10);
		setLootSeedValue(lvl * 10);
		Calculate_SubStats();
	}
	public int LootSeedValue;
	public final int getLootSeedValue()
	{
		return LootSeedValue;
	}
	public final void setLootSeedValue(int value)
	{
		LootSeedValue = value;
	}
	public int ExperienceValue;
	public final int getExperienceValue()
	{
		return ExperienceValue;
	}
	public final void setExperienceValue(int value)
	{
		ExperienceValue = value;
	}
	public int RndCashLoot;
	public final int getRndCashLoot()
	{
		return RndCashLoot;
	}
	public final void setRndCashLoot(int value)
	{
		RndCashLoot = value;
	}
	public int Difficulty;
	public final int getDifficulty()
	{
		return Difficulty;
	}
	public final void setDifficulty(int value)
	{
		Difficulty = value;
	}
	public double Initiative;
	public final double getInitiative()
	{
		return Initiative;
	}
	public final void setInitiative(double value)
	{
		Initiative = value;
	}
}