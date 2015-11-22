package com.bdevlin.apps.utils;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;


public class ViewMode {

    /**
     *
     * A listener for changes on a ViewMode. To listen to mode changes,
     * implement this
     *
     * interface and register your object with the single ViewMode held by the
     * ActivityController
     *
     * instance. On mode changes, the onViewModeChanged method will be called
     * with the new mode.
     */

    public interface ModeChangeListener {

        /**
         *
         * Called when the mode has changed.
         */

        void onViewModeChanged(int newMode);

    }

    /**
     *
     * Mode when showing a single conversation.
     */

    public static final int CONVERSATION = 1;

    /**
     *
     * Mode when showing a list of conversations
     */

    public static final int CONVERSATION_LIST = 2;

    /**
     *
     * Mode when showing results from user search.
     */

    public static final int SEARCH_RESULTS_LIST = 3;

    /**
     *
     * Mode when showing results from user search.
     */

    public static final int SEARCH_RESULTS_CONVERSATION = 4;

    /**
     *
     * Mode when showing the "waiting for sync" message.
     */

    public static final int WAITING_FOR_ACCOUNT_INITIALIZATION = 5;

    /**
     *
     * Mode when showing ads.
     */

    public static final int AD = 6;

    /**
     *
     * Uncertain mode. The mode has not been initialized.
     */

    public static final int UNKNOWN = 0;

    // Key used to save this {@link ViewMode}.

    private static final String VIEW_MODE_KEY = "view-mode";
    private int mMode = UNKNOWN;

    private static final String[] MODE_NAMES = {

            "Unknown",

            "Conversation",

            "Conversation list",

            "Search results list",

            "Search results conversation",

            "Waiting for sync",

            "Ad"

    };

    private final ArrayList<ModeChangeListener> mListeners = newArrayList();

    public ViewMode() {

    }

    @Override
    public String toString() {

        return "[mode=" + MODE_NAMES[mMode] + "]";

    }

    public String getModeString() {

        return MODE_NAMES[mMode];
    }

    public int getMode() {

        return mMode;
    }
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    /**
     * Creates a resizable {@code ArrayList} instance containing the given
     * elements.
     *
     * <p><b>Note:</b> due to a bug in javac 1.5.0_06, we cannot support the
     * following:
     *
     * <p>{@code List<Base> list = Lists.newArrayList(sub1, sub2);}
     *
     * <p>where {@code sub1} and {@code sub2} are references to subtypes of
     * {@code Base}, not of {@code Base} itself. To get around this, you must
     * use:
     *
     * <p>{@code List<Base> list = Lists.<Base>newArrayList(sub1, sub2);}
     *
     * @param elements the elements that the list should contain, in order
     * @return a newly-created {@code ArrayList} containing those elements
     */
    public static <E> ArrayList<E> newArrayList(E... elements) {
        int capacity = (elements.length * 110) / 100 + 5;
        ArrayList<E> list = new ArrayList<E>(capacity);
        Collections.addAll(list, elements);
        return list;
    }
//	public boolean isListMode() {
//
//		return isListMode(mMode);
//
//	}
//
//	public static boolean isListMode(final int mode) {
//
//		return mode == LISTING || mode == SEARCH_RESULTS_LIST;
//
//	}

    // public boolean isConversationMode() {
    //
    // return isConversationMode(mMode);
    //
    // }
    //
    // public static boolean isConversationMode(final int mode) {
    //
    // return mode == CONVERSATION || mode == SEARCH_RESULTS_CONVERSATION;
    //
    // }

    public void addListener(ModeChangeListener listener) {

        mListeners.add(listener);

    }

    public void removeListener(ModeChangeListener listener) {

        mListeners.remove(listener);

    }

    private void dispatchModeChange() {

        ArrayList<ModeChangeListener> list = new ArrayList<ModeChangeListener>(
                mListeners);

        for (ModeChangeListener listener : list) {

            assert (listener != null);

            listener.onViewModeChanged(mMode);

        }

    }

    public void enterConversationMode() {

        setModeInternal(CONVERSATION);

    }

    public void enterSearchResultsListMode() {

        setModeInternal(SEARCH_RESULTS_LIST);

    }

    public void enterSearchResultsConversationMode() {

        setModeInternal(SEARCH_RESULTS_CONVERSATION);

    }

    public void enterWaitingForInitializationMode() {

        setModeInternal(WAITING_FOR_ACCOUNT_INITIALIZATION);

    }


    public void enterConversationListMode() {

        setModeInternal(CONVERSATION_LIST);

    }




    public boolean isListMode() {

        return isListMode(mMode);

    }



    public static boolean isListMode(final int mode) {

        return mode == CONVERSATION_LIST || mode == SEARCH_RESULTS_LIST;

    }



    public boolean isConversationMode() {

        return isConversationMode(mMode);

    }



    public static boolean isConversationMode(final int mode) {

        return mode == CONVERSATION || mode == SEARCH_RESULTS_CONVERSATION;

    }



    public static boolean isSearchMode(final int mode) {

        return mode == SEARCH_RESULTS_LIST || mode == SEARCH_RESULTS_CONVERSATION;

    }


    public boolean isWaitingForSync() {

        return isWaitingForSync(mMode);

    }



    public static boolean isWaitingForSync(final int mode) {

        return mode == WAITING_FOR_ACCOUNT_INITIALIZATION;

    }




    public void handleRestore(Bundle inState) {

        if (inState == null) {

            return;

        }

        // Restore the previous mode, and UNKNOWN if nothing exists.

        final int newMode = inState.getInt(VIEW_MODE_KEY, UNKNOWN);

        if (newMode != UNKNOWN) {

            setModeInternal(newMode);

        }

    }

    public void handleSaveInstanceState(Bundle outState) {

        if (outState == null) {

            return;

        }

        outState.putInt(VIEW_MODE_KEY, mMode);

    }

    private boolean setModeInternal(int mode) {

        if (mMode == mode) {
            return false;
        }

        mMode = mode;

        dispatchModeChange();

        return true;

    }

}
