package hng_java_boilerplate.sale.service.serviceImpl;

import hng_java_boilerplate.sale.dto.ErrorResponse;
import hng_java_boilerplate.sale.dto.ResponseDTO;
import hng_java_boilerplate.sale.entity.Sale;
import hng_java_boilerplate.sale.repository.SaleRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.UserNotFoundException;
import hng_java_boilerplate.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class SalesServiceImplTest {

    @InjectMocks
    private SalesServiceImpl salesService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SaleRepository salesRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user@example.com", "password"));
    }

    @Test
    void testGetSummary_Success() {
        User user = new User();
        user.setIsEnabled(true);
        user.setCreatedAt(LocalDateTime.now().minus(3, ChronoUnit.DAYS));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        Sale sale = new Sale();
        sale.setTotalSale(100L);
        sale.setCreatedAt(LocalDateTime.of(2024, Month.JULY, 15, 0, 0));
        List<Sale> saleList = new ArrayList<>();
        saleList.add(sale);
        when(salesRepository.findAll()).thenReturn(saleList);
        Object result = salesService.getSummary();
        assertTrue(result instanceof ResponseDTO);
        ResponseDTO responseDTO = (ResponseDTO) result;
        assertEquals("true", responseDTO.getStatus());
        assertEquals(200, responseDTO.getStatus_code());
        assertEquals(1, responseDTO.getTotal_users());
        assertEquals(1, responseDTO.getActive_users());
        assertEquals(1, responseDTO.getNew_users());
        assertEquals(100, responseDTO.getTotal_revenue());
    }

    @Test
    void testGetSummary_Exception() {
        User user = new User();
        user.setIsEnabled(true);
        user.setCreatedAt(LocalDateTime.now().minus(3, ChronoUnit.DAYS));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(salesRepository.findAll()).thenThrow(new RuntimeException("Database error"));
        Object result = salesService.getSummary();
        assertTrue(result instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) result;
        assertEquals("false", errorResponse.getStatus());
        assertEquals(404, errorResponse.getStatus_code());
        assertEquals("ResourceNotFound", errorResponse.getError());
        assertEquals("The Summary data could not be found", errorResponse.getMessage());
    }

    @Test
    public void testGetPieChartData_Success() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = new User();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        Sale sale1 = new Sale();
        sale1.setTotalSale(100L);
        sale1.setCreatedAt(LocalDateTime.of(2024, Month.JANUARY, 15, 0, 0));
        Sale sale2 = new Sale();
        sale2.setTotalSale(200L);
        sale2.setCreatedAt(LocalDateTime.of(2024, Month.FEBRUARY, 15, 0, 0));
        List<Sale> saleList = new ArrayList<>();
        saleList.add(sale1);
        saleList.add(sale2);
        when(salesRepository.findAll()).thenReturn(saleList);
        Object response = salesService.getPieChartData();
        assertTrue(response instanceof ResponseDTO);
        ResponseDTO responseDTO = (ResponseDTO) response;
        assertEquals("true", responseDTO.getStatus());
        assertEquals(200, responseDTO.getStatus_code());
        assertEquals(Arrays.asList("January", "February"), responseDTO.getMonth());
        double totalSales = 300.0;
        List<Double> expectedTotalSalesList = Arrays.asList(100.0, 200.0);
        List<Integer> expectedDegreesList = Arrays.asList(
                (int) ((100.0 / totalSales) * 360),
                (int) ((200.0 / totalSales) * 360)
        );
        assertEquals(expectedTotalSalesList, responseDTO.getTotalSales());
        assertEquals(expectedDegreesList, responseDTO.getTotalSalesInDegree());
    }

    @Test
    public void testGetPieChartData_NoSalesData() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = new User();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(salesRepository.findAll()).thenReturn(Collections.emptyList());
        Object response = salesService.getPieChartData();
        assertTrue(response instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response;
        assertEquals("false", errorResponse.getStatus());
        assertEquals(404, errorResponse.getStatus_code());
        assertEquals("DataFetchError", errorResponse.getError());
        assertEquals("There was an error fetching the pie chart data", errorResponse.getMessage());
    }

    @Test
    public void testGetPieChartData_Unauthenticated() {
        SecurityContextHolder.getContext().setAuthentication(null);
        Object response = salesService.getPieChartData();
        assertTrue(response instanceof UserNotFoundException);
        UserNotFoundException exception = (UserNotFoundException) response;
        assertEquals("User not authorized or token invalid.", exception.getMessage());
    }

    @Test
    public void testGetPieChartData_ExceptionHandling() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = new User();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(salesRepository.findAll()).thenThrow(new RuntimeException("Database error"));
        Object response = salesService.getPieChartData();
        assertTrue(response instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response;
        assertEquals("false", errorResponse.getStatus());
        assertEquals(404, errorResponse.getStatus_code());
        assertEquals("DataFetchError", errorResponse.getError());
        assertEquals("There was an error fetching the pie chart data", errorResponse.getMessage());
    }

    @Test
    public void testGetBarChartData_Success() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = new User();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        Sale sale1 = new Sale();
        sale1.setTotalSale(100L);
        sale1.setCreatedAt(LocalDateTime.of(2024, Month.JANUARY, 15, 0, 0));
        Sale sale2 = new Sale();
        sale2.setTotalSale(200L);
        sale2.setCreatedAt(LocalDateTime.of(2024, Month.FEBRUARY, 15, 0, 0));
        List<Sale> saleList = new ArrayList<>();
        saleList.add(sale1);
        saleList.add(sale2);
        when(salesRepository.findAll()).thenReturn(saleList);
        Object response = salesService.getBarChartData();
        assertTrue(response instanceof ResponseDTO);
        ResponseDTO responseDTO = (ResponseDTO) response;
        assertEquals("true", responseDTO.getStatus());
        assertEquals(200, responseDTO.getStatus_code());
        assertEquals(Arrays.asList("January", "February"), responseDTO.getMonth());
        List<Double> expectedTotalSalesList = Arrays.asList(100.0, 200.0);
        assertEquals(expectedTotalSalesList, responseDTO.getTotalSales());
    }

    @Test
    public void testGetLineChartDataSuccess() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = new User();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        Sale sale1 = new Sale();
        sale1.setTotalSale(100L);
        sale1.setCreatedAt(LocalDateTime.of(2024, Month.JANUARY, 15, 0, 0));
        Sale sale2 = new Sale();
        sale2.setTotalSale(200L);
        sale2.setCreatedAt(LocalDateTime.of(2024, Month.FEBRUARY, 15, 0, 0));
        List<Sale> saleList = new ArrayList<>();
        saleList.add(sale1);
        saleList.add(sale2);
        when(salesRepository.findAll()).thenReturn(saleList);
        Object response = salesService.getLineChartData();
        assertTrue(response instanceof ResponseDTO);
        ResponseDTO responseDTO = (ResponseDTO) response;
        assertEquals("true", responseDTO.getStatus());
        assertEquals(200, responseDTO.getStatus_code());
        assertEquals(Arrays.asList("January", "February"), responseDTO.getMonth());
        List<Double> expectedTotalSalesList = Arrays.asList(100.0, 200.0);
        assertEquals(expectedTotalSalesList, responseDTO.getTotalSales());
    }
}
