package com.yangzhou.diplomaproject.module.question;

import com.yangzhou.diplomaproject.module.BaseModel;

import java.util.ArrayList;

/**
 * Created by zy on 2017/3/18.
 */

public class BaseQuestionModel extends BaseModel {
    public int ecode;
    public String emsg;
    public ArrayList<QuestionBodyValue> data;
}
