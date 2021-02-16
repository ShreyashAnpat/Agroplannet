package com.e.agroplannet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.e.agroplannet.Adapter.searchAdapter;
import com.e.agroplannet.ui.gallery.GalleryFragment;
import com.e.agroplannet.ui.home.HomeFragment;
import com.e.agroplannet.ui.slideshow.SlideshowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.customview.widget.Openable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;

public class NevigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private BottomNavigationView bottomNavigationView ;
    List<String> Image1, details, sellingPrice , productMRP ;
    searchAdapter adapter ;
    FirebaseFirestore db ;
    View fragment ;
    RecyclerView searchList;
    Fragment NewFragment ;
    Context context ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nevigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchList = findViewById(R.id.searchItem);
        context = getApplicationContext() ;
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        bottomNavigationView = findViewById(R.id.botom_nav);
        NavigationView navigationView = findViewById(R.id.nav_view);
        db =FirebaseFirestore.getInstance() ;
        fragment = findViewById(R.id.nav_host_fragment);
        productMRP = new ArrayList<>();
        Image1 = new ArrayList<>();
        details = new ArrayList<>();
        sellingPrice = new ArrayList<>();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nevigation, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);

        androidx.appcompat.widget.SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setBackgroundResource(R.drawable.search_background);
        searchView.setQueryHint("Search");
        searchView.setIconifiedByDefault(true);
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    fragment.setVisibility(View.GONE);
                    searchList.setVisibility(View.VISIBLE);
                    Query query = FirebaseFirestore.getInstance().collection("Product").orderBy("Product_Details").startAt(newText ).endAt(newText+"\uf9ff" );
                    query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            Image1.clear();
                            details.clear();
                            sellingPrice.clear();
                            productMRP.clear();
                            for (DocumentChange doc : value.getDocumentChanges()){
                                if (doc.getType()== DocumentChange.Type.ADDED){
                                    Image1.add(doc.getDocument().get("Image1").toString());
                                    details.add(doc.getDocument().get("Product_Details").toString());
                                    sellingPrice.add(doc.getDocument().get("Product_Selling_Price").toString());
                                    productMRP.add(doc.getDocument().get("MRP").toString());
                                }
                                else {
                                    Image1.clear();
                                    details.clear();
                                    sellingPrice.clear();
                                    productMRP.clear();
                                }

                            }
                            searchList.setLayoutManager(new LinearLayoutManager(NevigationActivity.this));
                            adapter = new searchAdapter(NevigationActivity.this , Image1,details,sellingPrice,productMRP);
                            searchList.setAdapter(adapter);
                        }
                    });
                }
                else {
                    Image1.clear();
                    details.clear();
                    sellingPrice.clear();
                    productMRP.clear();
                    fragment.setVisibility(View.VISIBLE);
                    searchList.setVisibility(View.GONE);
                }

                return false;
            }
        });
        return true;
    }




    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}