package web.validator;

import db.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: dboyko
 * Date: 8/12/13
 */
@FieldEquals(field = "password", equalsTo = "confirmPassword")
public class UserForm {
    private static final Log logger = LogFactory.getLog(UserForm.class);
    private int id;
    @NotEmpty
    @Size(min = 3, max = 25)
    private String login;
    @NotEmpty
    @Size(min = 6, max = 25)
    private String password;
    @NotEmpty
    @Size(min = 6, max = 25)
    private String confirmPassword;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Size(min = 3, max = 25)
    private String firstName;
    @NotEmpty
    @Size(min = 3, max = 25)
    private String lastName;
    @Year
    private String birthDate;
    private String role;

    public void setUser(User user) {
        logger.trace("Constructor " + user);
        this.id = user.getId();
        logger.trace("Get 'id' " + user.getId());
        this.login = user.getLogin();
        logger.trace("Get 'login' " + user.getLogin());
        this.password = user.getPassword();
        logger.trace("Get 'password' " + user.getPassword());
        this.email = user.getEmail();
        logger.trace("Get 'email' " + user.getEmail());
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        logger.trace("format in to String Date " + format.format(user.getBirthDate()));
        java.util.Date date = new java.util.Date(user.getBirthDate().getTime());
        logger.trace("get 'Date'" + date);
        this.birthDate = format.format(user.getBirthDate());
        logger.trace("get 'birthDate'" + birthDate);
        this.firstName = user.getFirstName();
        logger.trace("get 'firstName'" + firstName);
        this.lastName = user.getLastName();
        logger.trace("get 'lastName'" + lastName);
        this.role = user.getRole().getName();
        logger.trace("get 'role'" + role);
    }

    public User parseDate(User user) {
        DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = null;
        Date sqlDate = null;
        try {
            logger.trace("get parameter 'birthDate' from request '" + birthDate + "'");
            logger.trace("try to parse date");
            date = simpleDateFormat.parse(birthDate);
            logger.trace("parse is complete");
            sqlDate = new Date(date.getTime());
        } catch (Exception e) {
            logger.error("Can't parse birthDate ", e);
        }
        user.setBirthDate(sqlDate);
        return user;
    }

    public User getUser() {
        User user = new User();
        user.setId(id);
        logger.trace("ser user 'login'" + login);
        user.setLogin(login);
        logger.trace("ser user 'email'" + login);
        user.setEmail(email);
        logger.trace("ser user 'firstName'" + login);
        user.setFirstName(firstName);
        logger.trace("ser user 'lastName'" + login);
        user.setLastName(lastName);
        logger.trace("ser user 'password'" + password);
        user.setPassword(password);
        parseDate(user);
        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
