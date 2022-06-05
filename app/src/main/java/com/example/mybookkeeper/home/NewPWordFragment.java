package com.example.mybookkeeper.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mybookkeeper.MainActivity;
import com.example.mybookkeeper.R;
import com.example.mybookkeeper.data.UIDataStore;
import com.example.mybookkeeper.managers.Manager;

public class NewPWordFragment extends Fragment {

    private EditText eName;
    private EditText ePassword;
    private Button eRegister;
    private TextView eAttemptsInfo;
    private CheckBox eRememberMe;
    Manager manager;
    UIDataStore mDatabase;

    String mngNameFromRegister;
    String phoneFromRegister;
    String pWordFromRegister;
    int mngIdFromRegister;
    private int counter = 5;
    private ProgressDialog progress;

    public NewPWordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_p_word, container, false);
        mDatabase = new UIDataStore(getActivity());
        eName = v.findViewById(R.id.tvName);
        ePassword = v.findViewById(R.id.enterNewPWord);
        eRegister = v.findViewById(R.id.Submit);
        if (getArguments() != null) {
            mngNameFromRegister = getArguments().getString("mngNameFromRegister");
            eName.setText(mngNameFromRegister);
            mngIdFromRegister = getArguments().getInt("mngIdFromRegister");
            phoneFromRegister = getArguments().getString("phoneFromRegister");
            pWordFromRegister = getArguments().getString("pWordFromRegister");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(mngNameFromRegister);
        } else {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO MANAGER SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED MANAGER NOT FOUND");
        }

        eRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ePassword.getText().equals("") || eName.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Phone or password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String newPw = ePassword.getText().toString();
                String name = eName.getText().toString();
                onRegister(newPw);
            }
        });
        return v;
    }

    private void onRegister(String newPw) {
        showProgressDialog("Registering...");
        UIDataStore.UiData<Manager> uiData = mDatabase.searchManagerByPassword(newPw);
        uiData.observe(getViewLifecycleOwner(), new Observer<UIDataStore.Result<Manager>>() {
            @Override
            public void onChanged(UIDataStore.Result<Manager> managerResult) {
                Manager newManager = managerResult.getResult();
                if (newManager == null) {
                    Toast.makeText(getActivity(), "Wrong Password", Toast.LENGTH_SHORT).show();
                    closeProgressDialog();
                } else {
                    Bundle args = new Bundle();
                    args.putInt("mngIdFromNewPwd", mngIdFromRegister);
                    args.putString("mngNameFromNewPwd", mngNameFromRegister);
                    args.putString("mngPhoneFromNewPwd", phoneFromRegister);
                    args.putString("mngPWoreFromNewPwd", pWordFromRegister);
                    args.putString("originPage", "FromNewPwd");
                    args.putString("btnState", "hideButton");
                    closeProgressDialog();
                    NavHostFragment.findNavController(NewPWordFragment.this)
                            .navigate(R.id.action_NewPWordFragment_to_AccountReceiptsFragment, args);
                }
            }
        });
    }

    private void closeProgressDialog() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    private void showProgressDialog(String message) {
        if (progress == null) {
            progress = new ProgressDialog(getContext());

            progress.setMessage(message);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.setIndeterminate(true); progress.show();
        }
    }
}