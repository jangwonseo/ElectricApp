package com.example.chungwonseo.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    public static final float R1 = 100000000.00f;
    public static final float R2 = 100000000.00f;

    public float validElasticModule = 0.0f; //유효탄성계수
    public float peakFaultCurrent = 0.0f; //피크전류
    public float Rx = 0.0f;
    public float Ry = 0.0f;
    public float Rc = 0.0f;
    public float c = 0.0f;
    public float BDivisionA = 0.0f;  // b/a
    public float a = 0.0f;
    public float b = 0.0f;
    public float alphaa = 0.0f;
    public float FDivision4ab = 0.0f;
    public float FDivision4abMinus20 = 0.0f;
    public float k = 0.0f;


    public float currentDensity = 0.0f; //전류밀도
    public String isConductionPass = null; //통전성능판정(pass 인가?)

    public float contactForce = 0.0f; //접촉력
    public float springForce = 0.0f; //스프링력
    public float attractiveForce = 0.0f; //단자간 인력
    public float repulsiveForce = 0.0f; //전자 반발력

    public float contactRadius1 = 0.0f; //접촉면-a
    public float contactRadius2 = 0.0f; //접촉면-b
    public float contactResistance = 0.0f; //접촉저항
    public float totalResistance = 0.0f; //전체 접촉저항

    public float meltingCurrent_stop = 0.0f; //용착전류(정지시)
    public float meltingCurrent_vib = 0.0f; //용착전류(시작시)
    public String isMeltingCurrentPass = null; //용착전류 판정( pass인가?)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent getIntent = getIntent();
        HashMap<String, Float> RatedSpecValue = (HashMap<String, Float>) getIntent.getSerializableExtra("RatedSpecValue");
        HashMap<String, Float> ClipConfiguarationValue = (HashMap<String, Float>) getIntent.getSerializableExtra("ClipConfiguarationValue");
        HashMap<String, Float> MaterialSpecValue = (HashMap<String, Float>) getIntent.getSerializableExtra("MaterialSpecValue");


        System.out.println("Radiusvalue : "+ClipConfiguarationValue.get("clipRadiusValue"));


        /*값 계산부분*/

        //맨 하단 부분
        peakFaultCurrent = RatedSpecValue.get("faultCurrentValue") * 2.6f;
        validElasticModule = (float)Math.pow(((1 - Math.pow(MaterialSpecValue.get("materialPoissonValue"),2)) / MaterialSpecValue.get("materialElasticityValue")) + ((1 - Math.pow(MaterialSpecValue.get("materialPoissonValue"),2)) / MaterialSpecValue.get("materialElasticityValue")),-1);
        Rx = (1 / R1) + (1 / ClipConfiguarationValue.get("clipRadiusValue"));
        Ry = (1 / R2) + (1 / (MaterialSpecValue.get("busbarRadiusValue") / 2));
        Rc = (float) Math.pow((Rx * Ry * ((Rx+Ry) / 2)), -(1/3));
        BDivisionA = (float) Math.pow((Ry/Rx),(2/3));


        //통전성능
        currentDensity = (RatedSpecValue.get("ratedCurrentValue")/ClipConfiguarationValue.get("theNumberOfClipValue"))/ClipConfiguarationValue.get("minimumAreaValue");
        if(currentDensity <= 2.5) isConductionPass = "Pass";
        else {
            isConductionPass = "Fail";
        }

        //접촉력
        springForce = (float)(2 * Math.sin(Math.PI/ClipConfiguarationValue.get("theNumberOfClipValue")) * MaterialSpecValue.get("busbarRadiusValue") * ClipConfiguarationValue.get("springArrayValue"));
        attractiveForce = (float)(((Math.pow(peakFaultCurrent,2) * ClipConfiguarationValue.get("clipLengthValue")) / (ClipConfiguarationValue.get("theNumberOfClipValue") * ClipConfiguarationValue.get("clipDistanceValue"))) / 9.8);
        repulsiveForce = (float) ((0.5 * Math.pow((peakFaultCurrent / ClipConfiguarationValue.get("theNumberOfClipValue")),2)) / 9.8);
        contactForce = springForce + attractiveForce - repulsiveForce;

        //맨 하단 부분
        c = (float) Math.pow(((0.75 * contactForce * Rc) / validElasticModule) , (1/3));
        a = (float) (c / Math.sqrt(BDivisionA));
        b = (float) (c * Math.sqrt(BDivisionA));
        alphaa = (float) (0.5 * ((Rx * Math.pow(a,2)) + (Ry * Math.pow(b,2))));
        FDivision4ab = (contactForce / (4 * a * b));
        FDivision4abMinus20 = FDivision4ab - 20;
        if(FDivision4ab >= 20 && FDivision4ab != 0.0f){
            k = 2.3f;
        }else{
            k = (float) (2.3 + ((0.000045 * Math.pow(FDivision4abMinus20,4)) + (0.006 * (Math.pow(FDivision4abMinus20,2)))));
        }


        //접촉면
        contactRadius1 = a;
        contactRadius2 = b;
        contactResistance = (float)(k * ((MaterialSpecValue.get("materialResistivityValue") + MaterialSpecValue.get("materialResistivityValue")) / (2 * Math.PI * b) * Math.log(4 * (2 * b) / (2 * a))));
        totalResistance = ((b * 2) / ClipConfiguarationValue.get("theNumberOfClipValue"));

        //용착
        meltingCurrent_stop = ClipConfiguarationValue.get("theNumberOfClipValue") * (MaterialSpecValue.get("materialVoltageDropValue") / contactResistance) * 1000;
        meltingCurrent_vib = (float) (meltingCurrent_stop * 0.65);
        if((meltingCurrent_stop > peakFaultCurrent) && (meltingCurrent_vib > peakFaultCurrent)){
            isMeltingCurrentPass = "Pass";
        }else{
            isMeltingCurrentPass = "Fail";
        }





        String[] LIST_MENU = {"전류밀도 : "+currentDensity, "통전성능판정 : "+isConductionPass, "접촉력 : "+contactForce, "스프링력 : "+springForce, "단자간인력 : "+attractiveForce, "전자반발력 : "+repulsiveForce, "접촉면 a : "+contactRadius1, "접촉면 b : "+contactRadius2, "접촉저항 : "+contactResistance, "전체 접촉저항 : "+totalResistance, "용착전류(정지시) : "+meltingCurrent_stop, "용착전류(진동시) : "+meltingCurrent_vib, "용착판정 : "+isMeltingCurrentPass};
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);

        ListView listview = (ListView) findViewById(R.id.resultList);
        listview.setAdapter(adapter);

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id) {
//
//                // get TextView's Text.
//                String strText = (String) parent.getItemAtPosition(position) ;
//
//                // TODO : use strText
//            }
//        }) ;
    }
}
