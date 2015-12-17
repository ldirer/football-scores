package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RemoteViews;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.ViewHolder;

public class ScoresWidgetProvider extends AppWidgetProvider {

    private final static String[] SCORES_COLUMNS = {
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.TIME_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.MATCH_ID,
            DatabaseContract.scores_table.MATCH_DAY
    };


    private final static int INDEX_LEAGUE_COL = 0;
    private final static int INDEX_DATE_COL = 1;
    private final static int INDEX_TIME_COL = 2;
    private final static int INDEX_HOME_COL = 3;
    private final static int INDEX_AWAY_COL = 4;
    private final static int INDEX_HOME_GOALS_COL = 5;
    private final static int INDEX_AWAY_GOALS_COL = 6;
    private final static int INDEX_MATCH_ID = 7;
    private final static int INDEX_MATCH_DAY = 8;

    private static final String LOG_TAG = ScoresWidgetProvider.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

//            Uri scoreWithDateUri = DatabaseContract.scores_table.buildScoreWithDate();
            Uri scoreWithDateUri = DatabaseContract.BASE_CONTENT_URI;
            Cursor data = context.getContentResolver().query(scoreWithDateUri, SCORES_COLUMNS, null, new String[]{"2015-12-15%"}, null);

            if(data == null || !data.moveToFirst()) {
                Log.d(LOG_TAG, "no data in cursor for widget!");
                return;
            }

            String home_name = data.getString(INDEX_HOME_COL);
            String away_name = data.getString(INDEX_AWAY_COL);
            int home_goals = data.getInt(INDEX_HOME_GOALS_COL);
            int away_goals = data.getInt(INDEX_AWAY_GOALS_COL);
            String data_textview = data.getString(INDEX_DATE_COL);


            views.setTextViewText(R.id.home_name, home_name);
            views.setTextViewText(R.id.away_name, away_name);
            views.setTextViewText(R.id.score_textview, Utilies.getScores(home_goals, away_goals));
            views.setTextViewText(R.id.data_textview, data_textview);
            views.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(home_name));
            views.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(away_name));


            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            data.close();
        }
    }
}
