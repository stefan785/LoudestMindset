/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loudest_mindset.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

/**
 * Photo
 */
@Entity
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("userId")
    private Long userId = null;

    @JsonProperty("url")
    private String url = null;

    public Photo id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * unique id of the photo
     *
     * @return id
   *
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Photo userId(Long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * id of the uploader
     *
     * @return userId
   *
     */

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Photo url(String url) {
        this.url = url;
        return this;
    }

    /**
     * path to the picture
     *
     * @return url
   *
     */

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Photo photo = (Photo) o;
        return Objects.equals(this.id, photo.id)
                && Objects.equals(this.userId, photo.userId)
                && Objects.equals(this.url, photo.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, url);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Photo {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    url: ").append(toIndentedString(url)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
