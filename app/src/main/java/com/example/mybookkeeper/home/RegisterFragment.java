package com.example.mybookkeeper.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mybookkeeper.MainActivity;
import com.example.mybookkeeper.R;
import com.example.mybookkeeper.data.UIDataStore;
import com.example.mybookkeeper.managers.Manager;

public class RegisterFragment extends Fragment {

    private EditText eOldPassword;
    private EditText eNewPassword;
    private EditText eName;
    private Button eRegister;
    private Button eCancel;
    private UIDataStore mDatabase;
    Manager manager;

    String mngNameFromHomePwd;
    String phoneFromHomePwd, pWordFromHomePwd;
    int mngIdFromHomePwd;
    private ProgressDialog progress;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        eOldPassword = v.findViewById(R.id.oldPassword);
        eOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        eOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eNewPassword = v.findViewById(R.id.newPassword);
        eNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        eNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eName = v.findViewById(R.id.fullName);
        eRegister = v.findViewById(R.id.Submit);
        mDatabase = UIDataStore.getInstance();
        if (getArguments() != null) {
            mngNameFromHomePwd = getArguments().getString("mngNameFromHomePwd");
            eName.setText(mngNameFromHomePwd);
            mngIdFromHomePwd = getArguments().getInt("mngIdFromHomePwd");
            phoneFromHomePwd = getArguments().getString("magNameFromHomePwd");
            pWordFromHomePwd = getArguments().getString("pWordFromHomePwd");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(mngNameFromHomePwd);

        } else {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO MANAGER SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED MANAGER NOT FOUND");
        }

        eRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eOldPassword.getText().equals("") || eNewPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Phone or password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (eNewPassword.getText().toString().length() < 4
                        || eNewPassword.getText().toString().length() > 4) {

                    eNewPassword.requestFocus();
                    eNewPassword.setError("Password should be 4 characters long");
                    return;
                }
                String oldPw = eOldPassword.getText().toString();
                String name = eName.getText().toString();
                registerOrUpdateManager(oldPw, name);
            }
        });
        return v;
    }

    private void registerOrUpdateManager(String oldPw, String name) {
        showProgressDialog("Registering Manager...");
        UIDataStore.UiData<Manager> uiData = mDatabase.searchManagerByPassword(oldPw);
        uiData.observe(getViewLifecycleOwner(), new Observer<UIDataStore.Result<Manager>>() {
            @Override
            public void onChanged(UIDataStore.Result<Manager> managerResult) {
                Manager newManager = managerResult.getResult();
                if (newManager == null) {
                    Toast.makeText(RegisterFragment.this.getActivity(), name + "  Wrong Password  " + oldPw, Toast.LENGTH_SHORT).show();
                    RegisterFragment.this.closeProgressDialog();
                } else {
                    String newPassword = eNewPassword.getText().toString();
                    newManager.setManagerPassword(newPassword);
                    UIDataStore.UiData<Void> updateManagers = mDatabase.updateManagers(newManager);
                    updateManagers.observe(RegisterFragment.this.getViewLifecycleOwner(), voidResult -> {
                        Bundle args = new Bundle();
                        args.putInt("mngIdFromRegister", mngIdFromHomePwd);
                        args.putString("mngNameFromRegister", eName.getText().toString());
                        args.putString("phoneFromRegister", phoneFromHomePwd);
                        args.putString("pWordFromRegister", pWordFromHomePwd);
                        RegisterFragment.this.closeProgressDialog();
                        NavHostFragment.findNavController(RegisterFragment.this)
                                .navigate(R.id.action_RegisterFragment_to_NewPWordFragment, args);
                    });
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
            progress.setIndeterminate(true);
            progress.show();
        }
    }
}