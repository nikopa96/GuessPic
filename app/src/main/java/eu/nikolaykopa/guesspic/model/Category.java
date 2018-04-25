package eu.nikolaykopa.guesspic.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import eu.nikolaykopa.guesspic.R;

/**
 * Category.
 */

public class Category {

    private String id;
    private int imageId;
    private int name;
    private int scoreLives;
    private int scoreTime;

    public Category(String id, int imageId, int name, int scoreLives, int scoreTime) {
        this.id = id;
        this.imageId = imageId;
        this.name = name;
        this.scoreLives = scoreLives;
        this.scoreTime = scoreTime;
    }

    public String getId() {
        return id;
    }

    public int getImageId() {
        return imageId;
    }

    public int getName() {
        return name;
    }

    public int getScoreLives() {
        return scoreLives;
    }

    public int getScoreTime() {
        return scoreTime;
    }
}
