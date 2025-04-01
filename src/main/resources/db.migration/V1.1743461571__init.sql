-- Table: Users
CREATE TABLE Users (
                       UserID INT PRIMARY KEY AUTO_INCREMENT,
                       Username VARCHAR(255) UNIQUE NOT NULL,
                       Email VARCHAR(255) UNIQUE NOT NULL,
                       Password VARCHAR(255) NOT NULL,
                       FirstName VARCHAR(255) NULL,
                       LastName VARCHAR(255) NULL,
                       CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       CreatedBy INT, -- User who created the account
                       UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       UpdatedBy INT, -- User who last updated the account
                       DeletedAt TIMESTAMP NULL, -- Soft delete timestamp
                       DeletedBy INT NULL, -- User who deleted the account
                       FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
                       FOREIGN KEY (UpdatedBy) REFERENCES Users(UserID),
                       FOREIGN KEY (DeletedBy) REFERENCES Users(UserID)
);

-- Table: Locations
CREATE TABLE Locations (
                           LocationID INT PRIMARY KEY AUTO_INCREMENT,
                           Latitude DECIMAL(9, 6) NOT NULL,
                           Longitude DECIMAL(9, 6) NOT NULL,
                           Address VARCHAR(255) NULL,
                           PlaceName VARCHAR(255) NULL,
                           CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           CreatedBy INT,
                           UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           UpdatedBy INT,
                           DeletedAt TIMESTAMP NULL,
                           DeletedBy INT NULL,
                           FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
                           FOREIGN KEY (UpdatedBy) REFERENCES Users(UserID),
                           FOREIGN KEY (DeletedBy) REFERENCES Users(UserID)
);

-- Table: Images
CREATE TABLE Images (
                        ImageID INT PRIMARY KEY AUTO_INCREMENT,
                        FilePath VARCHAR(255) NOT NULL,
                        UploadedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        CreatedBy INT,
                        UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        UpdatedBy INT,
                        DeletedAt TIMESTAMP NULL,
                        DeletedBy INT NULL,
                        FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
                        FOREIGN KEY (UpdatedBy) REFERENCES Users(UserID),
                        FOREIGN KEY (DeletedBy) REFERENCES Users(UserID)
);

-- Table: Events
CREATE TABLE Events (
                        EventID INT PRIMARY KEY AUTO_INCREMENT,
                        Title VARCHAR(255) NOT NULL,
                        Description TEXT NULL,
                        LocationID INT,
                        StartTime DATETIME NOT NULL,
                        EndTime DATETIME NOT NULL,
                        Capacity INT NULL,
                        ImageID INT NULL,
                        CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        CreatedBy INT,
                        UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        UpdatedBy INT,
                        DeletedAt TIMESTAMP NULL,
                        DeletedBy INT NULL,
                        FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
                        FOREIGN KEY (UpdatedBy) REFERENCES Users(UserID),
                        FOREIGN KEY (DeletedBy) REFERENCES Users(UserID),
                        FOREIGN KEY (LocationID) REFERENCES Locations(LocationID),
                        FOREIGN KEY (ImageID) REFERENCES Images(ImageID)
);

-- Table: Event_Categories (for enum values)
CREATE TABLE Event_Categories (
    EventID INT,
    Category VARCHAR(50) NOT NULL,
    PRIMARY KEY (EventID, Category),
    FOREIGN KEY (EventID) REFERENCES Events(EventID),
    CONSTRAINT chk_category CHECK (Category IN ('SPORTS', 'MUSIC', 'ARTS', 'EDUCATION', 'BUSINESS', 'TECHNOLOGY', 'FOOD', 'TRAVEL', 'HEALTH', 'OTHER'))
);

-- Table: Registrations
CREATE TABLE Registrations (
                               RegistrationID INT PRIMARY KEY AUTO_INCREMENT,
                               EventID INT,
                               UserID INT,
                               RegistrationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               CreatedBy INT,
                               UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               UpdatedBy INT,
                               DeletedAt TIMESTAMP NULL,
                               DeletedBy INT NULL,
                               FOREIGN KEY (EventID) REFERENCES Events(EventID),
                               FOREIGN KEY (UserID) REFERENCES Users(UserID),
                               FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
                               FOREIGN KEY (UpdatedBy) REFERENCES Users(UserID),
                               FOREIGN KEY (DeletedBy) REFERENCES Users(UserID),
                               UNIQUE KEY `event_user` (`EventID`, `UserID`)
);

-- Table: Categories
CREATE TABLE Categories (
                            CategoryID INT PRIMARY KEY AUTO_INCREMENT,
                            CategoryName VARCHAR(255) UNIQUE NOT NULL,
                            CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            CreatedBy INT,
                            UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            UpdatedBy INT,
                            DeletedAt TIMESTAMP NULL,
                            DeletedBy INT NULL,
                            FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
                            FOREIGN KEY (UpdatedBy) REFERENCES Users(UserID),
                            FOREIGN KEY (DeletedBy) REFERENCES Users(UserID)
);
