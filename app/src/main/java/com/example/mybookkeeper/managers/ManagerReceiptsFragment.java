package com.example.mybookkeeper.managers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.MainActivity;
import com.example.mybookkeeper.R;
import com.example.mybookkeeper.SqliteDatabase;
import com.example.mybookkeeper.accounts.Account;
import com.example.mybookkeeper.accounts.AccountTotal;
import com.example.mybookkeeper.clients.ClientTotal;
import com.example.mybookkeeper.fragmernts.receipts.ReceiptData;
import com.example.mybookkeeper.subaccounts.SubAccount;
import com.example.mybookkeeper.subaccounts.SubAccountTotal;

import java.util.ArrayList;

public class ManagerReceiptsFragment extends Fragment implements RefreshableFragment {

    private SqliteDatabase mDatabase;
    RecyclerView ReceiptManagerView;
    ReceiptData ReceiptData;
    EditText eRctNo, eDate, eSubName, eMgclid, eAccId, eSubaccId, eClientId, eAmount;
    int accId;
    String fromAcc;
    String clientName;
    EditText dateFrom, dateTo;

    String subAccNameFromGallety;;
    int mngIdFromFFromGallety;
    int acntIdFFromGallety;
    int subAccIdFFromGallety;
    String clientNameFFromGallety;
    int mngIdFFromGallety;
    String startDate, endDate;
    Manager manager;
    String mngNameFromHome;
    int mngIdFromHome;

    public static ManagerReceiptsFragment getInstance(int mngId){
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

        ReceiptManagerView = view.findViewById(R.id.myReceiptManagerList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        ReceiptManagerView.setLayoutManager(linearLayoutManager);
        ReceiptManagerView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(getActivity());
        eRctNo = view.findViewById(R.id.eRctNo);
        eSubName = view.findViewById(R.id.eSubName);
        eMgclid = view.findViewById(R.id.eMgclid);
        eDate = view.findViewById(R.id.eDate);
        eAccId = view.findViewById(R.id.eAccId);
        eSubaccId = view.findViewById(R.id.eSubaccId);
        eClientId = view.findViewById(R.id.eClient);
        eAmount = view.findViewById(R.id.eAmount);

        dateFrom = view.findViewById(R.id.edDateFrom);
        dateTo = view.findViewById(R.id.edDateTo);

        if (getArguments() != null) {
//            subAccNameFromGallety = getArguments().getString("subAccNameFromGallety");
//            mngNameFromHome = getArguments().getString("mngNameFromHome");
//            mngIdFromHome = getArguments().getInt("mngIdFromHome");
//            acntIdFFromGallety = getArguments().getInt("acntIdFFromGallety");
//            subAccIdFFromGallety = getArguments().getInt("subAccIdFFromGallety");
//            clientNameFFromGallety = getArguments().getString("clientNameFFromGallety");
//            clientIDFFromGallety = getArguments().getInt("clientIDFFromGallety");
            startDate = getArguments().getString("startDate");
            endDate = getArguments().getString("endDate");
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("MANAGERS LIST");

        }else{
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO ACCOUNT SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED ACCOUNT NOT FOUND");
        }

        dateFrom.setText(startDate);
        dateTo.setText(endDate);
        refresh();
        return view;
    }
    public void refresh(){
        ArrayList<ManagerTotal> allReceipts = mDatabase.listMngrTotalReceipts(startDate, endDate);
        if (allReceipts.size() > 0) {
            ReceiptManagerView.setVisibility(View.VISIBLE);
            ManagerReceiptAdapter mAdapter = new ManagerReceiptAdapter(getActivity(),  this, allReceipts, getArguments().getInt("mngNameFromHome"));
            ReceiptManagerView.setAdapter(mAdapter);
        }
        else {
            ReceiptManagerView.setVisibility(View.GONE);
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
        Bundle args = new Bundle();
//        args.putInt("mngIdFromMngs", manager.getManagerID());
//        args.putString("mngNameFromMngs", manager.getManagerName());
//        args.putString("mngPhoneFromMngs", manager.getManagerPhone());
//        args.putString("originPage", "FromMngs");
        args.putString("btnState", "showeButton");
        args.putString("startDate", dateFrom.getText().toString());
        args.putString("endDate", dateTo.getText().toString());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase.close();
        }
    }
}