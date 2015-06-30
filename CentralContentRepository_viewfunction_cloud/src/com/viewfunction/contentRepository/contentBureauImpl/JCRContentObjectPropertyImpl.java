package com.viewfunction.contentRepository.contentBureauImpl;

import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;

public class JCRContentObjectPropertyImpl implements ContentObjectProperty {

	private String propertyName;
	private int propertyType;
	private Object propertyValue;
	private boolean isMultiple=false;	
	
	@Override
	public String getPropertyName() {		
		return this.propertyName;
	}

	@Override
	public int getPropertyType() {		
		return this.propertyType;
	}

	@Override
	public Object getPropertyValue() {		
		return this.propertyValue;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public void setPropertyType(int propertyType) {
		this.propertyType = propertyType;
	}

	public void setPropertyValue(Object propertyValue) {
		this.propertyValue = propertyValue;
	}

	public void setMultiple(boolean isMultiple) {
		this.isMultiple = isMultiple;
	}

	public boolean isMultiple() {
		return this.isMultiple;
	}

}
