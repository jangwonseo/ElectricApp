package com.example.chungwonseo.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

public class MaterialSpecActivity extends AppCompatActivity {
    ArrayAdapter<CharSequence> adspin;

    EditText spring_editText;
    EditText busbar_editText;
    Button nextButton;

    float springValue;
    float busbarValue;
    float materialElasticity; //탄성계수
    float materialPoisson; //포아송비
    float materialResistivity; //저항률
    float materialVoltageDrop; //전압강하

    HashMap<String, Float> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_spec);

        map = new HashMap<String, Float>();

        nextButton = (Button)findViewById(R.id.materialSpec_nextBtn);
        spring_editText = (EditText)findViewById(R.id.spring);
        busbar_editText = (EditText)findViewById(R.id.busbar);

        Spinner spinner = (Spinner) findViewById(R.id.materialSpinner);
        spinner.setPrompt("선택하시오");

        adspin = ArrayAdapter.createFromResource(this, R.array.selected, android.R.layout.simple_spinner_item);

        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adspin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String materialStr = (String) parent.getItemAtPosition(position);
                switch (materialStr){
                    //구리, 철, 알루미늄, 은 각각의 고유 값들을 입력한다.
                    case "구리":
                        materialElasticity = 11900.00f;
                        materialPoisson = 0.326f;
                        materialResistivity = 17.8f;
                        materialVoltageDrop = 0.43f;
                        break;
                    case "철":
                        materialElasticity = 11900.00f;
                        materialPoisson = 0.326f;
                        materialResistivity = 17.8f;
                        materialVoltageDrop = 0.43f;
                        break;
                    case "알루미늄":
                        materialElasticity = 11900.00f;
                        materialPoisson = 0.326f;
                        materialResistivity = 17.8f;
                        materialVoltageDrop = 0.43f;
                        break;
                    case "은":
                        materialElasticity = 11900.00f;
                        materialPoisson = 0.326f;
                        materialResistivity = 17.8f;
                        materialVoltageDrop = 0.43f;
                        break;
                    default:
                        Toast.makeText(MaterialSpecActivity.this, "선택 물질이 잘 못 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        nextButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                springValue = Float.parseFloat(spring_editText.getText().toString());
                busbarValue = Float.parseFloat(busbar_editText.getText().toString());

                map.put("springValue",springValue);
                map.put("busbarValue",busbarValue);
                map.put("materialElasticity",materialElasticity);
                map.put("materialPoisson",materialPoisson);
                map.put("materialResistivity",materialResistivity);
                map.put("materialVoltageDrop",materialVoltageDrop);

                Intent getIntent = getIntent();
                HashMap<String, Float> RatedSpecValue = (HashMap<String, Float>) getIntent.getSerializableExtra("RatedSpecValue");
                HashMap<String, Float> ClipConfiguarationValue = (HashMap<String, Float>) getIntent.getSerializableExtra("ClipConfiguarationValue");

                Intent intent = new Intent(MaterialSpecActivity.this,ResultActivity.class);
                intent.putExtra("RatedSpecValue",RatedSpecValue);
                intent.putExtra("ClipConfiguarationValue",ClipConfiguarationValue);
                intent.putExtra("MaterialSpecValue",map);
                startActivity(intent);
            }
        });

    }
}
