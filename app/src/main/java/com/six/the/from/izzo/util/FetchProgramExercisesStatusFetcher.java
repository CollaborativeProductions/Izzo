package com.six.the.from.izzo.util;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


public class FetchProgramExercisesStatusFetcher {
    public volatile boolean fetching;
    public List<ParseObject> exerciseParseObjs = new ArrayList<>();
    public ParseObject programParseObj;
}