package com.viewfunction.contentRepository.util.observationImpl;

import java.util.Date;

import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;

import com.viewfunction.contentRepository.util.observation.ContentSpaceEventListener;

public class JCRDefaultContentSpaceEventListenerImpl implements
		ContentSpaceEventListener {
	@Override
	public void onEvent(EventIterator ei) {

		while (ei.hasNext()) {
			Event ce = ei.nextEvent();			
			try {			
				if(ce.getPath().contains("jcr")){
					break;
				}
				switch(ce.getType()){
					case Event.NODE_ADDED:System.out.print("NODE_ADDED");break;
					case Event.NODE_MOVED:System.out.print("NODE_MOVED");break;
					case Event.NODE_REMOVED:System.out.print("NODE_REMOVED");break;
					case Event.PERSIST:System.out.print("PERSIST");break;
					case Event.PROPERTY_ADDED:System.out.print("PROPERTY_ADDED");break;
					case Event.PROPERTY_CHANGED:System.out.print("PROPERTY_CHANGED");break;
					case Event.PROPERTY_REMOVED:System.out.print("PROPERTY_REMOVED");break;				
				}				
				System.out.println(" At :"+new Date(ce.getDate()).toLocaleString()+",by :"+ce.getUserID());				
				System.out.println("Path :"+ce.getPath());
				
				//System.out.println(ce.getUserData());
				//System.out.println(ce.getInfo());
				
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	

		}

	}

}
