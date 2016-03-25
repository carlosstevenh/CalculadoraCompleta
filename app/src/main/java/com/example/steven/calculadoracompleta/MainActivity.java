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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private EditText res;
    private EditText prueba;
    String ant="";//verificar lo ultimo q se digito
    boolean van=false;//si hay operador de ultimo
    int par=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res=(EditText)findViewById(R.id.display);
        prueba=(EditText)findViewById(R.id.prueba);
        SharedPreferences pre = getSharedPreferences("datos", Context.MODE_PRIVATE);
        res.setText(pre.getString("dato",""));

    }
    public void obtener(View v){
        if(v.getTag().toString().equals("borrar")){
            int con=0;
            String cad=res.getText().toString();
            String borrar="";
            int aux = cad.length();
            for(int i=0;i<aux-1;i++){
                borrar=borrar+cad.charAt(i);
                con=i;
            }
            if(cad.charAt(con+1)=='(')par--;
            else if(cad.charAt(con+1)==')')par++;
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
        String aux = res.getText().toString();//si tiene algo escrito en la calculadora
        if(v.getTag().toString().equals("(")){
            par++;
            obtener(v);
        }
        else if(aux.length()==0){
            if(v.getTag().toString().equals(")")||v.getTag().toString().equals("+")||v.getTag().toString().equals("*")||v.getTag().toString().equals("/")||v.getTag().toString().equals(".")||v.getTag().toString().equals("^"))Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            else obtener(v);
        }
        else if(ant.length()==0) obtener(v);
        else{
            if(van){
                if(v.getTag().toString().equals(")")||v.getTag().toString().equals("+")||v.getTag().toString().equals("-")||v.getTag().toString().equals("*")||v.getTag().toString().equals("/")||v.getTag().toString().equals("^")||v.getTag().toString().equals("."))Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                else{
                    obtener(v);
                    van =false;
                }
            }
            else {
                if(v.getTag().toString().equals(")"))par--;
                obtener(v);
            }
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
        if(par==0){
            try {
                OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("operaciones.txt", Activity.MODE_APPEND));
                archivo.write(res.getText().toString() + "\n");
                archivo.flush();
                archivo.close();

                int dig=calcular(res.getText().toString());

            } catch (IOException e) {
            }

        }
        else Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }
    private int calcular(String s){
        int resultado=0;
        Stack<String> ope = new Stack<String>();
        Stack<String> numero = new Stack<String>();
        int pro=0;
        String aux="";
        boolean van = true;

        for(int i=0;i<s.length();i++){

            if(s.charAt(i)=='0'||s.charAt(i)=='1'||s.charAt(i)=='2'||s.charAt(i)=='3'||s.charAt(i)=='4'||
                    s.charAt(i)=='5'||s.charAt(i)=='6'||s.charAt(i)=='7'||s.charAt(i)=='8'||s.charAt(i)=='9'){
                aux+=s.charAt(i);
            }
            else {
                numero.push(aux);
                if(ope.empty())ope.push(""+s.charAt(i));
                else{
                    String ult=ope.peek();
                    if(s.charAt(i)=='+'||s.charAt(i)=='-') pro=1;
                    else if(s.charAt(i)=='*'||s.charAt(i)=='/')pro=2;
                    else if(s.charAt(i)=='^')pro=3;
                    else if(s.charAt(i)==')')pro=4;
                    else if(s.charAt(i)=='(')pro=5;

                    if(ult.equals("+")||ult.equals("-")){
                        if(pro==1){
                            numero.push(ope.pop());
                            ope.push(""+s.charAt(i));
                        }
                        else if(pro==2||pro==3||pro==5)ope.push(""+s.charAt(i));
                        else{
                            while(!ope.empty()&&van){
                                if(!ope.peek().equals("("))numero.push(ope.pop());
                                else{
                                    ope.pop();
                                    van=false;
                                }
                            }
                        }

                    }
                    else if(ult.equals("*")||ult.equals("/")){
                        if(pro==1){
                            while(!ope.empty()&&van){
                                if(ope.peek().equals("(")) van=false;
                                else numero.push(ope.pop());
                            }
                            ope.push(""+s.charAt(i));
                        }
                        else if(pro==2){
                            numero.push(ope.pop());
                            ope.push(""+s.charAt(i));
                        }
                        else if(pro==3||pro==5)ope.push(""+s.charAt(i));
                        else{
                            while(!ope.empty()&&van){
                                if(!ope.peek().equals("("))numero.push(ope.pop());
                                else{
                                    ope.pop();
                                    van=false;
                                }
                            }
                        }

                    }
                    else if(ult.equals("^")){
                        if(pro<=2){
                            while(!ope.empty()&&van){
                                if(ope.peek().equals("(")) van=false;
                                else numero.push(ope.pop());
                            }
                        }
                        else if(pro ==3){
                            numero.push(ope.pop());
                            ope.push(""+s.charAt(i));
                        }
                        else if(pro==5)ope.push(""+s.charAt(i));
                        else{
                            while(!ope.empty()&&van){
                                if(!ope.peek().equals("("))numero.push(ope.pop());
                                else{
                                    ope.pop();
                                    van=false;
                                }
                            }
                        }
                    }
                    else if(ult.equals("("))ope.push(""+s.charAt(i));

                }
                aux = "";
                van=true;

            }
        }
        numero.push(aux);
        String richar="";
        if(!ope.empty()){
            while(!ope.empty()){
                if(!ope.peek().equals("("))numero.push(ope.pop());
                else ope.pop();
            }
        }

        while(!numero.empty())richar+=numero.pop();
        prueba.setText(richar);

        return resultado;
    }

}
