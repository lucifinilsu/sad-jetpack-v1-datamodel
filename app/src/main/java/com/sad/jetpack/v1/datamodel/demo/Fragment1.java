package com.sad.jetpack.v1.datamodel.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sad.jetpack.v1.datamodel.api.DataModelProviders;
import com.sad.jetpack.v1.datamodel.api.IDataModel;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;
import com.sad.jetpack.v1.datamodel.api.utils.LogcatUtils;

import java.util.Observable;
import java.util.Observer;

public class Fragment1 extends Fragment {
    private TextView tv_console;
    private IDataModel dataModel;
    private Observer observer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataModel= DataModelProviders.get("xxxx");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_1,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        observeData();
    }

    private void initView(){
        tv_console=getView().findViewById(R.id.console_f1);
    }

    private void observeData(){
        if (dataModel!=null){
            IDataModelResponse<String> response=dataModel.get("xxx");
            if (response!=null){
                tv_console.setText(response.body());
            }
            //观察者
            if (observer==null){
                observer=new Observer() {
                    @Override
                    public void update(Observable o, Object arg) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LogcatUtils.e("观察者接收到变化,arg="+arg);
                                IDataModelResponse<String> responseNew= (IDataModelResponse<String>) arg;
                                tv_console.setText(responseNew.body());
                            }
                        });

                    }
                };
                dataModel.addObserver(observer);
            }
        }
    }
}
