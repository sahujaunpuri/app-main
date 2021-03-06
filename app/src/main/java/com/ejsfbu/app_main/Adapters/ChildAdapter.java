package com.ejsfbu.app_main.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.Activities.ParentActivity;
import com.ejsfbu.app_main.Fragments.ChildDetailFragment;
import com.ejsfbu.app_main.Models.Goal;
import com.ejsfbu.app_main.Models.Request;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    private List<User> children;
    private Context context;

    public ChildAdapter(Context context, List<User> children) {
        this.context = context;
        this.children = children;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_child, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User child = children.get(position);
        holder.bind(child);
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivChildProfilePic;
        private TextView tvChildName;
        private TextView tvChildNumGoalsInProgress;
        private TextView tvChildNumGoalsCompleted;
        private TextView tvChildPendingRequests;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivChildProfilePic = itemView.findViewById(R.id.ivChildProfilePic);
            tvChildName = itemView.findViewById(R.id.tvChildName);
            tvChildNumGoalsCompleted = itemView.findViewById(R.id.tvChildNumGoalsCompleted);
            tvChildNumGoalsInProgress = itemView.findViewById(R.id.tvChildNumGoalsInProgress);
            tvChildPendingRequests = itemView.findViewById(R.id.tvChildPendingRequests);

            itemView.setOnClickListener(this);
        }

        public void bind(User child) {

            tvChildName.setText(child.getName());

            getNumGoalsInProgress(child);
            getNumGoalsCompleted(child);
            getNumPendingRequests(child);


            ParseFile image = child.getProfilePic();
            if (image != null) {
                String imageUrl = image.getUrl();
                imageUrl = imageUrl.substring(4);
                imageUrl = "https" + imageUrl;
                RequestOptions options = new RequestOptions();
                Glide.with(context)
                        .load(imageUrl)
                        .apply(options.placeholder(R.drawable.icon_user)
                                .error(R.drawable.icon_user)
                                .transform(new CenterCrop())
                                .transform(new CircleCrop())) // Extra: round image corners
                        .into(ivChildProfilePic);
            }
        }

        public void getNumGoalsInProgress(User child) {
            Goal.Query goalQuery = new Goal.Query();
            goalQuery.fromUser(child).areNotCompleted();
            goalQuery.findInBackground(new FindCallback<Goal>() {
                @Override
                public void done(List<Goal> objects, ParseException e) {
                    if (e == null) {
                        tvChildNumGoalsInProgress.setText(objects.size() + " Goals in Progress");
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }

        public void getNumGoalsCompleted(User child) {
            Goal.Query goalQuery = new Goal.Query();
            goalQuery.fromUser(child).areCompleted();
            goalQuery.findInBackground(new FindCallback<Goal>() {
                @Override
                public void done(List<Goal> objects, ParseException e) {
                    if (e == null) {
                        tvChildNumGoalsCompleted.setText(objects.size() + " Goals Completed");
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }

        public void getNumPendingRequests(User child) {
            Request.Query requestQuery = new Request.Query();
            requestQuery.fromUser(child);
            requestQuery.findInBackground(new FindCallback<Request>() {
                @Override
                public void done(List<Request> objects, ParseException e) {
                    if (e == null) {
                        tvChildPendingRequests.setText(objects.size() + " Requests Pending");
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                User child = children.get(position);

                Fragment childDetailFragment =
                        ChildDetailFragment.newInstance("Child Detail", child);
                ParentActivity.fragmentManager.beginTransaction()
                        .replace(R.id.flParentContainer, childDetailFragment)
                        .commit();
            }
        }
    }

}
