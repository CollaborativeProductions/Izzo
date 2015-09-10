package com.six.the.from.izzo;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class MainActivityTest extends TestCase {

    @Test
    public void testHelloWorld() {
        String helloWorld = "hello world!";
        assertEquals("hello world!", helloWorld);
    }

}