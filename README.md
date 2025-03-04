Expify is a personal project designed for managing vehicle documents and payments. The application allows users to add and track vehicle-related information, including VIN, license plate number, RCA, insurance, leasing details, and notes. Additionally, it can send email notifications for document expirations, reminders, or upcoming leasing due dates.

Technologies Used:

Java – Main programming language.

SQLite3 – Local database for storing vehicle data.

Swing – Used for building the graphical user interface.

SendGrid API – Used for sending email notifications.

JUnit – Testing framework.

dotenv – Used for managing environment variables.

Installation:
1. Clone the Repository
git clone https://github.com/Mihai-04/Expify.git
cd Expify

2. Database Setup
SQLite3 database is included in the repository and does not require additional setup.

3. SendGrid Configuration
Create a config.txt file and add your SendGrid API key.

4. Required Libraries
To run the project, you need the following libraries:

SQLite JDBC Driver – Required for database connection.

SendGrid Java API – For sending emails.

dotenv Java – For handling environment variables.

If using Maven, dependencies are managed automatically via pom.xml.
If running manually, make sure you have the necessary JAR files added to the classpath.

Usage:

Add a Vehicle – Enter VIN, license plate number, RCA, insurance details, and optionally, leasing information.

View Vehicle List – See all added vehicles along with the number of days remaining until the nearest document expiration.

View More Details – Access the "View More" section to check all documents, expiration dates, leasing details, notes, and due dates.

Remove a Vehicle – Deleting a vehicle from the list will automatically remove it from the database.

Emails – The system sends email notifications in the following cases:
When a document (RCA or insurance) is less than 30 days to expire.
When a reminder date is set and has less than 7 days left.
When a leasing due date is approaching.
