package com.example.mybookkeeper.home;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.R;
import com.example.mybookkeeper.databinding.HomeListLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    //private final GGroup group;
    int preSelectedIndex = -1;
    private final List<HomeData> mHomeValues;
    private final BaseKiharaNavigator navigator;
    //private Home.DialogInterface mHomeListener;
    ArrayList Home = new ArrayList<>();

    SparseBooleanArray checkBoxStateArray = new SparseBooleanArray();

    public HomeAdapter(List<HomeData> homeValues, BaseKiharaNavigator navigator) {
        mHomeValues = homeValues;
        this.navigator = navigator;
        //group = new GGroup();
    }

/*    public HomeAdapter(List<Home> homeItems, Home.DialogInterface listener) {
        mHomeValues = homeItems;
        group = new GGroup();
        mHomeListener = listener;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(HomeListLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //[KIHARA, Sunday 19 Sep] Added checkbox to group
        //group.addButton(holder.mHomeCheckBox);

        holder.mHomeItem = mHomeValues.get(position);
        //holder.mHomeCheckBox.setChecked(false);
        holder.mTxtHomName.setText(mHomeValues.get(position).getMyHomeName());
        holder.mImageView.setImageResource(mHomeValues.get(position).getImgId());
        /*holder.mHomeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                group.clearCheckExcept(buttonView);
                navigator.switchToFragment(mHomeValues.get(position).getNavigationId());
            }
        });*/
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // group.clearCheckExcept(buttonView);
                navigator.switchToFragment(mHomeValues.get(position).getNavigationId());
/*                //getAdapterPosition returns clicked item position
                //Home model = mHomeValues.get(position);
               if (!checkBoxStateArray.get(position, false)) {
                    //checkbox checked
                    holder.mHomeCheckBox.setChecked(true);
                    //checkbox state stored.
                    checkBoxStateArray.put(position, true);
                    //mHomeListener.openDialog(holder.mHomeItem);
                } else {
                   //checkbox unchecked.
                    holder.mHomeCheckBox.setChecked(false);
                    //checkbox state stored
                    checkBoxStateArray.put(position, false);
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHomeValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTxtHomName;
        //public final CheckBox mHomeCheckBox;
        public final ImageView mImageView;
        public HomeData mHomeItem;

        public RelativeLayout relativeLayout;

        public ViewHolder(HomeListLayoutBinding binding) {
            super(binding.getRoot());
            mTxtHomName = binding.txtHomName;
            //mHomeCheckBox = binding.homeCb;
            mImageView = binding.imageView;
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);

            //[KIHARA, Sunday 19th Sep] Added this to uncheck other checkboxes when checkbox
            // is checked
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTxtHomName.getText() + "'";
        }
    }

/*    public static class GGroup {
        private final Set<CheckBox> checkboxes = new HashSet<>();

        GGroup() {

        }*/

/*        public Set<CheckBox> getCheckboxes() {
            return checkboxes;
        }

        public void addButton(CheckBox button) {
            checkboxes.add(button);
        }

        public void clearCheck() {
            for (CheckBox checkBox : checkboxes) {
                checkBox.setChecked(false);
            }
        }

        public void clearCheckExcept(CompoundButton buttonView) {
            for (CheckBox checkBox : checkboxes) {
                if (checkBox.equals(buttonView)) {
                    continue;
                }
                checkBox.setChecked(false);
            }
        }*/
}

