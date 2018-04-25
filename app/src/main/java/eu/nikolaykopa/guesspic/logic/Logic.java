package eu.nikolaykopa.guesspic.logic;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.nikolaykopa.guesspic.R;
import eu.nikolaykopa.guesspic.model.Variant;

/**
 * Logic.
 */

public class Logic {

    private List<Variant> easy = new ArrayList<>();
    private List<Variant> medium = new ArrayList<>();
    private List<Variant> hard = new ArrayList<>();

    private String level;
    private int counterPositive;
    private int counterNegative;
    private int lives;
    private int time;
    private int score;
    private int timeForScore;

    private String image;
    private String firstAnswer;
    private String secondAnswer;
    private String thirdAnswer;
    private String fourthAnswer;
    private String author;
    private String link;
    private String license;
    private Context context;
    private String type;

    public Logic(String level, int counterPositive, int counterNegative, int lives, int time, int score, int timeForScore, Context context, String type) {
        this.level = level;
        this.counterPositive = counterPositive;
        this.counterNegative = counterNegative;
        this.lives = lives;
        this.time = time;
        this.score = score;
        this.timeForScore = timeForScore;
        this.context = context;
        this.type = type;
    }

    public void putData(String level, List<Variant> list) {
        switch (level) {
            case "easy":
                easy.addAll(list);
                break;
            case "medium":
                medium.addAll(list);
                break;
            case "hard":
                hard.addAll(list);
                break;
        }
    }

    public void setCounterPositive() {
        this.counterPositive++;
    }

    public void setCounterNegative() {
        this.counterNegative++;
    }

    public void updateTimeForScore() {
        this.timeForScore = 11;
    }

    public void updateTime() {
        this.time = 11;
    }

    public void minusTime() {
        this.time--;
        this.timeForScore--;
    }

    public int getTime() {
        return time;
    }

    public boolean changedLevel() {
        if (type.equals("L")) {
            return (level.equals("easy") && counterPositive - counterNegative == 10)
                    || (level.equals("medium") && counterPositive - counterNegative == 10);
        } else {
            return (level.equals("easy") && counterPositive == 12) || (level.equals("medium") && counterPositive == 12);
        }
    }

    public boolean noMoreAnswersFunc() {
        return hard.size() <= 5 && level.equals("hard");
    }

    public void setData() {
        if (type.equals("L")) {
            if (level.equals("easy") && counterPositive - counterNegative == 10) {
                level = "medium";
                counterPositive = 0;
                counterNegative = 0;
            } else if (level.equals("medium") && counterPositive - counterNegative == 10) {
                level = "hard";
                counterPositive = 0;
                counterNegative = 0;
            }
        } else {
            if (level.equals("easy") && counterPositive == 12) {
                level = "medium";
                counterPositive = 0;
            } else if (level.equals("medium") && counterPositive == 12) {
                level = "hard";
                counterPositive = 0;
            }
        }

        switch (level) {
            case "easy":
                Collections.shuffle(easy);
                this.image = easy.get(0).getImage();
                this.firstAnswer = easy.get(0).getWord();
                this.secondAnswer = easy.get(1).getWord();
                this.thirdAnswer = easy.get(2).getWord();
                this.fourthAnswer = easy.get(3).getWord();

                this.author = easy.get(0).getAuthor();
                this.link = easy.get(0).getLink();
                this.license = easy.get(0).getLicense();
                break;
            case "medium":
                Collections.shuffle(medium);
                this.image = medium.get(0).getImage();
                this.firstAnswer = medium.get(0).getWord();
                this.secondAnswer = medium.get(1).getWord();
                this.thirdAnswer = medium.get(2).getWord();
                this.fourthAnswer = medium.get(3).getWord();

                this.author = medium.get(0).getAuthor();
                this.link = medium.get(0).getLink();
                this.license = medium.get(0).getLicense();
                break;
            case "hard":
                easy.clear();
                Collections.shuffle(hard);
                this.image = hard.get(0).getImage();
                this.firstAnswer = hard.get(0).getWord();
                this.secondAnswer = hard.get(1).getWord();
                this.thirdAnswer = hard.get(2).getWord();
                this.fourthAnswer = hard.get(3).getWord();

                this.author = hard.get(0).getAuthor();
                this.link = hard.get(0).getLink();
                this.license = hard.get(0).getLicense();
                break;
        }
    }

    public String[] getAttribution() {
        if (author.contains("[!]")) {
            ArrayMap<String, String> autorsAttributions = new ArrayMap();
            autorsAttributions.put("Cyrillic", context.getResources().getString(R.string.custom_attribution_1));
            autorsAttributions.put("Sinodov", context.getResources().getString(R.string.custom_attribution_2));
            autorsAttributions.put("Ad Meskens", context.getResources().getString(R.string.custom_attribution_3));
            autorsAttributions.put("Graeme Main", context.getResources().getString(R.string.custom_attribution_4));
            autorsAttributions.put("Quickload", context.getResources().getString(R.string.custom_attribution_5));
            autorsAttributions.put("tontantravel", context.getResources().getString(R.string.custom_attribution_6));
            autorsAttributions.put("Wilhelm Kuhnert", context.getResources().getString(R.string.custom_attribution_7));
            autorsAttributions.put("Greg Hume", context.getResources().getString(R.string.custom_attribution_8));

            return new String[]{autorsAttributions.get(author.substring(0, author.length() - 4)), ""};
        }

        if (license.contains("CC") && !license.contains("ODbL")) {
            String localLicense = "";

            if (license.split(" ").length == 4) {
                localLicense = license.split(" ")[3].toLowerCase();
            }

            String attrFirst = context.getResources().getString(R.string.cc_attribution_1)  +
                    " <a href='" + link + "'>" + context.getResources().getString(R.string.cc_attribution_2)
                    + "</a> " + context.getResources().getString(R.string.cc_attribution_3) + "&nbsp;" + "<b>" + author + "</b>" +
                    context.getResources().getString(R.string.cc_attribution_4) +
                    " <a href='https://creativecommons.org/licenses/" +
                    license.split(" ")[1].toLowerCase() + "/"
                    + license.split(" ")[2] + "/"
                    + localLicense + "/deed.ru'>" + license + "</a>.";

            String attrSecond = context.getResources().getString(R.string.cc_attribution_5)
                    + " <a href='https://creativecommons.org/licenses/" +
                    license.split(" ")[1].toLowerCase() + "/"
                    + license.split(" ")[2] + "/" + localLicense
                    + "/deed.ru'>" + license + "</a> " + context.getResources().getString(R.string.cc_attribution_6);

            return new String[]{attrFirst, attrSecond};

        } else if (license.contains("public domain") || license.contains("pixabay")) {

            String attrFirst = context.getResources().getString(R.string.pd_attribution_1) +
                    " <a href='" + link + "'>" + context.getResources().getString(R.string.pd_attribution_2) + "</a> " +
                    context.getResources().getString(R.string.pd_attribution_3) + "&nbsp;" + "<b>" + author + "</b>" +
                    context.getResources().getString(R.string.pd_attribution_4);

            String attrSecond = context.getResources().getString(R.string.pd_attribution_5);

            return new String[]{attrFirst, attrSecond};

        } else if (license.contains("OGL")){
            String attrFirst = context.getResources().getString(R.string.cc_attribution_1)  +
                    " <a href='" + link + "'>" + context.getResources().getString(R.string.cc_attribution_2)
                    + "</a> " + context.getResources().getString(R.string.cc_attribution_3) + "&nbsp;" + "<b>" + author + "</b>" +
                    context.getResources().getString(R.string.cc_attribution_4) +
                    " <a href='https://www.nationalarchives.gov.uk/doc/open-government-licence/version/1/'>" + license + "</a>.";

            String attrSecond = context.getResources().getString(R.string.cc_attribution_5)
                    + " <a href='https://www.nationalarchives.gov.uk/doc/open-government-licence/version/1/'>" + license + "</a> " + context.getResources().getString(R.string.cc_attribution_6);

            return new String[]{attrFirst, attrSecond};

        } else if (license.contains("GFDL")){
            String attrFirst = context.getResources().getString(R.string.cc_attribution_1)  +
                    " <a href='" + link + "'>" + context.getResources().getString(R.string.cc_attribution_2)
                    + "</a> " + context.getResources().getString(R.string.cc_attribution_3) + "&nbsp;" + "<b>" + author + "</b>" +
                    context.getResources().getString(R.string.cc_attribution_4) +
                    " <a href='http://www.gnu.org/licenses/fdl-1.3.en.html'>" + license + "</a>.";

            String attrSecond = context.getResources().getString(R.string.cc_attribution_5)
                    + " <a href='http://www.gnu.org/licenses/fdl-1.3.en.html'>" + license + "</a> " + context.getResources().getString(R.string.cc_attribution_6);
            return new String[]{attrFirst, attrSecond};

        } else if (license.contains("ODbL")){
            if (!license.contains("CC")) {
                return new String[]{context.getResources().getString(R.string.odbl_attribution_1), ""};
            } else {
                String newLicense = license.substring(0, license.length() - 5);
                String localLicense = "";

                if (newLicense.split(" ").length == 4) {
                    localLicense = newLicense.split(" ")[3].toLowerCase();
                }

                String attrFirst = context.getResources().getString(R.string.cc_attribution_1)  +
                        " <a href='" + link + "'>" + context.getResources().getString(R.string.cc_attribution_2)
                        + "</a> " + context.getResources().getString(R.string.cc_attribution_3) + "&nbsp;" + "<b>" + author + "</b>" +
                        context.getResources().getString(R.string.cc_attribution_4) +
                        " <a href='https://creativecommons.org/licenses/" +
                        newLicense.split(" ")[1].toLowerCase() + "/"
                        + newLicense.split(" ")[2] + "/"
                        + localLicense + "/deed.ru'>" + newLicense + "</a>.";

                String attrSecond = context.getResources().getString(R.string.cc_attribution_5)
                        + " <a href='https://creativecommons.org/licenses/" +
                        newLicense.split(" ")[1].toLowerCase() + "/"
                        + newLicense.split(" ")[2] + "/" + localLicense
                        + "/deed.ru'>" + newLicense + "</a> " + context.getResources().getString(R.string.cc_attribution_6) + "<p>" + context.getResources().getString(R.string.odbl_attribution_1) + "</p>";

                return new String[]{attrFirst, attrSecond};
            }
        } else {
            return null;
        }
    }

    public String getImage() {
        return image;
    }

    public String getFirstAnswer() {
        return firstAnswer;
    }

    public String getSecondAnswer() {
        return secondAnswer;
    }

    public String getThirdAnswer() {
        return thirdAnswer;
    }

    public String getFourthAnswer() {
        return fourthAnswer;
    }

    public String getLicense() {
        return license;
    }

    public String getAuthor() {
        return author;
    }

    public String getLink() {
        return link;
    }

    public void removeAnswer() {
        switch (level) {
            case "easy":
                easy.remove(easy.get(0));
                break;
            case "medium":
                medium.remove(medium.get(0));
                break;
            case "hard":
                hard.remove(hard.get(0));
                break;
        }
    }

    public boolean checkLives() {
        return lives == 0;
    }

    public void minusLive() {
        this.lives--;
    }

    public String getScore() {
        score += timeForScore * 10;
        return String.valueOf(score);
    }

}
