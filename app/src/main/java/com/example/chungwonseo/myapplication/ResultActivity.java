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

    public float validElasticModule; //유효탄성계수
    public float peakFaultCurrent; //피크전류
    public float Rx;
    public float Ry;
    public float Rc;
    public float c;
    public float BDivisionA;  // b/a
    public float a;
    public float b;


    public float currentDensity; //전류밀도
    public String isConductionPass; //통전성능판정(pass 인가?)

    public float contactForce; //접촉력
    public float springForce; //스프링력
    public float attractiveForce; //단자간 인력
    public float repulsiveForce; //전자 반발력

    public float contactRadius1; //접촉면-a
    public float contactRadius2; //접촉면-b
    public float contactResistance; //접촉저항
    public float totalResistance; //전체 접촉저항

    public float meltingCurrent_stop; //용착전류(정지시)
    public float meltingCurrent_vib; //용착전류(시작시)
    public String isMeltingCurrentPass; //용착전류 판정( pass인가?)

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


        //접촉면
        contactRadius1 = a;
        contactRadius2 = b;
        contactResistance = 0;







        String[] LIST_MENU = {"전류밀도 : "+currentDensity, "통전성능판정 : "+isConductionPass, "LIST3"};
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
