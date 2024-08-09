package hng_java_boilerplate.util;

import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.InvalidPageNumberException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PaginationUtils {

    public static final int ONE = 1;
    public static final int ZERO = 0;
    public static  final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_LIMIT = 5;


    public static Pageable buildPageRequest(int page, int items){
        if (page<=ZERO) page=DEFAULT_PAGE_NUMBER;
        if (items<=ZERO) items = DEFAULT_PAGE_LIMIT;
        page-=ONE;
        return PageRequest.of(page, items);
    }

    public static Page<User> getPaginatedUsers(int page, List<User> allUser) {
        Pageable pageable = buildPageRequest(page, 0);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allUser.size());
        return new PageImpl<>(allUser.subList(start, end), pageable, allUser.size());
    }

    public static void validatePageNumber(int page, List<User> allUser) throws InvalidPageNumberException {
        int pageSize = 5;
        int totalUsers = allUser.size();
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

        if (page < 1 || page > totalPages) {
            throw new InvalidPageNumberException("Invalid page number requested: " + page);
        }
    }

}
