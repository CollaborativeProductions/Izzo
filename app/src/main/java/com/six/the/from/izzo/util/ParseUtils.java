package com.six.the.from.izzo.util;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.six.the.from.izzo.models.Athlete;
import com.six.the.from.izzo.models.Team;
import com.six.the.from.izzo.ui.ContactsListActivity;
import com.six.the.from.izzo.ui.StartUpActivity.CurrentAthleteFetcher;
import com.six.the.from.izzo.ui.ProgramsActivity.TeamsInfoFetcher;
import com.six.the.from.izzo.ui.TeamMembersActivity.TeamMembersFetcher;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;

import java.util.List;


public class ParseUtils {
    public static void saveTeam(ContactArrayAdapter contactArrayAdapter, String teamName, String uuid, ParseFile file, final ContactsListActivity.OperationStatusFetcher fetcher) {
        final ParseObject team = new ParseObject("Team");
        team.put("name", teamName);
        team.put("iconImageUrl", file);
        team.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    fetcher.teamId = team.getObjectId();
                }
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Athlete");
        query.whereEqualTo("uuid", uuid);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject athlete, ParseException e) {
                if (e == null) {
                    ParseObject athleteteam = new ParseObject("AthleteTeam");
                    athleteteam.put("admin", true);
                    athleteteam.put("athlete", athlete);
                    athleteteam.put("creator", true);
                    athleteteam.put("team", team);
                    athleteteam.saveInBackground();
                }
            }
        });

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
            athleteteam.put("admin", false);
            athleteteam.put("athlete", athlete);
            athleteteam.put("creator", false);
            athleteteam.put("team", team);
            if (pos == contactArrayAdapter.getCount() - 1) {
                athleteteam.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            fetcher.saving = false;
                        }
                    }
                });
            } else {
                athleteteam.saveInBackground();
            }
        }

        fetcher.saving = true;
    }

    public static ParseObject saveAthlete(String uuid, String firstName, String lastName, String phoneNumber, ParseObject currentAthlete) {
        ParseObject newAthlete = new ParseObject("Athlete");
        newAthlete.put("uuid", uuid);
        newAthlete.put("firstName", firstName);
        newAthlete.put("lastName", lastName);
        newAthlete.put("phoneNumber", phoneNumber);
        newAthlete.saveInBackground();
        return newAthlete;
    }

    public static void uuidPhoneNumberExists(final CurrentAthleteFetcher fetcher, String phoneNumber, String uuid) {
        fetcher.fetching = true;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Athlete");
        query.whereEqualTo("uuid", uuid);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject athlete, ParseException e) {
                if (e == null) {
                    if (athlete != null) {
                        fetcher.athlete = athlete;
                    }
                }
                fetcher.fetching = false;
            }
        });
    }

    public static void fetchCurrentAthleteTeams(final TeamsInfoFetcher fetcher, ParseObject currentAthlete) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("AthleteTeam");
        query.whereEqualTo("athlete", currentAthlete);
        query.include("team");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> teamList, ParseException e) {
                for (ParseObject athleteTeamParseObj : teamList) {
                    ParseObject teamParseObj = athleteTeamParseObj.getParseObject("team");
                    Team team = new Team(
                            teamParseObj.getObjectId(),
                            teamParseObj.getString("name")
                    );
                    fetcher.teamList.add(team);
                }
                fetcher.fetching = false;
            }
        });
        fetcher.fetching = true;
    }

    public static void fetchTeam(final TeamFetcher fetcher, String teamId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
        query.whereEqualTo("objectId", teamId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject teamParseObj, ParseException e) {
                if (e == null) {
                    fetcher.teamParseObj = teamParseObj;
                }
                fetcher.fetching = false;
            }
        });
        fetcher.fetching = true;
    }

    public static void fetchTeamMembers(final TeamMembersFetcher fetcher, ParseObject teamParseObj) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("AthleteTeam");
        query.whereEqualTo("team", teamParseObj);
        query.include("athlete");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> athleteList, ParseException e) {
                for (ParseObject athleteTeamParseObj : athleteList) {
                    ParseObject teamParseObj = athleteTeamParseObj.getParseObject("athlete");
                    Athlete athlete = new Athlete(
                            teamParseObj.getObjectId(),
                            teamParseObj.getString("uuid"),
                            teamParseObj.getString("firstName"),
                            teamParseObj.getString("lastName"),
                            teamParseObj.getString("phoneNumber")
                    );
                    fetcher.teamMembers.add(athlete);
                }
                fetcher.fetching = false;
            }
        });
        fetcher.fetching = true;
    }

}
