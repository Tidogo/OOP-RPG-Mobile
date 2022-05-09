package com.example.ooprpgproto;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlSerializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import com.example.ooprpgproto.*;

public class NewGameActivity extends AppCompatActivity implements Serializable {

    public int statCount = 0;
    public int str = 1;
    public int dex = 1;
    public int con = 1;
    public String charName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        refreshStats();
    }

    public void addStr(View v) {
        if (statCount < 3) {
            str = str + 1;
            refreshStats();
            statCount = statCount + 1;
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(NewGameActivity.this).create();
            alertDialog.setTitle("Not Enough Points");
            alertDialog.setMessage("You ran out of points to allocate to your skills!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    public void addDex(View v) {
        if (statCount < 3) {
            dex = dex + 1;
            statCount = statCount + 1;
            refreshStats();
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(NewGameActivity.this).create();
            alertDialog.setTitle("Not Enough Points");
            alertDialog.setMessage("You ran out of points to allocate to your skills!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    public void addCon(View v) {
        if (statCount < 3) {
            con = con + 1;
            statCount = statCount + 1;
            refreshStats();
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(NewGameActivity.this).create();
            alertDialog.setTitle("Not Enough Points");
            alertDialog.setMessage("You ran out of points to allocate to your skills!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    public void btnReset(View v) {
        str = 1;
        dex = 1;
        con = 1;
        statCount = 0;
        refreshStats();
    }

    public void refreshStats() {
        TextView txtStr = (TextView) findViewById(R.id.txtStr);
        txtStr.setText("Strength: " + str);
        TextView txtDex = (TextView) findViewById(R.id.txtDex);
        txtDex.setText("Dexterity: " + dex);
        TextView txtCon = (TextView) findViewById(R.id.txtCon);
        txtCon.setText("Constitution: " + con);
    }

    public void createChar(View v) throws ParserConfigurationException, TransformerException, IOException {
        EditText txtName;
        txtName = findViewById(R.id.editTextTextPersonName3);
        charName = String.valueOf(txtName.getText());
        Player newPlayer = new Player(charName, str, con, dex);

        FileOutputStream fos;
        fos = openFileOutput("player", Context.MODE_PRIVATE);

        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(fos, "UTF-8");
        serializer.startDocument(null, Boolean.valueOf(true));
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

        serializer.startTag(null, "player");
        serializer.startTag(null, "name");
        serializer.text(charName);
        serializer.endTag(null, "name");
        serializer.startTag(null, "strength");
        serializer.text(String.valueOf(str));
        serializer.endTag(null, "strength");
        serializer.startTag(null, "constitution");
        serializer.text(String.valueOf(con));
        serializer.endTag(null, "constitution");
        serializer.startTag(null, "dexterity");
        serializer.text(String.valueOf(dex));
        serializer.endTag(null, "dexterity");
        serializer.startTag(null, "level");
        serializer.text(String.valueOf(newPlayer.getLevel()));
        serializer.endTag(null, "level");
        serializer.startTag(null, "cash");
        serializer.text(String.valueOf(newPlayer.getCash()));
        serializer.endTag(null, "cash");
        serializer.startTag(null, "experience");
        serializer.text(String.valueOf(newPlayer.getExperience()));
        serializer.endTag(null, "experience");
        serializer.endTag(null, "player");

        serializer.endDocument();
        serializer.flush();

        fos.close();
        AlertDialog alertDialog = new AlertDialog.Builder(NewGameActivity.this).create();
        alertDialog.setTitle("Welcome!");
        alertDialog.setMessage("Your character is now created and ready to adventure!" + newPlayer.getName());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        openHub(newPlayer);
    }

    public void openHub(Player p) {
        Intent intent = new Intent(this, HubActivity.class);
        intent.putExtra("thePlayer", p);
        startActivity(intent);
    }


}