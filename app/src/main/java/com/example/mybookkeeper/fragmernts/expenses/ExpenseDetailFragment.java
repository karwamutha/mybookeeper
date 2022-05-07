package com.example.mybookkeeper.fragmernts.expenses;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.MainActivity;
import com.example.mybookkeeper.R;
import com.example.mybookkeeper.SqliteDatabase;
import com.example.mybookkeeper.accounts.Account;
import com.example.mybookkeeper.accounts.AccountTotal;
import com.example.mybookkeeper.clients.ClientTotal;
import com.example.mybookkeeper.managers.Manager;
import com.example.mybookkeeper.managers.ManagerTotal;
import com.example.mybookkeeper.managers.RefreshableFragment;
import com.example.mybookkeeper.subaccounts.SubAccount;
import com.example.mybookkeeper.subaccounts.SubAccountTotal;

import java.util.ArrayList;

public class ExpenseDetailFragment extends Fragment implements RefreshableFragment {

    private SqliteDatabase mDatabase;
    RecyclerView ExpenseView;
    ExpenseData ExpenseData;
    EditText eRctNo, eDate, eSubName, eMgclid, eAccId, eSubaccId, eClientId, eAmount;
    String fromAcc;
    String subAccNameFromGallety;;
    int mngIdFromFFromGallety;
    int acntIdFFromGallety;
    int subAccIdFFromGallety;
    String clientNameFFromGallety;
    int clientIDFFromGallety;

    public static ExpenseDetailFragment getInstance(int clientID){
        ExpenseDetailFragment r = new ExpenseDetailFragment();
        Bundle args = new Bundle();
        args.putInt("ClientID", clientID);
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

        ExpenseData ExpenseData = null;
        Bundle args = getArguments();

        View v = inflater.inflate(R.layout.fragment_expense_detail, container, false);

        ExpenseView = v.findViewById(R.id.myExpenseList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        ExpenseView.setLayoutManager(linearLayoutManager);
        ExpenseView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(getActivity());
        eRctNo = v.findViewById(R.id.eRctNo);
        eSubName = v.findViewById(R.id.eSubName);
        eMgclid = v.findViewById(R.id.eMgclid);
        eDate = v.findViewById(R.id.eDate);
        eAccId = v.findViewById(R.id.eAccId);
        eSubaccId = v.findViewById(R.id.eSubaccId);
        eClientId = v.findViewById(R.id.eClient);
        eAmount = v.findViewById(R.id.eAmount);

        if (getArguments() != null) {
            subAccNameFromGallety = getArguments().getString("subAccNameFromGallety");
            mngIdFromFFromGallety = getArguments().getInt("mngIdFromFFromGallety");
            acntIdFFromGallety = getArguments().getInt("acntIdFFromGallety");
            subAccIdFFromGallety = getArguments().getInt("subAccIdFFromGallety");
            clientNameFFromGallety = getArguments().getString("clientNameFFromGallety");
            clientIDFFromGallety = getArguments().getInt("clientIDFFromGallety");
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Expense Details for ");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("CLIENT:- " + clientNameFFromGallety);

        }else{
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO EXPENSES SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED EXPENSES NOT FOUND");
        }
        refresh();
        return v;
    }
    public void refresh(){

        ArrayList<ExpenseData> allExpenses = mDatabase.listExpenses(clientIDFFromGallety);
        if (allExpenses.size() > 0) {
            ExpenseView.setVisibility(View.VISIBLE);
            ExpenseAdapter mAdapter = new ExpenseAdapter(getActivity(),  this, allExpenses, getArguments().getInt("clientIDFFromGallety"));
            ExpenseView.setAdapter(mAdapter);
        }
        else {
            ExpenseView.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "There is no account in the database. Start adding now", Toast.LENGTH_LONG).show();
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
    public void navigateToCreaateAccount() {

    }

    @Override
    public void navigateToClients(SubAccount subaccounts) {

    }

    @Override
    public void navigateToClientsDialog(ClientTotal client) {

    }

    @Override
    public void navigateToSubAccountTotal(AccountTotal subAccountTotal) {

    }

    @Override
    public void navigateToClientTotal(SubAccountTotal accountTotal) {

    }

    private void addTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.expenses_list_layout, null);
        final EditText dateField = subView.findViewById(R.id.eDate);
        final EditText rctnoField = subView.findViewById(R.id.eRctNo);
        final EditText subField = subView.findViewById(R.id.eSubName);
        final EditText mngField = subView.findViewById(R.id.eMgclid);
        final EditText accField = subView.findViewById(R.id.eAccId);
        final EditText subaccField = subView.findViewById(R.id.eSubaccId);
        final EditText clientField = subView.findViewById(R.id.eClient);
        final EditText amountField = subView.findViewById(R.id.eAmount);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add new Expense");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("ADD Expense", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExpenseData = mDatabase.searchExpenseByID(Integer.parseInt(mngField.getText().toString()));
                final String date = dateField.getText().toString();
                final int expNo = Integer.parseInt(ExpenseData.getExpNo()+"");
                final String subname = subField.getText().toString();
                final int mngId = Integer.parseInt(ExpenseData.getMgId()+"");
                final int accId = Integer.parseInt(ExpenseData.getAccId()+"");
                final int subaccId = Integer.parseInt(ExpenseData.getSubId()+"");
                final int clientId = Integer.parseInt(ExpenseData.getClientId()+"");
                final double amount = Double.parseDouble(amountField.getText().toString());

                if (!TextUtils.isEmpty("" + amount)) {
                    ExpenseData newExpense = new ExpenseData(date, expNo, subname, mngId, accId, subaccId, clientId, amount);
                    mDatabase.addExpense(newExpense);
                    refresh();
                } else {
                    Toast.makeText(getActivity(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
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