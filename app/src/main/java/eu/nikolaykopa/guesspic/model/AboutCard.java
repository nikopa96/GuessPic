package eu.nikolaykopa.guesspic.model;

public class AboutCard {

    private String aboutTitle;
    private String aboutLicense;
    private String aboutText;

    public AboutCard(String aboutTitle, String aboutLicense, String aboutText) {
        this.aboutTitle = aboutTitle;
        this.aboutLicense = aboutLicense;
        this.aboutText = aboutText;
    }

    public String getAboutTitle() {
        return aboutTitle;
    }

    public String getAboutLicense() {
        return aboutLicense;
    }

    public String getAboutText() {
        return aboutText;
    }
}
