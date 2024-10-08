package com.umatech;
import com.umatech.TomcatApplication.AutoScan.TomcatApplication;
import com.umatech.TomcatApplication.TomcatApplicationRunner;

@TomcatApplication
public class Main {
    public static void main(String[] args) throws Exception {
        TomcatApplicationRunner.run(Main.class, args);
    }

}