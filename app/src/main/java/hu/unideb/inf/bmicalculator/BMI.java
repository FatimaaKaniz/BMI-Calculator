package hu.unideb.inf.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BMI extends AppCompatActivity {

    private TextView tv;
    private SensorManager sensorManager;
    private ShakeSensor shakeListner = new ShakeSensor();
    private Sensor shakeSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_m_i);

        tv = findViewById(R.id.shake);
        final EditText edWeg,edHei;
        final TextView txtRes,txtInter,txtCon;
        final Button btnRes,btnReset;

        edWeg = (EditText) findViewById(R.id.edweg);
        edHei = (EditText) findViewById(R.id.edhei);

        txtInter = (TextView) findViewById(R.id.txtinter);
        txtRes = (TextView) findViewById(R.id.txtres);
        txtCon=(TextView)findViewById(R.id.txtCon);

        btnRes = (Button) findViewById(R.id.btnres);
        btnReset = (Button) findViewById(R.id.btnreset);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        shakeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(shakeListner, shakeSensor, SensorManager.SENSOR_DELAY_GAME);
        shakeListner.setTv(tv);
        final Activity act = this;
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals(getString(R.string.shaken))){
                   if(txtCon.getText().toString().trim().equals("")){
                       Toast.makeText(getApplicationContext(),"Please Calculate BMI First!!",Toast.LENGTH_SHORT).show();
                   }else{
                       String message="Condition: "+txtCon.getText().toString().trim()+"\nBMI: "+txtRes.getText().toString().trim()+".";
                       Intent share = new Intent(Intent.ACTION_SEND);
                       share.setType("text/plain");

                       share.putExtra(Intent.EXTRA_TEXT, message);
                       startActivity(Intent.createChooser(share, "Share to"));
                   }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strweg = edWeg.getText().toString();
                String strhei = edHei.getText().toString();

                if (strweg.equals("")) {
                    edWeg.setError("Please enter your weight ");
                    edWeg.requestFocus();
                    return;
                }
                if (strhei.equals("")) {
                    edHei.setError("Please enter your height ");
                    edHei.requestFocus();
                    return;
                }
                float weight = Float.parseFloat(strweg);
                float height = Float.parseFloat(strhei)/100;

                float bmiValue = BMICalculate(weight, height);
                txtCon.setText(interpreteBMI(bmiValue));
                txtRes.setText("  BMI= " + bmiValue);
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edHei.setText("");
                edWeg.setText("");
                txtInter.setText("");
                txtRes.setText("");
                txtCon.setText("");
            }
        });


    }

    public float BMICalculate(float weight, float height) {
        return weight / (height * height);
    }
    public String interpreteBMI(float bmiValue){
        if (bmiValue < 16) {
            return "Severely Underweight";
        } else if (bmiValue < 18.5) {
            return "Underweight";
        } else if (bmiValue < 25) {
            return "Normal";
        } else if (bmiValue < 30) {
            return "Overweight";
        } else
            return "Obese";
    }

}