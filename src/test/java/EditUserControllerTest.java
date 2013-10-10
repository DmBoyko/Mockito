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
import org.springframework.validation.BindingResult;
import service.RoleService;
import service.UserService;
import web.controller.EditUserController;
import web.validator.UserForm;

import javax.servlet.http.HttpServletRequest;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class EditUserControllerTest {
    @InjectMocks
    private EditUserController controller = new EditUserController();

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
    public void testValidUser() throws Exception {
        when(result.hasErrors()).thenReturn(true);

        String nexPage = controller.processEditUser(userForm, result, model, "add");

        assertEquals("edit", nexPage);

        verify(result).hasErrors();
    }

    @Test
    public void testInvalidUser() throws Exception {
        when(result.hasErrors()).thenReturn(false);

        String nexPage = controller.processEditUser(userForm, result, model, "add");

        assertEquals("redirect: admin", nexPage);

        verify(result).hasErrors();
        verify(userService).create(any(User.class));
    }

    @Test
    public void testAddUserError() throws Exception {
        when(result.hasErrors()).thenReturn(false);

        doThrow(new NotUniqueLoginException("Test"))
                .when(userService).create(any(User.class));

        String nexPage = controller.processEditUser(userForm, result, model, "add");

        assertEquals("edit", nexPage);

        verify(result).hasErrors();
        verify(userService).create(any(User.class));
    }

    @Test
    public void testAddUser() throws Exception {
        when(result.hasErrors()).thenReturn(false);

        String nexPage = controller.processEditUser(userForm, result, model, "add");

        assertEquals("redirect: admin", nexPage);

        verify(result).hasErrors();
        verify(userService).create(any(User.class));
    }

    @Test
    public void testRedirect() throws Exception {
        String nextPage = controller.signup();

        assertEquals("redirect: admin", nextPage);
    }
}
