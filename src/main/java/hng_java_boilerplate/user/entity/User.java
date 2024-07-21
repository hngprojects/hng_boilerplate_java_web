package hng_java_boilerplate.user.entity;

//import hng_java_boilerplate.converter.SecurityQuestionAnswerConverter;
//import hng_java_boilerplate.dtos.requests.SecurityQuestionAnswer;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.profile.entity.Profile;
import hng_java_boilerplate.util.UUIDGenarator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private String id;

    private String name;
    private String email;

    private String recoveryEmail;
    private String recoveryPhoneNumber;


//    @Convert(converter = SecurityQuestionAnswerConverter.class)
//    private List<SecurityQuestionAnswer> answers;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;


    @ManyToMany
    @JoinTable(
            name = "user_organisation",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "organisation_id")
    )
    private List<Organisation> organisations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Product> products;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRecoveryEmail() {
        return recoveryEmail;
    }

    public void setRecoveryEmail(String recoveryEmail) {
        this.recoveryEmail = recoveryEmail;
    }

    public String getRecoveryPhoneNumber() {
        return recoveryPhoneNumber;
    }

    public void setRecoveryPhoneNumber(String recoveryPhoneNumber) {
        this.recoveryPhoneNumber = recoveryPhoneNumber;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Organisation> getOrganisations() {
        return organisations;
    }

    public void setOrganisations(List<Organisation> organisations) {
        this.organisations = organisations;
    }

//    public void setSecurityAnswers(List<SecurityQuestionAnswer> answers) {
//        this.answers = answers;
//    }

//    public List<SecurityQuestionAnswer> getSecurityAnswers(List<SecurityQuestionAnswer> answers) {
//        return answers;
//    }


}
