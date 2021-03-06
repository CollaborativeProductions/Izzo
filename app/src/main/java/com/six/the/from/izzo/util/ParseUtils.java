package com.six.the.from.izzo.util;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.six.the.from.izzo.models.Athlete;
import com.six.the.from.izzo.models.Exercise;
import com.six.the.from.izzo.ui.ContactsListActivity.OperationStatusFetcher;
import com.six.the.from.izzo.ui.TeamHubActivity.FetchTeamProgramsStatusFetcher;
import com.six.the.from.izzo.ui.NewProgramExercisesActivity.SaveProgramStatusFetcher;
import com.six.the.from.izzo.ui.StartUpActivity.CurrentAthleteFetcher;
import com.six.the.from.izzo.ui.TeamMembersActivity.TeamMembersFetcher;
import com.six.the.from.izzo.ui.WorkoutActivity.SaveWorkoutStatusFetcher;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;


public class ParseUtils {
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

    public static ParseObject saveAthlete(String uuid, String firstName, String lastName, String phoneNumber, ParseObject currentAthlete) {
        ParseObject newAthlete = new ParseObject("Athlete");
        newAthlete.put("uuid", uuid);
        newAthlete.put("firstName", firstName);
        newAthlete.put("lastName", lastName);
        newAthlete.put("phoneNumber", phoneNumber);
        newAthlete.saveInBackground();
        return newAthlete;
    }

    public static void fetchCurrentAthleteTeams(final TeamsInfoFetcher fetcher, ParseObject currentAthlete) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("AthleteTeam");
        query.whereEqualTo("athlete", currentAthlete);
        query.include("team");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> teamList, ParseException e) {
                for (ParseObject athleteTeamParseObj : teamList) {
                    ParseObject teamParseObj = athleteTeamParseObj.getParseObject("team");
                    fetcher.teamList.add(teamParseObj);
                }
                fetcher.fetching = false;
            }
        });
        fetcher.fetching = true;
    }

    public static void saveProgramWithoutTeam(
            String programName,
            ExerciseArrayAdapter exerciseArrayAdapter,
            ParseFile parseFile,
            final SaveProgramStatusFetcher fetcher,
            ParseObject currentAthlete)
    {
        final ParseObject programParseObj = new ParseObject("Program");
        programParseObj.put("name", programName);
        programParseObj.put("iconImageUrl", parseFile);
        programParseObj.put("owner", currentAthlete);

        saveExercises(exerciseArrayAdapter, programParseObj);

        programParseObj.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    fetcher.saving = false;
                }
            }
        });
        fetcher.saving = true;
    }

    public static void saveProgramWithTeam(
            String programName,
            ParseObject teamParseObj,
            ExerciseArrayAdapter exerciseArrayAdapter,
            ParseFile parseFile,
            final SaveProgramStatusFetcher fetcher,
            ParseObject currentAthlete)
    {
        final ParseObject programParseObj = new ParseObject("Program");
        programParseObj.put("name", programName);
        programParseObj.put("iconImageUrl", parseFile);
        programParseObj.put("owner", currentAthlete);
        programParseObj.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    fetcher.programParseObj = programParseObj;
                }
            }
        });

        saveExercises(exerciseArrayAdapter, programParseObj);
        saveProgramToTeam(teamParseObj, programParseObj, fetcher);
    }

    public static void saveProgramToTeam(ParseObject teamParseObj, ParseObject programParseObj, final SaveProgramStatusFetcher fetcher) {
        ParseObject teamProgramParseObj = new ParseObject("TeamProgram");
        teamProgramParseObj.put("team", teamParseObj);
        teamProgramParseObj.put("program", programParseObj);
        teamProgramParseObj.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    fetcher.saving = false;
                }
            }
        });
        fetcher.saving = true;
    }

    public static void saveTeam(final ContactArrayAdapter contactArrayAdapter, String teamName, String uuid, ParseFile file, final OperationStatusFetcher fetcher) {
        final ParseObject team = new ParseObject("Team");
        team.put("name", teamName);
        team.put("iconImageUrl", file);
        team.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    fetcher.teamParseObj = team;
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
                    addTeamMembers(contactArrayAdapter, team, fetcher);
                }
            }
        });
        fetcher.saving = true;
    }

    public static void updateTeam(final ContactArrayAdapter contactArrayAdapter, String teamId, final OperationStatusFetcher fetcher) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
        query.whereEqualTo("objectId", teamId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject team, ParseException e) {
                if (e == null) {
                    fetcher.teamParseObj = team;
                    addTeamMembers(contactArrayAdapter, team, fetcher);
                }
            }
        });
        fetcher.saving = true;
    }

    public static void addTeamMembers(ContactArrayAdapter contactArrayAdapter, ParseObject team, final OperationStatusFetcher fetcher) {
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

    public static void fetchTeamPrograms(final FetchTeamProgramsStatusFetcher fetcher, String teamId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
        query.whereEqualTo("objectId", teamId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject teamParseObj, ParseException e) {
                if (e == null) {
                    fetchPrograms(fetcher, teamParseObj);
                }
            }
        });
        fetcher.fetching = true;
    }

    public static void fetchPrograms(final FetchTeamProgramsStatusFetcher fetcher, ParseObject teamParseObj) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TeamProgram");
        query.whereEqualTo("team", teamParseObj);
        query.orderByDescending("createdAt");
        query.include("program");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> programList, ParseException e) {
                ParseObject program;
                for (ParseObject programParseObj : programList) {
                    program = programParseObj.getParseObject("program");
                    fetcher.programParseObjs.add(program);
                }
                fetcher.fetching = false;
            }
        });
    }

    public static void fetchProgramExercises(final FetchProgramExercisesStatusFetcher fetcher, String programId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Program");
        query.whereEqualTo("objectId", programId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject programParseObj, ParseException e) {
                if (e == null) {
                    fetchExercises(fetcher, programParseObj);
                }
            }
        });
        fetcher.fetching = true;

    }

    private static void fetchExercises(final FetchProgramExercisesStatusFetcher fetcher, ParseObject programParseObj) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ProgramExercise");
        query.whereEqualTo("program", programParseObj);
        query.include("exercise");
        query.orderByAscending("updatedAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> exerciseList, ParseException e) {
                ParseObject exercise;
                for (ParseObject exerciseParseObj : exerciseList) {
                    exercise = exerciseParseObj.getParseObject("exercise");
                    fetcher.exerciseParseObjs.add(exercise);
                }
                fetcher.fetching = false;
            }
        });
        fetcher.programParseObj = programParseObj;
    }

    public static void saveExercises(ExerciseArrayAdapter exerciseArrayAdapter, ParseObject programParseObj) {
        Exercise exercise;
        for (int i = 0; i < exerciseArrayAdapter.getCount(); i++) {
            exercise = exerciseArrayAdapter.getItem(i);

            ParseObject exerciseParseObj = new ParseObject("Exercise");
            exerciseParseObj.put("name", exercise.getName());
            exerciseParseObj.put("type", exercise.getType());
            if (exercise.getType().equals("Cardio")) {
                exerciseParseObj.put("distance", exercise.getDistance());
                exerciseParseObj.put("duration", exercise.getDuration());
            } else {
                try {
                    exerciseParseObj.put("reps", new JSONArray(exercise.getNumReps()));
                    exerciseParseObj.put("weight", new JSONArray(exercise.getWeight()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            exerciseParseObj.saveInBackground();

            ParseObject programExerciseParseObj = new ParseObject("ProgramExercise");
            programExerciseParseObj.put("program", programParseObj);
            programExerciseParseObj.put("exercise", exerciseParseObj);
            programExerciseParseObj.saveInBackground();
        }
    }

    public static void saveWorkout(
            EditableExerciseArrayAdapter exerciseArrayAdapter,
            final SaveWorkoutStatusFetcher fetcher,
            ParseObject currentAthlete,
            ParseObject programParseObj
    ) {
        final ParseObject workoutParseObj = new ParseObject("Workout");
        workoutParseObj.put("athlete", currentAthlete);
        workoutParseObj.put("program", programParseObj);

        saveWorkoutToAthlete(workoutParseObj, programParseObj, currentAthlete);
        saveWorkoutExercises(exerciseArrayAdapter, workoutParseObj, programParseObj);

        workoutParseObj.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    fetcher.saving = false;
                }
            }
        });
        fetcher.saving = true;
    }

    public static void saveWorkoutToAthlete(ParseObject workoutParseObject, ParseObject programParseObj, ParseObject currentAthlete) {
        ParseObject athleteWorkoutParseObj = new ParseObject("AthleteWorkout");
        athleteWorkoutParseObj.put("athlete", currentAthlete);
        athleteWorkoutParseObj.put("workout", workoutParseObject);
        athleteWorkoutParseObj.put("program", programParseObj);
        athleteWorkoutParseObj.saveInBackground();
    }

    public static void saveWorkoutExercises(
            EditableExerciseArrayAdapter exerciseArrayAdapter,
            ParseObject workoutParseObj,
            ParseObject programParseObj
    ) {
        Exercise exercise;
        for (int i = 0; i < exerciseArrayAdapter.getCount(); i++) {
            exercise = exerciseArrayAdapter.getItem(i);

            ParseObject exerciseParseObj = new ParseObject("Exercise");
            exerciseParseObj.put("name", exercise.getName());
            exerciseParseObj.put("type", exercise.getType());
            if (exercise.getType().equals("Cardio")) {
                exerciseParseObj.put("distance", exercise.getDistance());
                exerciseParseObj.put("duration", exercise.getDuration());
            } else {
                try {
                    exerciseParseObj.put("reps", new JSONArray(exercise.getNumReps()));
                    exerciseParseObj.put("weight", new JSONArray(exercise.getWeight()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            ParseObject workoutExerciseParseObj = new ParseObject("WorkoutExercise");
            workoutExerciseParseObj.put("exercise", exerciseParseObj);
            workoutExerciseParseObj.put("workout", workoutParseObj);
            workoutExerciseParseObj.put("program", programParseObj);
            workoutExerciseParseObj.saveInBackground();
        }
    }

}
