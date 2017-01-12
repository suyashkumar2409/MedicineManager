package com.example.suyashkumar.medicinescheduler;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LauncherActivity extends Activity {

    public static final String EXTRA_POSITION = "extra position";
    private static final String VISIBLE_FRAG = "visible_fragment";

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String[] titles;
    private int currentPosition = 0;

    private class DrawerItemListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
            setFragment(position);
        }
    }

//    ************* Fragment choosing code *********************
    public void setFragment(int position){
        Fragment frag;

        switch (position){
            case 0:
                frag = new TopActivity();
                break;
            case 1:
                frag = new MedicineList();
                break;
            case 2:
                frag = new InventoryList();
                break;
            default:
                frag = new TopActivity();
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.container_frame, frag, VISIBLE_FRAG);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        ft.commit();

        setTitleName(position);

        drawerLayout.closeDrawer(drawerList);
    }
//    ************* All activity related methods below ******************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        titles = getResources().getStringArray(R.array.drawerList);
        drawerList = (ListView)findViewById(R.id.drawer_list);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        drawerList.setAdapter(adapter);

        if(savedInstanceState == null){
            setFragment(0);
        }
        else
        {
            currentPosition = savedInstanceState.getInt(EXTRA_POSITION);
            setTitleName(currentPosition);
        }

        drawerList.setOnItemClickListener(new DrawerItemListener());

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_closed){

            @Override
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View view){
                super.onDrawerOpened(view);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        getFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        Fragment frag = getFragmentManager().findFragmentByTag(VISIBLE_FRAG);

                        if(frag instanceof TopActivity){
                            currentPosition = 0;
                        }


                        if(frag instanceof MedicineList){
                            currentPosition = 1;
                        }


                        if(frag instanceof InventoryList){
                            currentPosition = 2;
                        }

                        setTitleName(currentPosition);
                        drawerList.setItemChecked(currentPosition, true);
                    }
                }
        );
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.action_create).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration config){
        super.onConfigurationChanged(config);
        drawerToggle.onConfigurationChanged(config);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(EXTRA_POSITION, currentPosition);
    }

//    *********** Action Bar functions ****************
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch(item.getItemId()){
            case R.id.action_create:
                Intent intent = new Intent(this, NewMedicine.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTitleName(int position) {
        String title;
        if (position == 0) {
            title = getResources().getString(R.string.app_name);
        } else {
            title = titles[position];
        }
        getActionBar().setTitle(title);
    }
}
