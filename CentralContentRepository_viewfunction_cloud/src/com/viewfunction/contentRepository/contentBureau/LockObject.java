package com.viewfunction.contentRepository.contentBureau;

import java.util.Date;

public interface LockObject {
	public String getLocker();
	public boolean 	isTemporaryLock();	
	public Date getLockDate();
}