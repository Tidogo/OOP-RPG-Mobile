package com.example.ooprpgproto;

import static com.example.ooprpgproto.MainActivity.getValue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class InventoryActivity extends AppCompatActivity implements Serializable {

    private ArrayList<String> playerInvenNames = new ArrayList<>();
    private ArrayList<Armor> armor = new ArrayList<Armor>();
    private ArrayList<Weapon> weapon = new ArrayList<Weapon>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Player thePlayer = (Player) getIntent().getSerializableExtra("thePlayer");
        refreshInventory(thePlayer);
        String file1 = "Armor.xml";
        String file3 = "Weapon.xml";
        try {
            InputStream in1 = this.getAssets().open(file1);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in1);
            Element element = doc.getDocumentElement();
            element.normalize();
            NodeList nList = doc.getElementsByTagName("Armor");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    String name = getValue("Name", element2);
                    int defense = Integer.parseInt(getValue("Defense", element2));
                    int dodgeMod = Integer.parseInt(getValue("DodgeMod", element2));
                    int slot = Integer.parseInt(getValue("Slot", element2));
                    int value = Integer.parseInt(getValue("Value", element2));
                    Armor a = new Armor(name, defense, dodgeMod, slot, value);
                    armor.add(a);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        try {
            InputStream in3 = this.getAssets().open(file3);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in3);
            Element element = doc.getDocumentElement();
            element.normalize();
            NodeList nList = doc.getElementsByTagName("Weapon");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    String name = getValue("Name", element2);
                    int apwr = Integer.parseInt(getValue("AttkPower", element2));
                    boolean twohand = Boolean.parseBoolean((getValue("TwoHanded", element2)));
                    int init = Integer.parseInt(getValue("Initiative", element2));
                    int critchance = Integer.parseInt(getValue("CritChance", element2));
                    int critdmod = Integer.parseInt(getValue("CritDmgMod", element2));
                    boolean tb = Boolean.parseBoolean((getValue("Equipable", element2)));
                    int value = Integer.parseInt(getValue("Value", element2));
                    Weapon w = new Weapon(name, apwr, twohand, init, critchance, critdmod, value);
                    weapon.add(w);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        Button equipButton = (Button) findViewById(R.id.btnEquipSelected);
        equipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                equip(thePlayer);
            }
        });
        Button leaveButton = (Button) findViewById(R.id.btnBackToMenu);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave(thePlayer);
            }
        });
    }

    public void refreshInventory(Player p) {
        for (Item i : p.Inventory) {
            playerInvenNames.add(i.getName());
        }
        Spinner lv = (Spinner) findViewById(R.id.spinInventory);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, playerInvenNames
        );
        lv.setAdapter(arrayAdapter);
    }

    public void leave(Player p) {
        Intent intent = new Intent(this, HubActivity.class);
        intent.putExtra("thePlayer", p);
        startActivity(intent);
    }
    public void refreshEquipped(Player p) {
        ArrayList<Item> equipped = new ArrayList<Item>();
        equipped = p.getEquippedGear();
    }
    public void equip(Player p) {
        TextView headgear = (TextView) findViewById(R.id.textView2);
        TextView chestgear = (TextView) findViewById(R.id.textView4);
        TextView weapongear = (TextView) findViewById(R.id.textView5);
        TextView leggear = (TextView) findViewById(R.id.textView7);
        Spinner spinInventory = (Spinner) findViewById(R.id.spinInventory);
        String iName = spinInventory.getSelectedItem().toString();
        Weapon wep = null;
        Armor armr = null;
        int woa = 0;
        for (Item i : p.Inventory) {
            if (i.getName() == iName) {
                woa = wepOrArmor(i);
                if (woa == 1) {
                    wep = (Weapon) i;
                    for (Item i2 : p.EquippedGear) {
                        if (wepOrArmor(i2) == 1) {
                            p.EquippedGear.remove(i2);
                            p.Inventory.add(i2);
                        }
                    }
                    p.EquippedGear.add(wep);
                    p.Inventory.remove(i);
                    weapongear.setText(wep.getName());
                    break;
                }
                else if (woa == 2) {
                    armr = (Armor) i;
                    if (armr.getSlot() == 1) {
                        for (Item i2 : p.EquippedGear) {
                            if (wepOrArmor(i2) == 2) {
                                Armor tempArmor = (Armor) i2;
                                if (tempArmor.getSlot() == 1) {
                                    p.EquippedGear.remove(i2);
                                    p.Inventory.add(i2);
                                    break;
                                }
                            }
                        }
                    }
                    if (armr.getSlot() == 2) {
                        for (Item i2 : p.EquippedGear) {
                            if (wepOrArmor(i2) == 2) {
                                Armor tempArmor = (Armor) i2;
                                if (tempArmor.getSlot() == 2) {
                                    p.EquippedGear.remove(i2);
                                    p.Inventory.add(i2);
                                    break;
                                }
                            }
                        }
                    }
                    if (armr.getSlot() == 3) {
                        for (Item i2 : p.EquippedGear) {
                            if (wepOrArmor(i2) == 2) {
                                Armor tempArmor = (Armor) i2;
                                if (tempArmor.getSlot() == 3) {
                                    p.EquippedGear.remove(i2);
                                    p.Inventory.add(i2);
                                    break;
                                }
                            }
                        }
                    }
                    p.EquippedGear.add(armr);
                    p.Inventory.remove(i);
                    break;
                }
                break;
            }
        }
    }
    public int wepOrArmor(Item i) {
        int result = 0;

        if (weapon.contains(i) == true) {
            result = 1;
        }
        else if (armor.contains(i) == true) {
            result = 2;
        }
        return result;
    }

}