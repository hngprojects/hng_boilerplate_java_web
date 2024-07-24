package hng_java_boilerplate.sale.service.serviceImpl;

import hng_java_boilerplate.sale.dto.ErrorDetails;
import hng_java_boilerplate.sale.dto.ErrorResponse;
import hng_java_boilerplate.sale.dto.MonthlySalesDTO;
import hng_java_boilerplate.sale.dto.ResponseDTO;
import hng_java_boilerplate.sale.entity.Sale;
import hng_java_boilerplate.sale.repository.SaleRepository;
import hng_java_boilerplate.sale.service.SaleService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.UserNotFoundException;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import hng_java_boilerplate.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SalesServiceImpl implements SaleService {
    @Autowired
    private SaleRepository salesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public Object getSummary() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication.isAuthenticated()) || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
            return new UserNotFoundException("User not authenticated or token is invalid.");
        }
        String username = authentication.getName();
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            return new UserNotFoundException("User not authenticated or token is invalid.");
        }
        try{
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setResource("Summary");
            List<User> userList = userRepository.findAll();
            if (userList.isEmpty()) {
                return new ErrorResponse(
                        "false",
                        404,
                        "ResourceNotFound",
                        "The Summary data could not be found",
                        errorDetails
                );
            }
            List<Sale> saleList = salesRepository.findAll();
            if (saleList.isEmpty()) {
                return new ErrorResponse(
                        "false",
                        404,
                        "ResourceNotFound",
                        "The Summary data could not be found",
                        errorDetails
                );
            }
            long totalUsers = userList.size();
            long totalRevenue = saleList.stream()
                    .mapToLong(sale -> (long) sale.getTotalSale())
                    .sum();
            long activeUsers = userList.stream()
                    .filter(User::isEnabled)
                    .count();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime oneWeekAgo = now.minus(1, ChronoUnit.WEEKS);
            long newUsers = userList.stream()
                    .filter(u -> u.getCreatedAt() != null &&
                            u.getCreatedAt().isAfter(oneWeekAgo) &&
                            u.getCreatedAt().isBefore(now))
                    .count();
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setStatus("true");
            responseDTO.setStatus_code(200);
            responseDTO.setTotal_users(totalUsers);
            responseDTO.setActive_users(activeUsers);
            responseDTO.setNew_users(newUsers);
            responseDTO.setTotal_revenue(totalRevenue);
            return responseDTO;
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setResource("Summary");
            return new ErrorResponse(
                    "false",
                    404,
                    "ResourceNotFound",
                    "There was an error fetching the summary data",
                    errorDetails
            );
        }
    }

    @Override
    public Object getPieChartData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.isAuthenticated()) || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
            return new UserNotFoundException("User not authenticated or token is invalid.");
        }
        String username = authentication.getName();
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            return new UserNotFoundException("User not authenticated or token is invalid.");
        }
        try {
            List<Sale> sales = salesRepository.findAll();
            if (sales.isEmpty()) {
                ErrorDetails errorDetails = new ErrorDetails();
                errorDetails.setChartType("pie");
                return new ErrorResponse(
                        "false",
                        404,
                        "DataFetchError",
                        "There was an error fetching the pie chart data",
                        errorDetails
                );
            }
            List<MonthlySalesDTO> monthlySalesDTOs = sales.stream()
                    .map(s -> new MonthlySalesDTO(s.getMonth(), s.getTotalSale()))
                    .collect(Collectors.toList());
            double totalSales = monthlySalesDTOs.stream()
                    .mapToDouble(MonthlySalesDTO::getTotalSales)
                    .sum();
            List<Double> totalSalesList = monthlySalesDTOs.stream()
                    .map(MonthlySalesDTO::getTotalSales)
                    .collect(Collectors.toList());
            List<Integer> degreesList = monthlySalesDTOs.stream()
                    .map(dto -> (int) ((dto.getTotalSales() / totalSales) * 360))
                    .collect(Collectors.toList());
            List<String> monthList = monthlySalesDTOs.stream()
                    .map(MonthlySalesDTO::getMonth)
                    .collect(Collectors.toList());
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setStatus("true");
            responseDTO.setStatus_code(200);
            responseDTO.setMonth(monthList);
            responseDTO.setTotalSales(totalSalesList);
            responseDTO.setTotalSalesInDegree(degreesList);
            return responseDTO;
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setChartType("pie");
            return new ErrorResponse(
                    "false",
                    404,
                    "DataFetchError",
                    "There was an error fetching the pie chart data",
                    errorDetails
            );
        }
    }
}
