package com.example.qrscanlocalisation;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment {
    private PageViewModel pageViewModel;

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialise ViewModel here
        pageViewModel = ViewModelProviders.of(requireActivity()).get(PageViewModel.class);

        scanCode();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.scan_fragment, container, false);
    }


    /**
     * Scan l'écran
     */
    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("scanning ...");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    /**
     * Récupère le résultat du scan et l'affiche dans une boite de dialogue
     */
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("Résultat");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {

                    String url = result.getContents();

                    Uri uri = Uri.parse(url);
                    if (url.contains("geo")) {
                        // Envoie les données à la page suivante
                        pageViewModel.setCoordinate(url);
                        //uri = Uri.parse("http://maps.google.com/maps?q=loc:" + url.replace("geo:", ""));
                    } else {
                        Intent urlIntent = new Intent(Intent.ACTION_VIEW, uri);

                        // Lance sur le lien
                        startActivity(urlIntent);
                    }
                }
            });

            builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                    scanCode();
                }
            });
            builder.show();
        }
    });
}