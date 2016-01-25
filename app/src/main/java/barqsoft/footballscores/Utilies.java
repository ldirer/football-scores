package barqsoft.footballscores;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import barqsoft.footballscores.service.myFetchService;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies {
    public static final Integer SERIE_A = Integer.parseInt(myFetchService.SERIE_A);
    public static final Integer PREMIER_LEAGUE = Integer.parseInt(myFetchService.PREMIER_LEAGUE);
    public static final Integer CHAMPIONS_LEAGUE = Integer.parseInt(myFetchService.CHAMPIONS_LEAGUE);
    public static final Integer PRIMERA_DIVISION = Integer.parseInt(myFetchService.PRIMERA_DIVISION);
    public static final Integer BUNDESLIGA = Integer.parseInt(myFetchService.BUNDESLIGA1);
    public static final Integer BUNDESLIGA2 = Integer.parseInt(myFetchService.BUNDESLIGA2);
    public static final Integer LIGUE1 = Integer.parseInt(myFetchService.LIGUE1);
    private static final String LOG_TAG = Utilies.class.getSimpleName();
    static final HashMap<Integer, String> leagueCodeToName = new HashMap<>();
    // Somewhat lame way to initialize the league-to-name map just once AND to use string resources.
    private static boolean mapInitialized = false;

    public static String getLeague(Context context, int league_num) {
        if(!mapInitialized) {
            leagueCodeToName.put(SERIE_A, context.getString(R.string.serie_a_friendly));
            leagueCodeToName.put(PREMIER_LEAGUE, context.getString(R.string.premier_league_friendly));
            leagueCodeToName.put(CHAMPIONS_LEAGUE, context.getString(R.string.champions_league_friendly));
            leagueCodeToName.put(PRIMERA_DIVISION, context.getString(R.string.primera_division_friendly));
            leagueCodeToName.put(BUNDESLIGA, context.getString(R.string.bundesliga_friendly));
            leagueCodeToName.put(BUNDESLIGA2, context.getString(R.string.bundesliga_2_friendly));
            leagueCodeToName.put(LIGUE1, context.getString(R.string.ligue_1_friendly));
            mapInitialized = true;
        }
        String leagueName = leagueCodeToName.get(league_num);
        return leagueName == null ? "Not known League Please report" : leagueName;
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
                // TODO: Before sub, add all the internet-fetched logos.
                // We remove non ascii characters to match the logos names.
                // The names come from a quick and dirty python script that just removes non ascii characters.
                String cleanTeamname = teamname.replaceAll("[^\\x00-\\x7F]", "");
                // DO NOT include the image extension in the drawable string here!
                int resourceId = context.getResources().getIdentifier(
                        cleanTeamname.toLowerCase()
                                .replace(".", "_")
                                .replace(" ", "_"), "drawable", context.getApplicationContext().getPackageName());
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
