package com.example.steven.calculadoracompleta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private EditText res;
    String ant="";
    boolean van=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res=(EditText)findViewById(R.id.display);
        SharedPreferences pre = getSharedPreferences("datos", Context.MODE_PRIVATE);
        res.setText(pre.getString("dato",""));

    }
    public void obtener(View v){
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


        SharedPreferences prefe = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();
        editor.putString("dato", res.getText().toString());
        editor.commit();
    }
    public void concatenar(View v){
        String aux = res.getText().toString();
        int par=0;
        if(aux.length()==0){
            if(v.getTag().toString().equals("+")||v.getTag().toString().equals("*")||v.getTag().toString().equals("/")||v.getTag().toString().equals(".")||v.getTag().toString().equals("^"))Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            else obtener(v);
        }
        else if(ant.length()==0) obtener(v);
        else{
            if(van){
                if(v.getTag().toString().equals("+")||v.getTag().toString().equals("-")||v.getTag().toString().equals("*")||v.getTag().toString().equals("/")||v.getTag().toString().equals("^")||v.getTag().toString().equals("."))Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                else{
                    obtener(v);
                    van =false;
                }
            }
            else obtener(v);
        }
        ant=v.getTag().toString();
        if(ant.equals("+")||ant.equals("-")||ant.equals("*")||ant.equals("/")||ant.equals("^")||ant.equals(".")) van = true;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.historial) {
            Intent i = new Intent(this, Historial.class );
            startActivity(i);
        }
        else if (id == R.id.about) {
            Intent i = new Intent(this, About.class );
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
    public void grabar(View v) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("operaciones.txt", Activity.MODE_APPEND));
            archivo.write(res.getText().toString()+"\n");
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
        }

    }

}
