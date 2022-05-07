package com.example.mybookkeeper.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mybookkeeper.R;
import com.example.mybookkeeper.SqliteDatabase;
import com.example.mybookkeeper.databinding.FragmentHomeBinding;
import com.example.mybookkeeper.managers.Manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    EditText eAttempts, ePhone, ePassword;
    Button buttonOpen;
    private FragmentHomeBinding binding;
    Manager manager;
    SqliteDatabase mDatabase;
    int counter = 5;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout adminButton = view.findViewById(R.id.admin_button);
        RelativeLayout loginButton = view.findViewById(R.id.login_btn);
        RelativeLayout passwordButton = view.findViewById(R.id.btnChangePassword);

        mDatabase = new SqliteDatabase(getActivity());
        ePhone = view.findViewById(R.id.edPhone);
        ePassword = view.findViewById(R.id.edPassword);
        eAttempts = view.findViewById(R.id.edAttempts);
        ePhone.setText("0724895791");
        buttonOpen = view.findViewById(R.id.btnOpen);


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
            if (ePhone.getText().equals("") || ePassword.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Phone or password is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            manager = mDatabase.searchManagerByPhone(ePhone.getText().toString(), ePassword.getText().toString());
            if (manager == null) {
                Toast.makeText(getActivity(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                counter--;
                eAttempts.setText(Integer.toString(counter));

                if (counter == 0) {
                    adminButton.setEnabled(false);
                    Toast.makeText(getActivity(), "ARE YOU THE ADMINISTRATOR??",Toast.LENGTH_SHORT).show();
                }
            } else {
                Bundle args = new Bundle();
                args.putString("phoneFromHome", ePhone.getText().toString());
                args.putString("pWordFromHome", ePassword.getText().toString());
                args.putInt("mngIdFromHome", manager.getManagerID());
                args.putString("mngNameFromHome", manager.getManagerName());
                args.putString("startDate", firstDayOfMonthStr);
                args.putString("endDate", lastDayOfMonthStr);
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_HomeFragment_to_ManagerReceiptsFragment, args);
            }
        });

        buttonOpen.setOnClickListener(v -> {
            Bundle args = new Bundle();
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_HomeFragment_to_ManagerReceiptsFragment, args);

        });

        loginButton.setOnClickListener(v -> {
            if (ePhone.getText().equals("") || ePassword.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Phone or password is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            manager = mDatabase.searchManagerByPhone(ePhone.getText().toString(), ePassword.getText().toString());
            if (manager == null) {
                Toast.makeText(getActivity(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                counter--;
                eAttempts.setText(Integer.toString(counter));

                if (counter == 0) {
                    adminButton.setEnabled(false);
                    Toast.makeText(getActivity(), "ARE YOU THE ADMINISTRATOR??",Toast.LENGTH_SHORT).show();
                }
            } else {
                Bundle args = new Bundle();
                args.putString("phoneFromHomeLgn", ePhone.getText().toString());
                args.putString("pWordromHomeLgn", ePassword.getText().toString());
                args.putInt("mngIdFromHomeLgn", manager.getManagerID());
                args.putString("mngNameFromHomeLgn", manager.getManagerName());
                args.putString("originPage", "FromHomeLgn");
                args.putString("btnState", "hideButton");
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_HomeFragment_to_AccountReceiptsFragment, args);
            }
        });

        passwordButton.setOnClickListener(v -> {
                manager = mDatabase.searchManagerByPhone(ePhone.getText().toString(), ePassword.getText().toString());
                if (manager == null) {
                    Toast.makeText(getActivity(), "Phone or password is empty", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    Bundle args = new Bundle();
                    args.putString("phoneFromHomePwd", ePhone.getText().toString());
                    args.putString("pWordFromHomePwd", ePassword.getText().toString());
                    args.putInt("mngIdFromHomePwd", manager.getManagerID());
                    args.putString("mngNameFromHomePwd", manager.getManagerName());
                    NavHostFragment.findNavController(HomeFragment.this)
                            .navigate(R.id.action_HomeFragment_to_RegisterFragment, args);
                }
            }
        );
    }
}