package com.example.ooprpgproto;

import java.io.Serializable;
import java.util.*;

public class Player extends Entity implements Serializable {
	public Player(String name, int str, int con, int dex, int lvl, int cash, int xp) {
		Name = name;
		Level = lvl;
		Health = 100 + (10 * con) + (lvl * 5);
		Strength = str;
		Constitution = con;
		Dexterity = dex;
		setExperience(xp);
		setCash(cash);
		ArrayList<Item> inven = new ArrayList<Item>();
		setInventory(inven);
		ArrayList<Item> equipped = new ArrayList<Item>();
		setEquippedGear(equipped);
		Calculate_SubStats();
	}

	public Player(String name, int str, int con, int dex) {
		Name = name;
		Level = 1;
		Health = 100 + (10 * con) + 5;
		Strength = str;
		Constitution = con;
		Dexterity = dex;
		setExperience(0);
		setCash(50);
		ArrayList<Item> inven = new ArrayList<Item>();
		setInventory(inven);
		ArrayList<Item> equipped = new ArrayList<Item>();
		ArrayList<Weapon> weapons = new ArrayList<Weapon>();
		setWeapons(weapons);
		setEquippedGear(equipped);
		Calculate_SubStats();
	}

	public int Cash;

	public final int getCash() {
		return Cash;
	}

	public final void setCash(int value) {
		Cash = value;
	}

	public int Experience;

	public final int getExperience() {
		return Experience;
	}

	public final void setExperience(int value) {
		Experience = value;
	}

	private ArrayList<Weapon> Weapons;
	public ArrayList<Item> Inventory;

	public final ArrayList<Item> getInventory() {
		return Inventory;
	}

	public final void setInventory(ArrayList<Item> value) {
		Inventory = value;
	}

	public ArrayList<Item> EquippedGear;

	public final ArrayList<Item> getEquippedGear() {
		return EquippedGear;
	}

	public final void setEquippedGear(ArrayList<Item> value) {
		EquippedGear = value;
	}

	public int GearDefenseTotal;

	public final int getGearDefenseTotal() {
		return GearDefenseTotal;
	}

	public final void setGearDefenseTotal(int value) {
		GearDefenseTotal = value;
	}

	public int GearDodgeTotal;

	public final int getGearDodgeTotal() {
		return GearDodgeTotal;
	}

	public final void setGearDodgeTotal(int value) {
		GearDodgeTotal = value;
	}

	public int GearStrengthMod;

	public final int getGearStrengthMod() {
		return GearStrengthMod;
	}

	public final void setGearStrengthMod(int value) {
		GearStrengthMod = value;
	}

	public int GearConstitutionMod;

	public final int getGearConstitutionMod() {
		return GearConstitutionMod;
	}

	public final void setGearConstitutionMod(int value) {
		GearConstitutionMod = value;
	}

	public int GearDexterityMod;

	public final int getGearDexterityMod() {
		return GearDexterityMod;
	}

	public final void setGearDexterityMod(int value) {
		GearDexterityMod = value;
	}

	public double GearDefenseMod;

	public final double getGearDefenseMod() {
		return GearDefenseMod;
	}

	public final void setGearDefenseMod(double value) {
		GearDefenseMod = value;
	}

	public double GearDodgeMod;

	public final double getGearDodgeMod() {
		return GearDodgeMod;
	}

	public final void setGearDodgeMod(double value) {
		GearDodgeMod = value;
	}

	public double GearCritMod;

	public final double getGearCritMod() {
		return GearCritMod;
	}

	public final void setGearCritMod(double value) {
		GearCritMod = value;
	}

	public int GearAttPowerMod;

	public final int getGearAttPowerMod() {
		return GearAttPowerMod;
	}

	public final void setGearAttPowerMod(int value) {
		GearAttPowerMod = value;
	}

	public double GearInitiativeMod;

	public final double getGearInitiativeMod() {
		return GearInitiativeMod;
	}

	public final void setGearInitiativeMod(double value) {
		GearInitiativeMod = value;
	}

	public void Calculate_Equip_Bonus()
	{
		setGearDefenseTotal(0);
		setGearInitiativeMod(0);
		setGearAttPowerMod(0);
		setGearCritMod(0);
		setGearConstitutionMod(0);
		setGearDexterityMod(0);
		setGearDodgeTotal(0);
		for (Item i : EquippedGear)
		{
			String itemType = i.getClass().getSimpleName();
			if (itemType.equals("Armor")) {
				setGearDefenseTotal(((Armor) i).getDefense());
				setGearDodgeTotal(((Armor) i).getDodgeMod());
			}
			else if (itemType.equals("Weapon")) {
				setGearAttPowerMod(((Weapon)i).getAttkPower());
				setGearCritMod(((Weapon) i).getCritChance());
				setGearInitiativeMod(((Weapon) i).getInitiative());
			}
		}
	}
	public void Equip(Item i)
	{
		if (i.Equipable)
		{

		}
	}
	public void refundGear() {
		for (Item i : EquippedGear) {
			Cash += i.getValue();
		}
		for (Item i2 : Inventory) {
			Cash += i2.getValue();
		}
	}
	public ArrayList<Item> returnItems() {
		ArrayList<Item> result = new ArrayList<Item>();
		for (Item i : EquippedGear) {
			result.add(i);
		}
		for (Item i2 : Inventory) {
			result.add(i2);
		}
		return result;
	}
	public void calcHealth() {
		Health = 100 + (10 * Constitution) + 5;
	}

	public ArrayList<Weapon> getWeapons() {
		return Weapons;
	}

	public void setWeapons(ArrayList<Weapon> weapons) {
		Weapons = weapons;
	}
}