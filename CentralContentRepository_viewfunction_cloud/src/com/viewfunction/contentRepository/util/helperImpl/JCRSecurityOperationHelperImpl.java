package com.viewfunction.contentRepository.util.helperImpl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.exception.LdapException;
import org.apache.directory.ldap.client.api.message.CompareResponse;
import org.apache.directory.ldap.client.api.message.SearchResponse;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.PermissionObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentObjectImpl;
import com.viewfunction.contentRepository.util.PerportyHandler;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.exception.ContentReposityLoginException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.SecurityOperationHelper;

public class JCRSecurityOperationHelperImpl implements SecurityOperationHelper{
	
	private static final String base64code = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789" + "+/";
	private static final int splitLinesAt = 76;
	private static final String LDAPEncodePerfix="{SHA}";
	private static final String dnPostfix=",organizationName=viewfunctionTec,dc=viewfunction,dc=org";
	
	@Override
	public boolean LDAPUserAuthentication(String userName, String userGroup,String userPWD) throws ContentReposityLoginException{
		try {
			String USER_AUTHENTICATION_LDAP_SERVER_ADDRESS=PerportyHandler.getPerportyValue(PerportyHandler.USER_AUTHENTICATION_LDAP_SERVER_ADDRESS); 
			int USER_AUTHENTICATION_LDAP_SERVICE_PORT=Integer.parseInt(PerportyHandler.getPerportyValue(PerportyHandler.USER_AUTHENTICATION_LDAP_SERVICE_PORT));
			LdapConnection connection = new LdapConnection( USER_AUTHENTICATION_LDAP_SERVER_ADDRESS, USER_AUTHENTICATION_LDAP_SERVICE_PORT );			
			connection.bind();			
			String dn="cn="+userName+"+sn="+userName+",ou="+userGroup+dnPostfix;			
			SearchResponse sr=connection.lookup(dn);
			if(sr==null){				
				return false;
			}			
		    String encodedUserPWD = LDAPEncodePerfix+SHA1Base64(userPWD).trim();		  
		    CompareResponse cr=connection.compare(dn, "userPassword", encodedUserPWD.getBytes());		   
		    boolean authenticationSuccessGlag=cr.isTrue();
			connection.close();
			return authenticationSuccessGlag;			
		} catch (LdapException e) {
			ContentReposityLoginException cpe=new ContentReposityLoginException();
			cpe.initCause(e);
			throw cpe;			
		} catch (Exception e) {
			ContentReposityLoginException cpe=new ContentReposityLoginException();
			cpe.initCause(e);
			throw cpe;
		}				
	}	
	
	/*
	public static void main(String[] args) throws IOException{		
		try {			
			//System.out.println(new JCRSecurityOperationHelperImpl().LDAPUserAuthentication("root","sysAdministrator","viewfunctionroot"));			
			LdapConnection connection = new LdapConnection( "localhost", 10389 );			
			//connection.bind( "ou=sysAdministrator,organizationName=viewfunctionTec,dc=viewfunction,dc=org", "secret" );
			connection.bind();			
			String dn="cn=root+sn=root,ou=sysAdministrator,organizationName=viewfunctionTec,dc=viewfunction,dc=org";
		    Cursor<SearchResponse> cursor = connection.search(dn, "(objectclass=*)", SearchScope.OBJECT, "*" );		    
		    Iterator<SearchResponse> sri=cursor.iterator();		    
		    while(sri.hasNext()){
		    	SearchResponse csr=sri.next();		    	
		    	System.out.println(csr);	    	
		    }		    
		  DN dn_0=new DN(dn);
		  BindRequest br=new BindRequest();
		//  connection.bind(dn_0);		    
		    SearchResponse sr=connection.lookup(dn);		    
		    System.out.println(sr);		    
		           
		    String sha1_ad2 = LDAPEncodePerfix+SHA1Base64("viewfunctionroot").trim();		    
		    System.out.println(sha1_ad2);		    
		    //"{SHA}H23mhFo3cQc/Mg86GovFOr/qcHc="		    
		    CompareResponse cr=connection.compare(dn, "userPassword", sha1_ad2.getBytes());		    
		    System.out.println(cr.isTrue());
		   		
			connection.close();			
		} catch (LdapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	*/
    private static String convertToHex(byte[] data) { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
 
    public static String SHA1Hex(String text)throws NoSuchAlgorithmException, UnsupportedEncodingException  { 
    	MessageDigest md;
    	md = MessageDigest.getInstance("SHA-1");
    	byte[] sha1hash = new byte[40];
    	md.update(text.getBytes("iso-8859-1"), 0, text.length());
    	sha1hash = md.digest();     	
    	return convertToHex(sha1hash);
    }
    
    public static String SHA1Base64(String text)throws NoSuchAlgorithmException, UnsupportedEncodingException  { 
    	MessageDigest md;
    	md = MessageDigest.getInstance("SHA-1");
    	byte[] sha1hash = new byte[40];
    	md.update(text.getBytes("iso-8859-1"), 0, text.length());
    	sha1hash = md.digest();    	
    	return	encodeBase64(sha1hash);
    }    

	private static byte[] zeroPad(int length, byte[] bytes) {
	    byte[] padded = new byte[length]; // initialized to zero by JVM
	    System.arraycopy(bytes, 0, padded, 0, bytes.length);
	    return padded;
	}
	
	private static String encodeBase64(byte[] ba) {	
	    String encoded = "";
	    // determine how many padding bytes to add to the output
	    int paddingCount = (3 - (ba.length % 3)) % 3;
	    // add any necessary padding to the input
	    ba = zeroPad(ba.length + paddingCount, ba);
	    // process 3 bytes at a time, churning out 4 output bytes
	    // worry about CRLF insertions later
	    for (int i = 0; i < ba.length; i += 3) {
	        int j = ((ba[i] & 0xff) << 16) +
	            ((ba[i + 1] & 0xff) << 8) + 
	            (ba[i + 2] & 0xff);
	        encoded = encoded + base64code.charAt((j >> 18) & 0x3f) +
	            base64code.charAt((j >> 12) & 0x3f) +
	            base64code.charAt((j >> 6) & 0x3f) +
	            base64code.charAt(j & 0x3f);
	    }
	    // replace encoded padding nulls with "="
	    return splitLines(encoded.substring(0, encoded.length() -
	        paddingCount) + "==".substring(0, paddingCount));	
	}
	
	private static String splitLines(String string) {
	    String lines = "";
	    for (int i = 0; i < string.length(); i += splitLinesAt) {
	        lines += string.substring(i, Math.min(string.length(), i + splitLinesAt));
	        lines += "\r\n";	
	    }
	    return lines;
	}

	@Override
	public List<PermissionObject> getContentPermissions(BaseContentObject contentObject) throws ContentReposityException{		
		List<PermissionObject> permissionList=new ArrayList<PermissionObject>();		
		try {
			JCRContentObjectImpl contentObjectImpe=(JCRContentObjectImpl)contentObject;
			Node contentObjectImpe_jcrNode=contentObjectImpe.getJcrNode();
			//Owner permission
			PermissionObject ownerPermission=ContentComponentFactory.createPermissionObject();
			ownerPermission.setPermissionScope(PermissionObject.PermissionScope_Owner);
			ownerPermission.setPermissionParticipant(PermissionObject.PermissionScope_Owner);
			if(contentObjectImpe_jcrNode.hasProperty("vfcr:contentOwnerPermission")){
				String permissionCode=contentObjectImpe_jcrNode.getProperty("vfcr:contentOwnerPermission").getString();
				setPermission(permissionCode,ownerPermission);
			}else{
				ownerPermission.setDisplayContentPermission(true);
				ownerPermission.setAddContentPermission(true);
				ownerPermission.setDeleteContentPermission(true);
				ownerPermission.setEditContentPermission(true);
				ownerPermission.setConfigPermissionPermission(true);
				ownerPermission.setAddSubFolderPermission(true);
				ownerPermission.setDeleteSubFolderPermission(true);				
			}
			permissionList.add(ownerPermission);			
			//Other permission
			PermissionObject otherPermission=ContentComponentFactory.createPermissionObject();			
			otherPermission.setPermissionScope(PermissionObject.PermissionScope_Other);
			otherPermission.setPermissionParticipant(PermissionObject.PermissionScope_Other);
			if(contentObjectImpe_jcrNode.hasProperty("vfcr:contentOtherPermission")){
				String permissionCode=contentObjectImpe_jcrNode.getProperty("vfcr:contentOtherPermission").getString();
				setPermission(permissionCode,otherPermission);
			}else{
				otherPermission.setDisplayContentPermission(true);
				otherPermission.setAddContentPermission(true);
				otherPermission.setDeleteContentPermission(true);
				otherPermission.setEditContentPermission(true);
				otherPermission.setConfigPermissionPermission(true);	
				otherPermission.setAddSubFolderPermission(true);
				otherPermission.setDeleteSubFolderPermission(true);
			}
			permissionList.add(otherPermission);
			//group permission
			if(contentObjectImpe_jcrNode.hasProperty("vfcr:contentGroupPermission")){
				Property p=contentObjectImpe_jcrNode.getProperty("vfcr:contentGroupPermission");				
				Value[] bva=p.getValues();
				String[] groupPermissionArray=new String[bva.length];
				for(int i=0;i<bva.length;i++){
					groupPermissionArray[i]=bva[i].getString();									
				} 			
				for(String groupPermissionCode:groupPermissionArray){		
					//groupPermissionCode format DADEC:groupName	
					String permissionCode=groupPermissionCode.substring(0,5);
					String permissionGroup=groupPermissionCode.substring(6,groupPermissionCode.length());					
					PermissionObject groupPermission=ContentComponentFactory.createPermissionObject();
					groupPermission.setPermissionParticipant(permissionGroup);
					groupPermission.setPermissionScope(PermissionObject.PermissionScope_Group);
					setPermission(permissionCode,groupPermission);
					permissionList.add(groupPermission);
				}					
			}			
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
		return permissionList;
	}	

	@Override
	public boolean setContentPermissions(BaseContentObject contentObject,List<PermissionObject> permissionList) throws ContentReposityException{
		try {
			JCRContentObjectImpl contentObjectImpe=(JCRContentObjectImpl)contentObject;
			Node contentObjectImpe_jcrNode=contentObjectImpe.getJcrNode();				
			
			List<String> groupPermissionParticipantList=new ArrayList<String>();
			Map<String,String> groupPermissionCodeMap=new HashMap<String,String>();
			
			for(PermissionObject currentPermissionObject:permissionList){
				StringBuffer permissionCode=new StringBuffer();
				if(currentPermissionObject.getDisplayContentPermission()){
					permissionCode.append("D");
				}else{
					permissionCode.append("-");
				}
				if(currentPermissionObject.getAddContentPermission()){
					permissionCode.append("A");
				}else{
					permissionCode.append("-");
				}
				if(currentPermissionObject.getDeleteContentPermission()){
					permissionCode.append("D");
				}else{
					permissionCode.append("-");
				}
				if(currentPermissionObject.getEditContentPermission()){
					permissionCode.append("E");
				}else{
					permissionCode.append("-");
				}	
				if(currentPermissionObject.getConfigPermissionPermission()){
					permissionCode.append("C");
				}else{
					permissionCode.append("-");
				}	
				if(currentPermissionObject.getAddSubFolderPermission()){
					permissionCode.append("F");
				}else{
					permissionCode.append("-");
				}						
				if(currentPermissionObject.getDeleteSubFolderPermission()){
					permissionCode.append("R");
				}else{
					permissionCode.append("-");
				}				
				if(currentPermissionObject.getPermissionScope().equals(PermissionObject.PermissionScope_Owner)){
					contentObjectImpe_jcrNode.setProperty("vfcr:contentOwnerPermission", permissionCode.toString());					
				}if(currentPermissionObject.getPermissionScope().equals(PermissionObject.PermissionScope_Other)){
					contentObjectImpe_jcrNode.setProperty("vfcr:contentOtherPermission", permissionCode.toString());					
				}
				if(currentPermissionObject.getPermissionScope().equals(PermissionObject.PermissionScope_Group)){
					String groupPermissionCode=permissionCode.toString()+":"+currentPermissionObject.getPermissionParticipant();						
					groupPermissionParticipantList.add(currentPermissionObject.getPermissionParticipant());
					groupPermissionCodeMap.put(currentPermissionObject.getPermissionParticipant(), groupPermissionCode);					
				}				
			}
			if(groupPermissionParticipantList.size()>0){					
				String[] groupPermissionCodeArray=new String[groupPermissionParticipantList.size()];					
				for(int i=0;i<groupPermissionParticipantList.size();i++){
					String currentGroupPermissionCode=groupPermissionCodeMap.get(groupPermissionParticipantList.get(i));
					groupPermissionCodeArray[i]=currentGroupPermissionCode;						
				}
				contentObjectImpe_jcrNode.setProperty("vfcr:contentGroupPermission", groupPermissionCodeArray);					
			}else{				
				if(contentObjectImpe_jcrNode.hasProperty("vfcr:contentGroupPermission")){
					Property p=contentObjectImpe_jcrNode.getProperty("vfcr:contentGroupPermission");
					p.remove();
				}				
			}					
			contentObjectImpe_jcrNode.getSession().save();
			return true;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}
	}	
	
	private static void setPermission(String permissionCode,PermissionObject permissionObject){
		String displayPermission=permissionCode.substring(0,1);		
		if(displayPermission.equals("D")){
			permissionObject.setDisplayContentPermission(true);
		}else{
			permissionObject.setDisplayContentPermission(false);
		}		
		String addPermission=permissionCode.substring(1,2);
		if(addPermission.equals("A")){
			permissionObject.setAddContentPermission(true);
		}else{
			permissionObject.setAddContentPermission(false);
		}			
		String deletePermission=permissionCode.substring(2,3);
		if(deletePermission.equals("D")){
			permissionObject.setDeleteContentPermission(true);
		}else{
			permissionObject.setDeleteContentPermission(false);
		}	
		String editPermission=permissionCode.substring(3,4);
		if(editPermission.equals("E")){
			permissionObject.setEditContentPermission(true);
		}else{
			permissionObject.setEditContentPermission(false);
		}	
		String configPermission=permissionCode.substring(4,5);
		if(configPermission.equals("C")){
			permissionObject.setConfigPermissionPermission(true);
		}else{
			permissionObject.setConfigPermissionPermission(false);
		}
		String addFolderPermission=permissionCode.substring(5,6);
		if(addFolderPermission.equals("F")){
			permissionObject.setAddSubFolderPermission(true);
		}else{
			permissionObject.setAddSubFolderPermission(false);
		}	
		String deleteSubFolderPermission=permissionCode.substring(6,7);
		if(deleteSubFolderPermission.equals("R")){
			permissionObject.setDeleteSubFolderPermission(true);
		}else{
			permissionObject.setDeleteSubFolderPermission(false);
		}	
	}
}