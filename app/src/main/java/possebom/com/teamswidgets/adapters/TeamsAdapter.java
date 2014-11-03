package possebom.com.teamswidgets.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import possebom.com.teamswidgets.MainActivity;
import possebom.com.teamswidgets.R;
import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.event.SelectTeamEvent;
import possebom.com.teamswidgets.model.Team;

/**
 * Created by alexandre on 01/11/14.
 */
public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder> {

    private List<Team> teamList;
    private int rowLayout;

    public TeamsAdapter() {
        teamList = new ArrayList<Team>();
        this.rowLayout = R.layout.card_teams;
    }

    public void setTeamList(List<Team> applications) {
        this.teamList = applications;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Team team = teamList.get(i);

        viewHolder.name.setText(team.getName());

        Picasso.with(viewHolder.image.getContext())
                .load(team.getImgUrl())
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.drawer_shadow)
                .into(viewHolder.image);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TWController.INSTANCE.getBus().post(new SelectTeamEvent(team.getName()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamList == null ? 0 : teamList.size();
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
