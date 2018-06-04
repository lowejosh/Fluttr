package com.example.charles.opencv;

import android.support.test.runner.AndroidJUnit4;

import com.example.charles.opencv.BirdBank.ListBirdAdapter;
import com.example.charles.opencv.Activity.BirdBankActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for the
 */
@RunWith(AndroidJUnit4.class)
public class BirdBankTest {
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
    public void getFeatureSuccess() {
        Assert.assertEquals(table, question.getFeature());
    }

    @Test
    public void getQuestionSuccess() {
        Assert.assertEquals(questionToAsk, question.getQuestion());
    }
}

