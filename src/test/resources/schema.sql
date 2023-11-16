create TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255),
    password VARCHAR(255),
    is_active BOOLEAN
);
CREATE TABLE training_type (
    id INT AUTO_INCREMENT PRIMARY KEY,
    training_type_name VARCHAR(255)
);
INSERT INTO training_type (training_type_name) VALUES
    ('CARDIO'),
    ('STRENGTH'),
    ('YOGA'),
    ('PILATES'),
    ('SWIMMING');

create TABLE trainee (
    trainee_id BIGINT PRIMARY KEY,
    date_of_birth DATE,
    address VARCHAR(255),
    FOREIGN KEY (trainee_id) REFERENCES users (id)
);
create TABLE trainer (
  trainer_id BIGINT PRIMARY KEY,
  training_type_id INT,
  FOREIGN KEY (trainer_id) REFERENCES users (id),
  FOREIGN KEY (training_type_id) REFERENCES training_type(id)
);
CREATE TABLE training (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    training_name VARCHAR(255),
    training_date DATE,
    training_duration INT,
    trainee_id BIGINT,
    trainer_id BIGINT,
    training_type_id INT,
    FOREIGN KEY (trainee_id) REFERENCES trainee(trainee_id),
    FOREIGN KEY (trainer_id) REFERENCES trainer(trainer_id),
    FOREIGN KEY (training_type_id) REFERENCES training_type(id)
);


