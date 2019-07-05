package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

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

        // bind the values based on the position of the leemnt
         public void onBindViewHolder(ViewHolder holder, int position){
           // get the data according to the position
            Tweet tweet = tweets.get(position);
            // set view data according to model
            holder.tvUserName.setText(tweet.user.name);
            holder.tvUserTweet.setText(tweet.body);
            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .into(holder.ivProfileImage);
        }

        public int getItemCount(){
            return tweets.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder{
            public ImageView ivProfileImage;
            public TextView tvUserName;
            public TextView tvUserTweet;

            public ViewHolder(View itemView){
                super(itemView);

                // perform findViewById lookups
                ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
                tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
                tvUserTweet = (TextView) itemView.findViewById(R.id.tvUserTweet);


            }
    }
}
