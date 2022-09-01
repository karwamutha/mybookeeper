package com.example.mybookkeeper.managers;

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

import java.util.ArrayList;
import java.util.List;

public class ManagerReceiptAdapter<S> extends RecyclerView.Adapter<ManagerReceiptAdapter.ReceiptDataViewHolder>
        implements Filterable {
    private final RefreshableFragment refreshable;
    private List<ManagerTotal> ManagerReceiptAdapter;
    private TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;
    private Context context;
    private UIDataStore mDatabase;
    int mngId;
    private AlertDialog alertDialog;
    private ProgressDialog progress;
//    TextView tvName, tvRctAmount, tvExpAmount, tvExBalAmount;

    public ManagerReceiptAdapter(Context context, RefreshableFragment refreshable, List<ManagerTotal> ManagerReceiptAdapter, int mngId) {
        this.context = context;
        this.refreshable = (RefreshableFragment) refreshable;
        this.ManagerReceiptAdapter = ManagerReceiptAdapter;
        this.mngId = mngId;
        mDatabase = UIDataStore.getInstance();
    }

    @Override
    public ReceiptDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_receipt_layout, parent, false);
        tvReceiptAmount = (TextView) view.findViewById(R.id.tvRctAmount);
        tvExpenseAmount = view.findViewById(R.id.tvExAmount);
        tvBalanceAmount = view.findViewById(R.id.tvBalAmount);
        return new ReceiptDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceiptDataViewHolder holder, int position) {
        final ManagerTotal managerTotal = ManagerReceiptAdapter.get(position);
        holder.tvName.setText(managerTotal.getManager().getManagerName());
        holder.tvReceiptAmount.setText("" + managerTotal.getReceiptsTotal());
        holder.tvExpenseAmount.setText("" + managerTotal.getExpensesTotal());
        holder.tvBalanceAmount.setText("" + (managerTotal.getBalance()));
        holder.viewManagerReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open fragment here
                refreshable.navigateToAccountTotal(managerTotal);
            }
        });
        holder.editManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(managerTotal);
            }
        });
        holder.deleteManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Confirm Delete");
                alertDialogBuilder.setIcon(R.drawable.delete);
                alertDialogBuilder.setMessage("Delete   " + "'" + managerTotal.getManager().getManagerName() + "'  ?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        showProgressDialog("Deleting...");
                        mDatabase.deleteManager(managerTotal.getManager().getManagerID())
                                .observe(refreshable.getViewLifecycleOwner(), voidResult -> {
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
                    ManagerReceiptAdapter = ManagerReceiptAdapter;
                } else {
                    ArrayList<ManagerTotal> filteredList = new ArrayList<>();
                    for (ManagerTotal managerTotal : ManagerReceiptAdapter) {
//                        if (receipts.getPhone().toLowerCase().contains(charString)) {
                        filteredList.add(managerTotal);
//                        }
                    }
                    ManagerReceiptAdapter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = ManagerReceiptAdapter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ManagerReceiptAdapter = (ArrayList<ManagerTotal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return ManagerReceiptAdapter.size();
    }

    private void editTaskDialog(final ManagerTotal managerTotal) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View accView = inflater.inflate(R.layout.add_managers, null);
        final EditText nameField = accView.findViewById(R.id.enterName);
        final EditText phoneField = accView.findViewById(R.id.enterPhone);
        final EditText passwordField = accView.findViewById(R.id.enterPword);

        if (managerTotal != null) {
            nameField.setText(managerTotal.getManager().getManagerName());
            phoneField.setText(managerTotal.getManager().getManagerPhone());
            passwordField.setText(managerTotal.getManager().getManagerPassword());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit manager");
        builder.setView(accView);
        AlertDialog alertDialog = builder.create();
        builder.setPositiveButton("EDIT CONTACT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String managerName = nameField.getText().toString();
                final String managerPhone = phoneField.getText().toString();
                final String managerPassword = passwordField.getText().toString();
                if (TextUtils.isEmpty(managerPhone) || managerPassword == null) {
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {

                    managerTotal.getManager().setManagerName(managerName);
                    managerTotal.getManager().setManagerPhone(managerPhone);
                    managerTotal.getManager().setManagerPassword(managerPassword);
                    showProgressDialog("Editing...");
                    mDatabase.updateManagers(managerTotal)
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
        View viewManagerReceipt;
        TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;
        ImageView deleteManager;
        ImageView editManager;

        ReceiptDataViewHolder(View itemView) {
            super(itemView);
            viewManagerReceipt = itemView.findViewById(R.id.viewManagerReceipt);
            tvName = itemView.findViewById(R.id.tvName);
            tvReceiptAmount = itemView.findViewById(R.id.tvRctAmount);
            tvExpenseAmount = itemView.findViewById(R.id.tvExAmount);
            tvBalanceAmount = itemView.findViewById(R.id.tvBalAmount);
            deleteManager = itemView.findViewById(R.id.deleteManager);
            editManager = itemView.findViewById(R.id.editManager);
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
            progress.setIndeterminate(true); progress.show();
        }
    }
}
