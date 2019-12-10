package com.example.kuttu;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.os.AsyncTask;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;
import com.skyfishjy.library.RippleBackground;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import me.omidh.liquidradiobutton.LiquidRadioButton;

public class MainActivity extends AppCompatActivity {

   private Button buttonUp;
   private Button buttonDown;
   private Button buttonLeft;
   private Button buttonRight;

   private LiquidRadioButton autoRadioButton;
   private LiquidRadioButton manualRadioButton;
   private RadioGroup radioGroup;

   boolean isStart = false;
   boolean isAutomatic = false;

   private String address = null;
   private String name = null;

   private TextView infoTextView;
   private boolean pressedUp = false;


   private BluetoothAdapter bluetoothAdapter = null;
   private BluetoothSocket bluetoothSocket = null;
   private Set<BluetoothDevice> pairedDevices;

    static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        infoTextView  = (TextView) findViewById(R.id.infoTextView);

        buttonUp = (Button) findViewById(R.id.buttonUp);
        buttonDown = (Button) findViewById(R.id.buttonDown);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonRight = (Button) findViewById(R.id.buttonRight);


        try {
            //getLocation();
            launch();

        } catch (Exception e) {
            infoTextView.setText("No Device Found!");
            infoTextView.setTextColor(Color.parseColor("#ff4040"));

        }


        class SendData extends AsyncTask<Void, Void, Void> {


            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... arg0) {
                while (pressedUp) {

                    try {
                        sendDataToVehicle("W","doinback");
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                   // System.out.print("W");

                }
                return null;
            }
        }

        buttonUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        sendDataToVehicle("W","UP");
                        buttonDown.setEnabled(false);
                        buttonLeft.setEnabled(false);
                        buttonRight.setEnabled(false);
                        buttonUp.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.button_pressed));
                        if(pressedUp == false){
                            pressedUp = true;
                            //new SendData().execute();
                        }
                        break;

                        case MotionEvent.ACTION_UP:
                        pressedUp=false;
                        reset();
                        sendDataToVehicle("S","Pause");
                     buttonUp.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.button_background));

                }

                return true;
            }
        });

        buttonDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        sendDataToVehicle("B","Down");
                        buttonUp.setEnabled(false);
                        buttonLeft.setEnabled(false);
                        buttonRight.setEnabled(false);

                        buttonDown.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.button_pressed));
                        if(pressedUp == false){
                            pressedUp = true;
                           // new SendData().execute();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        pressedUp=false;
                        reset();
                        sendDataToVehicle("S","Pause");
                        buttonDown.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.button_background));

                }

                return true;
            }
        });

        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        sendDataToVehicle("A","Left");

                        buttonDown.setEnabled(false);
                        buttonUp.setEnabled(false);
                        buttonRight.setEnabled(false);

                        buttonLeft.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.button_pressed));
                        if(pressedUp == false){
                            pressedUp = true;
                            //new SendData().execute();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        pressedUp=false;
                        reset();
                        sendDataToVehicle("S","Pause");
                        buttonLeft.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.button_background));

                }

                return true;
            }
        });

        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        sendDataToVehicle("D","Right");

                        buttonDown.setEnabled(false);
                        buttonLeft.setEnabled(false);
                        buttonUp.setEnabled(false);

                        buttonRight.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.button_pressed));
                        if(pressedUp == false){
                            pressedUp = true;
                           // new SendData().execute();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        pressedUp=false;
                        reset();
                        sendDataToVehicle("S","Pause");
                        buttonRight.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.button_background));

                }

                return true;
            }
        });

        autoRadioButton = (LiquidRadioButton) findViewById(R.id.autoRadioButton);
        manualRadioButton = (LiquidRadioButton) findViewById(R.id.manualRadioButton);
       // manualRadioButton.setChecked(true);

        radioGroup = (RadioGroup) findViewById(R.id.radioButtonGroup);
        manualRadioButton.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == 2131230751) {
                    isAutomatic=true;
                    sendDataToVehicle("Y","Automatic Mode On");
                }
                else {
                    isAutomatic=false;
                    sendDataToVehicle("Z","Manual Mode On");

                }
            }
        });


        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
       SparkButton sparkButton = (SparkButton) findViewById(R.id.spark_button);
//        rippleBackground.getLayoutParams().height=height;
//        rippleBackground.getLayoutParams().width=width;

        sparkButton.setEventListener(new SparkEventListener(){
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    isStart=true;
                    sendDataToVehicle("T","Start");
                   // System.out.println("Start");
                    autoRadioButton.setEnabled(false);
                    manualRadioButton.setEnabled(false);
                    rippleBackground.startRippleAnimation();

                } else {
                    isStart = false;
                    sendDataToVehicle("F","Stop");
                    //System.out.println("Stop");
                    autoRadioButton.setEnabled(true);
                    manualRadioButton.setEnabled(true);
                   rippleBackground.stopRippleAnimation();

                    // Button is inactive
                    //System.out.println("S");
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });



        Croller croller = (Croller) findViewById(R.id.croller);
        croller.setIndicatorWidth(10);
        croller.setBackCircleColor(Color.parseColor("#EDEDED"));
        croller.setMainCircleColor(Color.parseColor("#088da5"));
        croller.setMax(10);
        croller.setStartOffset(50);
        croller.setIsContinuous(true);
        croller.setLabelColor(Color.BLACK);
        croller.setProgressPrimaryColor(Color.parseColor("#8a2be2"));
        croller.setIndicatorColor(Color.parseColor("#363636"));
        croller.setProgressSecondaryColor(Color.parseColor("#ffdab9"));

        croller.setLabel("Speed");
        croller.setLabelSize(40);
        croller.setProgress(10);


         croller.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {
                // use the progress
            }

            @Override
            public void onStartTrackingTouch(Croller croller) {
                // tracking started
            }

            @Override
            public void onStopTrackingTouch(Croller croller) {
                int speed  = croller.getProgress()-1;
                croller.setLabel(String.valueOf(speed));
                sendDataToVehicle(String.valueOf(speed),"Speed Changed");
               // System.out.println("Speed Changed");
                // tracking stopped
            }
        });



    }

    private void launch() {
        try {
            bluetooth_connect_device();
        } catch (IOException e) {
            infoTextView.setText("No Device Found!");
            infoTextView.setTextColor(Color.parseColor("cc0000"));
        }
    }

    private void bluetooth_connect_device() throws IOException {

        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            address = bluetoothAdapter.getAddress();
            pairedDevices = bluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice bt : pairedDevices) {
                    address = bt.getAddress().toString();
                    name = bt.getName().toString();
                    Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();

                    //  infoTextView.setText("Device Connected");


                    infoTextView.setText("Connected Device: " + name);
                    infoTextView.setTextColor(Color.parseColor("428bca"));
                }
            }
        } catch (Exception e) {


        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice dispositivo = bluetoothAdapter.getRemoteDevice(address);
        bluetoothSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(uuid);
        bluetoothSocket.connect();

    }

private void sendDataToVehicle(String data,String msg){
    try {
        if (bluetoothSocket != null) {
            bluetoothSocket.getOutputStream().write(data.getBytes());
            System.out.println(msg);
        }
    } catch (Exception e) {

    }
}

private void reset(){

    buttonUp.setEnabled(true);
    buttonDown.setEnabled(true);
    buttonLeft.setEnabled(true);
    buttonRight.setEnabled(true);
}
    @Override
    public void onStop()
    {
        //sendDataToVehicle("#","Application Stops");
        super.onStop();

        //Do whatever you want to do when the application stops.
    }
    @Override
    public void onDestroy() {
        sendDataToVehicle("#","Application Terminated");
        super.onDestroy();
        //Do whatever you want to do when the application is destroyed.
    }

}

