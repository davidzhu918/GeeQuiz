package edu.gatech.geequizsample;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import java.util.ArrayList;

import edu.gatech.quiz.TaskCards.QuestionState;
import edu.gatech.quiz.TaskCards.QuizMgr;
import edu.gatech.quiz.TaskCards.SessionMgr;
import edu.gatech.quiz.helpers.Question;

public class QuizSessionActivity extends FragmentActivity implements ActionBar.TabListener {
    public final static String EXTRA_MESSAGE = "edu.gatech.qeequizsample.MESSAGE";
    private final static String CHECKMARK = " ✓";
    private final static String CROSS = " x";

    static SessionMgr sm;

    private int currPos;
    private Question q;
    private String categoryName;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections.
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    QuestionPagesAdapter mQuestionPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    /**
     * Create the activity. Sets up an {@link android.app.ActionBar} with tabs, and then configures the
     * {@link ViewPager} contained inside R.layout.activity_main.
     *
     * <p>A {@link QuestionPagesAdapter} will be instantiated to hold the different pages of
     * fragments that are to be displayed. A
     * {@link android.support.v4.view.ViewPager.SimpleOnPageChangeListener} will also be configured
     * to receive callbacks when the user swipes between pages in the ViewPager.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        categoryName = intent.getStringExtra(CategorySelectActivity.EXTRA_MESSAGE);
        boolean reset = intent.getBooleanExtra(CategorySelectActivity.EXTRA_FLAG, false);
        if(reset)
            sm = QuizMgr.getInstance(getApplicationContext()).createSession(categoryName);
        else
            sm = QuizMgr.getInstance(getApplicationContext()).getSession(categoryName);

        currPos = 0;
        sm.resetIterator();
        q = sm.getNextQuestion();

        // Set up the action bar. The navigation mode is set to NAVIGATION_MODE_TABS, which will
        // cause the ActionBar to render a set of tabs. Note that these tabs are *not* rendered
        // by the ViewPager; additional logic is lower in this file to synchronize the ViewPager
        // state with the tab state. (See mViewPager.setOnPageChangeListener() and onTabSelected().)
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mQuestionPagerAdapter = new QuestionPagesAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mQuestionPagerAdapter);

        // When swiping between different sections, select the corresponding tab. We can also use
        // ActionBar.Tab#select() to do this if we have a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mQuestionPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter. Also
            // specify this Activity object, which implements the TabListener interface, as the
            // callback (listener) for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mQuestionPagerAdapter.getPageTitle(i))
                            .setContentDescription(String.format("tab%s", i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish:
                Intent i = new Intent(this, ExplanationActivity.class);
                i.putExtra(EXTRA_MESSAGE, categoryName);
                startActivity(i);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Update {@link ViewPager} after a tab has been selected in the ActionBar.
     *
     * @param tab Tab that was selected.
     * @param fragmentTransaction A {@link android.app.FragmentTransaction} for queuing fragment operations to
     *                            execute once this method returns. This FragmentTransaction does
     *                            not support being added to the back stack.
     */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, tell the ViewPager to switch to the corresponding page.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    /**
     * Unused. Required for {@link android.app.ActionBar.TabListener}.
     */
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * Unused. Required for {@link android.app.ActionBar.TabListener}.
     */
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages. This provides the data for the {@link ViewPager}.
     */
    public class QuestionPagesAdapter extends FragmentStatePagerAdapter {

        public QuestionPagesAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Get fragment corresponding to a specific position. This will be used to populate the
         * contents of the {@link ViewPager}.
         *
         * @param position Position to fetch fragment for.
         * @return Fragment for specified position.
         */
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a QuestionSectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.

            if(position > currPos) {
                int steps = position - currPos;
                int i = 0;
                while(i < steps && sm.hasNextQuestion()) {
                    q = sm.getNextQuestion();
                    i++;
                }
            } else if(position < currPos) {
                int steps = currPos - position;
                int i = 0;
                while(i < steps && sm.hasPreviousQuestion()) {
                    q = sm.getPreviousQuestion();
                    i++;
                }
            }

            Fragment fragment = new QuestionSectionFragment();
            Bundle args = new Bundle();
            args.putString(QuestionSectionFragment.ARG_QUESTION_NUMBER, "" + position);

            //update question text and currPos
            args.putString(QuestionSectionFragment.ARG_QUESTION_TEXT, q.getBodyText());

            //set option texts
            ArrayList<String> opTexts = new ArrayList<String>();
            for(int i = 0; i < q.getOptions().size(); i++) {
                opTexts.add(q.getOptions().get(i).getOptionText());
            }
            args.putStringArrayList(QuestionSectionFragment.ARG_QUESTION_OPTIONS, opTexts);

            currPos = position;

            fragment.setArguments(args);
            return fragment;
        }

        /**
         * Get number of pages the {@link ViewPager} should render.
         *
         * @return Number of fragments to be rendered as pages.
         */
        @Override
        public int getCount() {
            // Show 10 total pages.
            return sm.getNumberQuestions();
        }

        /**
         * Get title for each of the pages. This will be displayed on each of the tabs.
         *
         * @param position Page to fetch title for.
         * @return Title for specified page.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            QuestionState state = sm.getQuestionState(position);
            switch(state) {
                case CORRECT:
                    return new Integer(position + 1).toString() + CHECKMARK;
                case WRONG:
                    return new Integer(position + 1).toString() + CROSS;
                case BLANK:
                default:
                    return new Integer(position + 1).toString();
            }
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     * This would be replaced with your application's content.
     */
    public static class QuestionSectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_QUESTION_NUMBER = "question_number";
        public static final String ARG_QUESTION_TEXT = "question_text";
        public static final String ARG_QUESTION_OPTIONS = "question_options";

        public QuestionSectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView questionTextView = (TextView) rootView.findViewById(R.id.question_text);
            Bundle args = getArguments();
            final int qId = Integer.parseInt(args.getString(ARG_QUESTION_NUMBER));
            questionTextView.setText(args.getString(ARG_QUESTION_TEXT));
            ArrayList<String> opList = args.getStringArrayList(ARG_QUESTION_OPTIONS);

            LinearLayout answerBox = (LinearLayout) rootView.findViewById(R.id.question_box);
            for(int i = 0; i < opList.size(); i++) {
                Button option = new Button(getActivity());
                // TODO: Put real answer options here
                option.setText(opList.get(i));
                option.setContentDescription(String.format("q%s_o%s", args.getString(ARG_QUESTION_NUMBER), i));

                final int oId = i;
                option.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        sm.setAnswer(qId, oId);
                        String addon = CROSS;
                        if(sm.isCorrect(qId, oId))
                            addon = CHECKMARK;

                        ActionBar.Tab t = getActivity().getActionBar().getSelectedTab();
                        if(t != null) {
                            String title = t.getText().toString().split(" ")[0]; //get id
                            t.setText(title + addon);
                        }
                        System.out.println("Number correct: " + sm.getNumberCorrectQuestions());
                    }
                });

                answerBox.addView(option);
            }

            return rootView;
        }
    }

}