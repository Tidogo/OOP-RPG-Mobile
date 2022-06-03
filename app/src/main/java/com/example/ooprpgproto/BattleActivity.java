package com.example.ooprpgproto;

import static com.example.ooprpgproto.MainActivity.getValue;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
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
import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class BattleActivity extends AppCompatActivity implements Serializable {

    static Random rnd = new Random();
    ArrayList<Consumable> consumable = new ArrayList<Consumable>();
    private ArrayList<String> playerInvenNames = new ArrayList<>();
    Player p = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        p = (Player) getIntent().getSerializableExtra("thePlayer");
        Monster theMon = (Monster) getIntent().getSerializableExtra("theMon");
        EditText etLog = (EditText) findViewById(R.id.textMultiLog);
        p.Calculate_Equip_Bonus();
        refreshUI(p, theMon);
        refreshInvConsumables(p);
        etLog.setEnabled(false);
        String file2 = "Consumable.xml";
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
        Button attBtn = (Button) findViewById(R.id.btnAtt);
        attBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attack(p, theMon, etLog);
            }
        });
        Button fleeBtn = (Button) findViewById(R.id.btnFlee);
        fleeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flee(p, theMon, etLog);
            }
        });
        Button useItemBtn = (Button) findViewById(R.id.button4);
        useItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useItem(p);
            }
        });

    }

    private void useItem(Player p) {
        Spinner spinItem = (Spinner) findViewById(R.id.spinConsumables);
        String iName = spinItem.getSelectedItem().toString();
        Consumable tempCon = null;
        for (Item i : p.Inventory) {
            if (iName.equals(i.getName())) {
                tempCon = (Consumable) i;
                p.setHealth(p.getHealth() + tempCon.getHealthBoost());
                p.setGearInitiativeMod(p.getGearInitiativeMod() + tempCon.getTempInitBoost());
                p.setGearDodgeTotal(p.getGearDodgeTotal() + tempCon.getTempDodgeBoost());
                p.setGearCritMod(p.getGearCritMod() + tempCon.getTempCritBoost());
                p.setGearAttPowerMod(p.getGearAttPowerMod() + tempCon.getTempAttackBoost());
                p.setGearDefenseTotal(p.getGearDefenseTotal() + tempCon.getTempDefenseBoost());
                p.Inventory.remove(i);
                break;
            }
        }
        refreshInvConsumables(p);
    }

    private void flee(Player thePlayer, Monster theMon, EditText etLog) {
        double monDmg = theMon.AttPower;
        if ((thePlayer.getGearInitiativeMod() + (thePlayer.getDexterity() * 3)) < ((theMon.getDexterity() * 3) + theMon.getInitiative()))
        {
            int hp = thePlayer.getHealth();
            thePlayer.setHealth( hp -= Math.round((monDmg - (thePlayer.Defense * 2.0))));
            etLog.append(theMon.getName() + " hit " + thePlayer.getName() + " for " + (monDmg - (thePlayer.getDefense() * 2.0)) + " DMG!" + "\n");
            etLog.append(thePlayer.getName() + " took a hit but ran away from the " + theMon.getName() + "\n");
            Player p = thePlayer;
            backToHub(p);
        }
        else
        {
            etLog.append(thePlayer.getName() + " ran away from the " + theMon.getName() + " before getting hit!" + "\n");
            Player p = thePlayer;
            backToHub(p);
        }
    }

    public void refreshUI(Player p, Monster m) {
        TextView txtPHP = (TextView) findViewById(R.id.txtPlayerHP);
        txtPHP.setText(p.getName() + " HP: " + p.getHealth());
        TextView txtMHP = (TextView) findViewById(R.id.txtMonHP);
        txtMHP.setText(m.getName() + " HP: " + String.valueOf(m.getHealth()));
    }
    public void refreshUI(Player p) {
        TextView txtPHP = (TextView) findViewById(R.id.txtPlayerHP);
        txtPHP.setText("Player HP: "+ p.getHealth());
    }
    public void refreshInvConsumables(Player p) {
        String className = "";
        playerInvenNames.clear();
        for (Item i : p.Inventory) {
            className = i.getClass().getSimpleName();
            if (className.equals("Consumable")) {
                playerInvenNames.add(i.getName());
            }
        }
        Spinner lv = (Spinner) findViewById(R.id.spinConsumables);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, playerInvenNames
        );
        lv.setAdapter(arrayAdapter);
        refreshUI(p);

    }
    public void attack(Player p, Monster m, EditText log) {
        double monDmg = m.getAttPower();
        double userDmg = p.getAttPower()+p.getGearAttPowerMod();
        if (rnd.nextInt(100) <= (p.getCrit()+p.getGearCritMod())*100 )
        {
            userDmg = (p.getAttPower() + p.getGearAttPowerMod()) * 2;
            if (monDmg >= p.Health)
            {
                p.setHealth(0);
                String aar = m.getName()+ " inflicted a near-fatal blow on " + p.getName() + "!" + "\n";
                log.append(aar);
                p.setHealth(1);
                p.setExperience(p.getExperience() - m.getExperienceValue());
                log.append(p.getName() + " barely makes it away in one piece. Be more careful next time!" + "\n");
                Intent intent = new Intent(this, HubActivity.class);
                intent.putExtra("thePlayer", p);
                startActivity(intent);
            }
            else if (userDmg >= m.getHealth())
            {
                String aar = p.getName() + " inflicted a fatal blow on " + m.getName() + "!" + "\n";
                log.append(aar);
                p.setExperience(p.getExperience() + m.getExperienceValue());
                p.setCash(m.getRndCashLoot() + p.getCash());
                p.setHealth(100 + (10 * p.getConstitution()) + 5);
                log.append(p.getName() + " vanquished the " + m.getName() + "! You gain "
                        + m.getExperienceValue() + "XP and " + m.getRndCashLoot() + " bucks!" + "\n");
                if (p.getExperience() >= ((p.getLevel() * (p.getLevel() - 1)) * 50))
                {
                    String[] stats = {"Strength", "Dexterity", "Constitution"};
                    int s = p.getStrength();
                    int d = p.getDexterity();
                    int c = p.getConstitution();
                    int l = p.getLevel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("You Leveled Up! Choose a skill to allocate your point!");
                    builder.setItems(stats, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    p.setStrength(s+1);
                                    p.setLevel(l+1);
                                    backToHub(p);
                                case 1:
                                    p.setDexterity(d+1);
                                    p.setLevel(l+1);
                                    backToHub(p);
                                case 2:
                                    p.setConstitution(c+1);
                                    p.setLevel(l+1);
                                    backToHub(p);
                            }
                        }
                    });

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    String[] stats = {"Back to Hub"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Enemy Vanquished!");
                    builder.setItems(stats, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    backToHub(p);
                            }
                        }
                    });

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
            else
            {
                int monHP = m.getHealth();
                m.setHealth(monHP -= Math.round((userDmg - (m.getDefense()) * 1.0)));
                String aar = p.getName() + " hit " + m.getName() + " for " + userDmg + " DMG!" + "\n";
                log.append(aar);
                int hp = p.getHealth();
                p.setHealth(hp -= Math.round((monDmg - (p.getDefense() * 1.0))));
                aar = m.getName() + " hit " + p.getName() + " for " + monDmg + " DMG!" + "\n";
                log.append(aar);
                monDmg = m.getAttPower();
                userDmg = p.getAttPower();
                refreshUI(p, m);
            }
        }
        else if (rnd.nextInt(100) <= m.getCrit()*100 )
        {
            monDmg = m.getAttPower() * 2;
            if (monDmg >= p.Health)
            {
                p.setHealth(0);
                String aar = m.getName()+ " inflicted a near-fatal blow on " + p.getName() + "!" + "\n";
                log.append(aar);
                p.setHealth(1);
                p.setExperience(p.getExperience() - m.getExperienceValue());
                log.append(p.getName() + " barely makes it away in one piece. Be more careful next time!" + "\n");
                Intent intent = new Intent(this, HubActivity.class);
                intent.putExtra("thePlayer", p);
                startActivity(intent);
            }
            else if (userDmg >= m.getHealth())
            {
                String aar = p.getName() + " inflicted a fatal blow on " + m.getName() + "!" + "\n";
                log.append(aar);
                p.setExperience(p.getExperience() + m.getExperienceValue());
                p.setCash(m.getRndCashLoot() + p.getCash());
                p.setHealth(100 + (10 * p.getConstitution()) + 5);
                log.append(p.getName() + " vanquished the " + m.getName() + "! You gain "
                        + m.getExperienceValue() + "XP and " + m.getRndCashLoot() + " bucks!" + "\n");
                if (p.getExperience() >= ((p.getLevel() * (p.getLevel() - 1)) * 50))
                {
                    String[] stats = {"Strength", "Dexterity", "Constitution"};
                    int s = p.getStrength();
                    int d = p.getDexterity();
                    int c = p.getConstitution();
                    int l = p.getLevel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("You Leveled Up! Choose a skill to allocate your point!");
                    builder.setItems(stats, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    p.setStrength(s+1);
                                    p.setLevel(l+1);
                                    backToHub(p);
                                case 1:
                                    p.setDexterity(d+1);
                                    p.setLevel(l+1);
                                    backToHub(p);
                                case 2:
                                    p.setConstitution(c+1);
                                    p.setLevel(l+1);
                                    backToHub(p);
                            }
                        }
                    });

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    String[] stats = {"Back to Hub"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Enemy Vanquished!");
                    builder.setItems(stats, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    backToHub(p);
                            }
                        }
                    });

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
            else
            {
                int monHP = m.getHealth();
                m.setHealth(monHP -= Math.round((userDmg - (m.getDefense()) * 1.0)));
                String aar = p.getName() + " hit " + m.getName() + " for " + userDmg + " DMG!" + "\n";
                log.append(aar);
                int hp = p.getHealth();
                p.setHealth(hp -= Math.round((monDmg - (p.getDefense() * 1.0))));
                aar = m.getName() + " hit " + p.getName() + " for " + monDmg + " DMG!" + "\n";
                log.append(aar);
                monDmg = m.getAttPower();
                userDmg = p.getAttPower();
                refreshUI(p, m);
            }
        }
        else
        {
            if (monDmg >= p.Health)
            {
                p.setHealth(0);
                String aar = m.getName()+ " inflicted a near-fatal blow on " + p.getName() + "!" + "\n";
                log.append(aar);
                p.setHealth(1);
                p.setExperience(p.getExperience() - m.getExperienceValue());
                log.append(p.getName() + " barely makes it away in one piece. Be more careful next time!" + "\n");
                Intent intent = new Intent(this, HubActivity.class);
                intent.putExtra("thePlayer", p);
                startActivity(intent);
            }
            else if (userDmg >= m.getHealth())
            {
                String aar = p.getName() + " inflicted a fatal blow on " + m.getName() + "!" + "\n";
                log.append(aar);
                p.setExperience(p.getExperience() + m.getExperienceValue());
                p.setCash(m.getRndCashLoot() + p.getCash());
                p.setHealth(100 + (10 * p.getConstitution()) + 5);
                log.append(p.getName() + " vanquished the " + m.getName() + "! You gain "
                        + m.getExperienceValue() + "XP and " + m.getRndCashLoot() + " bucks!" + "\n");
                if (p.getExperience() >= ((p.getLevel() * (p.getLevel() - 1)) * 50))
                {
                    String[] stats = {"Strength", "Dexterity", "Constitution"};
                    int s = p.getStrength();
                    int d = p.getDexterity();
                    int c = p.getConstitution();
                    int l = p.getLevel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("You Leveled Up! Choose a skill to allocate your point!");
                    builder.setItems(stats, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    p.setStrength(s+1);
                                    p.setLevel(l+1);
                                    backToHub(p);
                                case 1:
                                    p.setDexterity(d+1);
                                    p.setLevel(l+1);
                                    backToHub(p);
                                case 2:
                                    p.setConstitution(c+1);
                                    p.setLevel(l+1);
                                    backToHub(p);
                            }
                        }
                    });

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    String[] stats = {"Back to Hub"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Enemy Vanquished!");
                    builder.setItems(stats, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    backToHub(p);
                            }
                        }
                    });

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
            else
            {
                int monHP = m.getHealth();
                m.setHealth(monHP -= Math.round((userDmg - (m.getDefense()) * 1.0)));
                String aar = p.getName() + " hit " + m.getName() + " for " + userDmg + " DMG!" + "\n";
                log.append(aar);
                int hp = p.getHealth();
                p.setHealth(hp -= Math.round((monDmg - (p.getDefense() * 1.0))));
                aar = m.getName() + " hit " + p.getName() + " for " + monDmg + " DMG!" + "\n";
                log.append(aar);
                monDmg = m.getAttPower();
                userDmg = p.getAttPower();
                refreshUI(p, m);
            }
        }


    }

    public void backToHub(Player p) {
        p.Calculate_SubStats();
        p.calcHealth();
        Intent intent = new Intent(this, HubActivity.class);
        intent.putExtra("thePlayer", p);
        startActivity(intent);
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