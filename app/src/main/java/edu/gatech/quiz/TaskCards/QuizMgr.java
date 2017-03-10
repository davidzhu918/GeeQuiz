package edu.gatech.quiz.TaskCards;

import java.util.*;

import android.content.Context;
import edu.gatech.quiz.data.QuizDB;

/**
 * Created by zixiangzhu on 11/25/16.
 */

public final class QuizMgr {
    private static QuizMgr qm;

    private QuizDB db;
    private Map<String, SessionMgr> sessionMgrs;
    private Map<String, int[]> cumuScores;

    public QuizMgr(QuizDB db) {
        this.db = db;
        List<String> categories = getCategories();
        sessionMgrs = new HashMap<String, SessionMgr>();
        cumuScores = new HashMap<String, int[]>();
        for(String s : categories) {
            SessionMgr sm = new SessionMgr(s, db);
            sessionMgrs.put(s, sm);
            cumuScores.put(s, new int[] {0, 0});
        }

    }

    /**
     * Singleton method
     * @param ctxt
     * @return
     */
    public static synchronized QuizMgr getInstance(Context ctxt) {
        if(qm == null)
            qm = new QuizMgr(new QuizDB(ctxt));
        return qm;
    };

    public List<String> getCategories() {
        return db.getCategories();
    }

    /**
     * Creates and returns a new session manager for the selected category;
     * Overwrite if there is a previous manager for the given category
     * @param category
     * @return SessionMgr
     */
    public SessionMgr createSession(String category) {
        SessionMgr sm = sessionMgrs.get(category);
        if(sm != null) {
            int cumuScore = cumuScores.get(category)[0] + sm.getNumberCorrectQuestions();
            int cumuAttempts = cumuScores.get(category)[1] + sm.getNumberAnsweredQuestion();
            cumuScores.put(category, new int[] {cumuScore, cumuAttempts});
        } else {
            cumuScores.put(category, new int[] {0, 0});
        }
        sessionMgrs.put(category, new SessionMgr(category, db));
        return sessionMgrs.get(category);
    }

    /**
     * returns the existing session manager, null if input is invalid
     * @param category
     * @return
     */
    public SessionMgr getSession(String category) {
        return sessionMgrs.get(category);
    }


    /**
     * returns the cumulative score for all sessions (including currently unfinished ones)
     * and cumulative attempts
     * @param category
     * @return
     */
    public int[] getCumuScore(String category) {
        SessionMgr sm = sessionMgrs.get(category);
        int cumuScore = cumuScores.get(category)[0] + sm.getNumberCorrectQuestions();
        int cumuAttempts = cumuScores.get(category)[1] + sm.getNumberAnsweredQuestion();
        return new int[] {cumuScore, cumuAttempts};
    }

}
