package com.example.chungwonseo.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class ClipConfiguarationActivity extends AppCompatActivity {
    EditText springArray_editText;
    EditText theNumberOfClip_editText;
    EditText minimumArea_editText;
    EditText clipLength_editText;
    EditText clipRadius_editText;
    EditText clipDistance_editText;
    Button nextButton;

    float springArrayValue;
    float theNumberOfClipValue;
    float minimumAreaValue;
    float clipLengthValue;
    float clipRadiusValue;
    float clipDistanceValue;

    HashMap<String, Float> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_configuaration);
        map = new HashMap<String, Float>();

        Button nextButton = (Button)findViewById(R.id.clipConfig_nextBtn);
        springArray_editText = (EditText)findViewById(R.id.springArray);
        theNumberOfClip_editText = (EditText)findViewById(R.id.theNumberOfClip);
        minimumArea_editText = (EditText)findViewById(R.id.minimumArea);
        clipLength_editText = (EditText)findViewById(R.id.clipLength);
        clipRadius_editText = (EditText)findViewById(R.id.clipRadius);
        clipDistance_editText = (EditText)findViewById(R.id.clipDistance);


        nextButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                springArrayValue = Float.parseFloat(springArray_editText.getText().toString());
                theNumberOfClipValue = Float.parseFloat(theNumberOfClip_editText.getText().toString());
                minimumAreaValue = Float.parseFloat(minimumArea_editText.getText().toString());
                clipLengthValue = Float.parseFloat(clipLength_editText.getText().toString());
                clipRadiusValue = Float.parseFloat(clipRadius_editText.getText().toString());
                clipDistanceValue = Float.parseFloat(clipDistance_editText.getText().toString());

                map.put("springArrayValue",springArrayValue);
                map.put("theNumberOfClipValue",theNumberOfClipValue);
                map.put("minimumAreaValue",minimumAreaValue);
                map.put("clipLengthValue",clipLengthValue);
                map.put("clipRadiusValue",clipRadiusValue);
                map.put("clipDistanceValue",clipDistanceValue);

                Intent getIntent = getIntent();
                HashMap<String, Float> RatedSpecValue = (HashMap<String, Float>) getIntent.getSerializableExtra("RatedSpecValue");

                Intent intent = new Intent(ClipConfiguarationActivity.this,MaterialSpecActivity.class);
                intent.putExtra("RatedSpecValue",RatedSpecValue);
                intent.putExtra("ClipConfiguarationValue",map);
                startActivity(intent);
            }
        });








    }
}