package com.example.qrscanlocalisation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    private PageViewModel pageViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialise le  ViewModel
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        pageViewModel.getCoordinate().observe(this, item -> {

            // Parse l'url pour récupérer les coordonnées
            String[] parts = item.split(",");
            float x = Float.parseFloat(parts[0].split(":")[1].trim());
            float y = Float.parseFloat(parts[1].trim());

            // Crée la coordonnée
            LatLng coordinate = new LatLng(x, y);

            // Remplacer le fragment scan par le fragment de map
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new MapFragment(coordinate)).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.container, new MessageFragment()).commit();
        });

        // Affiche le scan au démarrage
        getSupportFragmentManager().beginTransaction().add(R.id.container, new ScanFragment()).commit();
    }
}