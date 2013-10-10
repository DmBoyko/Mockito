import db.Role;
import db.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import service.UserService;
import web.controller.HomeController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.sql.Date;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {
    @InjectMocks
    private HomeController controller = new HomeController();

    @Mock
    private ModelMap modelMap;

    @Mock
    private UserService userService;

    @Mock
    private User user;

    @Mock
    private Model model;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIndex() throws Exception {
        String loginPage = controller.showLoginPage();
        String indexPage = controller.showIndexPage();

        assertEquals("index", loginPage);
        assertEquals("index", indexPage);
    }

    @Test
    public void testUserPage() throws Exception {
        String userPage = controller.userPage();

        assertEquals("HiUserPage", userPage);
    }

    @Test
    public void testAdminPage() throws Exception {
        String adminPage = controller.adminPage(model);

        assertEquals("admin", adminPage);
        verify(userService).findAll();
    }

    @Test
    public void testLoginFailed() throws Exception {
        String loginFailedPage = controller.loginError(session);

        verify(session).setAttribute(anyString(), anyString());
        assertEquals("index", loginFailedPage);
    }

    @Test
    public void testLogout() throws Exception {
        String nextPage = controller.logout(session, model, request);
        assertEquals("index", nextPage);
    }

    @Test
    public void testShowEditPage() throws Exception {
        User user = new User();
        user.setLogin("dboyko");
        user.setRole(new Role("ROLE_USER"));
        user.setBirthDate(new Date(1987, 10, 28));
        when(userService.findByLogin(anyString())).thenReturn(user);
        String nextPage = controller.showEditPage("edit", session, "dboyko", model);

        verify(userService).findByLogin(anyString());
        assertEquals("edit", nextPage);
    }

    @Test
    public void testShowAddPage() throws Exception {
        String nextPage = controller.showEditPage("add", session, "newUser", model);
        assertEquals("edit", nextPage);
    }

    @Test
    public void testLoginAdmin() throws Exception {
        User user = new User();
        user.setLogin("dboyko");
        user.setRole(new Role("ROLE_ADMIN"));
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(user, user));

        when(userService.findByLogin(anyString())).thenReturn(user);

        String nextPage = controller.login(model, session);

        assertEquals("admin", nextPage);

        verify(userService).findByLogin(anyString());
    }

    @Test
    public void testLoginUser() throws Exception {
        User user = new User();
        user.setLogin("dboyko");
        user.setRole(new Role("ROLE_USER"));
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(user, user));

        when(userService.findByLogin(anyString())).thenReturn(user);

        String nextPage = controller.login(model, session);

        assertEquals("HiUserPage", nextPage);

        verify(userService).findByLogin(anyString());
    }
}
