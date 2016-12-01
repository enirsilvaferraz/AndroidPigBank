package com.system.androidpigbank.controllers.helpers;

import android.Manifest;

import java.util.Collections;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public interface Constants {

    int LOADER_DEFAULT_ID = 1000;

    int REQUEST_PERMISSION_DEFAULT_ID = 2000;
    int REQUEST_ACTION_SAVE = 2001;
    int REQUEST_ACTION_DELETE = 2002;

    List<String> ACCESS_PERM_BACKUP = Collections.singletonList(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    int ACTION_VIEW = 1;
    int ACTION_EDIT = 2;
    int ACTION_DELETE = 3;
    int ACTION_COPY = 4;

    String BUNDLE_MODEL_DEFAULT = "BUNDLE_MODEL_DEFAULT";
    String BUNDLE_MESSAGE_ID = "BUNDLE_MESSAGE_ID";
    String FRAGMENT_ID = "FRAGMENT_ID";

    int DATE_ITAU_CARD_FECHAMENTO = 6;
    int DATE_ITAU_CARD_VENCIMENTO = 17;
    int DATE_NUBANK_CARD_VENCIMENTO = 10;
    int DATE_NUBANK_CARD_FECHAMENTO = 3;

    int FRAGMENT_ID_SUMMARY_CATEGORY = 1;
    int FRAGMENT_ID_TRANSACTION = 2;
    int FRAGMENT_ID_MONTH = 3;
    int FRAGMENT_ID_ESTIMATE = 4;
}
