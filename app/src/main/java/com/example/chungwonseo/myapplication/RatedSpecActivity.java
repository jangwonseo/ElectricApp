package com.example.chungwonseo.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class RatedSpecActivity extends AppCompatActivity {
    EditText ratedCurrent_editText;
    EditText faultCurrent_editText;
    Button nextButton;

    float ratedCurrentValue;
    float faultCurrentValue;

    HashMap<String, Float> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rated_spec);
        map = new HashMap<String, Float>();

        Button nextButton = (Button)findViewById(R.id.ratedSepc_nextBtn);
        ratedCurrent_editText = (EditText)findViewById(R.id.ratedCurrent);
        faultCurrent_editText = (EditText)findViewById(R.id.faultCurrent);


        nextButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ratedCurrent_editText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "입력되지 않은 값이 있습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    ratedCurrentValue = Float.parseFloat(ratedCurrent_editText.getText().toString());
                }
                if(faultCurrent_editText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "입력되지 않은 값이 있습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    faultCurrentValue = Float.parseFloat(faultCurrent_editText.getText().toString());
                }


                map.put("ratedCurrentValue",ratedCurrentValue);
                map.put("faultCurrentValue",faultCurrentValue);

                Intent intent = new Intent(RatedSpecActivity.this,ClipConfiguarationActivity.class);
                intent.putExtra("RatedSpecValue",map);
                startActivity(intent);
            }
        });
    }
}
