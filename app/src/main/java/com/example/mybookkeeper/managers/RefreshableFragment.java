package com.example.mybookkeeper.managers;

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

    void navigateToSubAccountTotal(AccountTotal accountTotal);

    void navigateToClientTotal(SubAccountTotal accountTotal);

    void navigateToCreaateAccount();

    void navigateToClients(SubAccount subaccount);

    void navigateToClientsDialog(ClientTotal client);
}