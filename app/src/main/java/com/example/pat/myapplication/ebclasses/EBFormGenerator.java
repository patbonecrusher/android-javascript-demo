package com.example.pat.myapplication.ebclasses;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.widget.TextView;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;

public class EBFormGenerator {
    private ConstraintLayout view;
    private Context context;
    public EBFormGenerator(ConstraintLayout view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void drawLabel(Integer x, Integer  y, Integer  height, Integer  width, String label) {
        System.out.printf("x: %d, y: %d, height: %d, width: %d, label: %s\n", x, y, height, width, label);

        TextView ProgrammaticallyTextView = new TextView(this.context);
        ProgrammaticallyTextView.setText(label);
        ProgrammaticallyTextView.setTextSize(22);
        ProgrammaticallyTextView.setPadding(x, y, width, height);
        view.addView(ProgrammaticallyTextView);
    }

    public V8Object constructV8Object(V8 runtime) {
        V8Object v8Generator = new V8Object(runtime);

        // runtime.add("console", v8Console);
        v8Generator.registerJavaMethod(this, "drawLabel", "drawLabel", new Class<?>[] {
                Integer.class,
                Integer.class,
                Integer.class,
                Integer.class,
                String.class,
        });
        return v8Generator;
    }
}