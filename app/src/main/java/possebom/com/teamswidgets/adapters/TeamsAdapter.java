package possebom.com.teamswidgets.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import possebom.com.teamswidgets.MainActivity;
import possebom.com.teamswidgets.R;

/**
 * Created by alexandre on 01/11/14.
 */
public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder> {

    private List<String> applications;
    private int rowLayout;
    private MainActivity mAct;

    public TeamsAdapter(int rowLayout, MainActivity act) {
        applications = new ArrayList<String>();
        this.rowLayout = rowLayout;
        this.mAct = act;
    }

    public void setApplications(List<String> applications) {
        this.applications = applications;
        this.notifyItemRangeInserted(0, applications.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final String teamName = applications.get(i);
        viewHolder.name.setText(teamName);
//        viewHolder.image.setImageDrawable(appInfo.getIcon());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAct.animateActivity(appInfo, viewHolder.image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return applications == null ? 0 : applications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.teamName);
            image = (ImageView) itemView.findViewById(R.id.teamImage);
        }

    }
}
