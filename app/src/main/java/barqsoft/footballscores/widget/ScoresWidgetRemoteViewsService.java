package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScoresWidgetRemoteViewsService extends RemoteViewsService {

    public static final String EXTRA_KEY_MATCH_ID = "matchId";
    public static final String EXTRA_KEY_MATCH_DATE = "matchDate";

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

    private final String LOG_TAG = ScoresWidgetRemoteViewsService.class.getSimpleName();






    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public class ScoresRemoteViewsFactory implements RemoteViewsFactory {

        private final String LOG_TAG = ScoresRemoteViewsFactory.class.getSimpleName();

        private Cursor mData;
        private Context mContext;

        public ScoresRemoteViewsFactory(Context context) {
            super();
            mContext = context;
        }

        @Override
        public void onCreate() {
            Log.d(LOG_TAG, "in onCreate");
        }

        @Override
        public void onDataSetChanged() {
            Log.d(LOG_TAG, "in onDataSetChanged");
            if (mData != null) {
                mData.close();
            }
            // Copied from Sunshine advanced android development.
            // This method is called by the app hosting the widget (e.g., the launcher)
            // However, our ContentProvider is not exported so it doesn't have access to the
            // data. Therefore we need to clear (and finally restore) the calling identity so
            // that calls use our process and permission
            final long identityToken = Binder.clearCallingIdentity();

            Uri scoreWithDateIntervalUri = DatabaseContract.scores_table.buildScoreWithDateInterval();
            mData = getContentResolver().query(scoreWithDateIntervalUri, SCORES_COLUMNS, null,
                    Utilies.getFormattedDatesForDatabase(2), DatabaseContract.scores_table.DATE_COL + " DESC");
            Binder.restoreCallingIdentity(identityToken);
        }

        @Override
        public void onDestroy() {
            if(mData != null) {
                mData.close();
            }
        }

        @Override
        public int getCount() {
            int count = mData == null ? 0 : mData.getCount();
            Log.d(LOG_TAG, String.format("in getCount with %d elements", count));
            return count;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Log.d(LOG_TAG, "in getViewAt");

            if(mData == null || !mData.moveToPosition(position)) {
                Log.d(LOG_TAG, "no data in cursor for widget at this position!");
                return null;
            }
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_list_item);

            int matchId = mData.getInt(INDEX_MATCH_ID);

            String home_name = mData.getString(INDEX_HOME_COL);
            String away_name = mData.getString(INDEX_AWAY_COL);
            int home_goals = mData.getInt(INDEX_HOME_GOALS_COL);
            int away_goals = mData.getInt(INDEX_AWAY_GOALS_COL);
            String data_textview = mData.getString(INDEX_DATE_COL);

            views.setTextViewText(R.id.home_name, home_name);
            views.setTextViewText(R.id.away_name, away_name);
            views.setTextViewText(R.id.score_textview, Utilies.getScores(home_goals, away_goals));
            views.setTextViewText(R.id.data_textview, data_textview);
            views.setTextViewCompoundDrawables(R.id.home_name, 0, 0, 0, Utilies.getTeamCrestByTeamName(home_name, mContext));
            views.setTextViewCompoundDrawables(R.id.away_name, 0, 0, 0, Utilies.getTeamCrestByTeamName(away_name, mContext));
//            views.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(home_name, mContext));
//            views.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(away_name, mContext));

            Intent intent = new Intent();
//            intent.setData(DatabaseContract.scores_table.buildScoreWithId());
            intent.putExtra(EXTRA_KEY_MATCH_ID, matchId);
            intent.putExtra(EXTRA_KEY_MATCH_DATE, data_textview);
            views.setOnClickFillInIntent(R.id.widget_list_item, intent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ScoresRemoteViewsFactory(this);
    }
}
