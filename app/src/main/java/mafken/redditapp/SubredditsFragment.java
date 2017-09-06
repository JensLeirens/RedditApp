package mafken.redditapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mafken.redditapp.callData.Return;
import mafken.redditapp.callData.SubredditChilds;
import mafken.redditapp.network.Calls;
import mafken.redditapp.network.Config;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SubredditsFragment extends Fragment implements MainActivity.RedditPostListener {
    @BindView(R.id.subredditSubject)
    RecyclerView mRecycler;

    public LinearLayoutManager mLayoutManager;

    public static Subreddit currentSubreddit;
    public static List<Subreddit> subreddits = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subreddit, container, false);
        ButterKnife.bind(this, v);

        subreddits = MainActivity.subreddits;

        String type = getArguments().getString("type");
        if(subreddits == null) {
            subreddits = new ArrayList<>();
            getRedditPosts(type,true);
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);
        SubredditOnclickListener SRlistener = new SubredditOnclickListener(getContext());
        final SubredditAdapter SRAdapter = new SubredditAdapter(subreddits,SRlistener);

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                boolean loading = true;
                int pastVisiblesItems, visibleItemCount, totalItemCount;

                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            //Do pagination.. i.e. fetch new data
                            Log.d("Recycler Scroll", "scrolled down fetching new items");
                            String type = getArguments().getString("type");
                            getRedditPosts(type,false);
                        }
                    }
                }
            }
        });



        mRecycler.setAdapter(SRAdapter);

        return v;
    }

    @Override
    public void refreshRedditPosts(String type) {
        getRedditPosts(type,true);
    }


    public class SubredditOnclickListener implements View.OnClickListener {

        private final Context context;

        public SubredditOnclickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int pos ;
            pos = mRecycler.getChildAdapterPosition(v);
            currentSubreddit = subreddits.get(pos);

            Fragment fragment = new SubredditDetailFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(fragment.toString());
            ft.commit();

        }


    }

    public void getRedditPosts(String type, final boolean refresh){
        Calls caller = Config.getRetrofit().create(Calls.class);
        final String subreddit = type.toLowerCase();

        Subreddit lastRedditPost;
        String s = null;
        if(!refresh){
            if(subreddits.size() >= 20){
                lastRedditPost = subreddits.get(subreddits.size() -1);
                s = lastRedditPost.getName();
                Log.d("call with last post", s);
            } else {
                subreddits.clear();
                s = null ;
                Log.d("call without last post", "NULL");
            }
        }


        Call<Return> call = caller.getSubreddits(subreddit,s,20);
        call.enqueue(new Callback<Return>() {
            @Override
            public void onResponse(Call<Return> call, Response<Return> response) {


                Return result = response.body();
                Log.d("Backend Call", " call successful getSubreddits: subreddit: " + subreddit);
                //subreddits.clear();

                for(SubredditChilds s : result.getData().getChildren()){

                    subreddits.add(s.getData());
                }
                SubredditOnclickListener SRlistener = new SubredditOnclickListener(getContext());
                SubredditAdapter SRAdapter = new SubredditAdapter(subreddits,SRlistener);
                mRecycler.setAdapter(SRAdapter);
                if(!refresh) {
                    mRecycler.scrollToPosition(subreddits.size() - 20 );
                }

            }

            @Override
            public void onFailure(Call<Return> call, Throwable t) {
                Log.e("Backend CAll", "call failed Subreddits: subreddit: " + subreddit + " Reason: " + t.getMessage());
            }
        });
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}