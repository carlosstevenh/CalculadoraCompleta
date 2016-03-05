package com.example.steven.calculadoracompleta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res=(EditText)findViewById(R.id.display);
    }
    public void concatenar(View v){
        if(v.getTag().toString().equals("borrar")){
            String cad=res.getText().toString();
            String borrar="";
            int aux = cad.length();
            for(int i=0;i<aux-1;i++){
                borrar=borrar+cad.charAt(i);
            }
            res.setText(borrar);
        }
        else if(v.getTag().toString().equals("bt"))res.setText("");
        else res.setText(res.getText()+v.getTag().toString());
    }
}
