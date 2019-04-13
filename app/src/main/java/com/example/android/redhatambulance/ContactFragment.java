package com.example.android.redhatambulance;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {
    View root_view;
    Button mum;
    Button dad;
    Button bro;
    Button wife;


    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root_view= inflater.inflate(R.layout.fragment_contact, container, false);

        mum=(Button)root_view.findViewById(R.id.mum);
        mum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + "7891028911"));
                    startActivity(intent);


            }
        });
        dad=(Button)root_view.findViewById(R.id.dad);
        dad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "7891028911"));
                startActivity(intent);

            }
        });
        bro=(Button)root_view.findViewById(R.id.bro);
        bro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "7891028911"));
                startActivity(intent);

            }
        });
        wife=(Button)root_view.findViewById(R.id.wife);
        wife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "7891028911"));
                startActivity(intent);

            }
        });

        return root_view;
    }

}
