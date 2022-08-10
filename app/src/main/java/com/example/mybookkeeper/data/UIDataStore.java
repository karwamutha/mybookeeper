package com.example.mybookkeeper.data;

import androidx.lifecycle.MutableLiveData;

import com.example.mybookkeeper.accounts.Account;
import com.example.mybookkeeper.accounts.AccountTotal;
import com.example.mybookkeeper.clients.Client;
import com.example.mybookkeeper.clients.ClientTotal;
import com.example.mybookkeeper.data.samis.OnlineDataStore;
import com.example.mybookkeeper.fragmernts.expenses.ExpenseData;
import com.example.mybookkeeper.fragmernts.receipts.ReceiptData;
import com.example.mybookkeeper.managers.Manager;
import com.example.mybookkeeper.managers.ManagerTotal;
import com.example.mybookkeeper.subaccounts.SubAccount;
import com.example.mybookkeeper.subaccounts.SubAccountTotal;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UIDataStore {

    private static UIDataStore store;
    private final BaseDataStore baseDataStore;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public UIDataStore(BaseDataStore baseDataStore) {
        this.baseDataStore = baseDataStore;
    }

    public static UIDataStore getInstance() {
        synchronized (UIDataStore.class) {
            if (store == null) {
                synchronized (UIDataStore.class) {
                    store = new UIDataStore(new OnlineDataStore());
                }
            }
        }
        return store;
    }

    public UiData<Manager> searchManagerByPhone(String phoneNo, String password) {
        UiData<Manager> uiData = new UiData<>();
        executor.execute(() -> {
            try {
                Manager manager = baseDataStore.searchManagerByPhone(phoneNo, password);
                uiData.postValue(new Result<>(manager));
            } catch (Throwable throwable) {
                uiData.postValue(new Result<>(throwable));
            }
        });
        return uiData;
    }

    public String getFirstManagerId() {
        return "0724895791";
    }

    public UiData<Manager> searchManagerByPassword(String password) {
        UiData<Manager> uiData = new UiData<>();
        executor.execute(() -> {
            try {
                Manager manager = baseDataStore.searchManagerByPassword(password);
                uiData.postValue(new Result<>(manager));
            } catch (Throwable throwable) {
                uiData.postValue(new Result<>(throwable));
            }
        });
        return uiData;
    }

    public UiData<Void> updateManagers(Manager newManager) {
        UiData<Void> uiData = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.updateManagers(newManager);
                uiData.postValue(new Result<>(null));
            } catch (Throwable throwable) {
                uiData.postValue(new Result<>(throwable));
            }
        });
        return uiData;
    }

    public UiData<Void> updateManagers(ManagerTotal managerTotal) {
        UiData<Void> uiData = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.updateManagerTotals(managerTotal);
                uiData.postValue(new Result<>(null));
            } catch (Throwable throwable) {
                uiData.postValue(new Result<>(throwable));
            }
        });
        return uiData;
    }

    public UiData<List<SubAccountTotal>> listSubAccTotalReceipts(String startDate, String endDate, int accountId) {
        UiData<List<SubAccountTotal>> uiData = new UiData<>();
        executor.execute(() -> {
            try {
                List<SubAccountTotal> accountTotals = baseDataStore
                        .listSubAccTotalReceipts(startDate, endDate, accountId);
                uiData.postValue(new Result<>(accountTotals));
            } catch (Throwable throwable) {
                uiData.postValue(new Result<>(throwable));
            }
        });
        return uiData;
    }

    public UiData<Void> addSubAccounts(SubAccount newSubAccount) {
        UiData<Void> uiData = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.addSubAccounts(newSubAccount);
                uiData.postValue(new Result<>(null));
            } catch (Throwable throwable) {
                uiData.postValue(new Result<>(throwable));
            }
        });
        return uiData;
    }


    public UiData<Void> addClients(Client newClient) {
        UiData<Void> uiData = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.addClients(newClient);
                uiData.postValue(new Result<>(null));
            } catch (Throwable throwable) {
                uiData.postValue(new Result<>(throwable));
            }
        });
        return uiData;
    }

    public void close() {
        synchronized (UIDataStore.class) {
            baseDataStore.close();
            store = null;
        }
    }

    public UiData<List<ClientTotal>> listClientTotalReceipts(String startDate, String endDate, int mngIdFromSubacc) {
        UiData<List<ClientTotal>> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                List<ClientTotal> list = baseDataStore.listClientTotalReceipts(startDate, endDate, mngIdFromSubacc);
                uidata.postValue(new Result<>(list));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<List<ManagerTotal>> listMngrTotalReceipts() {
        UiData<List<ManagerTotal>> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                List<ManagerTotal> list = baseDataStore.listMngrTotalReceipts();
                uidata.postValue(new Result<>(list));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> addManagers(Manager newManager) {
        UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.addManagers(newManager);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<List<ReceiptData>> listReceipts(int clientIDFFromDialog) {
        UiData<List<ReceiptData>> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                List<ReceiptData> receiptData = baseDataStore.listReceipts(clientIDFFromDialog);
                uidata.postValue(new Result<>(receiptData));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<List<ExpenseData>> listExpenses(int clientIDFFromDialog) {
        UiData<List<ExpenseData>> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                List<ExpenseData> receiptData = baseDataStore.listExpenses(clientIDFFromDialog);
                uidata.postValue(new Result<>(receiptData));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Integer> getNextExpenseID() {
        final UiData<Integer> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                int d = baseDataStore.getNextExpenseID();
                uidata.postValue(new Result<>(d));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> addExpense(ExpenseData newExpense) {
        final UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.addExpense(newExpense);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Integer> getNextReceiptID() {
        final UiData<Integer> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                int d = baseDataStore.getNextReceiptID();
                uidata.postValue(new Result<>(d));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> addReceipt(ReceiptData newReceipt) {
        final UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.addReceipt(newReceipt);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> deleteReceipt(int rctID) {
        final UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.deleteReceipt(rctID);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> updateReceipts(ReceiptData receipt) {
        final UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.updateReceipts(receipt);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<List<AccountTotal>> listAccTotalReceipts(String startDate, String endDate, int mngIdFromMngs) {
        final UiData<List<AccountTotal>> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                List<AccountTotal> totals = baseDataStore.listAccTotalReceipts(startDate, endDate, mngIdFromMngs);
                uidata.postValue(new Result<>(totals));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> addAccounts(Account newAccount) {
        final UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.addAccounts(newAccount);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> deleteClient(int id) {
        final UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.deleteClient(id);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> updateClients(ClientTotal clientTotal) {
        final UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.updateClients(clientTotal);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> deleteSubAccount(int getsubAccId) {
        final UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.deleteSubAccount(getsubAccId);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> updateSubAccount(SubAccountTotal subAccountTotal) {
        final UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.updateSubAccount(subAccountTotal);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> deleteExpense(int expID) {
        final UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.deleteExpense(expID);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> deleteManager(int managerID) {
        final UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.deleteManager(managerID);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> updateAccounts(AccountTotal accountTotal) {
        final UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.updateAccounts(accountTotal);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> deleteAccount(int accountId) {
        final UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.deleteAccount(accountId);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public UiData<Void> updateExpense(ExpenseData expense) {
        final UiData<Void> uidata = new UiData<>();
        executor.execute(() -> {
            try {
                baseDataStore.updateExpense(expense);
                uidata.postValue(new Result<>(null));
            } catch (Throwable e) {
                uidata.postValue(new Result<>(e));
            }
        });
        return uidata;
    }

    public static class UiData<T> extends MutableLiveData<Result<T>> {
    }

    public static class Result<R> {
        private final R result;
        private final Throwable exception;

        public Result(R result) {
            this(result, null);
        }

        public Result(Throwable exception) {
            this(null, exception);
        }

        public Result(R result, Throwable exception) {
            this.result = result;
            this.exception = exception;
        }

        public boolean isFailure() {
            return exception != null;
        }

        public String getErrorMessage() {
            if (exception != null) {
                String message = exception.getMessage();
                if (message != null) {
                    return message;
                }
            }
            return "";
        }

        public Throwable getException() {
            return exception;
        }

        public R getResult() {
            return result;
        }
    }
}
