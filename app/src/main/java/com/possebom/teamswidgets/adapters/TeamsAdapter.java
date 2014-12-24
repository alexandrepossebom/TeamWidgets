package com.possebom.teamswidgets.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.possebom.teamswidgets.BaseApplication;
import com.possebom.teamswidgets.R;
import com.possebom.teamswidgets.model.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandre on 01/11/14.
 */
public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder> {
    private final int rowLayout;
    private final OnTeamSelectedListener mCallback;
    private List<Team> teamList;

    public TeamsAdapter(final OnTeamSelectedListener mCallback) {
        this(mCallback, R.layout.card_teams);
    }

    public TeamsAdapter(final OnTeamSelectedListener mCallback, final int layoutId) {
        teamList = new ArrayList<>();
        this.rowLayout = layoutId;
        this.mCallback = mCallback;
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

        BaseApplication.getPicasso()
                .load(team.getImgUrl())
                .error(R.drawable.generic_team)
                .centerInside()
                .resizeDimen(R.dimen.detail_team_image_size, R.dimen.detail_team_image_size)
                .into(viewHolder.image);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onTeamSelected(team.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamList == null ? 0 : teamList.size();
    }

    public interface OnTeamSelectedListener {
        public void onTeamSelected(final int teamId);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final ImageView image;

        public ViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.teamName);
            image = (ImageView) itemView.findViewById(R.id.teamImage);
        }

    }
}
