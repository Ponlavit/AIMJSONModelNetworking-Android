package android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.style;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.JSONVariable;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMModel;

/**
 * Created by Nattapongr on 7/5/16 AD.
 */
public class QTNStyle extends AIMModel {

    @JSONVariable
    String backgroundHEXColor = "#c5ffe9";
    @JSONVariable
    String backgroundTitleHEXColor = "#2bfeb4";
    @JSONVariable
    String backgroundAnswerHEXColor = "#00ffdc";
    @JSONVariable
    String backgroundTitleResultHEXColor = "#ecbd27";
    @JSONVariable
    String backgroundResultHEXColor = "#00e6ff";
    @JSONVariable
    String titleFontHEXColor = "#303a41";
    @JSONVariable
    String answerFontHEXColor = "#303a41";
    @JSONVariable
    String titleResultFontHEXColor = "#303a41";
    @JSONVariable
    String detailResultFontHEXColor = "#303a41";
    @JSONVariable
    int titleFontSize = 18;
    @JSONVariable
    int detailFontSize = 16;

    public int getTitleFontSize() {
        return titleFontSize;
    }

    public void setTitleFontSize(int titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    public int getDetailFontSize() {
        return detailFontSize;
    }

    public void setDetailFontSize(int detailFontSize) {
        this.detailFontSize = detailFontSize;
    }

    public String getBackgroundHEXColor() {
        return backgroundHEXColor;
    }

    public void setBackgroundHEXColor(String backgroundHEXColor) {
        this.backgroundHEXColor = backgroundHEXColor;
    }

    public String getBackgroundTitleHEXColor() {
        return backgroundTitleHEXColor;
    }

    public void setBackgroundTitleHEXColor(String backgroundTitleHEXColor) {
        this.backgroundTitleHEXColor = backgroundTitleHEXColor;
    }

    public String getBackgroundAnswerHEXColor() {
        return backgroundAnswerHEXColor;
    }

    public void setBackgroundAnswerHEXColor(String backgroundAnswerHEXColor) {
        this.backgroundAnswerHEXColor = backgroundAnswerHEXColor;
    }

    public String getBackgroundTitleResultHEXColor() {
        return backgroundTitleResultHEXColor;
    }

    public void setBackgroundTitleResultHEXColor(String backgroundTitleResultHEXColor) {
        this.backgroundTitleResultHEXColor = backgroundTitleResultHEXColor;
    }

    public String getBackgroundResultHEXColor() {
        return backgroundResultHEXColor;
    }

    public void setBackgroundResultHEXColor(String backgroundResultHEXColor) {
        this.backgroundResultHEXColor = backgroundResultHEXColor;
    }

    public String getTitleFontHEXColor() {
        return titleFontHEXColor;
    }

    public void setTitleFontHEXColor(String titleFontHEXColor) {
        this.titleFontHEXColor = titleFontHEXColor;
    }

    public String getAnswerFontHEXColor() {
        return answerFontHEXColor;
    }

    public void setAnswerFontHEXColor(String answerFontHEXColor) {
        this.answerFontHEXColor = answerFontHEXColor;
    }

    public String getTitleResultFontHEXColor() {
        return titleResultFontHEXColor;
    }

    public void setTitleResultFontHEXColor(String titleResultFontHEXColor) {
        this.titleResultFontHEXColor = titleResultFontHEXColor;
    }

    public String getDetailResultFontHEXColor() {
        return detailResultFontHEXColor;
    }

    public void setDetailResultFontHEXColor(String detailResultFontHEXColor) {
        this.detailResultFontHEXColor = detailResultFontHEXColor;
    }

    @Override
    public String getObjectIdentificationRequest() {
        return null;
    }

    @Override
    public String getKeyForObjectIdentificationRequest() {
        return null;
    }
}
