package com.six.the.from.izzo;

import com.parse.ParseObject;


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
            if (!contactName[1].isEmpty()) athlete.put("lastName", contactName[1]);
            athlete.put("phoneNumber", contact.getPhoneNumber());
            athlete.saveInBackground();

            ParseObject athleteteam = new ParseObject("AthleteTeam");
            athleteteam.put("team", team);
            athleteteam.put("athlete", athlete);
            athleteteam.saveInBackground();
        }
    }
}
