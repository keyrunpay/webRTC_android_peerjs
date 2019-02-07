package np.com.kirann.thirdeye;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.maxwell.speechrecognition.OnSpeechRecognitionListener;
import com.maxwell.speechrecognition.OnSpeechRecognitionPermissionListener;
import com.maxwell.speechrecognition.SpeechRecognition;

import java.util.Locale;

public class StartupActivity extends AppCompatActivity implements View.OnClickListener, OnSpeechRecognitionListener, OnSpeechRecognitionPermissionListener {
    Switch btnSwitchVoice;
    Button btnLetsGo;
    TextView txtName;
    SpeechRecognition speechRecognition;
    TextToSpeech t1;
    Boolean voiceEnable=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        btnSwitchVoice = (Switch) findViewById(R.id.switchVoiceCommand);
        btnLetsGo = (Button) findViewById(R.id.btnLetsGo);
        txtName = findViewById(R.id.txtListen);
        btnSwitchVoice.setChecked(false);
        btnLetsGo.setOnClickListener(this);
        checkPermission();

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    t1.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onDone(String utteranceId) {
                            btnSwitchVoice.setChecked(true);
                        }

                        @Override
                        public void onError(String utteranceId) {
                        }

                        @Override
                        public void onStart(String utteranceId) {
                        }
                    });
                }

                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    t1.setSpeechRate(0.8f);
//                    t1.speak("Voice Control is enabled, if you are visually impared, say 'YES' to get connected with volunteers, else you can disable voice control.", TextToSpeech.QUEUE_FLUSH, null);
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    if(voiceEnable){
                                        txtName.setText("Listening ...");
//                                        btnSwitchVoice.setChecked(true);
                                    }
                                }
                            }, 14000
                    );
                }
            }

        });


        speechRecognition = new SpeechRecognition(this);
        speechRecognition.setSpeechRecognitionPermissionListener(this);
        speechRecognition.setSpeechRecognitionListener(this);
        speechRecognition.handleAudioPermissions(false);

        btnSwitchVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    speechRecognition.startSpeechRecognition();
                } else {
                    speechRecognition.stopSpeechRecognition();
                }
            }
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLetsGo) {
            Intent i = new Intent(StartupActivity.this, WelcomeActivity.class);
            startActivity(i);
        }
    }

    void startCall() {
        Intent i = new Intent(StartupActivity.this, WebActivity.class);
        startActivity(i);
    }

    @Override
    public void OnSpeechRecognitionStarted() {

    }

    @Override
    public void OnSpeechRecognitionStopped() {
    }

    @Override
    public void OnSpeechRecognitionFinalResult(String s) {
        if (btnSwitchVoice.isChecked()) speechRecognition.startSpeechRecognition();
    }

    @Override
    public void OnSpeechRecognitionCurrentResult(String s) {
        if (s.length() > 2) txtName.setText(s);
        if (s.trim().toLowerCase().contains("yes")) {
            btnSwitchVoice.setChecked(false);
            startCall();
        }
    }

    @Override
    public void OnSpeechRecognitionError(int i, String s) {
        if (btnSwitchVoice.isChecked()) speechRecognition.startSpeechRecognition();
    }

    @Override
    public void onPermissionGranted() {

    }

    @Override
    public void onPermissionDenied() {

    }

    @Override
    protected void onPause() {
        t1.stop();
        speechRecognition.stopSpeechRecognition();
        super.onPause();
        voiceEnable=false;
    }

    @Override
    protected void onResume() {
        if (btnSwitchVoice.isChecked()) speechRecognition.startSpeechRecognition();
        super.onResume();
        voiceEnable=true;
    }
}
