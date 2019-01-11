package com.dawson.fesamm;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class is used to get a random financial hint
 * @author Alpha Kabine Kaba and Mohamed Hamza
 */
public class FinancialHintsActivity extends MenuActivity {
    private Fragment fragment;
    private FirebaseDatabase fd;
    DatabaseReference dr;
    String hint;
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_hints);

        fd = FirebaseDatabase.getInstance();
        dr = fd.getReference().child("hints");
        final HashMap<String, String> hints = new HashMap<>();
        final HashMap<String, String> sources = new HashMap<>();

        // Retrieve financial hints from firebase and one of them will be chosen randomly and will be passed to the FinancialHintsFragment object
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    hints.put(key, map.get("text"));
                    sources.put(key, map.get("source"));
                }

                String[] keys = new String[hints.size()];
                keys = hints.keySet().toArray(keys);
                String randomKey = keys[new Random().nextInt(keys.length)];
                hint = hints.get(randomKey);

                fragment = FinancialHintsFragment.newInstance(hint);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.hintFragmentPlaceholder, fragment);
                ft.commit();

                link = sources.get(randomKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Handles event when the user wants to retrieve a new financial hint
     * Retrieve financial hints from firebase and one of them will be chosen randomly and will be passed to the FinancialHintsFragment object
     * @param v
     */
    public void newHint(View v){
        final HashMap<String, String> hints = new HashMap<>();
        final HashMap<String, String> sources = new HashMap<>();

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    hints.put(key, map.get("text"));
                    sources.put(key, map.get("source"));
                }

                String[] keys = new String[hints.size()];
                keys = hints.keySet().toArray(keys);
                String randomKey = keys[new Random().nextInt(keys.length)];
                hint = hints.get(randomKey);

                fragment = FinancialHintsFragment.newInstance(hint);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.hintFragmentPlaceholder, fragment);
                ft.commit();

                link = sources.get(randomKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Handles event when the user wants to open the full article of the financial hint
     * Will open the web page of the article
     * @param v
     */
    public void openArticle(View v){
        Button sourceBtn = (Button) v;
        if(link != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(browserIntent);
        }
    }

    /**
     * This method saves the instance state
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("hint", hint);
        outState.putString("link", link);
    }
}
