package services;

import entity.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class UserRatingService {
    private final BorrowingService borrowingService;
    private final UserService userService;

    public UserRatingService(BorrowingService brs, UserService us) {
        borrowingService = brs;
        userService = us;
    }

    public long monthsSinceRegistration(User user) {
        return ChronoUnit.MONTHS.between(user.getApprovalDate(), LocalDate.now());
    }

    public List<List<User>> calculateUserRatings() {
        List<User> allUsers = userService.getAllReaders();

        List<List<User>> userRatings = new ArrayList<>();
        List<User> loyalUsers = new ArrayList<>();
        List<User> nonLoyalUsers = new ArrayList<>();

        for (User user : allUsers) {
            int booksBorrowed = borrowingService.getBorrowedBooksCount(user.getUserId());
            int booksOverdue = borrowingService.getLateReturnCount(user.getUserId());
            int booksDamaged = borrowingService.getDamagedBooksCount(user.getUserId());

            long monthsSinceRegistration = monthsSinceRegistration(user);
            int booksThreshold = (int) (0.8 * monthsSinceRegistration);
            int overdueThreshold = (int) (0.2 * monthsSinceRegistration);
            int booksDamagedThreshold = (int) (0.08 * monthsSinceRegistration);

            if (booksBorrowed >= booksThreshold && booksOverdue <= overdueThreshold
                    && booksDamaged <= booksDamagedThreshold) {
                loyalUsers.add(user);
            } else {
                nonLoyalUsers.add(user);
            }

            userRatings.add(0, loyalUsers);
            userRatings.add(1, nonLoyalUsers);
        }
        return userRatings;
    }
}
