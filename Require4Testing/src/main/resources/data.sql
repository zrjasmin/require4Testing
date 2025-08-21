INSERT INTO berechtigung (name) VALUES ('create_requirement'), ('edit_requirement'),
			('delete_requirement'), ('create_test'),('edit_test') ,
			('delete_test'),('create_testlauf'), ('edit_testlauf') ,
			('delete_testlauf'), ('assign_testlauf');


INSERT INTO role (name) VALUES ('Requirement Engineer'), ('Tester:in'), ('Testfallersteller:in'), ('Testmanager:in');

INSERT INTO role_berechtigungen (role_id, berechtigung_id)
SELECT r.id, b.id
FROM role r
CROSS JOIN berechtigung b
WHERE r.name = 'Requirement Engineer'
  AND b.name IN ('create_requirement', 'edit_requirement', 'delete_requirement');
  
INSERT INTO role_berechtigungen (role_id, berechtigung_id)
SELECT r.id, b.id
FROM role r
CROSS JOIN berechtigung b
WHERE r.name = 'Testfallersteller:in'
  AND b.name IN ('create_test', 'edit_test', 'delete_test');
  
INSERT INTO role_berechtigungen (role_id, berechtigung_id)
SELECT r.id, b.id
FROM role r
CROSS JOIN berechtigung b
WHERE r.name = 'Testmanager:in';


INSERT INTO user (name, vorname, email, profile_image_path) VALUES 
	('Zimmer', 'Jasmin', 'email@gmx.net', '/images/profiles/pic1.png'),
	('Müller', 'Lukas', 'email@gmx.net', '/images/profiles/pic2.png'),
	('Meyer', 'Louse', 'email@gmx.net', '/images/profiles/pic3.png'),
	('Heinrich', 'Lena', 'email@gmx.net', '/images/profiles/pic4.png'),
	('Karls', 'Niklas', 'email@gmx.net', '/images/profiles/pic5.png'),
	('Peter', 'Thomas', 'email@gmx.net', '/images/profiles/pic6.png');



INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM user u
CROSS JOIN role r
WHERE u.name = 'Zimmer' AND r.name IN ('Testmanager:in');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM user u
CROSS JOIN role r
WHERE u.name = 'Müller' AND r.name IN ('Requirement Engineer');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM user u
CROSS JOIN role r
WHERE u.name = 'Meyer' AND r.name IN ('Testfallersteller:in');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM user u
CROSS JOIN role r
WHERE u.name = 'Heinrich' AND r.name IN ('Testfallersteller:in', 'Requirement Engineer');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM user u
CROSS JOIN role r
WHERE u.name = 'Karls' AND r.name IN ('Tester:in');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM user u
CROSS JOIN role r
WHERE u.name = 'Peter' AND r.name IN ('Testmanager:in', 'Tester:in');





START TRANSACTION;
SELECT id INTO @creator_id
FROM user
WHERE name = 'Heinrich';

INSERT INTO anforderung (nr,title, beschreibung, kategorie, notizen, quelle, prioritaet, ersteller_id)
VALUES (
'AR-001',
'Benutzeranmeldung erfolgreich',
'Der Benutzer kann sich mit gültigen Anmeldedaten erfolgreich anmelden und wird zur Startseite weitergeleitet.',
'FUNKTIONAL',
'Bei falscher Kombination: Fehlermeldung „Ungültige Anmeldedaten“ anzeigen; Account-Sperrung nach 5 Fehlversuchen beachten.',
'Produkt-Backlog Item #A-101',
'HOCH',
@creator_id
);

SET @anf_id = LAST_INSERT_ID();

INSERT INTO kriterium (beschreibung, anf_id) 
VALUES
('Eingabe gültiger E-Mail und Passwort', @anf_id),
('Erfolgreiche Authentifizierung durch Backend', @anf_id),
('Weiterleitung zur Startseite und Anzeige des Benutzernamens', @anf_id);



INSERT INTO anforderung (nr,title, beschreibung, kategorie, notizen, quelle, prioritaet, ersteller_id)
VALUES (
'AR-002',
'Passwort vergessen – Reset-Link',
'Nutzer kann über „Passwort vergessen“ einen Reset-Link erhalten und neues Passwort setzen.',
'FUNKTIONAL',
'Sicherheitscheck gegen Missbrauch; Link nur 1x verwendbar.',
'Produkt-Backlog Item #A-205',
'MITTEL',
@creator_id
);

SET @anf_id = LAST_INSERT_ID();

INSERT INTO kriterium (beschreibung, anf_id) 
VALUES
('E-Mail-Adressermittlung korrekt', @anf_id),
('Versand des Reset-Links mit gültigem Token', @anf_id),
('Token gilt 60 Minuten', @anf_id);


INSERT INTO anforderung (nr,title, beschreibung, kategorie, notizen, quelle, prioritaet, ersteller_id)
VALUES (
'AR-003',
'Produktliste lädt korrekt mit Filter',
'Die Produktliste wird geladen und Filter (Kategorie, Preisbereich) wirken erwartungsgemäß auf die Anzeige.',
'FUNKTIONAL',
'Leere Ergebnisse sollten sinnvoll gemeldet werden.',
'Sprintziel-Review',
'HOCH',
@creator_id
);

SET @anf_id = LAST_INSERT_ID();

INSERT INTO kriterium (beschreibung, anf_id) 
VALUES
('Liste lädt innerhalb von 2–3 Sekunden', @anf_id),
('Filteroptionen funktionieren (inkl. Mehrfachauswahl)', @anf_id),
('Gefilterte Ergebnisse entsprechen den Kriterien', @anf_id);
COMMIT;
	
	
	
START TRANSACTION;
SELECT id INTO @creator_id
FROM user
WHERE name = 'Müller';

INSERT INTO anforderung (nr,title, beschreibung, kategorie, notizen, quelle, prioritaet, ersteller_id)
VALUES (
'AR-004',
'Mobile Menü-UX – Zugriff auf Hauptnavigation',
'Auf mobilen Geräten öffnet sich das Hamburger-Menü zuverlässig und navigiert zu den Hauptbereichen.',
'USABILITY',
'Responsives Verhalten auf iOS/Android prüfen.',
'UX-Design-Stilguide',
'HOCH',
@creator_id
);

SET @anf_id = LAST_INSERT_ID();

INSERT INTO kriterium (beschreibung, anf_id) 
VALUES
('Menü öffnet/schließt bei Klick', @anf_id),
('Alle wichtigen Links sichtbar', @anf_id),
('Kein Layout-Verschieben oder Überlappen', @anf_id);


INSERT INTO anforderung (nr,title, beschreibung, kategorie, notizen, quelle, prioritaet, ersteller_id)
VALUES (
'AR-005',
'Warenkorb-Summe korrekt berechnen',
'Beim Hinzufügen von Produkten wird die korrekte Gesamtsumme inklusive Mehrwertsteuer angezeigt',
'FUNKTIONAL',
'Rabatte/Nutzerausnahmen separat testen.',
'Preiskalkulationsspektrum',
'HOCH',
@creator_id
);

SET @anf_id = LAST_INSERT_ID();

INSERT INTO kriterium (beschreibung, anf_id) 
VALUES
('Einzelpreise korrekt multipliziert mit Mengen', @anf_id),
('Mehrwertsteuer korrekt angewendet', @anf_id),
('Gesamtsumme aktualisiert nach jeder Änderung', @anf_id);


COMMIT;


START TRANSACTION;

INSERT INTO test (nr, title, beschreibung, erwartetes_ergebnis, testdaten, notizen, anf_id, ersteller_id)
VALUES(
'TF-001',
'Erfolgreiche Benutzeranmeldung mit gültigen Daten',
'Nutzer ist registriert; gültige E-Mail und Passwort bekannt',
'Backend bestätigt Authentifizierung; Benutzer wird zur Startseite weitergeleitet; Benutzername wird angezeigt',
' Benutzer (z. B. email: user@example.com, password: Match123!)',
'Authentifizierungsservice erreichbar; Session-Management aktiv',
1, 3
);


SET @test_id = LAST_INSERT_ID();

INSERT INTO testschritt (beschreibung,step_number, test_id) 
VALUES
('Öffne die Login-Seite.', 1,@test_id),
('Gib gültige E-Mail und gültiges Passwort ein.',2, @test_id),
('klicke auf „Anmelden“.',3, @test_id);

INSERT INTO test (nr, title, beschreibung, erwartetes_ergebnis, testdaten, notizen, anf_id, ersteller_id)
VALUES(
'TF-002',
'Passwort-Reset-Link per E-Mail',
' Nutzer existiert im System; korrekte E-Mail-Adresse bekannt',
'Reset-Link wird versendet (Token in E-Mail enthalten).
Token ist gültig (60 Minuten) und ermöglicht Passwort-Update.
Neues Passwort funktioniert beim Login.',
' ',
'Test-Token-Gültigkeit prüfen; 1x Verwendung beachten',
2, 4
);


SET @test_id = LAST_INSERT_ID();

INSERT INTO testschritt (beschreibung,step_number, test_id) 
VALUES
('Öffne Login-Seite.', 1,@test_id),
('Klicke auf „Passwort vergessen“.',2, @test_id),
('Gib die registrierte E-Mail-Adresse ein und bestätige.',3, @test_id),
('Warte auf Empfang des Reset-Links im Postfach.',4, @test_id),
('Folge dem Reset-Link und setze neues Passwort.',5, @test_id);


INSERT INTO test (nr, title, beschreibung, erwartetes_ergebnis, testdaten, notizen, anf_id, ersteller_id)
VALUES(
'TF-003',
'Produktliste lädt und Filter funktionieren',
' Produktkatalog vorhanden; Filteroptionen sichtbar',
'Produktliste lädt innerhalb der vorgegebenen Zeit (2–3 Sekunden).
Gefilterte Ergebnisse entsprechen den gewählten Kriterien.
Reihenfolge/Seitennavigation funktionieren.',
'',
'Keine Produkte vorhanden, dann Meldung "Keine Ergebnisse".',
3, 3
);


SET @test_id = LAST_INSERT_ID();

INSERT INTO testschritt (beschreibung,step_number, test_id) 
VALUES
('Öffne Produktliste.', 1,@test_id),
('Stelle Filter Kategorie auf „Elektronik“ ein.',2, @test_id),
('Stelle Filter Preisbereich auf „100–300€“ ein; Mehrfachauswahl aktivieren (falls vorgesehen).',3, @test_id),
('Anwenden der Filter.',4, @test_id);

INSERT INTO test (nr, title, beschreibung, erwartetes_ergebnis, testdaten, notizen, anf_id, ersteller_id)
VALUES(
'TF-004',
'Mobile Hamburger-Menü öffnet und navigiert korrekt',
'Übereinstimmende responsive UI; getestet auf Mobilgerät oder Emulator',
' Menü öffnet/schließt zuverlässig bei Klick.
Alle wichtigen Links sichtbar und anklickbar.
Navigation führt zu der entsprechenden Seite, Layout bleibt stabil (kein Überlappen).',
'',
'Test auf iOS und Android (verschiedene Browser)',
3, 3
);


SET @test_id = LAST_INSERT_ID();

INSERT INTO testschritt (beschreibung,step_number, test_id) 
VALUES
('Öffne Anwendung auf Mobilgerät (Verkleinerte Ansicht).', 1,@test_id),
('Tippe auf das Hamburger-Symbol.',2, @test_id),
('Prüfe, dass das Menü aufgeklappt wird und alle Hauptlinks sichtbar sind.',3, @test_id),
('Tippe auf einen Hauptlink (z. B. „Kategorien“).',4, @test_id);
	


INSERT INTO test (nr, title, beschreibung, erwartetes_ergebnis, testdaten, notizen, anf_id, ersteller_id)
VALUES(
'TF-005',
'Filter-URL generieren und sharable Link öffnen',
' enutzer kann einen aktuellen Filterzustand als URL generieren und diese URL in einem anderen Browser/Device öffnen, um dieselbe gefilterte Liste zu sehen.',
'Die neue Instanz zeigt dieselben gefilterten Ergebnisse; Ladezeit ≤3 Sekunden.',
'Temperaturunabhängige Testdaten; öffentlich zugängliche URL-Struktur',
'Frontend-URL-Generierung, Router/Query-Parameter-Verarbeitung',
3, 3
);


SET @test_id = LAST_INSERT_ID();

INSERT INTO testschritt (beschreibung,step_number, test_id) 
VALUES
('Öffne Produktliste.', 1,@test_id),
('Stelle Filter Kategorie auf „Elektronik“ ein.',2, @test_id),
('Stelle Filter Preisbereich auf „100–300€“ ein; Mehrfachauswahl aktivieren (falls vorgesehen).',3, @test_id),
('Anwenden der Filter.',4, @test_id);
	
COMMIT;



INSERT INTO status (name, beschreibung) VALUES 
('GEPLANT', 'Geplant'), 
('BEARBEITUNG', 'In Bearbeitung'),
('BESTANDEN', 'Bestanden'), 
('NICHT_BESTANDEN', 'Nicht Bestanden'),
('ABGEBROCHEN','Abgebrochen');
			
	


INSERT INTO testlauf (nr, title, beschreibung, testumgebung, ersteller_id, tester_id, status_id) 
VALUES (
'TL-001',
'Produktliste lädt und Filter funktionieren',
'Vorbedingungen: Produktkatalog vorhanden; Filteroptionen sichtbar; Backend-Filterdienst erreichbar',
'DEV',
1, 5, 1
);

INSERT INTO testfall_testlauf (testlauf_id, test_id) 
VALUES 
(1,1),
(1,5);


	
	


