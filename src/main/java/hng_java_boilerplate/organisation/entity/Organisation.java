package hng_java_boilerplate.organisation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hng_java_boilerplate.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organisations")
@SoftDelete(columnName = "deleted")
public class Organisation {
    @Id
    private String id;

    private String name;
    private String description;
    private boolean deleted;


    @ManyToMany(mappedBy = "organisations")
    @JsonIgnore
    private List<User> users;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
