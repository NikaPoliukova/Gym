package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TrainerConverter;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.User;
import com.epam.upskill.exception.TraineeNotFoundException;
import com.epam.upskill.exception.TrainerNotFoundException;
import com.epam.upskill.service.UserService;
import com.epam.upskill.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

  @InjectMocks
  private TrainerServiceImpl trainerService;

  @Mock
  private TrainerRepository trainerRepository;

  @Mock
  private UserService userService;

  @Mock
  private TrainerConverter trainerConverter;


//  @BeforeEach
//  public void setUp() {
//    MockitoAnnotations.openMocks(this);
//  }

  @Test
  void testFindById() {
    long trainerId = 1L;
    Trainer trainer = new Trainer();
    when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));
   Trainer result = trainerService.findById(trainerId);
    assertTrue(result!= null);
    assertEquals(trainer, result);
  }

  @Test
  void testFindById_TrainerNotFound() {
    long trainerId = 55555L;
    when(trainerRepository.findById(trainerId)).thenReturn(Optional.empty());
    assertThrows(TraineeNotFoundException.class, () -> {
      trainerService.findById(trainerId);
    });
  }

  @Test
  void testFindByUsername() {
    String username = "john_doe";
    Trainer trainer = new Trainer();
    when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
    Trainer result = trainerService.findByUsername(username);

    assertTrue(result != null);
    assertEquals(trainer, result);
  }

  @Test
  void testFindByUsername_TrainerNotFound() {
    String username = "john_doe";
    when(trainerRepository.findByUsername(username)).thenReturn(Optional.empty());

    assertThrows(TrainerNotFoundException.class, () -> trainerService.findByUsername(username));
  }

  @Test
  void testFindAll() {
    List<Trainer> trainers = new ArrayList<>();
    trainers.add(new Trainer());
    when(trainerRepository.findAll()).thenReturn(trainers);

    List<Trainer> result = trainerService.findAll();

    assertFalse(result.isEmpty());
    assertEquals(trainers, result);
  }

  @Test
  void testSaveTrainer() {
    // Arrange
    TrainerRegistration trainerDto = new TrainerRegistration("John", "Doe", "Specialization");
    String username = "John.Doe";
    String password = "randomPassword";
    Trainer trainer = new Trainer();
    trainer.setFirstName(trainerDto.firstName());
    trainer.setLastName(trainerDto.lastName());
    trainer.setSpecialization(trainerDto.specialization());
    trainer.setUsername(username);
    trainer.setPassword(password);
    List<User> userList = new ArrayList<>();
    userList.add(new User(33L, "Jon", "Mo", "Jon.Mo", "randomPassword", true,
        null));

    when(userService.findAll()).thenReturn(userList);
    when(trainerConverter.toTrainer(trainerDto)).thenReturn(trainer);
    try (MockedStatic<UserUtils> utilities = Mockito.mockStatic(UserUtils.class)) {
      utilities.when(() -> UserUtils.createUsername(any(), any(), any())).thenReturn(username);
      utilities.when(UserUtils::generateRandomPassword).thenReturn(password);
      when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
      // Act
      Trainer savedTrainer = trainerService.saveTrainer(trainerDto);
      // Assert
      assertNotNull(savedTrainer);
      assertEquals(username, savedTrainer.getUsername());
      assertEquals(password, savedTrainer.getPassword());
      assertTrue(savedTrainer.isActive());
      verify(trainerRepository, times(1)).save(any(Trainer.class));
    }
  }


  @Test
  void testUpdateTrainer() {
    long trainerId = 1L;
    TrainerDto trainerDto = new TrainerDto(trainerId, "Updated Specialization", "Updatedpassword");
    Trainer trainer = new Trainer();
    when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));
    when(trainerRepository.update(trainer)).thenReturn(trainer);

    Trainer result = trainerService.updateTrainer(trainerDto);

    assertTrue(result != null);
    assertEquals(trainer, result);
    assertEquals(trainerDto.specialization(), result.getSpecialization());
  }

  @Test
  void testUpdateTrainer_TrainerNotFound() {
    long trainerId = 1L;
    TrainerDto trainerDto = new TrainerDto(trainerId, "Updated Specialization", "Updatedpassword");
    when(trainerRepository.findById(trainerId)).thenReturn(Optional.empty());
    assertThrows(NoSuchElementException.class, () -> trainerService.updateTrainer(trainerDto));
  }


  @Test
  void testFindByIsActive() {
    List<Trainer> activeTrainers = new ArrayList<>();
    activeTrainers.add(new Trainer());
    when(trainerRepository.findByIsActive()).thenReturn(activeTrainers);
    List<Trainer> result = trainerService.findByIsActive();
    assertFalse(result.isEmpty());
    assertEquals(activeTrainers, result);
  }

  @Test
  void testFindByIsActive_NoActiveTrainers() {
    when(trainerRepository.findByIsActive()).thenReturn(Collections.emptyList());

    List<Trainer> result = trainerService.findByIsActive();

    assertTrue(result.isEmpty());
  }

  @Test
  void testToggleProfileActivation() {
    long trainerId = 1L;
    Trainer trainer = new Trainer();
    trainer.setActive(true);
    when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));

    trainerService.toggleProfileActivation(trainerId);

    assertFalse(trainer.isActive());
    verify(trainerRepository).toggleProfileActivation(trainer);
  }
}
