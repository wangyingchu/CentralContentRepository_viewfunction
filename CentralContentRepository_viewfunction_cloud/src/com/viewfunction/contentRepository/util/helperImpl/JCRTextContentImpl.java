package com.viewfunction.contentRepository.util.helperImpl;

import com.viewfunction.contentRepository.util.helper.TextContent;

public class JCRTextContentImpl extends JCRBinaryContentImpl implements TextContent{
	private String encoding;
	@Override
	public String getEncoding() {		
		return this.encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	
	
	
	

}
