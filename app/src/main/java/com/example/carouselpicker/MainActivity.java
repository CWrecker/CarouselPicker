package com.example.carouselpicker;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {






    //--------------------------------
    //      Serial Connection
    //-------------------------------

    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    Button startButton, sendButton, orderButton, clearButton, stopButton;
    TextView textView;
    EditText editText;
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;

    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() { //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {
            String data = null;
            try {
                data = new String(arg0, "UTF-8");
                data.concat("/n");
                tvAppend(textView, data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }
    };
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { //Broadcast Receiver to automatically start and stop the Serial connection.
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
                boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) {
                    connection = usbManager.openDevice(device);
                    serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
                    if (serialPort != null) {
                        if (serialPort.open()) { //Set Serial Connection Parameters.
                            setUiEnabled(true);
                            serialPort.setBaudRate(9600);
                            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                            serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                            serialPort.read(mCallback);
                            tvAppend(textView,"Serial Connection Opened!\n");

                        } else {
                            Log.d("SERIAL", "PORT NOT OPEN");
                        }
                    } else {
                        Log.d("SERIAL", "PORT IS NULL");
                    }
                } else {
                    Log.d("SERIAL", "PERM NOT GRANTED");
                }
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                onClickStart(startButton);
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                onClickStop(stopButton);

            }
        }

        ;
    };



//BUTTON

    Dialog myDialog;
    String DrinkName="A Drink";  //Note these strings should never appear
    String DrinkType="A Drink Type";   //Note these strings should never appear

    public int Vodka_Measure;
    public int Rum_Measure;
    public int Gin_Measure;
    public int Whiskey_Measure;
    public int TonicWater_Measure;
    public int CranberryJuice_Measure;
    public int OrangeJuice_Measure;
    public int Pineapple_Measure;
    public int Mint_Measure;
    public int Sugar_Measure;
    public int Lime_Measure;
    public int Lime_Slice;
    public int Stir;
    public int Mash;
    //Human Functions
    public int Ice_Measure;
    public int Shake;
    public int Kalua;
    public int Cointreau;
    public int Vermouth;
    public int Peach_Schnapps;
    public int Cream_De_Cacao;



     public String DrinkSizeText;
    public int DrinkSize;
















    CarouselPicker carouselPicker1, carouselPicker2, carouselPicker3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//-------------------------------
        //Serial Connection Functions
        //----------------------

        usbManager = (UsbManager) getSystemService(this.USB_SERVICE);
        startButton = (Button) findViewById(R.id.buttonStart);
        sendButton = (Button) findViewById(R.id.buttonSend);
        orderButton = (Button) findViewById(R.id.btnorder);
        clearButton = (Button) findViewById(R.id.buttonClear);
        stopButton = (Button) findViewById(R.id.buttonStop);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        setUiEnabled(false);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(broadcastReceiver, filter);




            //BUTTON
        myDialog = new Dialog(this);

        //---------------------------------------------
        //          DRINK ITEMS / IMAGES ON CAROUSEL!
        //---------------------------------------------

        carouselPicker1 = findViewById(R.id.carouselPicker1);
        carouselPicker2 = findViewById(R.id.carouselPicker2);
        carouselPicker3 = findViewById(R.id.carouselPicker3);


//VODKA BASED DRINKS

        List<CarouselPicker.PickerItem> itemsImages = new ArrayList<>();
        itemsImages.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher_round));
        itemsImages.add(new CarouselPicker.DrawableItem(R.mipmap.drinkpic01));
        itemsImages.add(new CarouselPicker.DrawableItem(R.mipmap.drinkpic02));
        itemsImages.add(new CarouselPicker.DrawableItem(R.mipmap.drinkpic03));
        itemsImages.add(new CarouselPicker.DrawableItem(R.mipmap.drinkpic04));
        CarouselPicker.CarouselViewAdapter imageAdapter = new CarouselPicker.CarouselViewAdapter(this, itemsImages, 0);
        carouselPicker1.setAdapter(imageAdapter);

        //RUM BASED DRINKS

/**
        List<CarouselPicker.PickerItem> textItems = new ArrayList<>();
        textItems.add(new CarouselPicker.TextItem("One", 2));
        textItems.add(new CarouselPicker.TextItem("Two", 2));
        textItems.add(new CarouselPicker.TextItem("Three", 2));
        CarouselPicker.CarouselViewAdapter textAdapter = new CarouselPicker.CarouselViewAdapter(this, textItems, 0);
        carouselPicker2.setAdapter(textAdapter);

**/

        List<CarouselPicker.PickerItem> RumImages = new ArrayList<>();
        RumImages.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher_round));
        RumImages.add(new CarouselPicker.DrawableItem(R.mipmap.drinkpic01));
        RumImages.add(new CarouselPicker.DrawableItem(R.mipmap.drinkpic02));
        RumImages.add(new CarouselPicker.DrawableItem(R.mipmap.drinkpic03));
        RumImages.add(new CarouselPicker.DrawableItem(R.mipmap.drinkpic04));
        CarouselPicker.CarouselViewAdapter imageRumAdapter = new CarouselPicker.CarouselViewAdapter(this, RumImages, 0);
        carouselPicker2.setAdapter(imageRumAdapter);


        //GIN BASED DRINKS

        /**
        List<CarouselPicker.PickerItem> mixItems = new ArrayList<>();
        mixItems.add(new CarouselPicker.TextItem("One", 2));
        mixItems.add(new CarouselPicker.DrawableItem(R.mipmap.drinkpic06));
        mixItems.add(new CarouselPicker.TextItem("Three", 2));
        mixItems.add(new CarouselPicker.DrawableItem(R.mipmap.drinkpic07));
        CarouselPicker.CarouselViewAdapter mixAdapter = new CarouselPicker.CarouselViewAdapter(this, mixItems, 0);
        carouselPicker3.setAdapter(mixAdapter);



**/


        List<CarouselPicker.PickerItem> GinImages = new ArrayList<>();
        GinImages.add(new CarouselPicker.DrawableItem(R.mipmap.ic_launcher_round));
        GinImages.add(new CarouselPicker.DrawableItem(R.mipmap.drinkpic01));
        GinImages.add(new CarouselPicker.DrawableItem(R.mipmap.drinkpic02));
        GinImages.add(new CarouselPicker.DrawableItem(R.mipmap.drinkpic03));
        GinImages.add(new CarouselPicker.DrawableItem(R.mipmap.drinkpic04));
        CarouselPicker.CarouselViewAdapter imageGinAdapter = new CarouselPicker.CarouselViewAdapter(this, GinImages, 0);
        carouselPicker3.setAdapter(imageGinAdapter);

        //---------------------------------------------
        //          DRINK SELECTORS / DRINK RECIPES
        //---------------------------------------------

        //VODKA BASED DRINKS

        carouselPicker1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //position of the selected item
                switch (position) {
                    case 0:

                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;



                        Toast BlackRussian = Toast.makeText(getApplicationContext(),
                                "Black Russian",
                                Toast.LENGTH_SHORT);

                        BlackRussian.show();


                    break;

                    case 1:
                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;



                        Toast BlackRuian = Toast.makeText(getApplicationContext(),
                                "Black ttttRussian",
                                Toast.LENGTH_SHORT);

                        BlackRuian.show();


                        break;

                    case 2:


                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;



                        Toast BlaRussian = Toast.makeText(getApplicationContext(),
                                "Black Ruyyyssian",
                                Toast.LENGTH_SHORT);

                        BlaRussian.show();




                        break;


                    case 3:


                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;

                        Toast BlackRussin = Toast.makeText(getApplicationContext(),
                                "Black Russiytrreean",
                                Toast.LENGTH_SHORT);

                        BlackRussin.show();




                        break;


                    case 4:


                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;



                        Toast Blackian = Toast.makeText(getApplicationContext(),
                                "Black456 Russian",
                                Toast.LENGTH_SHORT);

                        Blackian.show();




                        break;







                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        //RUM DRINK SELECTORS


        carouselPicker2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //position of the selected item
                switch (position) {
                    case 0:

                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;



                        Toast BlackRussian = Toast.makeText(getApplicationContext(),
                                "Black Russian",
                                Toast.LENGTH_SHORT);

                        BlackRussian.show();


                        break;

                    case 1:
                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;



                        Toast BlackRuian = Toast.makeText(getApplicationContext(),
                                "Black ttttRussian",
                                Toast.LENGTH_SHORT);

                        BlackRuian.show();


                        break;

                    case 2:


                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;



                        Toast BlaRussian = Toast.makeText(getApplicationContext(),
                                "Black Ruyyyssian",
                                Toast.LENGTH_SHORT);

                        BlaRussian.show();




                        break;


                    case 3:


                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;

                        Toast BlackRussin = Toast.makeText(getApplicationContext(),
                                "Black Russiytrreean",
                                Toast.LENGTH_SHORT);

                        BlackRussin.show();




                        break;


                    case 4:


                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;



                        Toast Blackian = Toast.makeText(getApplicationContext(),
                                "Black456 Russian",
                                Toast.LENGTH_SHORT);

                        Blackian.show();




                        break;







                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });






        carouselPicker3.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //position of the selected item
                switch (position) {
                    case 0:

                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;



                        Toast BlackRussian = Toast.makeText(getApplicationContext(),
                                "Black Russian",
                                Toast.LENGTH_SHORT);

                        BlackRussian.show();


                        break;

                    case 1:
                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;



                        Toast BlackRuian = Toast.makeText(getApplicationContext(),
                                "Black ttttRussian",
                                Toast.LENGTH_SHORT);

                        BlackRuian.show();


                        break;

                    case 2:


                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;



                        Toast BlaRussian = Toast.makeText(getApplicationContext(),
                                "Black Ruyyyssian",
                                Toast.LENGTH_SHORT);

                        BlaRussian.show();




                        break;


                    case 3:


                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;

                        Toast BlackRussin = Toast.makeText(getApplicationContext(),
                                "Black Russiytrreean",
                                Toast.LENGTH_SHORT);

                        BlackRussin.show();




                        break;


                    case 4:


                        DrinkName = "Black Russian";
                        DrinkType = "A Vodka Drink";
                        Vodka_Measure = 2;
                        Rum_Measure = 0;
                        Gin_Measure = 0;
                        Whiskey_Measure = 0;
                        TonicWater_Measure = 0;
                        CranberryJuice_Measure = 0;
                        OrangeJuice_Measure = 0;
                        Pineapple_Measure = 0;
                        Mint_Measure = 0;
                        Sugar_Measure = 0;
                        Lime_Measure = 0;
                        Lime_Slice = 0;
                        Stir = 1;
                        Mash = 0;
                        Shake = 0;
                        Ice_Measure = 0;
                        Kalua = 0;
                        Cointreau = 0;
                        Vermouth = 0;
                        Peach_Schnapps = 0;
                        Cream_De_Cacao = 0;



                        Toast Blackian = Toast.makeText(getApplicationContext(),
                                "Black456 Russian",
                                Toast.LENGTH_SHORT);

                        Blackian.show();




                        break;







                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });















    }

//BUTTON TO INITIATE POPUP
    public void ShowPopup(View v) {
        TextView txtclose;
        Button btnOrder;
        myDialog.setContentView(R.layout.custompopup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        btnOrder = (Button) myDialog.findViewById(R.id.btnorder);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();


        TextView DrinkNameText;
        DrinkNameText = (TextView) myDialog.findViewById(R.id.DrinkNameText);
        DrinkNameText.setText(DrinkName);

        TextView DrinkTypeText;
        DrinkTypeText = (TextView) myDialog.findViewById(R.id.DrinkNameTypeText);
        DrinkTypeText.setText(DrinkType);

        /**
            TextView SetDrinkNameText = (TextView) findViewById(R.id.DrinkNameText);
            SetDrinkNameText.setText(DrinkName);
            TextView SetDrinkTypeText = (TextView) findViewById(R.id.DrinkNameTypeText);
            SetDrinkTypeText.setText(DrinkType);
        **/
        LinearLayout SetDrinkSizeSmall;
        SetDrinkSizeSmall = (LinearLayout) myDialog.findViewById(R.id.SetDrinkSize_Small);
        SetDrinkSizeSmall.setOnClickListener(new View.OnClickListener() {
//SETTING THE SIZE OF THE DRINK USING THE 3 BUTTONS IN THE POP UP! THIS IS ONE VALUE OF THE DRINK ORDER
            @Override
            public void onClick(View v) {
                DrinkSize = 1;
                DrinkSizeText = "Small";
                Toast Small = Toast.makeText(getApplicationContext(),
                        "Drink Size Set To Small",
                        Toast.LENGTH_SHORT);

                Small.show();
            }

        });


        LinearLayout SetDrinkSizeMedium;
        SetDrinkSizeMedium = (LinearLayout) myDialog.findViewById(R.id.SetDrinkSize_Medium);
        SetDrinkSizeMedium.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DrinkSize = 2;
                DrinkSizeText = "Medium";
                Toast Medium = Toast.makeText(getApplicationContext(),
                        "Drink Size Set To Medium",
                        Toast.LENGTH_SHORT);

                Medium.show();
            }

        });



        LinearLayout SetDrinkSizeLarge;
        SetDrinkSizeLarge = (LinearLayout) myDialog.findViewById(R.id.SetDrinkSize_Large);
        SetDrinkSizeLarge.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DrinkSize = 3;
                DrinkSizeText = "Large";
                Toast Large = Toast.makeText(getApplicationContext(),
                        "Drink Size Set To Large",
                        Toast.LENGTH_SHORT);

                Large.show();
            }

        });

    }
/**


    //SETTING THE DRINK SIZE BASED ON POPUP BUTTONS
     public int DrinkSize;

    public void SetDrinkSize_Small(View view) {

        DrinkSize = 1;
        Toast Small = Toast.makeText(getApplicationContext(),
                "Drink Size Set To Small",
                Toast.LENGTH_SHORT);

        Small.show();
    }

    public void SetDrinkSize_Medium(View view) {

        DrinkSize = 2;
        Toast Medium = Toast.makeText(getApplicationContext(),
                "Drink Size Set To Medium",
                Toast.LENGTH_SHORT);

        Medium.show();
    }


    public void SetDrinkSize_Large(View view) {

        DrinkSize = 3;
        Toast Large = Toast.makeText(getApplicationContext(),
                "Drink Size Set To Large",
                Toast.LENGTH_SHORT);

        Large.show();
    }
**/

    //-------------------------------------------------
    //              CREATING ORDER MATRIX
    //-------------------------------------------------














//---------------------------------------------
//---------------------------------------------
    //      Serial Connection
    //-----------------------------------------
    //-----------------------------------------

    public void setUiEnabled(boolean bool) {
        startButton.setEnabled(!bool);
        sendButton.setEnabled(bool);
        stopButton.setEnabled(bool);
        textView.setEnabled(bool);

    }
//Starts the serial connection between the android device and arduino!
    public void onClickStart(View view) {

        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                if (deviceVID == 0x2341)//Arduino Vendor ID
                {
                    PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(device, pi);
                    keep = false;
                } else {
                    connection = null;
                    device = null;
                }

                if (!keep)
                    break;
            }
        }


    }
//Sends What ever characters are in the edit text field. I kept this in the app for debugging purposes and custom drinks on the fly!!! Fun right! :)
    public void onClickSend(View view) {
        String string = editText.getText().toString();
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");

    }
//FINAL ORDER BUTTON (POPUP, SENDS DRINK ARRAY TO ARDUINO)
    //------------------------------------------------
    public void onClickOrder(View view) {

        //The Functions bellow are converting intiger values into strings which can then be written to the arduino via a Serial Connection
        String VodkaOrder = Integer.toString(Vodka_Measure);
        String RumOrder = Integer.toString(Rum_Measure);
        String GinOrder = Integer.toString(Gin_Measure);
        String WhiskeyOrder = Integer.toString(Whiskey_Measure);
        String TonicWaterOrder = Integer.toString(TonicWater_Measure);
        String CranberryJuiceOrder = Integer.toString(CranberryJuice_Measure);
        String OrangeJuiceOrder = Integer.toString(OrangeJuice_Measure);
        String PineappleJuiceOrder = Integer.toString(Pineapple_Measure);
        String MintOrder = Integer.toString(Mint_Measure);
        String SugarOrder = Integer.toString(Sugar_Measure);
        String LimeJuiceOrder = Integer.toString(Lime_Measure);
        String LimeSliceOrder = Integer.toString(Lime_Slice);
        String StirOrder = Integer.toString(Stir);
        String MashOrder = Integer.toString(Mash);
        String ShakeOrder = Integer.toString(Shake);
        String IceOrder = Integer.toString(Ice_Measure);
        String KaluaOrder = Integer.toString(Kalua);
        String CointreauOrder = Integer.toString(Cointreau);
        String VermouthOrder = Integer.toString(Vermouth);
        String PeachSchnappsOrder = Integer.toString(Peach_Schnapps);
        String CreamDeCacoOrder = Integer.toString(Cream_De_Cacao);
        String DrinkSizeOrder = Integer.toString(DrinkSize);

        //Actually writing the data to the arduino using the new strings created above
        serialPort.write(VodkaOrder.getBytes());
        serialPort.write(RumOrder.getBytes());
        serialPort.write(GinOrder.getBytes());
        serialPort.write(WhiskeyOrder.getBytes());
        serialPort.write(TonicWaterOrder.getBytes());
        serialPort.write(CranberryJuiceOrder.getBytes());
        serialPort.write(OrangeJuiceOrder.getBytes());
        serialPort.write(PineappleJuiceOrder.getBytes());
        serialPort.write(MintOrder.getBytes());
        serialPort.write(SugarOrder.getBytes());
        serialPort.write(LimeJuiceOrder.getBytes());
        serialPort.write(LimeSliceOrder.getBytes());
        serialPort.write(StirOrder.getBytes());
        serialPort.write(MashOrder.getBytes());
        serialPort.write(ShakeOrder.getBytes());
        serialPort.write(IceOrder.getBytes());
        serialPort.write(KaluaOrder.getBytes());
        serialPort.write(CointreauOrder.getBytes());
        serialPort.write(VermouthOrder.getBytes());
        serialPort.write(PeachSchnappsOrder.getBytes());
        serialPort.write(CreamDeCacoOrder.getBytes());
        serialPort.write(DrinkSizeOrder.getBytes());

        //A small text so the user knows what he/she ordered!

        tvAppend(textView, "\nYour Drink Order is one " + DrinkSizeText + " " + DrinkName + " (" + DrinkType + ")" + "\n");




                myDialog.dismiss(); //Closes the popup after the order button is pressed, and order data is sent



    }
//---------------------------------------------------------------
//Stops/Closes Serial Connection
    public void onClickStop(View view) {
        setUiEnabled(false);
        serialPort.close();
        tvAppend(textView,"\nSerial Connection Closed! \n");

    }
//Clears the TextView (E.i: What we see and send. (our drink orders)
    public void onClickClear(View view) {
        textView.setText(" ");
    }

    private void tvAppend(TextView tv, CharSequence text) {
        final TextView ftv = tv;
        final CharSequence ftext = text;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ftv.append(ftext);
            }
        });
    }












}