package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;


public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{
        private List<Tweet> tweets;
        Context context;

        public TweetAdapter(List<Tweet> tweets){
            this.tweets = tweets;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View tweetView = inflater.inflate(R.layout.item_tweet,parent,false);
            ViewHolder viewHolder = new ViewHolder(tweetView);
            return viewHolder;
        }

         public void onBindViewHolder(ViewHolder holder, int position){
            Tweet tweet = tweets.get(position);
            holder.tvUserName.setText(tweet.user.name);
            holder.tvUserTweet.setText(tweet.body);

            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .into(holder.ivProfileImage);

        }

        public int getItemCount(){
            return tweets.size();
        }

        public void clear() {
            tweets.clear();
            notifyDataSetChanged();
        }

        public void addAll(List<Tweet> list) {
             tweets.addAll(list);
             notifyDataSetChanged();
         }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView ivProfileImage;
            public TextView tvUserName;
            public TextView tvUserTweet;
            ImageButton btnReply;

            public ViewHolder(View itemView) {
                super(itemView);

                ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
                tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
                tvUserTweet = (TextView) itemView.findViewById(R.id.tvUserTweet);
                btnReply = (ImageButton) itemView.findViewById(R.id.btnReply);

                btnReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {

                            Tweet tweet = tweets.get(position);
                            Intent intent = new Intent(context, ReplyActivity.class);

                            intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                            intent.putExtra("username", tweet.user.getUsername());
                            intent.putExtra("uid", tweet.uid);

                            context.startActivity(intent);
                        }
                    }
                });
            }

         }
}
