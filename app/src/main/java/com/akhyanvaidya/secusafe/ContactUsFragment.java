package com.akhyanvaidya.secusafe;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by akhyanvaidya on 24/04/16.
 */
public class ContactUsFragment extends Fragment {

    TextView cuNumber;
    View myView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.contactus_layout, container, false);
        cuNumber=(TextView)myView.findViewById(R.id.cuNumber); //get the id
        cuNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            //When the phone number is click the app goes to the call function of the phone to dial the number but not call it
            public void onClick(View v) {
                String phoneNumber="0296494477";

                Intent dial=new Intent(Intent.ACTION_DIAL);//this will dial the number
                dial.setData(Uri.parse("tel:"+phoneNumber));//"tel:" is needed
                startActivity(dial);
            }
        });



        return myView;

    }
}
