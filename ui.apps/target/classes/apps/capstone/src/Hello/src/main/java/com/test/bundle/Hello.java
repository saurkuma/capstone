package com.test.bundle;
import com.test.service;

public class Hello{

    public String getBundleMsg(){

        HelloService hs=new HelloService();

		return "Hi from Bundasdle"+hs.getServiceMsg();
    }
}