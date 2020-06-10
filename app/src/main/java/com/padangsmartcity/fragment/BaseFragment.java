package com.padangsmartcity.fragment;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    public abstract void findView();
    public abstract void initView();
    public abstract void initListener();
}
