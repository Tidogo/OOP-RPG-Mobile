package com.example.ooprpgproto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

public class InventoryActivity extends AppCompatActivity implements Serializable {

    private ArrayList<Item> playerInven = new ArrayList<>();
    private ArrayList<Armor> equippedArmor = new ArrayList<>();
    private ArrayList<Weapon> equippedWeapon = new ArrayList<>();
    private ArrayList<String> playerInvenNames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Player thePlayer = (Player) getIntent().getSerializableExtra("thePlayer");
        for (Item i : thePlayer.Inventory) {
            playerInven.add(i);
            playerInvenNames.add(i.getName());
        }
        ListView lv = (ListView) findViewById(R.id.listInventory);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, playerInvenNames
                );
        lv.setAdapter(arrayAdapter);

    }
}