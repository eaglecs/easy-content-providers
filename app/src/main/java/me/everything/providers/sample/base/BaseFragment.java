package me.everything.providers.sample.base;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import me.everything.providers.sample.R;

public class BaseFragment extends Fragment {

    protected void setToolbarTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void setFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (Exception e) {
        }
    }

    protected void setFragment(Class<? extends Fragment> fragmentCls, Integer param) {
        try {
            Fragment fragment = fragmentCls.newInstance();
            if (param != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("param", param);
                fragment.setArguments(bundle);
            }
            setFragment(fragment);
        } catch (Exception e) {
        }
    }

    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

}
