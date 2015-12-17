package barqsoft.footballscores;

import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ViewHolder
{
    public TextView home_name;
    public TextView away_name;
    public TextView score;
    public TextView date;
    public ImageView home_crest;
    public ImageView away_crest;
    public double match_id;
    public ViewHolder(View view)
    {
        home_name = (TextView) view.findViewById(R.id.home_name);
        away_name = (TextView) view.findViewById(R.id.away_name);
        score     = (TextView) view.findViewById(R.id.score_textview);
        date      = (TextView) view.findViewById(R.id.data_textview);
        home_crest = (ImageView) view.findViewById(R.id.home_crest);
        away_crest = (ImageView) view.findViewById(R.id.away_crest);
    }

    /**
     * Constructor used for a widget list item, we just populate the data in the RemoteViews.
     * @param views
     */

    public ViewHolder(RemoteViews views)
    {
        views.setTextViewText(R.id.home_name, "Arsenal");
        views.setTextViewText(R.id.away_name, "Tottenham hotspur");
        views.setTextViewText(R.id.score_textview, "5 - 2");
        views.setTextViewText(R.id.data_textview, "15h00");
        views.setImageViewResource(R.id.home_crest, R.drawable.arsenal);
        views.setImageViewResource(R.id.away_crest, R.drawable.tottenham_hotspur);
    }
}
