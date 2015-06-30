package com.viewfunction.contentRepository.contentBureau;

public interface ContentObjectProperty{
	public String getPropertyName();	
	public void setPropertyName(String propertyName);
	public int getPropertyType();
	public void setPropertyType(int propertyType);	
	public Object getPropertyValue() ;
	public void setPropertyValue(Object propertyValue);
	public void setMultiple(boolean isMultiple);
	public boolean isMultiple();
}
