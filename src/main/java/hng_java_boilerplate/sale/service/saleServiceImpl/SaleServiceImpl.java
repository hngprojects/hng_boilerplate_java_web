package hng_java_boilerplate.sale.service.saleServiceImpl;

import hng_java_boilerplate.sale.dto.ErrorDetails;
import hng_java_boilerplate.sale.dto.MonthlySalesDTO;
import hng_java_boilerplate.sale.dto.ResponseDTO;
import hng_java_boilerplate.sale.entity.Sale;
import hng_java_boilerplate.sale.repository.SaleRepository;
import hng_java_boilerplate.sale.service.SaleService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.UserNotFoundException;
import hng_java_boilerplate.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {
    private final SaleRepository salesRepository;
    private final UserRepository userRepository;

    public SaleServiceImpl(SaleRepository salesRepository, UserRepository userRepository) {
        this.salesRepository = salesRepository;
        this.userRepository = userRepository;
    }

    private void checkAuthentication() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Optional<User> user = userRepository.findByEmail(username);

            if (!(authentication.isAuthenticated()) || !(authentication instanceof UsernamePasswordAuthenticationToken) || user.isEmpty()) {
                throw new UserNotFoundException("User not authenticated or token is invalid.");
            }
        }catch(UserNotFoundException e){
            e.getMessage();
        }

    }

    @Override
    public ResponseEntity<ResponseDTO> getSummary() {
        checkAuthentication();
        try{
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setResource("Summary");
            List<User> userList = userRepository.findAll();
            if (userList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                        false,
                        404,
                        "ResourceNotFound",
                        "The Summary data could not be found",
                        errorDetails
                ));
            }
            List<Sale> saleList = salesRepository.findAll();
            if (saleList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                        false,
                        404,
                        "ResourceNotFound",
                        "The Summary data could not be found",
                        errorDetails
                ));
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
            responseDTO.setStatus(true);
            responseDTO.setStatus_code(200);
            responseDTO.setTotal_users(totalUsers);
            responseDTO.setActive_users(activeUsers);
            responseDTO.setNew_users(newUsers);
            responseDTO.setTotal_revenue(totalRevenue);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setResource("Summary");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                    false,
                    404,
                    "ResourceNotFound",
                    "There was an error fetching the summary data",
                    errorDetails
            ));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> getPieChartData() {
        checkAuthentication();
        try {
            List<Sale> sales = salesRepository.findAll();
            if (sales.isEmpty()) {
                ErrorDetails errorDetails = new ErrorDetails();
                errorDetails.setChartType("pie");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                        false,
                        404,
                        "DataFetchError",
                        "There was an error fetching the pie chart data",
                        errorDetails
                ));
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
            responseDTO.setStatus(true);
            responseDTO.setStatus_code(200);
            responseDTO.setMonth(monthList);
            responseDTO.setTotalSales(totalSalesList);
            responseDTO.setTotalSalesInDegree(degreesList);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setChartType("pie");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                    false,
                    404,
                    "DataFetchError",
                    "There was an error fetching the pie chart data",
                    errorDetails
            ));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> getBarChartData() {
        checkAuthentication();
        try {
            List<Sale> sales = salesRepository.findAll();
            if (sales.isEmpty()) {
                ErrorDetails errorDetails = new ErrorDetails();
                errorDetails.setChartType("bar");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                        false,
                        404,
                        "DataFetchError",
                        "There was an error fetching the bar chart data",
                        errorDetails
                ));
            }
            List<MonthlySalesDTO> monthlySalesDTOs = sales.stream()
                    .map(s -> new MonthlySalesDTO(s.getMonth(), s.getTotalSale()))
                    .collect(Collectors.toList());
            List<Double> totalSalesList = monthlySalesDTOs.stream()
                    .map(MonthlySalesDTO::getTotalSales)
                    .collect(Collectors.toList());
            List<String> monthList = monthlySalesDTOs.stream()
                    .map(MonthlySalesDTO::getMonth)
                    .collect(Collectors.toList());
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setStatus(true);
            responseDTO.setStatus_code(200);
            responseDTO.setMonth(monthList);
            responseDTO.setTotalSales(totalSalesList);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setChartType("bar");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                    false,
                    404,
                    "DataFetchError",
                    "There was an error fetching the bar chart data",
                    errorDetails
            ));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> getLineChartData() {
        checkAuthentication();
        try {
            List<Sale> sales = salesRepository.findAll();
            if (sales.isEmpty()) {
                ErrorDetails errorDetails = new ErrorDetails();
                errorDetails.setChartType("line");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                        false,
                        404,
                        "DataFetchError",
                        "There was an error fetching the line chart data",
                        errorDetails
                ));
            }
            List<MonthlySalesDTO> monthlySalesDTOs = sales.stream()
                    .map(s -> new MonthlySalesDTO(s.getMonth(), s.getTotalSale()))
                    .collect(Collectors.toList());
            List<Double> totalSalesList = monthlySalesDTOs.stream()
                    .map(MonthlySalesDTO::getTotalSales)
                    .collect(Collectors.toList());
            List<String> monthList = monthlySalesDTOs.stream()
                    .map(MonthlySalesDTO::getMonth)
                    .collect(Collectors.toList());
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setStatus(true);
            responseDTO.setStatus_code(200);
            responseDTO.setMonth(monthList);
            responseDTO.setTotalSales(totalSalesList);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setChartType("line");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(
                    false,
                    404,
                    "DataFetchError",
                    "There was an error fetching the line chart data",
                    errorDetails
            ));
        }
    }
}

