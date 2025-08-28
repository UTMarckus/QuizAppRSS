package com.utmarckus.quizapprss

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.utmarckus.quizapprss.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = requireNotNull(_binding)
    private var chosenOption = 0

    private var listener: QuizListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val questionNumber = arguments?.getInt(QUESTION_NUMBER_KEY) ?: 0
        val checkedOption = arguments?.getInt(CHECKED_NUMBER_KEY)
        val questionDataList = getQuestionDataList(arguments?.getString(DATA_QUESTION_KEY))

        listener = activity as QuizListener

        chosenOption = checkedOption ?: 0

        binding.apply {
            question.text = questionDataList[0]
            optionOne.text = questionDataList[1]
            optionTwo.text = questionDataList[2]
            optionThree.text = questionDataList[3]
            optionFour.text = questionDataList[4]
            optionFive.text = questionDataList[5]

            when (checkedOption) {
                0 -> nextButton.isEnabled = false
                1 -> optionOne.isChecked = true
                2 -> optionTwo.isChecked = true
                3 -> optionThree.isChecked = true
                4 -> optionFour.isChecked = true
                5 -> optionFive.isChecked = true
            }

            if (questionNumber == LAST_QUESTION_NUMBER) nextButton.text = "Submit"

            if (questionNumber == FIRST_QUESTION_NUMBER) {
                previousButton.isEnabled = false
                toolbar.navigationIcon = null
            }

            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                binding.nextButton.isEnabled = true
                chosenOption = when (checkedId) {
                    R.id.option_one -> 1
                    R.id.option_two -> 2
                    R.id.option_three -> 3
                    R.id.option_four -> 4
                    R.id.option_five -> 5
                    else -> 0
                }
            }

            nextButton.setOnClickListener {
                listener?.next(chosenOption, questionNumber)
            }

            previousButton.setOnClickListener {
                listener?.previous(chosenOption, questionNumber)
            }

            toolbar.title = "Question ${questionNumber + 1}"
            toolbar.setNavigationOnClickListener {
                listener?.previous(chosenOption, questionNumber)
            }
        }
    }

    private fun getQuestionDataList(dataQuestion: String?): List<String> =
        dataQuestion?.split(DELIMITER) ?: emptyList()

    interface QuizListener {
        fun next(chosenOption: Int, questionNumber: Int)

        fun previous(chosenOption: Int, questionNumber: Int)
    }

    companion object {
        private const val DATA_QUESTION_KEY = "TEXT_Q"
        private const val QUESTION_NUMBER_KEY = "QUESTION_NUMBER"
        private const val CHECKED_NUMBER_KEY = "CHECKED_NUMBER"
        private const val DELIMITER = '#'
        private const val FIRST_QUESTION_NUMBER = 0
        private const val LAST_QUESTION_NUMBER = 4

        fun newInstance(
            text: String,
            checkedOptionNumber: Int,
            questionNumber: Int
        ): QuizFragment {
            return QuizFragment().apply {
                arguments = bundleOf(
                    DATA_QUESTION_KEY to text,
                    CHECKED_NUMBER_KEY to checkedOptionNumber,
                    QUESTION_NUMBER_KEY to questionNumber
                )
            }
        }
    }
}
