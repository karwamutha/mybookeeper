package com.example.mybookkeeper.clients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.R;
import com.example.mybookkeeper.SqliteDatabase;
import com.example.mybookkeeper.managers.RefreshableFragment;

import java.util.ArrayList;

public class ClientReceiptAdapter<S> extends RecyclerView.Adapter<ClientReceiptAdapter.ClientViewHolder>
        implements Filterable {
    private final RefreshableFragment refreshable;
    private ArrayList<ClientTotal> ClientReceiptAdapter;
    private  TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;
    private SqliteDatabase mDatabase;
    int mngId;

    private String myString;
    private Context context;

//    TextView tvName, tvRctAmount, tvExpAmount, tvExBalAmount;

    public ClientReceiptAdapter(Context context, RefreshableFragment refreshable, ArrayList<ClientTotal> ClientReceiptAdapter, int mngId) {
        this.context = context;
        this.refreshable = (RefreshableFragment) refreshable;
        this.ClientReceiptAdapter = ClientReceiptAdapter;
        this.mngId = mngId;
        mDatabase = new SqliteDatabase(context);
    }

    @Override
    public ClientReceiptAdapter.ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_receipt_layout, parent, false);
        tvReceiptAmount = (TextView) view.findViewById(R.id.tvRctAmount);
        tvExpenseAmount = view.findViewById(R.id.tvExAmount);
        tvBalanceAmount = view.findViewById(R.id.tvBalAmount);
        return new ClientReceiptAdapter.ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientReceiptAdapter.ClientViewHolder holder, int position) {
        final ClientTotal clientTotal = ClientReceiptAdapter.get(position);
        holder.tvName.setText(clientTotal.getClient().getCltName());
        holder.tvReceiptAmount.setText("" + clientTotal.getReceiptsTotal());
        holder.tvExpenseAmount.setText("" + clientTotal.getExpensesTotal());
        holder.tvBalanceAmount.setText("" + (clientTotal.getReceiptsTotal() - clientTotal.getExpensesTotal()));
        holder.itemView.setOnClickListener(ll -> {
            refreshable.navigateToClientsDialog(clientTotal);
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

    static class ClientViewHolder extends RecyclerView.ViewHolder {
        View viewClientReceipt;
        TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;

        ClientViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvReceiptAmount = itemView.findViewById(R.id.tvRctAmount);
            tvExpenseAmount = itemView.findViewById(R.id.tvExAmount);
            tvBalanceAmount = itemView.findViewById(R.id.tvBalAmount);
        }
    }
}

