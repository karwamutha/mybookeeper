package com.example.mybookkeeper.subaccounts;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.R;
import com.example.mybookkeeper.data.UIDataStore;
import com.example.mybookkeeper.managers.RefreshableFragment;

import java.util.ArrayList;
import java.util.List;

public class SubAccountReceiptAdapter<S> extends RecyclerView.Adapter<SubAccountReceiptAdapter.ReceiptDataViewHolder>
        implements Filterable {
    private final RefreshableFragment refreshable;
    private List<SubAccountTotal> SubAccountReceiptAdapter;
    private TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;
    private Context context;
    private UIDataStore mDatabase;
    int mngId;
    private AlertDialog alertDialog;
    private ProgressDialog progress;
//    TextView tvName, tvRctAmount, tvExpAmount, tvExBalAmount;

    public SubAccountReceiptAdapter(Context context, RefreshableFragment refreshable, List<SubAccountTotal> SubAccountReceiptAdapter, int mngId) {
        this.context = context;
        this.refreshable = (RefreshableFragment) refreshable;
        this.SubAccountReceiptAdapter = SubAccountReceiptAdapter;
        this.mngId = mngId;
        mDatabase = UIDataStore.getInstance();
    }

    @Override
    public SubAccountReceiptAdapter.ReceiptDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subaccount_receipt_layout, parent, false);
        tvReceiptAmount = (TextView) view.findViewById(R.id.tvRctAmount);
        tvExpenseAmount = view.findViewById(R.id.tvExAmount);
        tvBalanceAmount = view.findViewById(R.id.tvBalAmount);
        return new SubAccountReceiptAdapter.ReceiptDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubAccountReceiptAdapter.ReceiptDataViewHolder holder, int position) {
        final SubAccountTotal subAccountTotal = SubAccountReceiptAdapter.get(position);
        holder.tvName.setText(subAccountTotal.getSubAccount().getSubAccName());
        holder.tvReceiptAmount.setText("" + subAccountTotal.getReceiptsTotal());
        holder.tvExpenseAmount.setText("" + subAccountTotal.getExpensesTotal());
        holder.tvBalanceAmount.setText("" + (subAccountTotal.getBalance()));
        holder.viewSubAccountReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open fragment here
                refreshable.navigateToClientTotal(subAccountTotal);
            }
        });
        holder.editSubAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(subAccountTotal);
            }
        });
        holder.deleteSubAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Confirm Delete");
                alertDialogBuilder.setIcon(R.drawable.delete);
                alertDialogBuilder.setMessage("Delete   " + "'" + subAccountTotal.getSubAccount().getSubAccName() + "'  ?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        showProgressDialog("Deleting..");
                        mDatabase.deleteSubAccount(subAccountTotal.getSubAccount().getSubAccId())
                                .observe(refreshable.getViewLifecycleOwner(), r -> {
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
            progress.setIndeterminate(true); progress.show();
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    SubAccountReceiptAdapter = SubAccountReceiptAdapter;
                } else {
                    ArrayList<SubAccountTotal> filteredList = new ArrayList<>();
                    for (SubAccountTotal subAccountTotal : SubAccountReceiptAdapter) {
//                        if (receipts.getPhone().toLowerCase().contains(charString)) {
                        filteredList.add(subAccountTotal);
//                        }
                    }
                    SubAccountReceiptAdapter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = SubAccountReceiptAdapter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                SubAccountReceiptAdapter = (ArrayList<SubAccountTotal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private void editTaskDialog(final SubAccountTotal subAccountTotal) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_subaccount, null);
        final EditText nameField = subView.findViewById(R.id.entersacName);
        final EditText mgidField = subView.findViewById(R.id.entersacMgId);
        final EditText accIdField = subView.findViewById(R.id.entersacAccId);
        if (subAccountTotal != null) {
            nameField.setText(subAccountTotal.getSubAccount().getSubAccName());
            mgidField.setText(subAccountTotal.getSubAccount().getSubMgId() + "");
            accIdField.setText(subAccountTotal.getSubAccount().getSubAccId() + "");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit subaccount");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();
        builder.setPositiveButton("EDIT SUBACCOUNT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final int mgid = Integer.parseInt(mgidField.getText().toString());
                final int accId = Integer.parseInt(accIdField.getText().toString());
                if (TextUtils.isEmpty(name) || subAccountTotal == null) {
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    subAccountTotal.getSubAccount().setSubAccName(name);
                    subAccountTotal.getSubAccount().setSubMgId(mgid);
                    subAccountTotal.getSubAccount().setAccId(accId);
                    showProgressDialog("Editing...");
                    mDatabase.updateSubAccount(subAccountTotal)
                            .observe(refreshable.getViewLifecycleOwner(), r -> closeProgressDialog());
                    refreshable.refresh();
                }
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return SubAccountReceiptAdapter.size();
    }

    static class ReceiptDataViewHolder extends RecyclerView.ViewHolder {
        View viewSubAccountReceipt;
        TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;
        ImageView deleteSubAccount;
        ImageView editSubAccount;

        ReceiptDataViewHolder(View itemView) {
            super(itemView);
            viewSubAccountReceipt = itemView.findViewById(R.id.viewSubAccountReceipt);
            tvName = itemView.findViewById(R.id.tvName);
            tvReceiptAmount = itemView.findViewById(R.id.tvRctAmount);
            tvExpenseAmount = itemView.findViewById(R.id.tvExAmount);
            tvBalanceAmount = itemView.findViewById(R.id.tvBalAmount);
            deleteSubAccount = itemView.findViewById(R.id.deleteSubAccount);
            editSubAccount = itemView.findViewById(R.id.editSubAccount);
        }
    }
}