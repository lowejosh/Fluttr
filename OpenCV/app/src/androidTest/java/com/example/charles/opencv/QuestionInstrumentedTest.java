package com.example.charles.opencv;

import android.support.test.runner.AndroidJUnit4;

import com.example.charles.opencv.InstrumentedTest.Question;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QuestionInstrumentedTest {
    Question question;
    String table = "Bill";
    String questionToAsk = "What Distinct Bill Shape does the Bird have?";

    @Before
    public void setup() {
        question = new Question(table, questionToAsk);
    }

    @Test
    public void getTableSuccess() {

    }
}
