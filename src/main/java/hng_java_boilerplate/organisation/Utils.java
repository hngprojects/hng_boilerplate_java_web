package hng_java_boilerplate.organisation;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Utils {

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

}
