package vault.web;

import com.Vault;
import Objects.interfaces.Safe;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextWebClass implements ServletContextListener {

    public static Safe safe = new Vault();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("servlet start");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("servlet end");
    }
}
