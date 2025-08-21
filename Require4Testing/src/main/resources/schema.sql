CREATE TABLE IF NOT EXISTS berechtigung (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL);


-- Tabelle: role
CREATE TABLE  IF NOT EXISTS role (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS role_berechtigungen (
  role_id BIGINT NOT NULL,
  berechtigung_id BIGINT NOT NULL,
  PRIMARY KEY (role_id, berechtigung_id),
  CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
  CONSTRAINT fk_berechtigung FOREIGN KEY (berechtigung_id) REFERENCES berechtigung(id) ON DELETE CASCADE
);


CREATE TABLE  IF NOT EXISTS user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  vorname VARCHAR(255),
  email VARCHAR(255),
  profile_image_path VARCHAR(512)
);



-- Join-Tabelle: user_roles (Many-to-Many zwischen User und Role)
CREATE TABLE  IF NOT EXISTS `user_roles` (
  `user_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `role`(`id`) ON DELETE CASCADE
);
CREATE TABLE  IF NOT EXISTS anforderung (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nr VARCHAR(255),
  title VARCHAR(100) NOT NULL,
  beschreibung VARCHAR(1000),
  quelle VARCHAR(100),
  notizen VARCHAR(1000),
  kategorie VARCHAR(255),
  prioritaet VARCHAR(255),
  ersteller_id BIGINT,

  CONSTRAINT fk_anforderung_ersteller FOREIGN KEY (ersteller_id)
    REFERENCES user(id)
    ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS kriterium (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  beschreibung VARCHAR(255),
  anf_id BIGINT,
  CONSTRAINT fk_kriterium_anforderung FOREIGN KEY (anf_id)
    REFERENCES anforderung(id)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS test (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nr VARCHAR(255),
  title VARCHAR(100) NOT NULL,
  beschreibung VARCHAR(1000),
  erwartetes_ergebnis VARCHAR(200),
  testdaten VARCHAR(1000),
  notizen VARCHAR(1000),
  ersteller_id BIGINT,
  anf_id BIGINT,
  
  CONSTRAINT fk_test_ersteller FOREIGN KEY (ersteller_id)
    REFERENCES user(id)
    ON DELETE SET NULL,
    
    CONSTRAINT fk_test_anforderung FOREIGN KEY (anf_id)
    REFERENCES anforderung(id)
     ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS testschritt (
  id BIGINT NOT NULL AUTO_INCREMENT,
  test_id BIGINT NOT NULL,
  beschreibung VARCHAR(1000),
  step_Number INTEGER,
     
  -- weitere Felder von Testschritt hier
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_testschritt_test` FOREIGN KEY (test_id) REFERENCES test(id) ON DELETE CASCADE
); 

CREATE TABLE  IF NOT EXISTS status(
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(100),
	beschreibung VARCHAR(200),
	  PRIMARY KEY (`id`)
);



CREATE TABLE IF NOT EXISTS testlauf(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nr VARCHAR(255),
  title VARCHAR(100) NOT NULL,
  beschreibung VARCHAR(1000),
  kommentar VARCHAR(200),
  testumgebung VARCHAR(1000),
  status_id BIGINT,
  ersteller_id BIGINT,
  tester_id BIGINT,
  
   CONSTRAINT fk_testlauf_status FOREIGN KEY (status_id)
   REFERENCES status(id)
   ON DELETE  SET NULL,
   
    CONSTRAINT fk_testlauf_ersteller FOREIGN KEY (ersteller_id)
   REFERENCES user(id)
   ON DELETE  SET NULL,
   
   CONSTRAINT fk_testlauf_teser FOREIGN KEY (tester_id)
   REFERENCES user(id)
   ON DELETE  SET NULL
     
     
);



CREATE TABLE IF NOT EXISTS `testfall_testlauf` (
  `testlauf_id` BIGINT NOT NULL,
  `test_id` BIGINT NOT NULL,
  PRIMARY KEY (`testlauf_id`, `test_id`),
  CONSTRAINT `fk_tf_tl` FOREIGN KEY (`testlauf_id`) REFERENCES `testlauf`(`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_tf_t` FOREIGN KEY (`test_id`) REFERENCES `test`(`id`) ON DELETE CASCADE
)


