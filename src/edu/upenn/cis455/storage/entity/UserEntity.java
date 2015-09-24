package edu.upenn.cis455.storage.entity;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;
/**
 * Entity for user information
 * @author cis455
 *
 */
@Entity
public class UserEntity {
	
	@PrimaryKey
	private String userId;
	
	@SecondaryKey(relate = Relationship.ONE_TO_ONE)
	private String userEmail;
	
	private String userPassword;
	private String userFirstName;
	private String userLastName;
	private String userType;
	private long lastLogin;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public long getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(long lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}

}
