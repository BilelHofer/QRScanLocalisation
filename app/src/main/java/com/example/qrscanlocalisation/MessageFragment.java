package com.example.qrscanlocalisation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class MessageFragment extends Fragment {

    private PageViewModel pageViewModel;
    private TextView textView;
    private EditText editText;
    private Button button;
    private String messageContent = "";
    private String phoneNumber = "";
    private Handler mHandler = new Handler();

    // constructor
    public MessageFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialise le ViewModel
        pageViewModel = ViewModelProviders.of(requireActivity()).get(PageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.message_fragment, container, false);

        // initialise les composants
        textView = view.findViewById(R.id.text_view);
        editText = view.findViewById(R.id.edit_text);
        button = view.findViewById(R.id.btn_send);

        return view;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pageViewModel.getCoordinateLat().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                messageContent = s;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                mHandler.postDelayed(mRunnable, 1000);
                // Vérifi que l'editText contient un numéro de téléhpone valide
                phoneNumber = editText.getText().toString();
                String regex = "^0\\d{9}$";

                if (phoneNumber.matches(regex)) {
                    sendSMSMessage();
                } else {
                    Toast.makeText(getContext(), "Numéro non valide", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Envoie un SMS
     */
    protected void sendSMSMessage() {
        SmsManager smsManager = SmsManager.getDefault();
        String[] parts = messageContent.split(",");
        float x = Float.parseFloat(parts[0].split(":")[1].trim());
        float y = Float.parseFloat(parts[1].trim());
        String message = "https://www.google.com/maps/search/?api=1&query=" + x + "," + y;
        smsManager.sendTextMessage(phoneNumber, null, messageContent + "\n" + message, null, null);
        Toast.makeText(getContext(), "Message envoyé", Toast.LENGTH_SHORT).show();
    }

    /**
     * Handler pour activer le bouton après 1 seconde
     */
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            button.setEnabled(true);
        }
    };
}