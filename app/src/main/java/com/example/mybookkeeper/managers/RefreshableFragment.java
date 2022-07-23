package com.example.mybookkeeper.managers;

import android.app.AlertDialog;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.example.mybookkeeper.accounts.Account;
import com.example.mybookkeeper.accounts.AccountTotal;
import com.example.mybookkeeper.clients.ClientTotal;
import com.example.mybookkeeper.subaccounts.SubAccount;
import com.example.mybookkeeper.subaccounts.SubAccountTotal;

public interface RefreshableFragment {
    public void refresh();

    void navigateToManagers(Manager manager);

    void navigateToAccounts(Account account);

    void navigateToSubAccounts(SubAccount subaccount);

    void navigateToAccountTotal(ManagerTotal managerTotal);

    void navigateToSubAccountTotal(AccountTotal subAccountTotal);

    void navigateToClientTotal(SubAccountTotal accountTotal);

    void navigateToCreaateAccount();

    void navigateToClients(SubAccount subaccount);

    void navigateToClientsDialog(ClientTotal client);

    LifecycleOwner getViewLifecycleOwner();


    default void displayNonBlockingError(Context context, Throwable error) {
        new AlertDialog.Builder(context)
                .setTitle("ERROR: " + error.getClass().getName())
                .setMessage(error.getMessage())
                .create()
                .show();
    }
}