package charles.database.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import charles.database.R;
import charles.database.model.Duck;

public class DuckAdapter extends BaseAdapter {
    private Context mContext;
    private List<Duck> mDuckList;

    public DuckAdapter(Context mContext, List<Duck> mDuckList) {
        this.mContext = mContext;
        this.mDuckList = mDuckList;
    }

    @Override
    public int getCount() {
        return mDuckList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDuckList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mDuckList.get(position).getDuckID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_duck_listview, null);

        TextView tvID = (TextView)v.findViewById(R.id.tv_duck_id);
        TextView tvName = (TextView)v.findViewById(R.id.tv_duck_name);
        TextView tvAtlas = (TextView)v.findViewById(R.id.tv_duck_atlas_no);
        TextView tvMinSize = (TextView)v.findViewById(R.id.tv_duck_min_size);
        TextView tvMaxSize = (TextView)v.findViewById(R.id.tv_duck_max_size);

        tvID.setText(String.valueOf(mDuckList.get(position).getDuckID()));
        tvName.setText(mDuckList.get(position).getName());
        tvAtlas.setText("Atlas No" + String.valueOf(mDuckList.get(position).getAtlasNo()));
        tvMinSize.setText("Min Size: " + String.valueOf(mDuckList.get(position).getMinSize()) + "cm");
        tvMaxSize.setText("Max Size: " + String.valueOf(mDuckList.get(position).getMaxSize()) + "cm");

        return v;
    }
}
