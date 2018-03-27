package nyc.c4q.capstone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by C4Q on 3/26/18.
 */

public class AlertDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.alert_dialog_greeting))
                .setMessage(context.getString(R.string.swipe_instructions))
                .setPositiveButton(context.getString(R.string.alert_dialog_ok), null);

        AlertDialog dialog = builder.create();
        return dialog;
    }

}


