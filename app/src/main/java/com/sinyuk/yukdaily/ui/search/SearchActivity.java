package com.sinyuk.yukdaily.ui.search;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewTreeObserver;

import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.base.BaseActivity;
import com.sinyuk.yukdaily.databinding.ActivitySearchBinding;

import java.util.ArrayList;

import br.com.mauker.materialsearchview.MaterialSearchView;

/**
 * Created by Sinyuk on 16.10.30.
 */

public class SearchActivity extends BaseActivity {
    public static final int TYPE_ZHIHU = 1024;
    public static final int TYPE_GANK = 1025;
    private static final String KEY_TYPE = "TYPE";
    private ActivitySearchBinding binding;
    private int type;

    public static void start(Context context, int type) {
        Intent starter = new Intent(context, SearchActivity.class);
        starter.putExtra(KEY_TYPE, type);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        setupSearchView();

        binding.searchView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.searchView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                binding.searchView.openSearch();
            }
        });
    }

    private void setupSearchView() {
        type = getIntent().getIntExtra(KEY_TYPE, 0);

        if (type == TYPE_ZHIHU) {
            binding.searchView.setHint(getString(R.string.search_hint_zhihu));
        } else if (type == TYPE_GANK) {
            binding.searchView.setHint(getString(R.string.search_hint_gank));
        }
        binding.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                SearchResultActivity.start(SearchActivity.this, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.searchView.setSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewOpened() {
                // Do something once the view is open.
            }

            @Override
            public void onSearchViewClosed() {
                // Do something once the view is closed.
                finish();
                overridePendingTransition(0, 0);
            }
        });

        binding.searchView.setOnItemClickListener((parent, view, position, id) -> {
            // Do something when the suggestion list is clicked.
            String suggestion = binding.searchView.getSuggestionAtPosition(position);
            binding.searchView.setQuery(suggestion, true);
        });

        //        searchView.setTintAlpha(200);
        binding.searchView.adjustTintAlpha(0.8f);

        final Context context = this;

        binding.searchView.setOnVoiceClickedListener(new MaterialSearchView.OnVoiceClickedListener() {
            @Override
            public void onVoiceClicked() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    binding.searchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (binding.searchView.isOpen()) {
            // Close the search on the back button press.
            binding.searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.searchView.clearSuggestions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.searchView.activityResumed();
        if (type == TYPE_GANK) {
            String[] arr = getResources().getStringArray(R.array.gank_suggestion);
            binding.searchView.addSuggestions(arr);
        }

    }

    private void clearHistory() {
        binding.searchView.clearHistory();
    }

    private void clearSuggestions() {
        binding.searchView.clearSuggestions();
    }

    private void clearAll() {
        binding.searchView.clearAll();
    }
}
