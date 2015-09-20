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
public class TeamTest extends TestCase {
    private Team team;
    private String objectId = "01s2x851";
    private String teamName = "Team Izzo";

    @Before
    public void setUpObjects() {
        team = new Team(objectId, teamName);
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(objectId, team.getObjectId());
    }

    @Test
    public void testGetGroupName() throws Exception {
        assertEquals(teamName, team.getName());
    }
}
