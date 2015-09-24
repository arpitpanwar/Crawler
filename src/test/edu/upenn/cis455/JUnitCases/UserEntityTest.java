package test.edu.upenn.cis455.JUnitCases;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.sleepycat.persist.EntityStore;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.accessor.UserAccessor;
import edu.upenn.cis455.storage.entity.UserEntity;
import edu.upenn.cis455.xpathengine.utils.Utils;

public class UserEntityTest {

	@Test
	public void testFetchEntityFromPrimaryKey() {
		
		EntityStore store = new DBWrapper("dbdir").getStore();
		UserAccessor accessor = new UserAccessor(store);
		UserEntity entity = new UserEntity();
		entity.setUserEmail("test@test.com"+Math.random());
		entity.setUserId(Utils.generateUniqueId());
		entity.setUserFirstName("Arpit");
		entity.setUserLastName("Panwar");
		entity.setLastLogin(new Date().getTime());
		entity.setUserType("test");
		
		accessor.putEntity(entity);
		
		UserEntity ent2 = accessor.fetchEntityFromSecondaryKey(entity.getUserEmail());
		
		assertNotEquals("Check for username", null, ent2.getUserEmail());
		assertEquals("Check for username", ent2.getUserEmail(), entity.getUserEmail());		
	}

}
