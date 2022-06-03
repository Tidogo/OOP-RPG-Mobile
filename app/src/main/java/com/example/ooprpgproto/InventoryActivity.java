package com.example.ooprpgproto;

import static com.example.ooprpgproto.MainActivity.getValue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    Player p = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        p = (Player) getIntent().getSerializableExtra("thePlayer");
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        refreshUI(p);
        /*SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(p);
        prefsEditor.putString("MyObject", json);
        prefsEditor.commit();*/
        /*ArrayList<Item> inv = new ArrayList<Item>();
        Gson gson2 = new Gson();
        String json2 = mPrefs.getString("MyObject", "");
        inv = gson2.fromJson(json2, Item.class);*/
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
                equip(p);
            }
        });
        Button leaveButton = (Button) findViewById(R.id.btnBackToMenu);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave(p);
            }
        });
        Button unequipButton = (Button) findViewById(R.id.btnUnequipAll);
        unequipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unequipAll(p);
            }
        });

    }

    public void refreshInventory(Player p) {
        playerInvenNames.clear();
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
        if (!p.Inventory.isEmpty()) {
            TextView headgear = (TextView) findViewById(R.id.txtHeadGear);
            TextView chestgear = (TextView) findViewById(R.id.txtChest);
            TextView weapongear = (TextView) findViewById(R.id.txtLHandGear);
            TextView leggear = (TextView) findViewById(R.id.textView6);
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
                        playerInvenNames.remove(i.getName());
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
                        playerInvenNames.remove(i.getName());
                        break;
                    }
                    break;
                }
            }
            refreshUI(p);
        }

    }
    public int wepOrArmor(Item i) {
        int result = 0;
        String name = i.getClass().getSimpleName();

        if (name.equals("Weapon")) {
            result = 1;
        }
        else if (name.equals("Armor")) {
            result = 2;
        }
        return result;
    }
    public void refreshUI(Player p) {
        TextView headgear = (TextView) findViewById(R.id.txtHeadGear);
        TextView chestgear = (TextView) findViewById(R.id.txtChest);
        TextView weapongear = (TextView) findViewById(R.id.txtLHandGear);
        TextView leggear = (TextView) findViewById(R.id.textView6);
        leggear.setText("Legs: ");
        headgear.setText("Helmet: ");
        weapongear.setText("Weapon: ");
        chestgear.setText("Body: ");

        for (Item i : p.EquippedGear) {
            if (wepOrArmor(i) == 1) {
                weapongear.setText("Weapon: " + i.getName());
            }
            else {
                if (((Armor) i).getSlot() == 1) {
                    headgear.setText("Helmet: " + i.getName());
                }
                else if (((Armor) i).getSlot() == 2) {
                    chestgear.setText("Body: " + i.getName());
                }
                else {
                    leggear.setText("Legs: " + i.getName());
                }
            }
        }
        refreshInventory(p);
    }
    public void unequipAll(Player p) {
        for (Item i : p.EquippedGear) {
            p.Inventory.add(i);
        }
        p.EquippedGear.clear();
        refreshUI(p);
    }
    @Override
    public void onPause() {

        super.onPause();
        if (p != null) {
            try {
                FileOutputStream fos;
                fos = openFileOutput("player", Context.MODE_PRIVATE);

                XmlSerializer serializer = Xml.newSerializer();
                serializer.setOutput(fos, "UTF-8");
                serializer.startDocument(null, Boolean.valueOf(true));
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                serializer.startTag(null, "player");
                serializer.startTag(null, "name");
                serializer.text(p.getName());
                serializer.endTag(null, "name");
                serializer.startTag(null, "strength");
                serializer.text(String.valueOf(p.getStrength()));
                serializer.endTag(null, "strength");
                serializer.startTag(null, "constitution");
                serializer.text(String.valueOf(p.getConstitution()));
                serializer.endTag(null, "constitution");
                serializer.startTag(null, "dexterity");
                serializer.text(String.valueOf(p.getDexterity()));
                serializer.endTag(null, "dexterity");
                serializer.startTag(null, "level");
                serializer.text(String.valueOf(p.getLevel()));
                serializer.endTag(null, "level");
                serializer.startTag(null, "cash");
                serializer.text(String.valueOf(p.getCash()));
                serializer.endTag(null, "cash");
                serializer.startTag(null, "experience");
                serializer.text(String.valueOf(p.getExperience()));
                serializer.endTag(null, "experience");
                serializer.endTag(null, "player");

                serializer.endDocument();
                serializer.flush();

                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        finish();

    }

}