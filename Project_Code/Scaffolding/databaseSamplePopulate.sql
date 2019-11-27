INSERT INTO people(firstName, lastName, isTutor, bio) VALUES 
("Mark", "Burrell", TRUE, "Junior SE student"),  /* 1 */
("Kaylie", "Prieur", TRUE, "Senior Buisness Student"), /* 2 */
("Luke", "Baird", TRUE, "Junior EE Student"); /* 3 */

INSERT INTO people(firstName, lastName) VALUES 
("Joe", "Burrell"), /* 4 */
("Paul", "Silva"), /* 5 */
("Matt", "Jaffe"); /* 6 */

INSERT INTO courses(courseCode, courseName) VALUES
("CEC320", "Microprocessor System"),
("EC211", "Microeconmics"),
("CS125", "C programming"),
("BA332", "Corprate Finance");

INSERT INTO review (tutorID, rating, review) VALUES 
(1, 4, "MRB test 4str Reveiw"),
(2, 5, "KRP test 5str Review"),
(1, 5, "MRP test 5str Review");

INSERT INTO tutored (tutorID, courseCode) VALUES 
(1, "CEC320"),
(1, "CS125"),
(2, "CEC320"),
(3, "EC211"),
(3, "BA332");

INSERT INTO appointments (startTIme, endTime, offeredBy, takenBy, course) VALUES
  ('2019-12-01 09:00:00', '2019-12-01 13:00:00', 1, NULL, NULL),
  ('2019-12-01 13:00:00', '2019-12-01 14:00:00', 1, 4, "CS125"),
  ('2019-12-01 14:00:00', '2019-12-01 18:00:00', 1, NULL, NULL),
  ('2019-12-01 09:00:00', '2019-12-01 18:00:00', 3, NULL, NULL),
  ('2019-12-01 10:00:00', '2019-12-01 11:00:00', 2, 5, "BA332");