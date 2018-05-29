package charles.database.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import charles.database.MainActivity;
import charles.database.R;
import charles.database.database.DatabaseHelper;
import charles.database.model.Duck;

public class ResultListAdapter extends BaseAdapter {
    private Context context;
    private List<Duck> ducklist;
    LayoutInflater inflter;

    /**
     * Creates a list of duck results
     *
     * @param context
     * @param ducklist
     */
    public ResultListAdapter(Context context, List<Duck> ducklist) {
        this.context = context;
        this.ducklist = ducklist;
        this.inflter = (LayoutInflater.from(context));
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return ducklist.size();
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
        return ducklist.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return ducklist.get(position).getDuckID();
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
     * @param v The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        v = inflter.inflate(R.layout.tq_result_item, null);

        //Get Views
        final TextView tv_duckName = (TextView)v.findViewById(R.id.result_item_bird_name);
        final ImageView iv_duckImage = (ImageView)v.findViewById(R.id.result_item_image);

        //Set duck name and image
        tv_duckName.setText(ducklist.get(position).getName());
        iv_duckImage.setImageBitmap(MainActivity.getBirdImage(context, "ibis.jpg"));

        //Set on click listeners for image and text
        iv_duckImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.onClickResult(tv_duckName.getText().toString());
            }
        });

        tv_duckName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.onClickResult(tv_duckName.getText().toString());
            }
        });

        return v;
    }
}
