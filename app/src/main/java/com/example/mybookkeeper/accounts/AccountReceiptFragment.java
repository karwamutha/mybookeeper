package com.example.mybookkeeper.accounts;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.MainActivity;
import com.example.mybookkeeper.R;
import com.example.mybookkeeper.SqliteDatabase;
import com.example.mybookkeeper.clients.ClientTotal;
import com.example.mybookkeeper.fragmernts.receipts.ReceiptData;
import com.example.mybookkeeper.managers.Manager;
import com.example.mybookkeeper.managers.ManagerTotal;
import com.example.mybookkeeper.managers.RefreshableFragment;
import com.example.mybookkeeper.subaccounts.SubAccount;
import com.example.mybookkeeper.subaccounts.SubAccountTotal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AccountReceiptFragment extends Fragment implements RefreshableFragment {

    private SqliteDatabase mDatabase;
    RecyclerView AccountReceiptView;
    EditText eRctNo, eAmount;
    Manager manager;
    Account account;
    String chooser;
    Button buttonAdd;

    EditText dateFrom, dateTo;
    String startDate, endDate;

    int mngIdFromMngs;
    int mngIdFromHomeLgn;
    int mngIdFromNewPwd;
    String mngPhoneFromMngs;
    String phoneFromHomeLgn;
    String mngNameFromMngs;
    String mngNameFromNewPwd;
    String mngPhoneFromNewPwd;
    String mngNameFromHomeLgn;

    public static AccountReceiptFragment getInstance(int accId){
        AccountReceiptFragment r = new AccountReceiptFragment();
        Bundle args = new Bundle();
//        args.putInt("ClientID", clientID);
        r.setArguments(args);
        return r;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ReceiptData ReceiptData = null;
        Bundle args = getArguments();

        View view = inflater.inflate(R.layout.fragment_account_receipts, container, false);

        AccountReceiptView = view.findViewById(R.id.myAccountReceiptList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        AccountReceiptView.setLayoutManager(linearLayoutManager);
        AccountReceiptView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(getActivity());
        eRctNo = view.findViewById(R.id.eRctNo);
        eAmount = view.findViewById(R.id.eAmount);
        buttonAdd = view.findViewById(R.id.btnAdd);

        dateFrom = view.findViewById(R.id.edDateFrom);
        dateTo = view.findViewById(R.id.edDateTo);
        Date date = Calendar.getInstance().getTime();

        //========FIRST AND LAST DAY OD MONTH
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();// Get the current date
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Setting the first day of month
        Date firstDayOfMonth = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);  // Move to next month
        calendar.set(Calendar.DAY_OF_MONTH, 1);    // setting the 1st day of the month
        calendar.add(Calendar.DATE, -1); // Move a day back from the date
        Date lastDayOfMonth = calendar.getTime();
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); // Formatting the date
        String firstDayOfMonthStr = sdf.format(firstDayOfMonth); // String todayStr = sdf.format(today);
        String lastDayOfMonthStr = sdf.format(lastDayOfMonth);
        //====================

        //=============CDATEPICKERS
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = df.format(date);
        dateFrom = view.findViewById(R.id.edDateFrom);
        dateFrom.setText(formattedDate);
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == dateFrom) {
                    final Calendar calendar = Calendar.getInstance();
                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH);
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    //show dialog
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(year, month, dayOfMonth);
                            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                            String formattedDate = df.format(calendar.getTime());
                            dateFrom.setText(formattedDate);
                            dateTo.setText(formattedDate);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        dateTo = view.findViewById(R.id.edDateTo);
        Date date1 = Calendar.getInstance().getTime();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate1 = df1.format(date1);
        dateTo.setText(formattedDate1);
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == dateTo) {
                    final Calendar calendar = Calendar.getInstance();
                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH);
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    //show dialog
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(year, month, dayOfMonth);
                            SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
                            String formattedDate1 = df1.format(calendar.getTime());
                            dateTo.setText(formattedDate1);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        if (getArguments() != null) {
            chooser = getArguments().getString("originPage");
            if (chooser.equals("FromMngs")) {
                mngIdFromMngs = getArguments().getInt("mngIdFromMngs");
                mngNameFromMngs = getArguments().getString("mngNameFromMngs");
                mngPhoneFromMngs = getArguments().getString("mngPhoneFromMngs");
                startDate = firstDayOfMonthStr;
                endDate = lastDayOfMonthStr;
                buttonAdd.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).getSupportActionBar().setTitle("Account's List for ");
                ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("ACCOUNT:- " + mngNameFromMngs);
            } else if (chooser.equals("FromHomeLgn")) {
                mngIdFromHomeLgn = getArguments().getInt("mngIdFromHomeLgn");
                mngNameFromHomeLgn = getArguments().getString("mngNameFromHomeLgn");
                phoneFromHomeLgn = getArguments().getString("phoneFromHomeLgn");
                args.putString("btnState", "showeButton");
                args.putString("originPage", "FromAccsLgn");
                startDate = firstDayOfMonthStr;
                endDate = lastDayOfMonthStr;
                buttonAdd.setVisibility(View.GONE);
                ((MainActivity) getActivity()).getSupportActionBar().setTitle("Account's List for ");
                ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("ACCOUNT:- " + mngNameFromHomeLgn);
            } else if (chooser.equals("FromNewPwd")) {
                mngIdFromNewPwd = getArguments().getInt("mngIdFromNewPwd");
                mngNameFromNewPwd = getArguments().getString("mngNameFromNewPwd");
                mngPhoneFromNewPwd = getArguments().getString("mngPhoneFromNewPwd");
                startDate = firstDayOfMonthStr;
                endDate = lastDayOfMonthStr;
                buttonAdd.setVisibility(View.GONE);
                ((MainActivity) getActivity()).getSupportActionBar().setTitle("Account's List for ");
                ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("ACCOUNT:- " + mngIdFromNewPwd);
            }
        } else {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO ACCOUNT SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED ACCOUNT NOT FOUND");
        }
        refresh();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog();
            }
        });

        dateFrom.setText(startDate);
        dateTo.setText(endDate);
        return view;
    }
        public void refresh() {
            if (chooser.equals("FromMngs")) {
                ArrayList<AccountTotal> allReceipts = mDatabase.listAccTotalReceipts(startDate, endDate, mngIdFromMngs);
                if (allReceipts.size() > 0) {
                    AccountReceiptView.setVisibility(View.VISIBLE);
                    AccountReceiptAdapter mAdapter = new AccountReceiptAdapter(getActivity(),  this, allReceipts, getArguments().getInt("mngIdFromMngs"));
                    AccountReceiptView.setAdapter(mAdapter);
                }
                else {
                    AccountReceiptView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "There is no account in the database. Start adding now", Toast.LENGTH_LONG).show();
                }
            } else if (chooser.equals("FromHomeLgn")) {
                ArrayList<AccountTotal> allReceipts = mDatabase.listAccTotalReceipts(startDate, endDate, mngIdFromHomeLgn);
                if (allReceipts.size() > 0) {
                    AccountReceiptView.setVisibility(View.VISIBLE);
                    AccountReceiptAdapter mAdapter = new AccountReceiptAdapter(getActivity(),  this, allReceipts, getArguments().getInt("mngIdFromHomeLgn"));
                    AccountReceiptView.setAdapter(mAdapter);
                }
                else {
                    AccountReceiptView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "There is no account in the database. Start adding now", Toast.LENGTH_LONG).show();
                }

            } else if (chooser.equals("FromNewPwd")) {
                ArrayList<AccountTotal> allReceipts = mDatabase.listAccTotalReceipts(startDate, endDate, mngIdFromNewPwd);
                if (allReceipts.size() > 0) {
                    AccountReceiptView.setVisibility(View.VISIBLE);
                    AccountReceiptAdapter mAdapter = new AccountReceiptAdapter(getActivity(),  this, allReceipts, getArguments().getInt("mngIdFromNewPwd"));
                    AccountReceiptView.setAdapter(mAdapter);
                }
                else {
                    AccountReceiptView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "There is no account in the database. Start adding now", Toast.LENGTH_LONG).show();
                }
            }
        }

    @Override
    public void navigateToManagers(Manager manager) {

    }

    @Override
    public void navigateToAccounts(Account account) {

    }

    @Override
    public void navigateToSubAccounts(SubAccount subaccount) {

    }

    @Override
    public void navigateToAccountTotal(ManagerTotal managerTotal) {

    }

    @Override
    public void navigateToSubAccountTotal(AccountTotal accountTotal) {
        Bundle args = new Bundle();
        if (chooser.equals("FromMngs")) {
            args.putInt("accIdFromAccs", accountTotal.getAccount().getAccountId());
            args.putString("accNameFromAccs", accountTotal.getAccount().getAccName());
            args.putInt("mngIdFromAccs", accountTotal.getAccount().getMgId());
            args.putString("btnState", "showeButton");
            args.putString("originPage", "FromAccsAdmin");
            NavHostFragment.findNavController(AccountReceiptFragment.this)
                    .navigate(R.id.action_AccountReceiptsFragment_to_SubAccountReceiptFragment, args);
        }else if (chooser.equals("FromHomeLgn")) {
            args.putInt("accIdFromAccs", accountTotal.getAccount().getAccountId());
            args.putString("accNameFromAccs", accountTotal.getAccount().getAccName());
            args.putInt("mngIdFromAccs", mngIdFromHomeLgn);
            args.putString("btnState", "showeButton");
            args.putString("originPage", "FromAccsLgn");
            NavHostFragment.findNavController(AccountReceiptFragment.this)
                    .navigate(R.id.action_AccountReceiptsFragment_to_SubAccountReceiptFragment, args);
        }else if (chooser.equals("FromNewPwd")) {
            args.putInt("accIdFromAccs", account.getAccountId());
            args.putString("accNameFromAccs", account.getAccName());
            args.putInt("mngIdFromAccs", mngIdFromNewPwd);
            args.putString("btnState", "showeButton");
            args.putString("originPage", "FromAccsPwd");
            NavHostFragment.findNavController(AccountReceiptFragment.this)
                    .navigate(R.id.action_AccountReceiptsFragment_to_SubAccountReceiptFragment, args);
        }

    }

    @Override
    public void navigateToCreaateAccount() {

    }

    @Override
    public void navigateToClients(SubAccount subaccounts) {

    }

    @Override
    public void navigateToClientsDialog(ClientTotal client) {

    }

    @Override
    public void navigateToClientTotal(SubAccountTotal accountTotal) {

    }

    private void addTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.add_accounts, null);
        final EditText nameField = subView.findViewById(R.id.enterAccName);
        final EditText mgidField = subView.findViewById(R.id.enterMgid);
        if (chooser.equals("FromMngs")){
            nameField.setText("");
            mgidField.setText(mngIdFromMngs + "");
        }else if (chooser.equals("FromHomeLgn")){
            mgidField.setText(mngIdFromHomeLgn + "");
//        }else if (chooser.equals("FromNewPwd")){
//            mgidField.setText(mngIdFromNewPwd + "");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add new ACCOUNT");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("ADD ACCOUNT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (chooser.equals("FromMngs")){
                    final String accNme = nameField.getText().toString();
                    final int mngid = mngIdFromMngs;
                    if (TextUtils.isEmpty(accNme)) {
                        Toast.makeText(getActivity(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                    } else {
                        Account newAccount = new Account(accNme, mngid);
                        mDatabase.addAccounts(newAccount);
                        refresh();
                    }
                }else if (chooser.equals("FromHomeLgn")){
                    final String accNme = nameField.getText().toString();
                    final int mngid = mngIdFromHomeLgn;
                    if (TextUtils.isEmpty(accNme)) {
                        Toast.makeText(getActivity(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                    } else {
                        Account newAccount = new Account(accNme, mngid);
                        mDatabase.addAccounts(newAccount);
                        refresh();
                    }
//                }else if (chooser.equals("FromNewPwd")){
//                    final String accNme = nameField.getText().toString();
//                    final int mngid = mngIdFromNewPwd;
//                    if (TextUtils.isEmpty(accNme)) {
//                        Toast.makeText(getActivity(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
//                    } else {
//                        Account newAccount = new Account(accNme, mngid);
//                        mDatabase.addAccounts(newAccount);
//                        refresh();
//                    }
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase.close();
        }
    }
}