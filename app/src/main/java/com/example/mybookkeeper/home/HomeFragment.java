package com.example.mybookkeeper.home;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mybookkeeper.R;
import com.example.mybookkeeper.data.UIDataStore;
import com.example.mybookkeeper.data.samis.OnlineDataStore;
import com.example.mybookkeeper.databinding.FragmentHomeBinding;
import com.example.mybookkeeper.managers.Manager;
import com.example.mybookkeeper.managers.ManagerTotal;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    EditText eAttempts;
    private FragmentHomeBinding binding;
    UIDataStore mDatabase;
    int counter = 5;
    private RelativeLayout adminButton;
    private ProgressDialog progress;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adminButton = view.findViewById(R.id.admin_button);
        RelativeLayout loginButton = view.findViewById(R.id.login_btn);
        RelativeLayout passwordButton = view.findViewById(R.id.btnChangePassword);
        RelativeLayout openButton = view.findViewById(R.id.btnOpen);

        mDatabase = UIDataStore.getInstance();
        final EditText ePhone = view.findViewById(R.id.edPhone);
        final EditText ePassword = view.findViewById(R.id.edPassword);
        eAttempts = view.findViewById(R.id.edAttempts);
        ePhone.setText(mDatabase.getFirstManagerId());

        Calendar calendar = Calendar.getInstance();
        // Get the current date
        Date today = calendar.getTime();
        // Setting the first day of month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = calendar.getTime();
        // Move to next month
        calendar.add(Calendar.MONTH, 1);
        // setting the 1st day of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // Move a day back from the date
        calendar.add(Calendar.DATE, -1);
        Date lastDayOfMonth = calendar.getTime();
        // Formatting the date
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//        String todayStr = sdf.format(today);
        String firstDayOfMonthStr = sdf.format(firstDayOfMonth);
        String lastDayOfMonthStr = sdf.format(lastDayOfMonth);

        adminButton.setOnClickListener(v -> {
            if ("".equals(ePhone.getText().toString()) || ePassword.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Phone or password is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            processAdminLogin(ePhone.getText().toString(), ePassword.getText().toString());
        });

        openButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_HomeFragment_to_ManagerReceiptsFragment, args);

        });

        loginButton.setOnClickListener(v -> {
            if (ePhone.getText().equals("") || ePassword.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Phone or password is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            processSimpleLogin(ePhone.getText().toString(), ePassword.getText().toString());
        });

        passwordButton.setOnClickListener(v -> {
                    processPasswordClickLogin(ePhone.getText().toString(), ePassword.getText().toString());
                }
        );
    }

    private void processPasswordClickLogin(String phoneNp, String password) {
        showProgressDialog("Logging in...");
        UIDataStore.UiData<Manager> managerUiData = mDatabase.searchManagerByPhone(phoneNp, password);
        managerUiData.observe(getViewLifecycleOwner(), managerResult -> {

            Manager manager = managerResult.getResult();
            if (manager == null) {
                String msg = managerResult.getErrorMessage().isEmpty() ? "Phone or password is empty"
                        : managerResult.getErrorMessage();
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                closeProgressDialog();
            } else {
                Bundle args = new Bundle();
                args.putString("phoneFromHomePwd", phoneNp);
                args.putString("pWordFromHomePwd", password);
                args.putInt("mngIdFromHomePwd", manager.getManagerID());
                args.putString("mngNameFromHomePwd", manager.getManagerName());
                closeProgressDialog();
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_HomeFragment_to_RegisterFragment, args);
            }
        });

    }

    private void processSimpleLogin(String phone, String password) {
        showProgressDialog("Logging in...");
        UIDataStore.UiData<Manager> managerUiData = mDatabase.searchManagerByPhone(phone, password);
        managerUiData.observe(getViewLifecycleOwner(), managerResult -> {
            onSimpleLoginresult(phone, password, managerResult);
            closeProgressDialog();
        });
    }


    private void processAdminLogin(String phoneNo, String passowrd) {
        showProgressDialog("Logging in...");
        UIDataStore.UiData<Manager> managerUiData = mDatabase.searchManagerByPhone(phoneNo, passowrd);
        managerUiData.observe(getViewLifecycleOwner(), managerResult -> {
            onAdminLoginResult(phoneNo, passowrd, managerResult);
            closeProgressDialog();
        });
    }

    private void onAdminLoginResult(String phoneNo, String passowrd, UIDataStore.Result<Manager> managerResult) {
        Manager manager = managerResult.getResult();
        if (manager == null) {
            final String msg = managerResult.getErrorMessage().isEmpty() ? "Wrong Credentials"
                    : managerResult.getErrorMessage();
            Log.e("API ERROR", msg);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            counter--;
            eAttempts.setText(Integer.toString(counter));

            if (counter == 0) {
                adminButton.setEnabled(false);
                Toast.makeText(getActivity(), "ARE YOU THE ADMINISTRATOR??", Toast.LENGTH_SHORT).show();
            }
        } else {
            Bundle args = new Bundle();
            args.putString("phoneFromHome", phoneNo);
            args.putString("pWordFromHome", passowrd);
            args.putInt("mngIdFromHome", manager.getManagerID());
            args.putString("mngNameFromHome", manager.getManagerName());
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_HomeFragment_to_ManagerReceiptsFragment, args);
        }
    }

    private void onSimpleLoginresult(String phone, String password, UIDataStore.Result<Manager> managerResult) {
        Manager manager = managerResult.getResult();
        if (manager == null) {
            final String msg = managerResult.getErrorMessage().isEmpty() ? "Wrong Credentials"
                    : managerResult.getErrorMessage();
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            counter--;
            eAttempts.setText(Integer.toString(counter));

            if (counter == 0) {
                adminButton.setEnabled(false);
                Toast.makeText(getActivity(), "ARE YOU THE ADMINISTRATOR??", Toast.LENGTH_SHORT).show();
            }
        } else {
            Bundle args = new Bundle();
            args.putString("phoneFromHomeLgn", phone);
            args.putString("pWordromHomeLgn", password);
            args.putInt("mngIdFromHomeLgn", manager.getManagerID());
            args.putString("mngNameFromHomeLgn", manager.getManagerName());
            args.putString("originPage", "FromHomeLgn");
            args.putString("btnState", "hideButton");
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_HomeFragment_to_AccountReceiptsFragment, args);
        }
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

    @Override
    public void onStart() {
        super.onStart();
    }
}