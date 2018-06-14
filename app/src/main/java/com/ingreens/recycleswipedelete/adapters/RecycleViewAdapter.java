package com.ingreens.recycleswipedelete.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingreens.recycleswipedelete.R;
import com.ingreens.recycleswipedelete.models.Players;

import java.util.List;

/**
 * Created by jeeban on 26/4/18.
 */



    public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
        private Context context;
        private List<Players> playersList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, team, strikeRate;
            //public ImageView thumbnail;
            public RelativeLayout viewBackground, viewForeground;

            public MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.name);
                team = view.findViewById(R.id.team);
                strikeRate = view.findViewById(R.id.strikeRate);
                //thumbnail = view.findViewById(R.id.thumbnail);
                viewBackground = view.findViewById(R.id.view_background);
                viewForeground = view.findViewById(R.id.view_foreground);
            }
        }


        public RecycleViewAdapter(Context context, List<Players> playersList) {
            this.context = context;
            this.playersList = playersList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cart_list_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Players players = playersList.get(position);
            holder.name.setText(players.getName());
            holder.team.setText(players.getTeam());
            holder.strikeRate.setText("Strike Rate: " + players.getStrike_rate());

          /*  Glide.with(context)
                    .load(item.getThumbnail())
                    .into(holder.thumbnail);*/
        }

        @Override
        public int getItemCount() {
            return playersList.size();
        }

        public void removeItem(int position) {
            playersList.remove(position);
            // notify the item removed by position
            // to perform recycler view delete animations
            // NOTE: don't call notifyDataSetChanged()
            notifyItemRemoved(position);
        }

        public void restoreItem(Players players, int position) {
            playersList.add(position, players);
            // notify item added by position
            notifyItemInserted(position);
        }
    }

