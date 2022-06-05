package com.example.mybookkeeper.data;

import com.example.mybookkeeper.accounts.Account;
import com.example.mybookkeeper.accounts.AccountTotal;
import com.example.mybookkeeper.clients.Client;
import com.example.mybookkeeper.clients.ClientTotal;
import com.example.mybookkeeper.fragmernts.expenses.ExpenseData;
import com.example.mybookkeeper.fragmernts.receipts.ReceiptData;
import com.example.mybookkeeper.managers.Manager;
import com.example.mybookkeeper.managers.ManagerTotal;
import com.example.mybookkeeper.subaccounts.SubAccount;
import com.example.mybookkeeper.subaccounts.SubAccountTotal;

import java.util.List;

public interface BaseDataStore {
    //FETCHING ALL ITEMS============================================================


    //FETCHING ALL MANAGERS
    List<Manager> listManagers() throws Exception;

    //FETCHING ALL ACCOUNTS
    List<Account> listAccounts(int mgid) throws Throwable;

    //FETCHING ALL SUBACCOUNTS
    List<SubAccount> listSubAccounts(int accId) throws Throwable;

    //FETCHING ALL CLIENTS
    List<Client> listClients(int clSubId) throws Throwable;

    //FETCHING ALL RECEIPTS
    List<ReceiptData> listReceipts(int clientId) throws Throwable;

    //FETCHING ALL RECEIPTS
    List<ReceiptData> listReceipts(int clientId, String startDate, String endDate) throws Throwable;

    //FETCHING ALL RECEIPTS
    List<ReceiptData> listAllReceipts() throws Throwable;

    //FETCHING ALL MANAGER RECEIPTS
    List<ReceiptData> listManagerReceipts(int mngIDFromManager, String startDate, String endDate) throws Throwable;

    List<ManagerTotal> listMngrTotalReceipts() throws Throwable;

    List<AccountTotal> listAccTotalReceipts(String startDate, String endDate) throws Throwable;

    //accounts_table(m) < receipts_table(r) < expense_table(e)
    /*
     2    2   2
     3        3
     4    4   4
     5    5   5
     6
     */
    List<AccountTotal> listAccTotalReceipts(String startDate, String endDate, int managerID) throws Throwable;

    List<SubAccountTotal> listSubAccTotalReceipts(String startDate, String endDate) throws Throwable;

    List<SubAccountTotal> listSubAccTotalReceipts(String startDate, String endDate, int accountID) throws Throwable;

    List<ClientTotal> listClientTotalReceipts(String startDate, String endDate) throws Throwable;

    List<ClientTotal> listClientTotalReceipts(String startDate, String endDate, int cltSubId) throws Throwable;

    SubAccount searchSubAccountByAccId(int id) throws Throwable;

    //FETCHING ALL RECEIPTS
    List<ExpenseData> listExpenses(int clientId) throws Throwable;

    //FETCHING ALL RECEIPTS
    List<ExpenseData> listExpenses(int clientId, String startDate, String endDate) throws Throwable;

    //ADD A MANAGER
    void addManagers(Manager manager) throws Throwable;

    //ADD AN ACCOUNT
    void addAccounts(Account account) throws Throwable;

    //ADD AN SUBACCOUNT
    void addSubAccounts(SubAccount subaccount) throws Throwable;

    //ADD A CLIENT
    void addClients(Client clients) throws Throwable;

    //ADD A RECEIPT
    void addReceipt(ReceiptData receipts) throws Throwable;

    //ADD A EXPENSE
    void addExpense(ExpenseData expenseData) throws Throwable;

    //==================== UPDAATE ITEM ========================================
    //UPDAATE MANAGER
    void updateManagerTotals(ManagerTotal managerTotal) throws Throwable;

    void updateManagers(Manager managerTotal) throws Throwable;

    //UPDATE ACCOUNT
    void updateAccounts(AccountTotal accountTotal) throws Throwable;

    //UPDATE SUBACCOUNT
    void updateSubAccount(SubAccountTotal subAccountTotal) throws Throwable;

    void updateClients(ClientTotal clientTotal) throws Throwable;

    void updateReceipts(ReceiptData receiptData) throws Throwable;

    void updateExpense(ExpenseData expense) throws Throwable;

    Manager searchManagerByID(int mg_id) throws Throwable;

    Account searchAccountByAccId(int id) throws Throwable;

    int getNextReceiptID() throws Throwable;

    int getNextExpenseID() throws Throwable;

    int getNextManagerID() throws Throwable;

    Account searchAccountByID(int id) throws Throwable;

    SubAccount searchSubAccountByID(String id) throws Throwable;

    Client searchClientByID(int id) throws Throwable;

    ReceiptData searchReceiptByID(int id) throws Throwable;

    Manager searchRctIdByManagerId(int mngIdFromDialog) throws Throwable;

    ExpenseData searchExpenseByID(int id) throws Throwable;

    //==================== DELETE ITEM ========================================
    //DELETE MANAGER
    void deleteManager(int managerId) throws Throwable;

    //DELETE ACCOUNT
    void deleteAccount(int accountId) throws Throwable;

    //DELETE SU BACCOUNT
    void deleteSubAccount(int subaccountsId) throws Throwable;

    //DELETE CLIENT
    void deleteClient(int clientsId) throws Throwable;

    //DELETE CLIENT
    void deleteReceipt(int receiptsID) throws Throwable;

    //DELETE CLIENT
    void deleteExpense(int ExpensesID) throws Throwable;

    Manager searchManagerByPassword(String password) throws Throwable;

    Manager searchManagerByPhone(String mg_phone, String password) throws Throwable;

    void close();
}
