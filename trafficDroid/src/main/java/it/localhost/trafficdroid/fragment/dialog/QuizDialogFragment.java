package it.localhost.trafficdroid.fragment.dialog;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.activity.MainActivity;

import java.util.Random;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class QuizDialogFragment extends DialogFragment {
	private static final String KEY_TITLE = "KEY_TITLE";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String[] questionsTrue = getResources().getStringArray(R.array.patenteTrue);
		String[] questionFalse = getResources().getStringArray(R.array.patenteFalse);
		Random rnd = new Random();
		final boolean typeQuestion = rnd.nextBoolean();
		Builder builder = new Builder(getActivity());
		builder.setPositiveButton(R.string.trueAns, new OnAnswerListener(typeQuestion, true));
		builder.setNegativeButton(R.string.falseAns, new OnAnswerListener(typeQuestion, false));
		builder.setNeutralButton(R.string.skip, new OnSkipListener());
		builder.setTitle(getArguments().getString(KEY_TITLE));
		builder.setMessage(typeQuestion ? questionsTrue[rnd.nextInt(questionsTrue.length)] : questionFalse[rnd.nextInt(questionFalse.length)]);
		setCancelable(false);
		builder.setCancelable(false);
		return builder.create();
	}

	public void show(FragmentManager fragmentManager, String title) {
		Bundle arguments = new Bundle(1);
		arguments.putString(KEY_TITLE, title);
		setArguments(arguments);
		super.show(fragmentManager, QuizDialogFragment.class.getSimpleName());
	}

	private final class OnSkipListener implements OnClickListener {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			((MainActivity) getActivity()).launchPurchaseFlow(MainActivity.SKU_QUIZ_FREE);
		}
	}

	private final class OnAnswerListener implements OnClickListener {
		private boolean type, answer;

		private OnAnswerListener(boolean type, boolean answer) {
			this.type = type;
			this.answer = answer;
		}

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			if (type != answer)
				new QuizDialogFragment().show(getFragmentManager(), getString(R.string.retry));
		}
	}
}