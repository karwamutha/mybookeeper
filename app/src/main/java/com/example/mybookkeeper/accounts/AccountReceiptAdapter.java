package com.example.mybookkeeper.accounts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.R;
import com.example.mybookkeeper.data.UIDataStore;
import com.example.mybookkeeper.managers.RefreshableFragment;

import java.util.ArrayList;
import java.util.List;

public class AccountReceiptAdapter<S> extends RecyclerView.Adapter<AccountReceiptAdapter.ReceiptDataViewHolder>
        implements Filterable {
    private final RefreshableFragment refreshable;
    private List<AccountTotal> AccountReceiptAdapter;
    private TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;
    private Context context;
    private UIDataStore mDatabase;
    int mngId;
    private AlertDialog alertDialog;
    private ProgressDialog progress;
//    TextView tvName, tvRctAmount, tvExpAmount, tvExBalAmount;

    public AccountReceiptAdapter(Context context, RefreshableFragment refreshable, List<AccountTotal> AccountReceiptAdapter, int mngId) {
        this.context = context;
        this.refreshable = (RefreshableFragment) refreshable;
        this.AccountReceiptAdapter = AccountReceiptAdapter;
        this.mngId = mngId;
        mDatabase = new UIDataStore(context);
    }

    @Override
    public ReceiptDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_receipt_layout, parent, false);
        tvReceiptAmount = (TextView) view.findViewById(R.id.tvRctAmount);
        tvExpenseAmount = view.findViewById(R.id.tvExAmount);
        tvBalanceAmount = view.findViewById(R.id.tvBalAmount);
        return new ReceiptDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceiptDataViewHolder holder, int position) {
        final AccountTotal accountTotal = AccountReceiptAdapter.get(position);
        holder.tvName.setText(accountTotal.getAccount().getAccName());
        holder.tvReceiptAmount.setText("" + accountTotal.getReceiptsTotal());
        holder.tvExpenseAmount.setText("" + accountTotal.getExpensesTotal());
        holder.tvBalanceAmount.setText("" + (accountTotal.getReceiptsTotal() - accountTotal.getExpensesTotal()));
        holder.viewAccountReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open fragment here
                refreshable.navigateToSubAccountTotal(accountTotal);
            }
        });
        holder.editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(accountTotal);
            }
        });
        holder.deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Confirm Delete");
                alertDialogBuilder.setIcon(R.drawable.delete);
                alertDialogBuilder.setMessage("Delete   " + "'" + accountTotal.getAccount().getAccName() + "'  ?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        showProgressDialog("Deleting...");
                        mDatabase.deleteAccount(accountTotal.getAccount().getAccountId())
                                .observe(refreshable.getViewLifecycleOwner(), (r) -> {
                                    closeProgressDialog();
                                    refreshable.refresh();
                                });
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    AccountReceiptAdapter = AccountReceiptAdapter;
                } else {
                    ArrayList<AccountTotal> filteredList = new ArrayList<>();
                    for (AccountTotal accountTotal : AccountReceiptAdapter) {
//                        if (receipts.getPhone().toLowerCase().contains(charString)) {
                        filteredList.add(accountTotal);
//                        }
                    }
                    AccountReceiptAdapter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = AccountReceiptAdapter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                AccountReceiptAdapter = (ArrayList<AccountTotal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return AccountReceiptAdapter.size();
    }

    private void editTaskDialog(final AccountTotal accountTotal) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View accView = inflater.inflate(R.layout.add_accounts, null);
        final EditText accNameField = accView.findViewById(R.id.enterAccName);
        final EditText mgIdField = accView.findViewById(R.id.enterMgid);

        if (accountTotal != null) {
            accNameField.setText(accountTotal.getAccount().getAccName());
            mgIdField.setText(String.valueOf(accountTotal.getAccount().getMgId()));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit account");
        builder.setView(accView);
        AlertDialog alertDialog = builder.create();
        builder.setPositiveButton("EDIT CONTACT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String accountName = accNameField.getText().toString();
                final int mgId = Integer.parseInt(mgIdField.getText().toString());
                if (TextUtils.isEmpty(accountName) || accountTotal == null) {
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    accountTotal.getAccount().setAccName(accountName);
                    accountTotal.getAccount().setMgId(mgId);
                    showProgressDialog("Editing...");
                    mDatabase.updateAccounts(accountTotal)
                            .observe(refreshable.getViewLifecycleOwner(), r -> closeProgressDialog());
                    refreshable.refresh();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    static class ReceiptDataViewHolder extends RecyclerView.ViewHolder {
        View viewAccountReceipt;
        TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;
        ImageView deleteAccount;
        ImageView editAccount;

        ReceiptDataViewHolder(View itemView) {
            super(itemView);
            viewAccountReceipt = itemView.findViewById(R.id.viewAccountReceipt);
            tvName = itemView.findViewById(R.id.tvName);
            tvReceiptAmount = itemView.findViewById(R.id.tvRctAmount);
            tvExpenseAmount = itemView.findViewById(R.id.tvExAmount);
            tvBalanceAmount = itemView.findViewById(R.id.tvBalAmount);
            deleteAccount = itemView.findViewById(R.id.deleteAccount);
            editAccount = itemView.findViewById(R.id.editAccount);
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
            progress = new ProgressDialog(context);

            progress.setMessage(message);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.setIndeterminate(true);
            progress.show();
        }
    }
}


