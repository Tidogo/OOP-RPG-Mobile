package com.example.ooprpgproto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button newGameBtn = (Button) findViewById(R.id.btnNewGame);
        Button loadGameBtn = (Button) findViewById(R.id.btnLoadGame);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewGame();
            }
        });
        loadGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGame();
            }
        });
    }
    public void openNewGame() {
        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);
    }

    public void loadGame() {
        String name = "";
        int str = 0;
        int dex = 0;
        int con = 0;
        int level = 0;
        int cash = 0;
        int experience = 0;
        File f = new File("/data/data/com.example.ooprpgproto/files/player");
        FileInputStream istream = null;
        try {
            istream = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = db.parse(istream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        Element element = doc.getDocumentElement();
        element.normalize();
        NodeList nList = doc.getElementsByTagName("player");
        for (int i=0; i<nList.getLength(); i++) {
            Node node = nList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element2 = (Element) node;
                name = getValue("name", element2);
                str = Integer.parseInt(getValue("strength", element2));
                con = Integer.parseInt(getValue("constitution", element2));
                dex = Integer.parseInt(getValue("dexterity", element2));
                level = Integer.parseInt(getValue("level", element2));
                cash = Integer.parseInt(getValue("cash", element2));
                experience = Integer.parseInt(getValue("experience", element2));
            }
        }
        Player p = new Player(name, str, con, dex, level, cash, experience);
        Intent intent = new Intent(this, HubActivity.class);
        intent.putExtra("thePlayer", p);
        startActivity(intent);

    }
    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
}