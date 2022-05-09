package com.example.ooprpgproto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Random;

public class BattleActivity extends AppCompatActivity implements Serializable {

    static Random rnd = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        Player thePlayer = (Player) getIntent().getSerializableExtra("thePlayer");
        Monster theMon = (Monster) getIntent().getSerializableExtra("theMon");
        EditText etLog = (EditText) findViewById(R.id.textMultiLog);
        refreshUI(thePlayer, theMon);
        etLog.setEnabled(false);
        Button attBtn = (Button) findViewById(R.id.btnAtt);
        attBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attack(thePlayer, theMon, etLog);
            }
        });
        Button fleeBtn = (Button) findViewById(R.id.btnFlee);
        fleeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flee(thePlayer, theMon, etLog);
            }
        });

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
        txtPHP.setText("Player HP: "+ p.getHealth());
        TextView txtMHP = (TextView) findViewById(R.id.txtMonHP);
        txtMHP.setText("Monster HP: " + String.valueOf(m.getHealth()));
    }
    public void attack(Player p, Monster m, EditText log) {
        double monDmg = m.getAttPower();
        double userDmg = p.getAttPower();
        if (rnd.nextInt(100) < p.getCrit()*100 )
        {
            userDmg = p.getAttPower() * 2;
        }
        else if (rnd.nextInt(100) < m.getCrit()*100 )
        {
            userDmg = m.getAttPower() * 2;
        }

        else if (monDmg >= p.Health)
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
            p.setCash(m.getRndCashLoot());
            log.append(p.getName() + " vanquished the " + m.getName() + "! You gain "
                    + m.getExperienceValue() + "XP and " + m.getRndCashLoot() + " bucks!" + "\n");
            if (p.getExperience() >= ((p.getLevel() * (p.getLevel() - 1)) * 50))
            {
                String[] stats = {"Strength", "Dexterity", "Constitution"};
                int s = p.getStrength();
                int d = p.getDexterity();
                int c = p.getConstitution();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("You Leveled Up! Choose a skill to allocate your point!");
                builder.setItems(stats, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: p.setStrength(s+1);
                            case 1: p.setDexterity(d+1);
                            case 2: p.setConstitution(c+1);
                        }
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else
            {
                backToHub(p);
            }
        }
        else
        {
            int monHP = m.getHealth();
            m.setHealth(monHP -= Math.round((userDmg - (m.getDefense()) * 1.0)));
            String aar = p.getName() + " hit " + m.getName() + " for " + p.getAttPower() + " DMG!" + "\n";
            log.append(aar);
            int hp = p.getHealth();
            p.setHealth(hp -= Math.round((monDmg - (p.getDefense() * 1.0))));
            aar = m.getName() + " hit " + p.getName() + " for " + m.getAttPower() + " DMG!" + "\n";
            log.append(aar);
            monDmg = m.getAttPower();
            userDmg = p.getAttPower();
            refreshUI(p, m);
        }

    }

    public void backToHub(Player p) {
        Intent intent = new Intent(this, HubActivity.class);
        intent.putExtra("thePlayer", p);
        startActivity(intent);
    }
}