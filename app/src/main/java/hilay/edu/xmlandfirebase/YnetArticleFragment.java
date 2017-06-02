package hilay.edu.xmlandfirebase;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import hilay.edu.xmlandfirebase.dummy.DummyContent;
import hilay.edu.xmlandfirebase.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * A fragment representing a list of Ynet Items.
 */
public class YnetArticleFragment extends Fragment implements YnetDataSource.OnYnetArriveListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_ynetarticle_list, container, false);

        YnetDataSource.getYnet(this);
        recyclerView.setAdapter(new YnetRecyclerAdapter(DummyContent.ITEMS));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return recyclerView;
    }

    @Override
    public void onYnetArrived(final List<YnetDataSource.Ynet> data, final Exception e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e == null){

                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
