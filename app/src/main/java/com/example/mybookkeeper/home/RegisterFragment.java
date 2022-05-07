package com.example.mybookkeeper.home;

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
import androidx.navigation.fragment.NavHostFragment;

import com.example.mybookkeeper.MainActivity;
import com.example.mybookkeeper.R;
import com.example.mybookkeeper.SqliteDatabase;
import com.example.mybookkeeper.managers.Manager;

public class RegisterFragment extends Fragment {

    private EditText eOldPassword;
    private EditText eNewPassword;
    private EditText eName;
    private Button eRegister;
    private Button eCancel;
    private SqliteDatabase mDatabase;
    Manager manager;

    String mngNameFromHomePwd;
    String phoneFromHomePwd, pWordFromHomePwd;
    int mngIdFromHomePwd;

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
        mDatabase = new SqliteDatabase(getActivity());
        if (getArguments() != null){
            mngNameFromHomePwd = getArguments().getString("mngNameFromHomePwd");
            eName.setText(mngNameFromHomePwd);
            mngIdFromHomePwd = getArguments().getInt("mngIdFromHomePwd");
            phoneFromHomePwd = getArguments().getString("magNameFromHomePwd");
            pWordFromHomePwd = getArguments().getString("pWordFromHomePwd");
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Change Password:");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(mngNameFromHomePwd);

        }else {
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
            Manager newManager = mDatabase.searchManagerByPassword(oldPw);
            if (newManager == null) {
                Toast.makeText(getActivity(), name+"  Wrong Password  "+oldPw, Toast.LENGTH_SHORT).show();
                return;
            } else {
                String newPassword = eNewPassword.getText().toString();
                newManager.setManagerPassword(newPassword);
                mDatabase.updateManagers(newManager);
                Bundle args = new Bundle();
                args.putInt("mngIdFromRegister", mngIdFromHomePwd);
                args.putString("mngNameFromRegister", eName.getText().toString());
                args.putString("phoneFromRegister", phoneFromHomePwd);
                args.putString("pWordFromRegister", pWordFromHomePwd);
                NavHostFragment.findNavController(RegisterFragment.this)
                        .navigate(R.id.action_RegisterFragment_to_NewPWordFragment, args);
            }
        }
        });
        return v;
    }
}