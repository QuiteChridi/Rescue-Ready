package models;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.validation.constraints.AssertTrue;

import static org.junit.Assert.*;

public class UserFactoryTest {

    UserFactory users;
    @Inject
    public UserFactoryTest(UserFactory users){
        this.users = users;
    }
    @Test
    public void authenticateAdminAdminShouldReturnAUser(){
        UserFactory.User user = users.authenticate("admin", "admin");
        assertNotNull(user);
    }

}