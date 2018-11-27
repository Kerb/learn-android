package com.firstapp.myapplication.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.firstapp.myapplication.R;

/**
 * @user: kerb
 * @created: 26/11/2018.
 */
public class MyFragment extends Fragment {
    
    public static final String TAG = MyFragment.class.getCanonicalName();

    private Button fragmentButton;

    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // setRetainInstance(true) - фрагмент не будет убиваться.
        // Использовать с осторожностью
        // Переживает смену языка системы

        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        fragmentButton = view.findViewById(R.id.fragment_button);
        textView = view.findViewById(R.id.fragment_text);
    }
}
