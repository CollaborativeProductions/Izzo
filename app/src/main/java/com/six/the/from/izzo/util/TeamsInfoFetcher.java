package com.six.the.from.izzo.util;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


public class TeamsInfoFetcher {
    public volatile boolean fetching;
    public volatile List<ParseObject> teamList = new ArrayList<>();
}