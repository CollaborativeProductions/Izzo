package com.six.the.from.izzo.models;

import com.six.the.from.izzo.BuildConfig;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class AthleteTest extends TestCase {
    private Athlete athlete;
    private String objectId = "01s2x851";
    private String uuid = "de305d54-75b4-431b-adb2-eb6b9e546014";
    private String firstName = "John";
    private String lastName = "Doe";
    private String phoneNumber = "6475023425";

    @Before
    public void setUpObjects() {
        athlete = new Athlete(objectId, uuid, firstName, lastName, phoneNumber);
    }

    @Test
    public void testGetId() {
        assertEquals(objectId, athlete.getId());
    }

    @Test
    public void testGetUuid() {
        assertEquals(uuid, athlete.getUuid());
    }

    @Test
    public void testGetFirstName() {
        assertEquals(firstName, athlete.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals(lastName, athlete.getLastName());
    }

    @Test
    public void testGetPhoneNumber() {
        assertEquals(phoneNumber, athlete.getPhoneNumber());
    }
}