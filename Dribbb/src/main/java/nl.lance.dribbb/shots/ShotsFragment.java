package nl.lance.dribbb.shots;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;

import com.lance.dribbb.R;

import nl.lance.dribbb.adapter.ContentShotsAdapter;
import nl.lance.dribbb.network.ShotsData;
import nl.lance.dribbb.views.FooterState;

public class ShotsFragment extends Fragment {
  
  private ShotsData data;
  private static int pageDebut = 1, pagePopular = 1, pageEveryone = 1, page = 1;
  private String currentUrl = null;
  private ContentShotsAdapter adapter;
  final FooterState footerState = new FooterState();
  
  public ShotsFragment(Activity a, String Url, int padding) {
    data = new ShotsData(a);
    currentUrl = Url;
    adapter = new ContentShotsAdapter(a, data.getList(), padding);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_shots, null);
    final GridView gridView = (GridView)rootView.findViewById(R.id.shots_grid);
    gridView.setAdapter(adapter);
    gridView.setOnScrollListener(new OnScrollListener() {
      
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {
        
      }
      
      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(footerState.getState() == nl.lance.dribbb.views.FooterState.State.Loading || footerState.getState() == nl.lance.dribbb.views.FooterState.State.TheEnd) {
          return;
        }
        if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && totalItemCount != 2 && adapter.getCount() > 0) {
          footerState.setState(nl.lance.dribbb.views.FooterState.State.Loading);
          data.getShotsRefresh(currentUrl + getCurrentPage(), adapter, footerState);
          adapter.notifyDataSetChanged();
          Log.i("GRIDVIEW", "BOTTOM");
        }
      }
    });
    
    initGridView(1, gridView);
    return rootView;
  }
  
  private void initGridView(int page, GridView gridView) {
    data.getShotsRefresh(currentUrl + page, adapter, footerState);
  }
  
  private int getCurrentPage(){
    if(currentUrl.equals(nl.lance.dribbb.network.DribbbleAPI.SHOTS_DEBUTS)){
      return ++ pageDebut;
    } else if (currentUrl.equals(nl.lance.dribbb.network.DribbbleAPI.SHOTS_POPULAR)) {
      return ++ pagePopular;
    } else if (currentUrl.equals(nl.lance.dribbb.network.DribbbleAPI.SHOTS_EVERYONE)){
      return ++ pageEveryone;
    } else {
      return ++ page;
    }
  }
  
  @Override
  public void onStop() {
    page = pageDebut = pageEveryone = pagePopular = 1;
    super.onStop();
  }

}