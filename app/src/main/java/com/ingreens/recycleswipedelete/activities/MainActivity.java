package com.ingreens.recycleswipedelete.activities;

import android.content.ClipData;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.ingreens.recycleswipedelete.R;
import com.ingreens.recycleswipedelete.adapters.RecycleViewAdapter;
import com.ingreens.recycleswipedelete.helpers.RecyclerItemTouchHelper;
import com.ingreens.recycleswipedelete.models.Players;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Players> playersLists=new ArrayList<>();
    private RecycleViewAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;

    // url to fetch menu json
    private static final String URL = "https://api.androidhive.info/json/menu.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("My Players");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        playersLists = new ArrayList<>();
        mAdapter = new RecycleViewAdapter(this, playersLists);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        // making http call and fetching menu json
        //prepareCart();
        preparePlayers();
    }

    private void preparePlayers() {
        Players players = new Players(1,"Rohit Sharma","Mumbai Indians",147.56);
        playersLists.add(players);

        players = new Players(2,"Shikhar Dhawan","Sunriser Hydrabad",137.22);
        playersLists.add(players);
        players = new Players(3,"Virat Kohli","Royal Challengers Bangalore",157.22);
        playersLists.add(players);
        players = new Players(4,"Sreyash Lyer","Delhi Daredevils",139.22);
        playersLists.add(players);
        players = new Players(5,"Suresh Raina","Chennai Super Kings",137.22);
        playersLists.add(players);
        players = new Players(6,"M.S Dhoni","Chennai Super Kings",137.22);
        playersLists.add(players);
        players = new Players(7,"Hardik Pandya","Mumbai Indians",137.22);
        playersLists.add(players);
        players = new Players(8,"Kuldip Yadav","Kolkata Knight Riders",137.22);
        playersLists.add(players);
        players = new Players(9,"Yubendra Chahal","Royal Challengers Bangalore",137.22);
        playersLists.add(players);
        players = new Players(10,"Bhubeneswar Kumar","Sunriser Hydrabad",127.22);
        playersLists.add(players);
        players = new Players(11,"Jasprit Bhumrah","Mumbai Indians",112.22);
        playersLists.add(players);


        mAdapter.notifyDataSetChanged();

    }

    /**
     * method make volley network call and parses json
     */
  /*  private void prepareCart() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the menu! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Item> items = new Gson().fromJson(response.toString(), new TypeToken<List<Item>>() {
                        }.getType());

                        // adding items to cart list
                        cartList.clear();
                        cartList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }*/

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof RecycleViewAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = playersLists.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            //final Item deletedItem = playersLists.get(viewHolder.getAdapterPosition());
            final Players deletedItem = playersLists.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from list!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(MainActivity.this, "undo hochhe re vaiii!!", Toast.LENGTH_LONG).show();
                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds cartList to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*@Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

    }*/
}