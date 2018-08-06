package com.example.charles.opencv.BirdBank;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charles.opencv.R;
import com.example.charles.opencv.Tables.Bird;

import java.util.List;

/**
 * Updates the screen with the birds which the user has identified.
 */
public class ListBirdAdapter extends BaseAdapter implements ListAdapter {
    private Context mContext;
    private List<Bird> mList;
    private List<String> dateList;
    public static Bird mBirdClicked;
    public static String mDateOfBirdClicked;

    public ListBirdAdapter(Context mContext, List<Bird> mList, List<String> dateList) {
        this.mContext = mContext;
        this.mList = mList;
        this.dateList = dateList;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return mList.get(position).getBirdID();
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.bird_listview, null);
        }

        ConstraintLayout constraintLayout = (ConstraintLayout)view.findViewById(R.id.list_item_constraint);
        TextView birdName = (TextView)view.findViewById(R.id.bird_name);
        TextView birdSeen = (TextView)view.findViewById(R.id.bird_status);
        birdName.setText(mList.get(position).getName());
        birdSeen.setText("Identified on " + dateList.get(position));

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBirdClicked = mList.get(position);
                mDateOfBirdClicked = dateList.get(position);
                Intent intent = new Intent(mContext, IndividualBirdActivity.class);
                mContext.startActivity(intent);
            }
        });

        return view;
    }
}
