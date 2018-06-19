package com.ingreens.recycleswipedelete.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingreens.recycleswipedelete.Interfaces;
import com.ingreens.recycleswipedelete.R;
import com.ingreens.recycleswipedelete.models.Players;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeeban on 26/4/18.
 */



    public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>
implements Filterable{

        private Context context;
        private List<Players> playersList;
        private List<Players> playersFilteredList;
        private PlayersAdapterListener playersAdapterListener;
        private Interfaces.changeView changeView;

        public ImageView left_delete_imv,right_delete_imv;
        public TextView left_tv_delete,right_tv_delete;

    public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, team, strikeRate;
            public ImageView image;
            //public ImageView thumbnail;
            public RelativeLayout viewBackground, viewForeground;

            public MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.name);
                team = view.findViewById(R.id.team);
                strikeRate = view.findViewById(R.id.strikeRate);
                image=view.findViewById(R.id.thumbnail);
                //thumbnail = view.findViewById(R.id.thumbnail);
                viewBackground = view.findViewById(R.id.view_background);
                viewForeground = view.findViewById(R.id.view_foreground);

                left_delete_imv=view.findViewById(R.id.left_delete_icon);
                left_tv_delete=view.findViewById(R.id.left_tv_delete);
                right_delete_imv=view.findViewById(R.id.right_delete_icon);
                right_tv_delete=view.findViewById(R.id.right_tv_delete);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playersAdapterListener.onplayerSelected(playersFilteredList.get(getAdapterPosition()));
                    }
                });
            }
        }


        public RecycleViewAdapter(Context context, List<Players> playersList,PlayersAdapterListener playersAdapterListener,Interfaces.changeView changeView) {
            this.context = context;
            this.playersList=playersList;
            this.playersFilteredList = playersList;
            this.playersAdapterListener=playersAdapterListener;
            this.changeView=changeView;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cart_list_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Players players = playersFilteredList.get(position);
            holder.name.setText(players.getName());
            holder.team.setText(players.getTeam());
            holder.strikeRate.setText("Strike Rate: " + players.getStrike_rate());
            holder.image.setImageResource(players.getImage());
            changeView.onSwipeViewChange(left_delete_imv,left_tv_delete,right_delete_imv,right_tv_delete);
            //changeView.onSwipeViewChange(left_delete_imv,,left_tv_delete,left_tv_delete.getId(),right_delete_imv,right_delete_imv.getId(),right_tv_delete,right_tv_delete.getId());

          /*  Glide.with(context)
                    .load(item.getThumbnail())
                    .into(holder.thumbnail);*/
        }

        @Override
        public int getItemCount() {
            return playersFilteredList.size();
        }

    @Override
    public Filter getFilter() {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                System.out.println("#####################3");
                System.out.println("performFiltering(CharSequence charSequence)");
                System.out.println("#####################3");
                String chrcString=charSequence.toString();
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@2");
                System.out.println(" performFiltering charSequence(String)=="+chrcString);
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@2");
                if (chrcString.isEmpty()){
                    System.out.println("################3");
                    System.out.println("chrcString empty ache ree..");
                    System.out.println("################3");
                    playersFilteredList=playersList;
                }else {
                    System.out.println("################3");
                    System.out.println("chrcString empty nei ree.....");
                    System.out.println("################3");
                    List<Players> players=new ArrayList<>();
                    for(Players p_row:playersList){
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        /*if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                            filteredList.add(row);
                        }*/
                        if (p_row.getName().toLowerCase().contains(chrcString.toLowerCase()) || p_row.getStrike_rate().contains(charSequence)){
                            players.add(p_row);
                        }
                    }
                    playersFilteredList=players;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=playersFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@2");
                System.out.println(" publishResults charSequence(String)=="+charSequence.toString());
                System.out.println(" publishResults filterResults(String)=="+filterResults.toString());
                System.out.println(" publishResults filterResults(count)=="+filterResults.count);
                System.out.println(" publishResults filterResults(values)=="+filterResults.values);
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@2");
                System.out.println("#####################3");
                System.out.println("publishResults(CharSequence charSequence, FilterResults filterResults)");
                System.out.println("#####################3");
                playersFilteredList= (List<Players>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

        public void removeItem(int position) {
            playersFilteredList.remove(position);
            // notify the item removed by position
            // to perform recycler view delete animations
            // NOTE: don't call notifyDataSetChanged()
            notifyItemRemoved(position);
        }

        public void restoreItem(Players players, int position) {
            playersFilteredList.add(position, players);
            // notify item added by position
            notifyItemInserted(position);
        }

    public interface PlayersAdapterListener {
        void onplayerSelected(Players players);
    }
    }

