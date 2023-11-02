//package com.epam.upskill.service.impl;
//
//import com.epam.upskill.converter.TraineeConverter;
//import com.epam.upskill.dao.TraineeRepository;
//import com.epam.upskill.dto.TraineeRegistration;
//import com.epam.upskill.entity.Trainee;
//import com.epam.upskill.entity.User;
//import com.epam.upskill.exception.TraineeNotFoundException;
//import com.epam.upskill.service.UserService;
//import com.epam.upskill.util.UserUtils;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TraineeServiceImplTest {
//  @InjectMocks
//  private TraineeServiceImpl traineeService;
//
//  @Mock
//  private TraineeRepository traineeRepository;
//
//  @Mock
//  private UserService userService;
//
//  @Mock
//  private TraineeConverter traineeConverter;
//
////  @BeforeEach
////  public void setUp() {
////    MockitoAnnotations.openMocks(this);
////  }
//
//  @Test
//  void testFindById() {
//    long traineeId = 1L;
//    Trainee trainee = new Trainee();
//    trainee.setId(traineeId);
//    when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));
//    Optional<Trainee> result = traineeService.findById(traineeId);
//    assertEquals(trainee, result.orElse(null));
//  }
//
//  @Test
//   void testFindByIdNotFound() {
//    long traineeId = 1L;
//    when(traineeRepository.findById(traineeId)).thenReturn(Optional.empty());
//
//    assertThrows(TraineeNotFoundException.class, () -> traineeService.findById(traineeId));
//  }
//
//  @Test
//   void testFindByUsername() {
//    String username = "testUser";
//    Trainee trainee = new Trainee();
//    trainee.setUsername(username);
//
//    when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
//
//    Trainee result = traineeService.findByUsername(username);
//
//    assertEquals(trainee, result);
//  }
//
//  @Test
//  void testFindByUsernameNotFound() {
//    String username = "nonExistentUser";
//
//    when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());
//
//    assertThrows(TraineeNotFoundException.class, () -> traineeService.findByUsername(username));
//  }
//
//  @Test
//   void testFindAll() {
//    List<Trainee> trainees = new ArrayList<>();
//    trainees.add(new Trainee());
//    trainees.add(new Trainee());
//
//    when(traineeRepository.findAll()).thenReturn(trainees);
//
//    List<Trainee> result = traineeService.findAll();
//
//    assertEquals(trainees, result);
//  }
//
//  @Test
//  void testSaveTrainee() {
//    TraineeRegistration traineeDto = new TraineeRegistration("John", "Doe",
//        "Address", LocalDate.now());
//    String username = "John.Doe";
//    String password = "randomPassword";
//    Trainee trainee = new Trainee();
//    trainee.setUsername(username);
//    trainee.setFirstName(traineeDto.firstName());
//    trainee.setLastName(traineeDto.lastName());
//    trainee.setAddress(traineeDto.address());
//    trainee.setUsername(username);
//    trainee.setPassword(password);
//    List<User> userList = new ArrayList<>();
//    userList.add(new User(33L, "Jon", "Mo", "Jon.Mo", "randomPassword", true,
//        null));
//
//    when(userService.findAll()).thenReturn(userList);
//    when(traineeConverter.toTrainee(traineeDto)).thenReturn(trainee);
//    try (MockedStatic<UserUtils> utilities = Mockito.mockStatic(UserUtils.class)) {
//      utilities.when(() -> UserUtils.createUsername(any(), any(), any())).thenReturn(username);
//      utilities.when(UserUtils::generateRandomPassword).thenReturn(password);
//      when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);
//      // Act
//      Trainee savedTrainee = traineeService.saveTrainee(traineeDto);
//      // Assert
//      assertNotNull(savedTrainee);
//      assertEquals(username, savedTrainee.getUsername());
//      assertEquals(password, savedTrainee.getPassword());
//      assertTrue(savedTrainee.isActive());
//      verify(traineeRepository, times(1)).save(any(Trainee.class));
//    }
//  }
//}