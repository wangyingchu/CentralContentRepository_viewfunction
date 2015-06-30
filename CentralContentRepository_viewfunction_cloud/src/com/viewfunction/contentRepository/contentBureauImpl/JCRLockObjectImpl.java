package com.viewfunction.contentRepository.contentBureauImpl;

import java.util.Date;

import javax.jcr.lock.Lock;

import com.viewfunction.contentRepository.contentBureau.LockObject;

public class JCRLockObjectImpl implements LockObject{
	
	private Lock lockData;	
	private String lockOwner;
	private Date lockDate;
	private boolean temporaryLock;
	@Override
	public String getLocker() {
		if(lockOwner!=null){
			return lockOwner;
		}else{
			return this.lockData.getLockOwner();
		}		
	}
	
	public void setLocker(String lockOwner) {
		this.lockOwner = lockOwner;
	}

	public void setLockData(Object LockData) {
		this.lockData=(Lock)LockData;		
	}

	
	@Override
	public boolean isTemporaryLock() {
		return this.temporaryLock;
	}

	@Override
	public Date getLockDate() {
		return this.lockDate;
	}

	public void setLockDate(Date lockDate) {
		this.lockDate = lockDate;
	}

	public void setTemporaryLock(boolean temporaryLock) {
		this.temporaryLock = temporaryLock;
	}	
}
