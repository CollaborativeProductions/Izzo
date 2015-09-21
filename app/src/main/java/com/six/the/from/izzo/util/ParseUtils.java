package com.six.the.from.izzo.util;

import com.parse.GetCallback;
import com.six.the.from.izzo.ui.StartUpActivity.Heartbeat;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;


import java.util.List;


public class ParseUtils {

    public static void saveTeam(ContactArrayAdapter contactArrayAdapter, String teamName) {
    ParseObject team = new ParseObject("Team");
    team.put("name", teamName);
    team.saveInBackground();

    for (int pos = 0; pos < contactArrayAdapter.getCount(); pos++) {
        ContactListItem contact = contactArrayAdapter.getItem(pos);
        String[] contactName = contact.getName().split(" ");
        ParseObject athlete = new ParseObject("Athlete");
        athlete.put("firstName", contactName[0]);
        try {
            if (!contactName[1].isEmpty()) athlete.put("lastName", contactName[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            // Ignore putting last name
        }
        athlete.put("phoneNumber", contact.getPhoneNumber());
        athlete.saveInBackground();

        ParseObject athleteteam = new ParseObject("AthleteTeam");
        athleteteam.put("team", team);
        athleteteam.put("athlete", athlete);
        athleteteam.saveInBackground();
        }
    }

    public static void saveAthlete(String uuid, String firstName, String lastName, String phoneNumber) {
        ParseObject newAthlete = new ParseObject("Athlete");
        newAthlete.put("uuid", uuid);
        newAthlete.put("firstName", firstName);
        newAthlete.put("lastName", lastName);
        newAthlete.put("phoneNumber", phoneNumber);
        newAthlete.saveInBackground();
    }

    public static void uuidPhoneNumberExists(final Heartbeat heartbeat, String phoneNumber, String uuid) {
        heartbeat.done = false;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Athlete");
        query.whereEqualTo("uuid", uuid);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject athlete, ParseException e) {
                if (e == null) {
                    if (athlete != null) heartbeat.exists = true;
                }
                heartbeat.done = true;
            }
        });
    }

    public static boolean uuidExists(String uuid) {
        return false;
    }



}
