package com.example.mybookkeeper.clients;

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

public class ClientReceiptAdapter<S> extends RecyclerView.Adapter<ClientReceiptAdapter.ReceiptDataViewHolder>
        implements Filterable {
    private final RefreshableFragment refreshable;
    private List<ClientTotal> ClientReceiptAdapter;
    private TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;
    private Context context;
    private UIDataStore mDatabase;
    int mngId;
    private ProgressDialog progress;

    public ClientReceiptAdapter(Context context, RefreshableFragment refreshable, List<ClientTotal> ClientReceiptAdapter, int mngId) {
        this.context = context;
        this.refreshable = (RefreshableFragment) refreshable;
        this.ClientReceiptAdapter = ClientReceiptAdapter;
        this.mngId = mngId;
        mDatabase = new UIDataStore(context);
    }

    @Override
    public ClientReceiptAdapter.ReceiptDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_receipt_layout, parent, false);
        tvReceiptAmount = (TextView) view.findViewById(R.id.tvRctAmount);
        tvExpenseAmount = view.findViewById(R.id.tvExAmount);
        tvBalanceAmount = view.findViewById(R.id.tvBalAmount);
        return new ClientReceiptAdapter.ReceiptDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientReceiptAdapter.ReceiptDataViewHolder holder, int position) {
        final ClientTotal clientTotal = ClientReceiptAdapter.get(position);
        holder.tvName.setText(clientTotal.getClient().getCltName());
        holder.tvReceiptAmount.setText("" + clientTotal.getReceiptsTotal());
        holder.tvExpenseAmount.setText("" + clientTotal.getExpensesTotal());
        holder.tvBalanceAmount.setText("" + (clientTotal.getReceiptsTotal() - clientTotal.getExpensesTotal()));
        holder.viewClientReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open fragment here
                refreshable.navigateToClientsDialog(clientTotal);
            }
        });
        holder.editClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(clientTotal);
            }
        });
        holder.deleteClient.setOnClickListener(new View.OnClickListener() {
            private AlertDialog alertDialog;

            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Confirm Delete");
                alertDialogBuilder.setIcon(R.drawable.delete);
                alertDialogBuilder.setMessage("Delete   " + "'" + clientTotal.getClient().getCltName() + "'  ?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        showProgressDialog("Deleting...");
                        mDatabase.deleteClient(clientTotal.getClient().getId())
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
                    ClientReceiptAdapter = ClientReceiptAdapter;
                } else {
                    ArrayList<ClientTotal> filteredList = new ArrayList<>();
                    for (ClientTotal clientTotal : ClientReceiptAdapter) {
//                        if (receipts.getPhone().toLowerCase().contains(charString)) {
                        filteredList.add(clientTotal);
//                        }
                    }
                    ClientReceiptAdapter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = ClientReceiptAdapter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ClientReceiptAdapter = (ArrayList<ClientTotal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return ClientReceiptAdapter.size();
    }

    private void editTaskDialog(final ClientTotal clientTotal) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View accView = inflater.inflate(R.layout.add_clients, null);
        final EditText clientNameField = accView.findViewById(R.id.enterCltName);
        final EditText mngIdField = accView.findViewById(R.id.enterCltMgid);
        final EditText accIdField = accView.findViewById(R.id.enterCltAccId);
        final EditText subIdField = accView.findViewById(R.id.enterCltSubId);

        if (clientTotal != null) {
            clientNameField.setText(clientTotal.getClient().getCltName());
            mngIdField.setText(String.valueOf(clientTotal.getClient().getCltMgid()));
            accIdField.setText(String.valueOf(clientTotal.getClient().getCltAccid()));
            subIdField.setText(String.valueOf(clientTotal.getClient().getCltSubId()));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit client");
        builder.setView(accView);
        AlertDialog alertDialog = builder.create();
        builder.setPositiveButton("EDIT CONTACT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String clientName = clientNameField.getText().toString();
                final int mngId = Integer.parseInt(mngIdField.getText().toString());
                final int accId = Integer.parseInt(accIdField.getText().toString());
                final int subId = Integer.parseInt(subIdField.getText().toString());
                if (TextUtils.isEmpty(clientName) || clientTotal == null) {
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    clientTotal.getClient().setCltName(clientName);
                    clientTotal.getClient().setCltMgid(mngId);
                    clientTotal.getClient().setCltAccid(accId);
                    clientTotal.getClient().setCltSubId(subId);
                    showProgressDialog("Updating...");
                    mDatabase.updateClients(clientTotal)
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

    static class ReceiptDataViewHolder extends RecyclerView.ViewHolder {
        View viewClientReceipt;
        TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;
        ImageView deleteClient;
        ImageView editClient;

        ReceiptDataViewHolder(View itemView) {
            super(itemView);
            viewClientReceipt = itemView.findViewById(R.id.viewClientReceipt);
            tvName = itemView.findViewById(R.id.tvName);
            tvReceiptAmount = itemView.findViewById(R.id.tvRctAmount);
            tvExpenseAmount = itemView.findViewById(R.id.tvExAmount);
            tvBalanceAmount = itemView.findViewById(R.id.tvBalAmount);
            deleteClient = itemView.findViewById(R.id.deleteClient);
            editClient = itemView.findViewById(R.id.editClient);
        }
    }
}

