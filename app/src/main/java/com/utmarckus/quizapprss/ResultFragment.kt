package com.utmarckus.quizapprss

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.utmarckus.quizapprss.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            result.text = arguments?.getString(RESULT_KEY) ?: ""

            backBtn.setOnClickListener {
                val listener = activity as? ResultListener
                listener?.back()
            }

            exitBtn.setOnClickListener {
                activity?.finish()
            }

            shareBtn.setOnClickListener {
                composeEmail("Quiz results", arguments?.getString(DESCRIPTION_RESULT_KEY) ?: "")
            }
        }
    }

    private fun composeEmail(subj: String, text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subj)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val packageManager = context?.packageManager ?: return
        if (intent.resolveActivity(packageManager) != null) startActivity(intent)
    }

    interface ResultListener {
        fun back()
    }

    companion object {

        private const val RESULT_KEY = "RESULT"
        private const val DESCRIPTION_RESULT_KEY = "DESCRIPTION_RESULT"

        fun newInstance(result: String, resultDescription: String): ResultFragment {
            return ResultFragment().apply {
                arguments = bundleOf(
                    RESULT_KEY to result,
                    DESCRIPTION_RESULT_KEY to resultDescription
                )
            }
        }
    }

}
