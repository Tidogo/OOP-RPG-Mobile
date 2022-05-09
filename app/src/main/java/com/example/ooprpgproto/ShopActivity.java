package com.example.ooprpgproto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ShopActivity extends AppCompatActivity implements Serializable {

    ArrayList<Armor> armor = new ArrayList<Armor>();
    ArrayList<Consumable> consumable = new ArrayList<Consumable>();
    ArrayList<Weapon> weapon = new ArrayList<Weapon>();
    ArrayList<String> itemNames = new ArrayList<String>();
    ArrayList<String> playerInventory = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Player thePlayer = (Player) getIntent().getSerializableExtra("thePlayer");
        refreshUI(thePlayer);
        String file1 = "Armor.xml";
        String file2 = "Consumable.xml";
        String file3 = "Weapon.xml";
        EditText etLog = (EditText) findViewById(R.id.textMultiLog);
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
            InputStream in2 = this.getAssets().open(file2);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in2);
            Element element = doc.getDocumentElement();
            element.normalize();
            NodeList nList = doc.getElementsByTagName("Consumable");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    String name = getValue("Name", element2);
                    int sl = Integer.parseInt(getValue("StackLimit", element2));
                    boolean tb = Boolean.parseBoolean((getValue("TempBoost", element2)));
                    int hb = Integer.parseInt(getValue("HealthBoost", element2));
                    int tcb = Integer.parseInt(getValue("TempCritBoost", element2));
                    int tab = Integer.parseInt(getValue("TempAttackBoost", element2));
                    int tdb = Integer.parseInt(getValue("TempDodgeBoost", element2));
                    int tdefb = Integer.parseInt(getValue("TempDefenseBoost", element2));
                    int tinitb = Integer.parseInt(getValue("TempInitBoost", element2));
                    int numbattles = Integer.parseInt(getValue("NumOfBattles", element2));
                    int val = Integer.parseInt(getValue("Value", element2));
                    Consumable c = new Consumable(name, sl, tb, hb, tcb, tab, tdb, tdefb, tinitb, numbattles, val);
                    consumable.add(c);
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
            InputStream in3 = this.getAssets().open(file2);
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
                    int value = Integer.parseInt(getValue("TempDodgeBoost", element2));
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
        for (Armor arm : armor) {
            itemNames.add(arm.getName());
        }
        for (Weapon wep : weapon) {
            itemNames.add(wep.getName());
        }
        for (Consumable con : consumable) {
            itemNames.add(con.getName());
        }
        Spinner diffShop = (Spinner) findViewById(R.id.spinShop);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diffShop.setAdapter(arrayAdapter);
        Spinner diffInventory = (Spinner) findViewById(R.id.spinInven);
        for (Item i : thePlayer.Inventory) {
            playerInventory.add(i.getName());
        }
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playerInventory);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diffInventory.setAdapter(arrayAdapter2);
        Button buyBtn = (Button) findViewById(R.id.btnBuy);
        Button sellBtn = (Button) findViewById(R.id.btnSell);
        Button leaveBtn = (Button) findViewById(R.id.btnLeave);
        EditText itemDetails = (EditText) findViewById(R.id.editTextItem);
        diffShop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = diffShop.getSelectedItem().toString();
                String details = "";
                for (Armor ar : armor) {
                    if (name == ar.getName()) {
                        details =
                                "Defense: " + ar.getDefense() + " | " +
                                "Cost: " + ar.getValue() + " | " +
                                "Dodge: " + ar.getDodgeMod();
                    }

                }
                for (Weapon wep : weapon) {
                    if (name == wep.getName()) {
                        details =
                                "Attack Power: " + wep.getAttkPower() + " | " +
                                "Cost: " + wep.getValue() + " | " +
                                "Crit Chance: " + wep.getCritChance() + " | "
                                + "Crit Multiplier: " + wep.getCritDmgMod();
                    }
                }
                for (Consumable cons : consumable) {
                    if (name == cons.getName()) {
                        details =
                                "Cost: " + cons.getValue() + " | " +
                                "Health Boost: " + cons.getHealthBoost() + " | "
                                + "Crit Boost: " + cons.getTempCritBoost() + " | "
                                + "Attack Boost: " + cons.getTempAttackBoost() + " | "
                                + "Dodge Boost: " + cons.getTempDodgeBoost() + " | "
                                + "Defense Boost: " + cons.getTempDefenseBoost() + " | "
                                + "Initiative Boost: " + cons.getTempInitBoost() + " | ";
                    }
                }
                itemDetails.setText(details);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy(thePlayer, diffShop, diffInventory);
            }
        });
        sellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sell(thePlayer, diffShop, diffInventory);
            }
            });
            leaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave(thePlayer);
            }
        });

    }

    public void refreshUI(Player p) {
        TextView playerCash = (TextView) findViewById(R.id.txtPCash);
        playerCash.setText("Your cash: " + p.getCash());
    }
    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
    public void leave(Player p) {
        Intent intent = new Intent(this, HubActivity.class);
        intent.putExtra("thePlayer", p);
        startActivity(intent);
    }
    public void buy(Player p, Spinner shop, Spinner inventory){
        String selectedItem = shop.getSelectedItem().toString();
        for (Armor ar : armor) {
            if (selectedItem == ar.getName()) {
                p.Inventory.add(ar);
                int sellMoney = ar.getValue();
                int prevPlayerCash = p.getCash();
                if (checkWallet(sellMoney, prevPlayerCash)) {
                    p.setCash(prevPlayerCash - sellMoney);
                }
            }
        }
        for (Weapon we : weapon) {
            if (selectedItem == we.getName()) {
                p.Inventory.add(we);
                int sellMoney = we.getValue();
                int prevPlayerCash = p.getCash();
                if (checkWallet(sellMoney, prevPlayerCash)) {
                    p.setCash(prevPlayerCash - sellMoney);
                }
            }
        }
        for (Consumable consume : consumable) {
            if (selectedItem == consume.getName()) {
                p.Inventory.add(consume);
                int sellMoney = consume.getValue();
                int prevPlayerCash = p.getCash();
                if (checkWallet(sellMoney, prevPlayerCash)) {
                    p.setCash(prevPlayerCash - sellMoney);
                }
            }
        }
        refreshUI(p);
        for (Item i : p.Inventory) {
            playerInventory.add(i.getName());
        }
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playerInventory);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inventory.setAdapter(arrayAdapter2);
    }
    public void sell(Player p, Spinner shop, Spinner inventory) {
        if (p.Inventory.isEmpty() || inventory.getCount() == 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(ShopActivity.this).create();
            alertDialog.setTitle("Inventory Empty!");
            alertDialog.setMessage("You don't have anything to sell!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
        else {
            String selectedItemInven = inventory.getSelectedItem().toString();
            for (Armor ar : armor) {
                if (selectedItemInven == ar.getName()) {
                    p.Inventory.remove(ar);
                    playerInventory.remove(ar.getName());
                    int sellMoney = ar.getValue();
                    int prevPlayerCash = p.getCash();
                    p.setCash(prevPlayerCash + sellMoney);
                }
            }
            for (Weapon we : weapon) {
                if (selectedItemInven == we.getName()) {
                    p.Inventory.remove(we);
                    playerInventory.remove(we.getName());
                    int sellMoney = we.getValue();
                    int prevPlayerCash = p.getCash();
                    p.setCash(prevPlayerCash + sellMoney);
                }
            }
            for (Consumable consume : consumable) {
                if (selectedItemInven == consume.getName()) {
                    p.Inventory.remove(consume);
                    playerInventory.remove(consume.getName());
                    int sellMoney = consume.getValue();
                    int prevPlayerCash = p.getCash();
                    p.setCash(prevPlayerCash + sellMoney);
                }
            }
            refreshUI(p);
            ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playerInventory);
            arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            inventory.setAdapter(arrayAdapter2);
        }

    }

    public boolean checkWallet(int value, int cash) {
        boolean cashSufficient;
        if (cash < value) {
            AlertDialog alertDialog = new AlertDialog.Builder(ShopActivity.this).create();
            alertDialog.setTitle("Not Enough Money!");
            alertDialog.setMessage("You don't have enough money to buy this item!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            cashSufficient = false;
        }
        else {
            cashSufficient = true;
        }
        return cashSufficient;
    }

}