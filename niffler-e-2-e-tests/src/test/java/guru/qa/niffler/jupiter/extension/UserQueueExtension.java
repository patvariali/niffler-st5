package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.user.UserJson;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

import static guru.qa.niffler.model.user.UserJson.simpleUser;

public class UserQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UserQueueExtension.class);


    private static final Map<User.UserType, Queue<UserJson>> USERS = new ConcurrentHashMap<>();

    static {
        USERS.put(User.UserType.WITH_FRIENDS, new ConcurrentLinkedQueue<>(
                List.of(simpleUser("ali1", "Misha280620"),
                        simpleUser("ali", "12345"))
        ));
        USERS.put(User.UserType.INVITATION_SENT, new ConcurrentLinkedQueue<>(
                List.of(simpleUser("ali2", "12345"))
        ));
        USERS.put(User.UserType.INVITATION_RECEIVED, new ConcurrentLinkedQueue<>(
                List.of(simpleUser("ali3", "12345"))
        ));
    }


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        List<Method> allMethods = Stream.concat(
                        Arrays.stream(
                                        context.getRequiredTestClass().getDeclaredMethods())
                                .filter(p -> p.isAnnotationPresent(BeforeEach.class)),
                        Stream.of(
                                context.getRequiredTestMethod()))
                .toList();


        List<User> usersTypeList = allMethods.stream()
                .flatMap(p -> Arrays.stream(p.getParameters()))
                .filter(p -> AnnotationSupport.isAnnotated(p, User.class))
                .map(p -> p.getAnnotation(User.class))
                .toList();


        Map<User.UserType, List<UserJson>> users = new HashMap<>();

        for (User eachUser : usersTypeList) {

            User.UserType userType = eachUser.userType();

            Queue<UserJson> queueOfRequiredUsers = USERS.get(userType);

            UserJson userForTest = null;
            while (userForTest == null) {
                userForTest = queueOfRequiredUsers.poll();
            }

            if (users.containsKey(userType)) {
                List<UserJson> userJsons = new ArrayList<>(users.get(userType));
                userJsons.add(userForTest);
                users.put(userType, userJsons);
            } else {
                users.put(userType, List.of(userForTest));
            }
        }

        Allure.getLifecycle().updateTestCase(testCase -> {
            testCase.setStart(new Date().getTime());
        });
        context.getStore(NAMESPACE).put(context.getUniqueId(), users);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<User.UserType, List<UserJson>> userFormTest = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);

        if (userFormTest != null) {
            for (Map.Entry<User.UserType, List<UserJson>> eachUser : userFormTest.entrySet()) {
                USERS.get(eachUser.getKey()).addAll(eachUser.getValue());
            }
        }

    }


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(UserJson.class)
                && parameterContext
                .getParameter()
                .isAnnotationPresent(User.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        User.UserType annotation = parameterContext
                .getParameter()
                .getAnnotation(User.class).userType();

        Map<User.UserType, List<UserJson>> map = extensionContext
                .getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);

        List<UserJson> userJsonList = map.get(annotation);
        UserJson resultUser = null;

        if (userJsonList != null && !userJsonList.isEmpty()) {

            if (map.get(annotation).size() > 1) {
                resultUser = userJsonList.getFirst();
                userJsonList.remove(resultUser);
            }else {
                resultUser = userJsonList.getFirst();
            }
        }

        return resultUser;

    }
}
