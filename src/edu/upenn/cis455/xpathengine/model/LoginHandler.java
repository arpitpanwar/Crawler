package edu.upenn.cis455.xpathengine.model;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.log4j.Logger;

import com.sleepycat.persist.EntityStore;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.accessor.UserAccessor;
import edu.upenn.cis455.storage.entity.UserEntity;
import edu.upenn.cis455.xpathengine.utils.PasswordHash;

/**
 * Utility class for handling user logins to the system
 * @author cis455
 *
 */

public class LoginHandler {
	static final Logger LOG = Logger.getLogger(LoginHandler.class);
	
	/**
	 * Check if user login is valid
	 * @param username
	 * @param password
	 * @param store
	 * @return boolean isValid
	 */
	
	public static boolean isLoginValid(String username, String password,EntityStore store){
		
		boolean isValid = false;
		
		UserAccessor user = new UserAccessor(store);
		
		UserEntity entity = user.fetchEntityFromSecondaryKey(username);
		
		if(entity != null){
			try{
				String pass = entity.getUserPassword();
				isValid = PasswordHash.validatePassword(password, pass);
			
			}catch(InvalidKeySpecException ivke){
				
				LOG.debug("Invalid key spec: "+ivke.getMessage());
				
			}catch(NoSuchAlgorithmException nsae){
				LOG.debug("Invalid Algorithm: "+nsae.getMessage());
			}
		}
		
		
		return isValid;
	}
	

}
