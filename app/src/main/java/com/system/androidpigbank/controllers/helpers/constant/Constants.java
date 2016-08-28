package com.system.androidpigbank.controllers.helpers.constant;

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


    String BUNDLE_MODEL_DEFAULT = "BUNDLE_MODEL_DEFAULT";
    String BUNDLE_MESSAGE_ID = "BUNDLE_MESSAGE_ID";
    int DATE_ITAU_CARD_FECHAMENTO = 10;
    int DATE_ITAU_CARD_VENCIMENTO = 17;
    int DATE_NUBANK_CARD_VENCIMENTO = 10;
    int DATE_NUBANK_CARD_FECHAMENTO = 5;
}
