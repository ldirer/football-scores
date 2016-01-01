package barqsoft.footballscores;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies {
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;
    private static final String LOG_TAG = Utilies.class.getSimpleName();
//    private static final Logger teamCrestLogger = Logger.getLogger("barqsoft.footballscores.utilities.teamcrests");


    public static String getLeague(int league_num) {
        switch (league_num) {
            case SERIE_A:
                return "Seria A";
            case PREMIER_LEGAUE:
                return "Premier League";
            case CHAMPIONS_LEAGUE:
                return "UEFA Champions League";
            case PRIMERA_DIVISION:
                return "Primera Division";
            case BUNDESLIGA:
                return "Bundesliga";
            default:
                return "Not known League Please report";
        }
    }

    public static String getMatchDay(int match_day, int league_num) {
        if (league_num == CHAMPIONS_LEAGUE) {
            if (match_day <= 6) {
                return "Group Stages, Matchday : 6";
            } else if (match_day == 7 || match_day == 8) {
                return "First Knockout round";
            } else if (match_day == 9 || match_day == 10) {
                return "QuarterFinal";
            } else if (match_day == 11 || match_day == 12) {
                return "SemiFinal";
            } else {
                return "Final";
            }
        } else {
            return "Matchday : " + String.valueOf(match_day);
        }
    }

    /**
     * Return a friendly String to display score.
     */
    public static String getScores(int home_goals, int awaygoals) {
        if (home_goals < 0 || awaygoals < 0) {
            Log.d(LOG_TAG, "Negative number of goals: that's weird.");
            return " - ";
        } else {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName(String teamname, Context context) {
        Log.d("getTeamCrestByTeamName", teamname);
        // This does not make sense! I would be creating a log file on the device!...
//        FileHandler teamCrestFileHandler = null;
//        try {
//            teamCrestFileHandler = new FileHandler("logTeamCrests");
//        } catch (IOException e) {
//            Log.d(LOG_TAG, e.getMessage());
//        }
//        teamCrestLogger.addHandler(teamCrestFileHandler);
        if (teamname == null) {
            return R.drawable.no_icon;
        }
        switch (teamname) { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC":
                return R.drawable.arsenal;
            case "Manchester United FC":
                return R.drawable.manchester_united;
            case "Swansea City":
                return R.drawable.swansea_city_afc;
            case "Leicester City":
                return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC":
                return R.drawable.everton_fc_logo1;
            case "West Ham United FC":
                return R.drawable.west_ham;
            case "Tottenham Hotspur FC":
                return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion":
                return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC":
                return R.drawable.sunderland;
            case "Stoke City FC":
                return R.drawable.stoke_city;
            default:
                // DO NOT include the image extension in the drawable string here!
                int resourceId = context.getResources().getIdentifier(teamname.toLowerCase().replace(" ", "_"), "drawable", context.getApplicationContext().getPackageName());
                return resourceId == 0 ? R.drawable.no_icon : resourceId;
        }
    }

    /**
     * Used to get a 2-length array that can be passed as selectionArgs to the content provider for a 'between dates' query.
     *
     * @param dateSpan
     * @return a string array with dates today - dateSpan and today + dateSpan.
     */
    public static String[] getFormattedDatesForDatabase(int dateSpan) {
        String[] formattedDates = new String[2];
        for (int i = 0;i < 2;i+=1)
        {
            Date fragmentdate = new Date(System.currentTimeMillis()+((i * 2 * dateSpan - dateSpan)*86400000));
            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
            formattedDates[i] = mformat.format(fragmentdate);
        }
        Log.d(LOG_TAG, "formattedDates: " + formattedDates.toString());
        return formattedDates;
    }
}
