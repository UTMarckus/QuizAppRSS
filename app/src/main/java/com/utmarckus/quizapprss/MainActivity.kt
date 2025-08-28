package com.utmarckus.quizapprss

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), QuizFragment.QuizListener, ResultFragment.ResultListener {

    private val data: List<String> = listOf(
        "To be, or not to be...#To be#Not to be#I don't know#It's not my problem#...that is the question",
        "Tallest skyscraper:#Shanghai Tower, Shanghai#Burj Khalifa, Dubai#Trump International Hotel, Chicago#Central Park Tower, New York#Eiffel Tower, Paris",
        "The biggest animal:#Cat#Elephant#Kitti's hog-nosed bat#Antarctic blue whale#Human",
        "What's superfluous?#Discord#Slack#Kotlin#MicrosoftTeams#Telegram",
        "The Answer to the Ultimate Question of Life, the Universe, and Everything is:#33#11#7#0#42"
    )

    private val rightAnswers: List<Int> = listOf(5, 2, 4, 3, 5)

    private val chosenOptions = mutableListOf<Int>(0, 0, 0, 0, 0)
    private var currentQuestion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openQuizFragment(data[0], chosenOptions[0], currentQuestion)
    }

    private fun openQuizFragment(dataQuestion: String, checkedOption: Int, questionNumber: Int) {
        supportFragmentManager.beginTransaction().apply {
            replace(
                R.id.container,
                QuizFragment.newInstance(dataQuestion, checkedOption, questionNumber)
            )
            commit()
        }
    }

    private fun openResultFragment(result: String, description: String) {
        supportFragmentManager.beginTransaction().apply {
            replace(
                R.id.container,
                ResultFragment.newInstance(result, description)
            )
            commit()
        }
    }

    override fun back() {
        currentQuestion = 0
        chosenOptions.fill(0)
        openQuizFragment(
            data[currentQuestion],
            chosenOptions[currentQuestion],
            currentQuestion
        )
    }

    override fun next(chosenOption: Int, questionNumber: Int) {
        setAttrOnFragmentChange(chosenOption, questionNumber)

        if (questionNumber == data.lastIndex) openResultFragment(generateResult(), generateDescription())
        else openQuizFragment(
            data[currentQuestion],
            chosenOptions[currentQuestion],
            currentQuestion
        )
    }

    override fun previous(chosenOption: Int, questionNumber: Int) {
        setAttrOnFragmentChange(chosenOption, questionNumber, false)
        openQuizFragment(data[currentQuestion], chosenOptions[currentQuestion], currentQuestion)
    }

    private fun generateResult(): String {
        var counter = 0L
        chosenOptions.forEachIndexed { index, i ->
            if (i == rightAnswers[index]) counter++
        }
        return "Your result: ${counter * 100 / data.size}%"
    }

    private fun generateDescription(): String {
        val description = StringBuilder().apply { append("${generateResult()}\n\n") }

        chosenOptions.forEachIndexed { index, i ->
            data[index].split('#').let {
                description.append("(${index + 1}) ${it[0]}\nYour answer: ${it[i]}\n\n")
            }
        }

        return description.toString()
    }

    private fun setAttrOnFragmentChange(chosenOption: Int, questionNumber: Int, isNext: Boolean = true) {
        chosenOptions[questionNumber] = chosenOption

        if (isNext) currentQuestion = questionNumber + 1
        else currentQuestion = questionNumber - 1

        setTheme(getThemeId(currentQuestion))
        window.statusBarColor = resources.getColor(getColorId(currentQuestion))
    }

    private fun getThemeId(questionNumber: Int) = when (questionNumber) {
        1 -> R.style.Theme_Quiz_Second
        2 -> R.style.Theme_Quiz_Third
        3 -> R.style.Theme_Quiz_Fourth
        4 -> R.style.Theme_Quiz_Fifth
        else -> R.style.Theme_Quiz_First
    }

    private fun getColorId(questionNumber: Int) = when (questionNumber) {
        1 -> R.color.yellow_100_dark
        2 -> R.color.light_green_100_dark
        3 -> R.color.cyan_100_dark
        4 -> R.color.deep_purple_100_dark
        else -> R.color.deep_orange_100_dark
    }

}
