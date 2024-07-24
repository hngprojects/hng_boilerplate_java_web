package hng_java_boilerplate.sale.service.serviceImpl;

import hng_java_boilerplate.sale.dto.ErrorResponse;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}
