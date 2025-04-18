﻿# Library information system.

## Development plan ##
1.Database<br>
&emsp;&emsp;1.1.Chen Model creation, relational diagram design<br>
&emsp;&emsp;1.2.Database creation<br>
2. GUI<br>
&emsp;&emsp;2.1. Graphic interface design for each role (Reader, Operator/Administrator)<br>
&emsp;&emsp;2.2. Implementation of JavaFX interfaces<br>
3. Business Logic implementation<br>
&emsp;&emsp;3.1. ORM<br>
&emsp;&emsp;3.2. Business logic implementation<br>
&emsp;&emsp;3.3. Connection creation between business logic and GUI<br>
4. Event logger implementation<br>
5. Tests<br>



## Description of the project ##

<p>The idea of the Library Information System project is to design and implement a JavaFX-based application that effectively manages library operations. This system automates book processes and operations like book lending, addition of new books to the database, archiving old ones and writing off damaged ones, user operations like creation of new users and removal if necessary, notifications for various events, and reporting while supporting user roles including administrators, operators, and readers to access the functionalities defined in the system. The system also allows new readers to register to the system via register form which is checked later by an operator or an administrator who are notified upon form creation and then approve the form or deny access to the library system. To improve efficiency, Library Information System project combines a user-friendly GUI with robust backend logic.</p>


## Goal and objectives of the development ##
<p>The goal is to develop a comprehensive Library Information System using JavaFX.</p>

**Objectives:**
<ul>
<li>Design and create a database.</li>
<li>Design and implement different graphic interfaces for each user role using JavaFX.</li>
<li>Implement Hibernate ORM.</li>
<li>Provide role-based access to different user accounts.</li>
<li>Enable operators to add, lend, return, archive and write off books, create and delete readers.</li>
<li>Grant administrator all the functionalities of the operators as well as the ability to create and delete operators.</li>
<li>Automate book lending, notifications, and report generation processes.</li>
<li>Implement event logger to record events related to the system.</li>
<li>Develop test cases to verify the correctness and reliability of the application’s functionality, ensuring that it performs as intended.</li>
</ul>

**Technologies used:**
<li>Java and JavaFX: Backend logic and GUI development.</li>
<li>Maven: Dependency management.</li>
<li>Oracle Database: Reliable and scalable RDBMS.</li>
<li>Hibernate (ORM): Simplifies database interactions.</li>
<li>Log4J: Event logging.</li>
<li>JUnit: Unit testing framework.</li>
<li>TestFX: a framework for use in automating JavaFX GUI tests</li>
<li>Functional and Integration Tests using JUnit and TestFX: Ensure system coherence and reliability</li>

## Project assignment ##
Implement Library information system. The application stores and processes data regarding books and readers. System allows multiple access.
The system supports two types of users: administrator and users (operators and readers) with different roles to access the functionalities defined in the system.

**User operations:**
- Operator creation by administrator.
- Reader’s subscription/unsubscribing by operator.
- Form for creating a reader profile;
- Book lending.

**The system supports following book operations:**
- New book addition (inventory number, title, author, genre, etc).
- Book lending with different level of security on book borrowing (for reading room, for takeout).
- Book return.
- Write off damaged books.
- Old editions archive (for read room usage only).

**The system offers reports for:**
- Forms created (date, status, form content).
- Books (book status, book information).
- Users (approval date, book list, user information).
- User rating (loyal and non-loyal readers).

**The system supports notifications for:**
- Reader profile opening request.
- Notification for the need to archive a book;
- Late return of books

