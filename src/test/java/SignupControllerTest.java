import db.NotUniqueLoginException;
import db.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import service.RoleService;
import service.UserService;
import web.controller.SignupController;
import web.validator.UserForm;

import javax.servlet.http.HttpServletRequest;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class SignupControllerTest {
    @InjectMocks
    private SignupController controller = new SignupController();
    @Mock
    private ModelMap modelMap;

    @Mock
    private RoleService roleService;

    @Mock
    private UserService userService;

    @Mock
    private BindingResult result;

    @Mock
    private User user;

    @Mock
    private Model model;

    @Mock
    private UserForm userForm;

    @Mock
    private HttpServletRequest request;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(userForm.getUser()).thenReturn(user);
    }

    @Test
    public void testSignupValid() throws Exception {
        when(result.hasErrors()).thenReturn(false);
        when(request.getRemoteAddr()).thenReturn("signup");
        String signupSuccess = controller.processSignup(
                userForm, result, model, "test", "test", request);

        assertEquals("signup-success", signupSuccess);

        verify(result).hasErrors();
        verify(userService).create(any(User.class));
    }

    @Test
    public void testSignupInvalid() throws Exception {
        when(result.hasErrors()).thenReturn(true);
        when(request.getRemoteAddr()).thenReturn("signup");
        String signupSuccess = controller.processSignup(
                userForm, result, model, "test", "test", request);

        assertEquals("signup", signupSuccess);

        verify(result).hasErrors();
    }

    @Test
    public void testSignupErrorCreate() throws Exception {
        when(result.hasErrors()).thenReturn(false);
        when(request.getRemoteAddr()).thenReturn("signup");

        doThrow(new NotUniqueLoginException("Test"))
                .when(userService).create(any(User.class));

        String signupSuccess = controller.processSignup(
                userForm, result, model, "test", "test", request);

        assertEquals("signup", signupSuccess);

        verify(result).hasErrors();
        verify(userService).create(any(User.class));
    }

    @Test
    public void testSignupGet() throws Exception {
        String nextPage = controller.signup(modelMap);

        assertEquals("signup", nextPage);
    }
}
