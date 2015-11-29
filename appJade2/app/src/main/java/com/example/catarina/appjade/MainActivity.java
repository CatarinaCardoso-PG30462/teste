package com.example.catarina.appjade;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {

    EditText agentNameField;
    EditText hostField;
    EditText portField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences("androidJade",
                0);

        String host = settings.getString("defaultHost", "");
        String port = settings.getString("defaultPort", "");
        String name= settings.getString("defaultName","");

        setContentView(R.layout.activity_main);

        agentNameField = (EditText)findViewById(R.id.edit_name);
        agentNameField.setText(name);

        hostField = (EditText)findViewById(R.id.edit_host);
        hostField.setText(host);

        portField = (EditText) findViewById(R.id.edit_port);
        portField.setText(port);

        Button button = (Button) findViewById(R.id.button_next);
        button.setOnClickListener(buttonUseListener);

    }

    private OnClickListener buttonUseListener = new OnClickListener() {
        public void onClick(View v) {
            SharedPreferences settings = getSharedPreferences(
                    "androidJade", 0);

            // TODO: Verify that edited parameters was formally correct
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("defaultHost", hostField.getText().toString());
            editor.putString("defaultPort", portField.getText().toString());
            editor.putString("defaultName", agentNameField.getText().toString());
            editor.commit();
            SharedPreferences f=getSharedPreferences("androidJade",0);
            String host = settings.getString("defaultHost", "");
            String port = settings.getString("defaultPort", "");
            String name= settings.getString("defaultName", "");
            Intent intent=new Intent(MainActivity.this,ActivityAgent.class);
            startActivity(intent);
        }
    };
}
