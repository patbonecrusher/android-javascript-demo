package com.example.pat.myapplication.ebclasses;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;

public class EBConsole {
    public void log(final String message) {
        System.out.println("[INFO] " + message);
    }
    public void error(final String message) {
        System.out.println("[ERROR] " + message);
    }

    public V8Object constructV8Object(V8 runtime) {
        V8Object v8Generator = new V8Object(runtime);

        v8Generator.registerJavaMethod(this, "log", "log", new Class<?>[] { String.class });
        v8Generator.registerJavaMethod(this, "error", "err", new Class<?>[] { String.class });

        return v8Generator;
    }
}
