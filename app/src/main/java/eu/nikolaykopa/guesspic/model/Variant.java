package eu.nikolaykopa.guesspic.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Variant.
 */
public class Variant implements Parcelable {

    @SerializedName("ru")
    @Expose
    private String word;

    @SerializedName("720p")
    @Expose
    private String image;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("license")
    @Expose
    private String license;

    public void setWord(String word) {
        this.word = word;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getWord() {
        return word;
    }

    public String getImage() {
        return image;
    }

    public String getAuthor() {
        return author;
    }

    public String getLink() {
        return link;
    }

    public String getLicense() {
        return license;
    }

    public Variant(String word, String image, String author, String link, String license) {
        this.word = word;
        this.image = image;
        this.author = author;
        this.link = link;
        this.license = license;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
        dest.writeString(image);
        dest.writeString(author);
        dest.writeString(link);
        dest.writeString(license);
    }

    public static final Parcelable.Creator<Variant> CREATOR = new Creator<Variant>() {
        @Override
        public Variant createFromParcel(Parcel source) {
            return new Variant(source);
        }

        @Override
        public Variant[] newArray(int size) {
            return new Variant[size];
        }
    };

    private Variant(Parcel parcel) {
        word = parcel.readString();
        image = parcel.readString();
        author = parcel.readString();
        link = parcel.readString();
        license = parcel.readString();
    }
}
