package com.possebom.teamswidgets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikpenz.iconics.IconicsDrawable;
import com.mikpenz.iconics.typeface.FontAwesome;
import com.possebom.teamswidgets.R;
import com.possebom.teamswidgets.controller.TWController;
import com.possebom.teamswidgets.model.Match;
import com.possebom.teamswidgets.model.Team;
import com.possebom.teamswidgets.util.RelativeTime;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by alexandre on 01/11/14.
 */
public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.ViewHolder> {

    private final int rowLayout;
    private Team team;
    private List<Integer> listExpanded = new ArrayList<Integer>();
    private boolean initialized = false;

    public MatchesAdapter() {
        rowLayout = R.layout.card_matches;
    }

    public static void expand(final View view) {
        view.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getMeasuredHeight();

        view.getLayoutParams().height = 0;
        view.setVisibility(View.VISIBLE);
        final Animation animation = new Animation() {
            @Override
            protected void applyTransformation(final float interpolatedTime, final Transformation transformation) {
                view.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration(250);
        view.startAnimation(animation);
    }

    public static void collapse(final View view) {
        final int initialHeight = view.getMeasuredHeight();
        final Animation animation = new Animation() {
            @Override
            protected void applyTransformation(final float interpolatedTime, final Transformation transformation) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration(250);
        view.startAnimation(animation);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Context context = viewHolder.textViewDate.getContext();

        final Match match = team.getMatches().get(i);

        if(match.equals(team.getNextMatch()) && !initialized){
            listExpanded.add(i);
            initialized = true;
        }

        viewHolder.textViewDate.setText(match.getDateFormatted());
        setIcon(viewHolder.textViewDate, FontAwesome.Icon.faw_calendar);

        viewHolder.textViewTimeRemain.setText(RelativeTime.getTime(context, match.getTimestamp()));
        setIcon(viewHolder.textViewTimeRemain, FontAwesome.Icon.faw_clock_o);

        viewHolder.textViewLeague.setText(match.getLeague());
        setIcon(viewHolder.textViewLeague, FontAwesome.Icon.faw_trophy);
        if (match.getTransmission().isEmpty()) {
            viewHolder.textViewTransmission.setVisibility(View.GONE);
        } else {
            viewHolder.textViewTransmission.setText(match.getTransmission());
            setIcon(viewHolder.textViewTransmission, FontAwesome.Icon.faw_youtube_play);
        }
        viewHolder.textViewPlace.setText(match.getPlace());
        setIcon(viewHolder.textViewPlace, FontAwesome.Icon.faw_map_marker);
        setIcon(viewHolder.textViewVisitingTeam, FontAwesome.Icon.faw_futbol_o);

        String urlOpponent = TWController.INSTANCE.getDao().getTeamLogoUrlByName(match.getVisitingTeam());

        if (match.getHome()) {
            setTeamLogo(viewHolder.imageView01, team.getImgUrl());
            setTeamLogo(viewHolder.imageView02, urlOpponent);
        } else {
            setTeamLogo(viewHolder.imageView01, urlOpponent);
            setTeamLogo(viewHolder.imageView02, team.getImgUrl());
        }

        if (TextUtils.isEmpty(match.getResult())) {
            viewHolder.textViewResult.setText(R.string.result);
        } else {
            final String result = match.getResult().replace("x", context.getString(R.string.result));
            viewHolder.textViewResult.setText(result);
        }

        if (listExpanded.contains(i)) {
            viewHolder.textViewVisitingTeam.setVisibility(View.VISIBLE);
            viewHolder.textViewTimeRemain.setVisibility(View.VISIBLE);
            viewHolder.textViewTransmission.setVisibility(View.VISIBLE);
            viewHolder.textViewPlace.setVisibility(View.VISIBLE);
        } else {
            viewHolder.textViewVisitingTeam.setVisibility(View.GONE);
            viewHolder.textViewTimeRemain.setVisibility(View.GONE);
            viewHolder.textViewTransmission.setVisibility(View.GONE);
            viewHolder.textViewPlace.setVisibility(View.GONE);
        }

        if(urlOpponent == null){
            viewHolder.textViewVisitingTeam.setVisibility(View.VISIBLE);
        }

        viewHolder.textViewVisitingTeam.setText(match.getVisitingTeam());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listExpanded.contains(i)) {
                    collapse(viewHolder.textViewVisitingTeam);
                    collapse(viewHolder.textViewTimeRemain);
                    collapse(viewHolder.textViewTransmission);
                    collapse(viewHolder.textViewPlace);
                    listExpanded.remove((Integer) i);
                } else {
                    expand(viewHolder.textViewVisitingTeam);
                    expand(viewHolder.textViewTimeRemain);
                    expand(viewHolder.textViewTransmission);
                    expand(viewHolder.textViewPlace);
                    listExpanded.add(i);
                }
            }
        });

    }

    private void setTeamLogo(final ImageView imageView, final String url) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.generic_team);
            return;
        }

        final Context context = imageView.getContext();
        Picasso.with(context)
                .load(url)
                .error(R.drawable.generic_team)
                .placeholder(R.drawable.generic_team)
                .resizeDimen(R.dimen.detail_team_image_size, R.dimen.detail_team_image_size)
                .centerInside()
                .into(imageView);
    }

    private void setIcon(final TextView textview, final FontAwesome.Icon icon) {
        final IconicsDrawable drawable = new IconicsDrawable(textview.getContext(), icon).colorRes(R.color.secondary).sizeRes(R.dimen.match_drawable_left);
        textview.setCompoundDrawables(drawable, null, null, null);
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

        public final TextView textViewDate;
        public final TextView textViewTimeRemain;
        public final TextView textViewLeague;
        public final TextView textViewTransmission;
        public final TextView textViewPlace;
        public final TextView textViewResult;
        public final TextView textViewVisitingTeam;
        public final ImageView imageView01;
        public final ImageView imageView02;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView01 = (ImageView) itemView.findViewById(R.id.imageView01);
            imageView02 = (ImageView) itemView.findViewById(R.id.imageView02);
            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            textViewTimeRemain = (TextView) itemView.findViewById(R.id.textViewTimeRemain);
            textViewLeague = (TextView) itemView.findViewById(R.id.textViewLeague);
            textViewTransmission = (TextView) itemView.findViewById(R.id.textViewTransmission);
            textViewPlace = (TextView) itemView.findViewById(R.id.textViewPlace);
            textViewResult = (TextView) itemView.findViewById(R.id.textViewResult);
            textViewVisitingTeam = (TextView) itemView.findViewById(R.id.textViewVisitingTeam);
        }

    }



}
