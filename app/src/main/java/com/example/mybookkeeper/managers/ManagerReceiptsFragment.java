package com.example.mybookkeeper.managers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.MainActivity;
import com.example.mybookkeeper.R;
import com.example.mybookkeeper.accounts.Account;
import com.example.mybookkeeper.accounts.AccountTotal;
import com.example.mybookkeeper.clients.ClientTotal;
import com.example.mybookkeeper.data.UIDataStore;
import com.example.mybookkeeper.fragmernts.receipts.ReceiptData;
import com.example.mybookkeeper.subaccounts.SubAccount;
import com.example.mybookkeeper.subaccounts.SubAccountTotal;

import java.util.List;

public class ManagerReceiptsFragment extends Fragment implements RefreshableFragment {

    private UIDataStore mDatabase;
    RecyclerView ReceiptManagerView;
    ReceiptData ReceiptData;
    EditText eRctNo, eDate, eSubName, eMgclid, eAccId, eSubaccId, eClientId, eAmount;
    int accId;
    String fromAcc;
    String clientName;

    String subAccNameFromGallety;
    ;
    int mngIdFromFFromGallety;
    int acntIdFFromGallety;
    int subAccIdFFromGallety;
    String clientNameFFromGallety;
    int mngIdFFromGallety;
    Manager manager;
    String mngNameFromHome;
    int mngIdFromHome;
    Button bAddNew, bReceipt, bExpense;
    private ProgressDialog progress;

    public static ManagerReceiptsFragment getInstance(int mngId) {
        ManagerReceiptsFragment r = new ManagerReceiptsFragment();
        Bundle args = new Bundle();
//        args.putInt("mngId", mngId);
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

        View view = inflater.inflate(R.layout.fragment_manager_receipts, container, false);
        bAddNew = view.findViewById(R.id.btnAdd);
        ReceiptManagerView = view.findViewById(R.id.myManagerReceiptList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        ReceiptManagerView.setLayoutManager(linearLayoutManager);
        ReceiptManagerView.setHasFixedSize(true);
        mDatabase = new UIDataStore(getActivity());
        eRctNo = view.findViewById(R.id.eRctNo);
        eAmount = view.findViewById(R.id.eAmount);

        if (getArguments() != null) {
            mngIdFromHome = getArguments().getInt("mngIdFromHome");
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("MANAGERS LIST");
//            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED ACCOUNT NOT FOUND");
        } else {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO ACCOUNT SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED ACCOUNT NOT FOUND");
        }

        refresh();
        bAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog();
            }
        });
        return view;
    }

    public void refresh() {
        showProgressDialog("Refreshing...");
        UIDataStore.UiData<List<ManagerTotal>> totalReceipts = mDatabase.listMngrTotalReceipts();
        totalReceipts.observe(getViewLifecycleOwner(), listResult -> {
            List<ManagerTotal> allReceipts = listResult.getResult();
            Log.d("TAGD", "listMngrTotalReceipts: 1234"+allReceipts);
            if (allReceipts != null && allReceipts.size() > 0) {
                ReceiptManagerView.setVisibility(View.VISIBLE);
                ManagerReceiptAdapter mAdapter = new ManagerReceiptAdapter(getActivity(), this, allReceipts, getArguments().getInt("mngIdFromHome"));
                ReceiptManagerView.setAdapter(mAdapter);
            } else {
                ReceiptManagerView.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "There is no manager in the database. Start adding now", Toast.LENGTH_LONG).show();
            }
            closeProgressDialog();
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
        Bundle args = new Bundle();
        args.putInt("mngIdFromMngs", managerTotal.getManager().getManagerID());
        args.putString("mngNameFromMngs", managerTotal.getManager().getManagerName());
        args.putString("mngPhoneFromMngs", managerTotal.getManager().getManagerPhone());
        args.putString("originPage", "FromMngs");
        args.putString("btnState", "showeButton");
        NavHostFragment.findNavController(ManagerReceiptsFragment.this)
                .navigate(R.id.action_ManagerReceiptsFragment_to_AccountReceiptsFragment, args);
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
        View mgView = inflater.inflate(R.layout.add_managers, null);
        final EditText nameField = mgView.findViewById(R.id.enterName);
        final EditText phoneField = mgView.findViewById(R.id.enterPhone);
        final EditText passwordField = mgView.findViewById(R.id.enterPword);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add new MANAGEER");
        builder.setView(mgView);
        builder.create();
        builder.setPositiveButton("ADD MANAGEER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String mgName = nameField.getText().toString();
                final String mgPhone = phoneField.getText().toString();
                final String mgPassword = passwordField.getText().toString();
                if (TextUtils.isEmpty(mgName)) {
                    Toast.makeText(getActivity(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    Manager newManager = new Manager(mgName, mgPhone, mgPassword);
                    showProgressDialog("Adding Manager...");
                    mDatabase.addManagers(newManager)
                            .observe(getViewLifecycleOwner(), r -> closeProgressDialog());
                    refresh();
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