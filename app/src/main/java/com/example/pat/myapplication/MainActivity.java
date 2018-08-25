package com.example.pat.myapplication;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.example.pat.myapplication.ebclasses.EBFormGenerator;
import com.example.pat.myapplication.ebclasses.EBConsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


class Console {
    public void log(final String message) {
        System.out.println("[INFO] " + message);
    }
    public void error(final String message) {
        System.out.println("[ERROR] " + message);
    }
}

public class MainActivity extends AppCompatActivity {

    private EBFormGenerator formGenerator;

    public static String readFromAssets(Context context, String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));

        // do reading, usually loop until end of file reading
        StringBuilder sb = new StringBuilder();
        String mLine = reader.readLine();
        while (mLine != null) {
            sb.append(mLine); // process line
            mLine = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final V8 runtime = V8.createV8Runtime();

        final EBFormGenerator formGenerator = new EBFormGenerator((ConstraintLayout) findViewById(R.id.scl), getApplicationContext());
        final V8Object v8FormGen = formGenerator.constructV8Object(runtime);
        runtime.add("formGen", v8FormGen);

        final EBConsole console = new EBConsole();
        final V8Object v8Console = console.constructV8Object(runtime);
        runtime.add("console", v8Console);

        runtime.executeVoidScript(""
                + "var person = {};\n"
                + "var hockeyTeam = {name : 'WolfPack22'};\n"
                + "person.first = 'Ian';\n"
                + "person['last'] = 'Bull';\n"
                + "person.hockeyTeam = hockeyTeam;\n");



        // The UI Has 2 buttons, one invoke a javascript function which invoke a Java call.
        // The console object is a class define above.
        Button clickButton = (Button) findViewById(R.id.button);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("[INFO] " + "running js");
                runtime.executeScript("console.log('hello, world');");

                runtime.executeScript("function f(log) { log.log('hi'); console.log(formGen !== undefined?'yes':'boo');}");
                V8Function f = (V8Function) runtime.executeScript("f");
                V8Array array = new V8Array(runtime).push(v8Console);
                f.call(null, array);

                String drawFormJs = "";
                try {
                    drawFormJs = MainActivity.readFromAssets(getApplicationContext(), "drawForm.js");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // runtime.executeScript("function drawForm(form, log) { log.log(typeof form); form.drawLabel(1,1,1,1,'hi'); }");
                runtime.executeScript(drawFormJs);
                V8Function drawForm = (V8Function) runtime.executeScript("drawForm");
                V8Array v8Args = new V8Array(runtime).push(v8FormGen).push(v8Console).push("<xml></xml>").push("templateForm");
                drawForm.call(null, v8Args);
            }
        });

        // The second button shows how to access data in javascript from Java
        Button clickButton2 = (Button) findViewById(R.id.button2);
        clickButton2.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("[INFO] " + "running js 2");

                V8Object person = runtime.getObject("person");
                System.out.println(person.getString("first"));

                V8Object hockeyTeam = person.getObject("hockeyTeam");
                System.out.println(hockeyTeam.getString("name"));
                person.release();
                hockeyTeam.release();
            }
        });
    }
}

