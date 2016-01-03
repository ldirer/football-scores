package barqsoft.footballscores;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import barqsoft.footballscores.service.myFetchService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MainScreenFragment.class.getSimpleName();
    private static final String SAVED_STATE_SELECTED_POSITION_KEY = "selectedPosition";
    public scoresAdapter mAdapter;
    public static final int SCORES_LOADER = 0;
    private String[] fragmentdate = new String[1];
    private int last_selected_item = -1;
    private int mSelectedPosition = -1;
    private boolean mScrolledAlready = false;
    private ListView score_list;

    public MainScreenFragment() {
    }



    private void update_scores() {
        Intent service_start = new Intent(getActivity(), myFetchService.class);
        getActivity().startService(service_start);
    }

    public void setFragmentDate(String date) {
        fragmentdate[0] = date;
    }

    private int getAdapterItemPosition(long id) {
        for (int position = 0; position < mAdapter.getCount(); position++)
            if (mAdapter.getItemMatchId(position) == id) {
                Log.d(LOG_TAG, String.format("getAdapterItemPosition found position: %d", position));
                return position;
            }
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        Log.d(LOG_TAG, "in onCreateView");
        update_scores();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        score_list = (ListView) rootView.findViewById(R.id.scores_list);
        TextView emptyView = (TextView) rootView.findViewById(R.id.scores_list_empty);
        score_list.setEmptyView(emptyView);
        mAdapter = new scoresAdapter(getActivity(), null, 0);
        score_list.setAdapter(mAdapter);
        getLoaderManager().initLoader(SCORES_LOADER, null, this);
        mAdapter.detail_match_id = MainActivity.selected_match_id;
        Log.d(LOG_TAG, String.format("selected match id: %d", MainActivity.selected_match_id));
        score_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder selected = (ViewHolder) view.getTag();
                mAdapter.detail_match_id = selected.match_id;
                MainActivity.selected_match_id = (int) selected.match_id;
                mAdapter.notifyDataSetChanged();
                mSelectedPosition = position;
            }
        });

        if (null != savedInstanceState) {
            mSelectedPosition = savedInstanceState.getInt(SAVED_STATE_SELECTED_POSITION_KEY);
            Log.d(LOG_TAG, String.format("Using savedInstanceState to scroll to position: %d", mSelectedPosition));
        }
        if (mSelectedPosition != -1) {
            Log.d(LOG_TAG, String.format("Supposedly scrolling to position %d", mSelectedPosition));
            // TODO: This just scrolls a tiny amount (but does scroll!) and does not quite reach the right item...
            score_list.smoothScrollToPosition(mSelectedPosition);
        }
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), DatabaseContract.scores_table.buildScoreWithDate(),
                scoresAdapter.MATCH_PROJECTION_COLUMNS, null, fragmentdate, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.d(LOG_TAG, "in onLoadFinished");

        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            i++;
            cursor.moveToNext();
        }
        mAdapter.swapCursor(cursor);
        if (0 != MainActivity.selected_match_id && !mScrolledAlready) {
            Log.d(LOG_TAG, "Going to enter getAdapterItemPosition");
            mSelectedPosition = getAdapterItemPosition(MainActivity.selected_match_id);
            Log.d(LOG_TAG, String.format("Supposedly scrolling to position %d", mSelectedPosition));
            score_list.smoothScrollToPosition(mSelectedPosition);
            mScrolledAlready = true;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_STATE_SELECTED_POSITION_KEY, mSelectedPosition);
    }

}
