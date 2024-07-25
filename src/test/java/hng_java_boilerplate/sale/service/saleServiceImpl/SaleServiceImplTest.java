package hng_java_boilerplate.sale.service.saleServiceImpl;

import hng_java_boilerplate.sale.dto.ResponseDTO;
import hng_java_boilerplate.sale.entity.Sale;
import hng_java_boilerplate.sale.repository.SaleRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
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

public class SaleServiceImplTest {

    @InjectMocks
    private SaleServiceImpl salesService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SaleRepository salesRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Test
    void testGetSummary_Success() {
        // Mock user repository
        User user = new User();
        user.setIsEnabled(true);
        user.setCreatedAt(LocalDateTime.now().minus(3, ChronoUnit.DAYS));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        //Mock user list
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);

        // Mock sales repository
        Sale sale = new Sale();
        sale.setTotalSale(100L);
        sale.setCreatedAt(LocalDateTime.of(2024, Month.JULY, 15, 0, 0)); // Proper date and time

        // Add Sale object to list
        List<Sale> saleList = new ArrayList<>();
        saleList.add(sale);
        when(salesRepository.findAll()).thenReturn(saleList);
        // Check the response

        ResponseEntity<ResponseDTO> responseDTO = salesService.getSummary();
        assertTrue(responseDTO.getBody().getStatus());
        assertEquals(200, responseDTO.getBody().getStatus_code());
        assertEquals(1, responseDTO.getBody().getTotal_users());
        assertEquals(1, responseDTO.getBody().getActive_users());
        assertEquals(1, responseDTO.getBody().getNew_users());
        assertEquals(100, responseDTO.getBody().getTotal_revenue());
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
        ResponseEntity<ResponseDTO> responseDTO = salesService.getPieChartData();
        assertTrue(responseDTO.getBody().getStatus());
        assertEquals(true, responseDTO.getBody().getStatus());
        assertEquals(200, responseDTO.getBody().getStatus_code());
        assertEquals(Arrays.asList("January", "February"), responseDTO.getBody().getMonth());
        double totalSales = 300.0;
        List<Double> expectedTotalSalesList = Arrays.asList(100.0, 200.0);
        List<Integer> expectedDegreesList = Arrays.asList(
                (int) ((100.0 / totalSales) * 360),
                (int) ((200.0 / totalSales) * 360)
        );
        assertEquals(expectedTotalSalesList, responseDTO.getBody().getTotalSales());
        assertEquals(expectedDegreesList, responseDTO.getBody().getTotalSalesInDegree());
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
        ResponseEntity<ResponseDTO> responseDTO = salesService.getBarChartData();
        assertTrue(responseDTO.getBody().getStatus());
        assertEquals(true, responseDTO.getBody().getStatus());
        assertEquals(200, responseDTO.getBody().getStatus_code());
        assertEquals(Arrays.asList("January", "February"), responseDTO.getBody().getMonth());
        List<Double> expectedTotalSalesList = Arrays.asList(100.0, 200.0);
        assertEquals(expectedTotalSalesList, responseDTO.getBody().getTotalSales());
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
        ResponseEntity<ResponseDTO> responseDTO = salesService.getBarChartData();
        assertTrue(responseDTO.getBody().getStatus());
        assertEquals(true, responseDTO.getBody().getStatus());
        assertEquals(200, responseDTO.getBody().getStatus_code());
        assertEquals(Arrays.asList("January", "February"), responseDTO.getBody().getMonth());
        List<Double> expectedTotalSalesList = Arrays.asList(100.0, 200.0);
        assertEquals(expectedTotalSalesList, responseDTO.getBody().getTotalSales());
    }
}
