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


        //값 계산부분
        currentDensity = (RatedSpecValue.get("ratedCurrentValue")/ClipConfiguarationValue.get("theNumberOfClipValue"))/ClipConfiguarationValue.get("minimumAreaValue");
        if(currentDensity <= 2.5) isConductionPass = "Pass";
        else {
            isConductionPass = "Fail";
        }
        











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
