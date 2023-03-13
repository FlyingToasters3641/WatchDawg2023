package com.example.ToastScout;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;



public class save extends Fragment implements View.OnClickListener{
    private static final int FILE_EXPORT_REQUEST_CODE = 12;
    private String data = "";

    public static Bitmap bitmap;
    private static boolean qrReady = false;

    public static final int CREATE_FILE = 1;
    public static Uri fileUri;

    public String autoChargeStation = "";
    public String teleopChargeStation = "";

    public Boolean attemptedCharge = true;

    public Boolean attemptedChargeAuto = true;
    private ImageView ivOutput;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save, container, false);

        view.findViewById(R.id.generateQR).setOnClickListener(this);
        view.findViewById(R.id.newMatch2).setOnClickListener(this);
        ivOutput = view.findViewById(R.id.iv_output);


        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (data != null) {
                try {
                    Uri uri = data.getData();
                    ParcelFileDescriptor pfd = getContext().getContentResolver().openFileDescriptor(uri, "w");
                    FileOutputStream stream = new FileOutputStream(pfd.getFileDescriptor());
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.generateQR:

                try{
                    if (MainActivity.teamNumText.getText().toString() != null) {
                        MainActivity.teamNumber = MainActivity.teamNumText.getText().toString();
                    }

                    if (MainActivity.matchNumText.getText().toString() != null){
                        MainActivity.matchNumber = MainActivity.matchNumText.getText().toString();
                    }
                    if (MainActivity.scoutNameText.getText().toString() != null){
                        MainActivity.scoutName = MainActivity.scoutNameText.getText().toString();
                    }
                    MainActivity.alliance = Auto.position;
                    if (MainActivity.AutoDocked == 0 && MainActivity.AutoEngaged == 1 && MainActivity.Parking == 0){
                        autoChargeStation = "Engaged";
                    } else if (MainActivity.AutoDocked == 1 && MainActivity.AutoEngaged == 0 && MainActivity.Parking == 0){
                        autoChargeStation = "Docked";
                    } else {
                        autoChargeStation = "Not on charging station";
                        attemptedChargeAuto = false;
                    }

                    if (MainActivity.TeleopEngaged == 1){
                        teleopChargeStation = "Engaged";
                    } else if (MainActivity.TeleopDocked == 1 ){
                        teleopChargeStation = "Docked";
                    } else if (MainActivity.Parking == 1){
                        teleopChargeStation = "In community";
                    } else if (MainActivity.NotInCommunity == 1){
                        teleopChargeStation = "Not in Community";
                    } else if (MainActivity.TeleopChargeAttempt == 1){
                        teleopChargeStation = "No attempt";
                    }
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                Boolean mobility = false;
                if (MainActivity.mobility == 1){mobility = true;} else {mobility = false;}

                Boolean parking = false;
                if (MainActivity.Parking == 1){parking = true;} else {parking = false;}

                Boolean gP = false;
                if (MainActivity.groundPickup == 1){gP = true;} else {gP = false;}

                Boolean pS = false;
                if (MainActivity.playerStation == 1){pS = true;} else {pS = false;}

                Boolean pD = false;
                if (MainActivity.playedDefense == 1){pD = true;} else {pD = false;}

                Boolean pScore = false;
                if (MainActivity.preventsScoring == 1){pScore = true;} else {pScore = false;}

                Boolean dO = false;
                if (MainActivity.preventsScoring == 1){dO = true;} else {dO = false;}

                Boolean eD = false;
                if (MainActivity.effectiveDefense == 1){eD = true;} else {eD = false;}

                Boolean aACS = false;
                if (MainActivity.AutoChargeAttempt == 1){aACS = true;} else {aACS = false;}





                data = "{" +
                        "\"e\":\"" + MainActivity.eventKey + "\"," +
                        "\"sN\":\"" + MainActivity.scoutName + "\"," +
                        "\"mN\":\"" + MainActivity.matchNumber + "\"," +
                        "\"p\":\"" + Auto.alliancePos.getSelectedItem().toString() + "\"," +
                        "\"tN\":\"" + MainActivity.teamNumber + "\"," +
                        "\"mOC\":\"" + bSB(mobility) + "\"," +
                        "\"aACS\":\"" + bSB(aACS) + "\"," +
                        "\"aCS\":\"" + autoChargeStation + "\"," +
                        "\"aTCS\":\"" + bSB(attemptedCharge) + "\"," +
                        "\"tCS\":\"" + teleopChargeStation + "\"," +
                        "\"pFG\":\"" + bSB(gP) + "\"," +
                        "\"pFPS\":\"" + bSB(pS) + "\"," +
                        "\"rOA\":\"" + Arrays.toString(MainActivity.autoUpperNodes).replaceAll("[\\[\\],\\s]", "") + "\"," +
                        "\"rTwA\":\"" + Arrays.toString(MainActivity.autoMiddleNodes).replaceAll("[\\[\\],\\s]", "") + "\"," +
                        "\"rThA\":\"" + Arrays.toString(MainActivity.autoHybridNodes).replaceAll("[\\[\\],\\s]", "") + "\"," +
                        "\"rOT\":\"" + Arrays.toString(MainActivity.teleopUpperNodes).replaceAll("[\\[\\],\\s]", "") + "\"," +
                        "\"rTwT\":\"" + Arrays.toString(MainActivity.teleopMiddleNodes).replaceAll("[\\[\\],\\s]", "") + "\"," +
                        "\"rThT\":\"" + Arrays.toString(MainActivity.teleopHybridNodes).replaceAll("[\\[\\],\\s]", "") + "\"," +
                        "\"pD\":\"" + bSB(pD) + "\"," +
                        "\"pS\":\"" + bSB(pScore) + "\"," +
                        "\"dO\":\"" + bSB(pD) + "\"," +
                        "\"eD\":\"" + bSB(eD) + "\"," +
                        "\"mK\":\"" + MainActivity.eventKey +"_" + MainActivity.matchNumber + "\"}";



                //Initialize multi format writer
                MultiFormatWriter writer = new MultiFormatWriter();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                try {
                    //Initialize bit matrix
                    BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, 600, 600);
                    //Initialize barcode Encoder
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    //initialize bitmap
                    bitmap = encoder.createBitmap(matrix);
                    System.out.println(bitmap);
                    qrReady = true;
                    //set bitmap on image view
                    //saveFragment.ivOutput.setImageBitmap(bitmap);


                    Bitmap localBmp = encoder.createBitmap(matrix);
                    localBmp.compress(Bitmap.CompressFormat.PNG, 100,stream);
                    byte[] byteArray = stream.toByteArray();
                    stream.close();

                } catch(WriterException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent newIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                newIntent.addCategory(Intent.CATEGORY_OPENABLE);
                newIntent.setType("image/png");
                String fileName = "match" + MainActivity.matchNumber + "_team" + MainActivity.teamNumber + ".png";
                newIntent.putExtra(Intent.EXTRA_TITLE, fileName);
                startActivityForResult(newIntent, FILE_EXPORT_REQUEST_CODE);

                ivOutput.setImageBitmap(bitmap);

                break;

        }
    }

    public String bSB (Boolean bool){
        return Boolean.toString(bool).substring(0,1).toUpperCase() + Boolean.toString(bool).substring(1);
    }

}