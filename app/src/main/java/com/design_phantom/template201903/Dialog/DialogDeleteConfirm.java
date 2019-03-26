package com.design_phantom.template201903.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

//import com.design_phantom.template201903.DB.GoalManager;
import com.design_phantom.template201903.R;

public class DialogDeleteConfirm extends AppCompatDialogFragment {

    private String dataType;
    private int id;
    private int order;
    private TextView detail;
    private String detailContents;

    //listener
    private DialogDeleteNoticeListener listener;


    public interface DialogDeleteNoticeListener{
        public void deleteResultNotice(int order);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            this.listener = (DialogDeleteNoticeListener)context;
        }catch (Exception e){
            View v = getActivity().findViewById(android.R.id.content);
            Snackbar.make(v, "DialogDeleteNoticeListener is not implemented!", Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //get data
        try{
            dataType = getArguments().getString("dataType", null);
        }catch(Exception e) {
            View v = getActivity().findViewById(android.R.id.content);
            Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
        try{
            id = getArguments().getInt("id");
        }catch(Exception e) {
            View v = getActivity().findViewById(android.R.id.content);
            Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG).show();
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.f_source, null);

        detail = view.findViewById(R.id.detail);
        if(detailContents != null){
            detail.setText(detailContents);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(view)
                .setTitle(getString(R.string.dialog_title1))
                .setNegativeButton(getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete実行する
                        if(dataType != null && id > 0 ){

                            int resultid = 0;

                            //GoalManager manager = new GoalManager(getContext());

                            switch (dataType){

                                case "goal" :
                                    //処理
                                   // manager.deleteGoal(id);
                                    resultid = 1;

                                    break;

                                case "history" :
                                    //処理
                                    //manager.deleteHistory(id);
                                    resultid = 1;

                                    break;

                                case "basicMeasure" :
                                    //処理
                                    //manager.deleteBasicMeasure(id);
                                    resultid = 1;

                                    break;

                            }

                            if(resultid > 0){
                                //notice result to activity
                                listener.deleteResultNotice(order);
                            }else{
                                View v = getActivity().findViewById(android.R.id.content);
                                Snackbar.make(v, getString(R.string.error_msg1), Snackbar.LENGTH_SHORT).show();
                            }

                        }else{

                            View v = getActivity().findViewById(android.R.id.content);
                            Snackbar.make(v, getString(R.string.error_msg2), Snackbar.LENGTH_SHORT).show();

                        }
                    }
                });

        Dialog dialog = builder.create();

        return dialog;



    }
}
