package com.example.ooprpgproto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import com.example.ooprpgproto.*;

public class HubActivity extends AppCompatActivity implements Serializable {

    ArrayList<Monster> mons = new ArrayList<Monster>();
    static Random rnd = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        Player thePlayer = (Player) getIntent().getSerializableExtra("thePlayer");
        refreshUI(thePlayer);
        String file = "Monster.xml";
        try {
            InputStream in1 = this.getAssets().open(file);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in1);
            Element element = doc.getDocumentElement();
            element.normalize();
            NodeList nList = doc.getElementsByTagName("Monster");
            for (int i=0; i<nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    String name = getValue("Name", element2);
                    int lvl = Integer.parseInt(getValue("Level", element2));
                    int strength = Integer.parseInt(getValue("Level", element2));
                    int difficulty = Integer.parseInt(getValue("Difficulty", element2));
                    int constitution = Integer.parseInt(getValue("Constitution", element2));
                    int dexterity = Integer.parseInt(getValue("Dexterity", element2));
                    int attkPower = Integer.parseInt(getValue("AttkPower", element2));
                    int initiative = Integer.parseInt(getValue("Initiative", element2));
                    Monster mon = new Monster(name,lvl,strength,difficulty,constitution,dexterity,
                            attkPower,initiative);
                    mons.add(mon);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        Spinner diff = (Spinner) findViewById(R.id.spinDiff);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Can I play, Daddy?");
        arrayList.add("Don't Hurt Me");
        arrayList.add("Bring 'Em On!");
        arrayList.add("I AM DEATH INCARNATE!");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diff.setAdapter(arrayAdapter);
        Button battleBtn = (Button) findViewById(R.id.btnFight);
        Button shopBtn = (Button) findViewById(R.id.btnItemShop);
        Button invBtn = (Button) findViewById(R.id.btnInven);
        battleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                battle(thePlayer);
            }
        });
        shopBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                shop(thePlayer);
            }
        });
        invBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                inventory(thePlayer);
            }
        });

    }

    private void inventory(Player thePlayer) {
        Intent intent = new Intent(this, InventoryActivity.class);
        intent.putExtra("thePlayer", thePlayer);
        startActivity(intent);
    }

    public void shop(Player thePlayer) {
        Intent intent = new Intent(this, ShopActivity.class);
        intent.putExtra("thePlayer", thePlayer);
        startActivity(intent);
    }
    public void refreshUI(Player p) {
        TextView txtName = (TextView) findViewById(R.id.txtName);
        txtName.setText("Name: "+p.getName());
        TextView txtHP = (TextView) findViewById(R.id.txtHP);
        txtHP.setText("Health: " + String.valueOf(p.getHealth()));
        TextView txtExp = (TextView) findViewById(R.id.txtExp);
        txtExp.setText("Experience" + String.valueOf(p.getExperience()));
    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public void battle(Player p) {
        final Spinner spinDiff = (Spinner) findViewById(R.id.spinDiff);
        int selectDiff = spinDiff.getSelectedItemPosition()+1;
        ArrayList<Monster> monJumble = new ArrayList<Monster>();
        for (Monster m : mons) {
            if (m.getDifficulty() == selectDiff) {
                monJumble.add(m);
            }
        }
        int r = rnd.nextInt(monJumble.size());
        Monster mon = monJumble.get(r);
        Intent intent = new Intent(this, BattleActivity.class);
        intent.putExtra("thePlayer", p);
        intent.putExtra("theMon", mon);
        startActivity(intent);
    }
}