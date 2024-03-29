package com.example.charles.opencv;

import android.support.test.runner.AndroidJUnit4;

import com.example.charles.opencv.Tables.Question;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for the Question DataType, requires the physical or emulated machine to run.
 */
@RunWith(AndroidJUnit4.class)
public class QuestionInstrumentedTest {
    private Question question;
    private String table = "Bill";
    private String questionToAsk = "What Distinct Bill Shape does the Bird have?";

    @Before
    public void setup() {
        question = new Question(table, questionToAsk);
    }

    @Test
    public void getTableSuccess() {
        Assert.assertEquals(table, question.getTable());
    }

    @Test
    public void getQuestionSuccess() {
        Assert.assertEquals(questionToAsk, question.getQuestion());
    }
}
