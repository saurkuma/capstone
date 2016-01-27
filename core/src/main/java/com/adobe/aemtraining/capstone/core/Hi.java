package com.adobe.aemtraining.capstone.core;

import com.adobe.cq.sightly.WCMUse;

public class Hi extends WCMUse {

	
	
	private String currentMsg;
	
	@Override
	public void activate() throws Exception {
		// String param1 = get("param1", String.class);
		// TODO Auto-generated method stub
		
		currentMsg = "Hello World";

	}
	
	public String getCurrentMsg() {
		 String text = get("text", String.class);
		return currentMsg+text;
	}

}
