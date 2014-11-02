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

import possebom.com.teamswidgets.DetailActivity;
import possebom.com.teamswidgets.MainActivity;
import possebom.com.teamswidgets.R;
import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.model.Match;
import possebom.com.teamswidgets.model.Team;
import possebom.com.teamswidgets.util.Log;

/**
 * Created by alexandre on 01/11/14.
 */
public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.ViewHolder> {

    private Team team;
    private int rowLayout;
    private DetailActivity mAct;

    public MatchesAdapter(int rowLayout, DetailActivity act) {
        this.rowLayout = rowLayout;
        this.mAct = act;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Match match = team.getMatches().get(i);
        viewHolder.textViewDate.setText("{fa-calendar} " + match.getTimestamp());
        viewHolder.textViewTimeRemain.setText("{fa-clock-o} " + match.getTimestamp());
        viewHolder.textViewLeague.setText("{fa-trophy} " +match.getLeague());
        if(match.getTransmission().isEmpty()){
            viewHolder.textViewTransmission.setVisibility(View.GONE);
        }else {
            viewHolder.textViewTransmission.setText("{fa-eye} " + match.getTransmission());
        }
        viewHolder.textViewPlace.setText("{fa-location-arrow} " +match.getPlace());

        String url01;
        String url02;

        Team opponent = TWController.INSTANCE.getDao().getTeamByName(match.getOpponent());
        String urlOpponent = null;
        if (opponent != null) {
            urlOpponent = opponent.getImgUrl();
        }

        if (match.getHome()) {
            url01 = team.getImgUrl();
            url02 = urlOpponent;
        } else {
            url01 = urlOpponent;
            url02 = team.getImgUrl();
        }

        Picasso.with(mAct)
                .load(url01)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.drawer_shadow)
                .into(viewHolder.imageView01);

        Picasso.with(mAct)
                .load(url02)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.drawer_shadow)
                .into(viewHolder.imageView02);
    }

    @Override
    public int getItemCount() {
        return team == null ? 0 : team.getMatches().size();
    }

    public void setTeam(final Team team) {
        this.team = team;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewDate;
        public TextView textViewTimeRemain;
        public TextView textViewLeague;
        public TextView textViewTransmission;
        public TextView textViewPlace;
        public ImageView imageView01;
        public ImageView imageView02;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView01 = (ImageView) itemView.findViewById(R.id.imageView01);
            imageView02 = (ImageView) itemView.findViewById(R.id.imageView02);
            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            textViewTimeRemain = (TextView) itemView.findViewById(R.id.textViewTimeRemain);
            textViewLeague = (TextView) itemView.findViewById(R.id.textViewLeague);
            textViewTransmission = (TextView) itemView.findViewById(R.id.textViewTransmission);
            textViewPlace = (TextView) itemView.findViewById(R.id.textViewPlace);
        }

    }
}
