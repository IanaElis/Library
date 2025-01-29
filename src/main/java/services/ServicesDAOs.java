package services;

import dao.*;

public class ServicesDAOs {
    private static final UserDAO userDAO = new UserDAO();
    private static final BookDAO bookDAO = new BookDAO();
    private static final BorrowingDAO borrowingDAO = new BorrowingDAO();
    private static final BorrowingStatusDAO borrowingStatusDAO = new BorrowingStatusDAO();
    private static final RoleDAO roleDAO = new RoleDAO();
    private static final RegisterFormDAO registerFormDAO = new RegisterFormDAO();
    private static final RegisterStatusDAO registerStatusDAO = new RegisterStatusDAO();
    private static final UserNotificationDAO userNotificationDAO = new UserNotificationDAO();
    private static final NotificationDAO notificationDAO = new NotificationDAO();
    private static final BookStatusDAO bookStatusDAO = new BookStatusDAO();

    private static final NotificationService notificationService =
            new NotificationService(userNotificationDAO, notificationDAO, userDAO);
    private static final UserService userService =
            new UserService(userDAO, registerFormDAO, notificationService, roleDAO,registerStatusDAO);
    private static final BookService bookService =
            new BookService(bookDAO,bookStatusDAO, notificationService);
    private static final BorrowingService borrowingService =
            new BorrowingService(userDAO, bookDAO, borrowingDAO, borrowingStatusDAO, bookService, notificationService);
    private static final UserRatingService userRatingService = new UserRatingService(borrowingService, userService);

    public static UserService getUserService(){
        return userService;
    }

    public static BookService getBookService(){
        return bookService;
    }

    public static BorrowingService getBorrowingService(){
        return borrowingService;
    }

    public static NotificationService getNotificationService(){
        return notificationService;
    }

    public static UserRatingService getUserRatingService(){
        return userRatingService;
    }


}
