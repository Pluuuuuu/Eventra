///****** Object:  Database [EventPlannerDB]    Script Date: 8/2/2025 1:45:58 AM ******/
//CREATE DATABASE [EventPlannerDB]  (EDITION = 'GeneralPurpose', SERVICE_OBJECTIVE = 'GP_S_Gen5_2', MAXSIZE = 32 GB) WITH CATALOG_COLLATION = SQL_Latin1_General_CP1_CI_AS, LEDGER = OFF;
//GO
//ALTER DATABASE [EventPlannerDB] SET COMPATIBILITY_LEVEL = 170
//GO
//ALTER DATABASE [EventPlannerDB] SET ANSI_NULL_DEFAULT OFF
//GO
//ALTER DATABASE [EventPlannerDB] SET ANSI_NULLS OFF
//GO
//ALTER DATABASE [EventPlannerDB] SET ANSI_PADDING OFF
//GO
//ALTER DATABASE [EventPlannerDB] SET ANSI_WARNINGS OFF
//GO
//ALTER DATABASE [EventPlannerDB] SET ARITHABORT OFF
//GO
//ALTER DATABASE [EventPlannerDB] SET AUTO_SHRINK OFF
//GO
//ALTER DATABASE [EventPlannerDB] SET AUTO_UPDATE_STATISTICS ON
//GO
//ALTER DATABASE [EventPlannerDB] SET CURSOR_CLOSE_ON_COMMIT OFF
//GO
//ALTER DATABASE [EventPlannerDB] SET CONCAT_NULL_YIELDS_NULL OFF
//GO
//ALTER DATABASE [EventPlannerDB] SET NUMERIC_ROUNDABORT OFF
//GO
//ALTER DATABASE [EventPlannerDB] SET QUOTED_IDENTIFIER OFF
//GO
//ALTER DATABASE [EventPlannerDB] SET RECURSIVE_TRIGGERS OFF
//GO
//ALTER DATABASE [EventPlannerDB] SET AUTO_UPDATE_STATISTICS_ASYNC OFF
//GO
//ALTER DATABASE [EventPlannerDB] SET ALLOW_SNAPSHOT_ISOLATION ON
//GO
//ALTER DATABASE [EventPlannerDB] SET PARAMETERIZATION SIMPLE
//GO
//ALTER DATABASE [EventPlannerDB] SET READ_COMMITTED_SNAPSHOT ON
//GO
//ALTER DATABASE [EventPlannerDB] SET  MULTI_USER
//GO
//ALTER DATABASE [EventPlannerDB] SET ENCRYPTION ON
//GO
//ALTER DATABASE [EventPlannerDB] SET QUERY_STORE = ON
//GO
//ALTER DATABASE [EventPlannerDB] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 100, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
//GO
///*** The scripts of database scoped configurations in Azure should be executed inside the target database connection. ***/
//GO
//-- ALTER DATABASE SCOPED CONFIGURATION SET MAXDOP = 8;
//GO
///****** Object:  User [planner]    Script Date: 8/2/2025 1:45:58 AM ******/
//CREATE USER [planner] FOR LOGIN [planner] WITH DEFAULT_SCHEMA=[dbo]
//GO
//sys.sp_addrolemember @rolename = N'db_owner', @membername = N'planner'
//GO
///****** Object:  Table [dbo].[UserM]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[UserM](
//	[UserID] [int] IDENTITY(1,1) NOT NULL,
//	[Username] [nvarchar](100) NOT NULL,
//	[FirstName] [nvarchar](100) NOT NULL,
//	[MiddleName] [nvarchar](100) NULL,
//	[LastName] [nvarchar](100) NOT NULL,
//	[Email] [nvarchar](255) NOT NULL,
//	[PasswordHash] [nvarchar](255) NOT NULL,
//	[ProfilePicUrl] [nvarchar](500) NULL,
//	[RoleTypeID] [tinyint] NOT NULL,
//	[StatusTypeID] [tinyint] NOT NULL,
//	[PeriodCanLoginInMinutes] [tinyint] NOT NULL,
//	[LastFailedLoginAt] [datetime2](7) NULL,
//	[CreatedAt] [datetime2](7) NOT NULL,
//	[UpdatedAt] [datetime2](7) NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[UserID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
//UNIQUE NONCLUSTERED
//(
//	[Username] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
//UNIQUE NONCLUSTERED
//(
//	[Email] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY]
//GO
///****** Object:  View [dbo].[view UserM]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE VIEW [dbo].[view UserM]
//AS
//SELECT        UserID, Username, FirstName, MiddleName, LastName, Email, PasswordHash, ProfilePicUrl, RoleTypeID, StatusTypeID, PeriodCanLoginInMinutes, LastFailedLoginAt, CreatedAt, UpdatedAt
//FROM            dbo.UserM
//GO
///****** Object:  Table [dbo].[Attendee]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[Attendee](
//	[AttendeeID] [int] IDENTITY(1,1) NOT NULL,
//	[FirstName] [nvarchar](100) NOT NULL,
//	[MiddleName] [nvarchar](100) NULL,
//	[LastName] [nvarchar](100) NOT NULL,
//	[Email] [nvarchar](255) NOT NULL,
//	[Organization] [nvarchar](255) NULL,
//	[Phone] [nvarchar](50) NULL,
//	[Location] [nvarchar](255) NULL,
//	[Gender] [nvarchar](20) NULL,
//	[DateOfBirth] [date] NULL,
//	[ProfilePicUrl] [nvarchar](500) NULL,
//	[Type] [nvarchar](50) NOT NULL,
//	[PasswordHash] [nvarchar](255) NOT NULL,
//	[StatusTypeID] [tinyint] NOT NULL,
//	[PeriodCanLoginInMinutes] [tinyint] NOT NULL,
//	[LastFailedLoginAt] [datetime2](7) NULL,
//	[CreatedAt] [datetime2](7) NOT NULL,
//	[UpdatedAt] [datetime2](7) NOT NULL,
//	[UserID] [int] NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[AttendeeID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
//UNIQUE NONCLUSTERED
//(
//	[Email] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY]
//GO
///****** Object:  Table [dbo].[EventM]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[EventM](
//	[EventID] [int] IDENTITY(1,1) NOT NULL,
//	[Title] [nvarchar](255) NOT NULL,
//	[Description] [nvarchar](max) NULL,
//	[StartDate] [datetime2](7) NOT NULL,
//	[EndDate] [datetime2](7) NOT NULL,
//	[VenueID] [int] NOT NULL,
//	[CreatedByUserID] [int] NOT NULL,
//	[StatusTypeID] [tinyint] NOT NULL,
//	[CreatedAt] [datetime2](7) NOT NULL,
//	[UpdatedAt] [datetime2](7) NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[EventID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
//GO
///****** Object:  Table [dbo].[EventStatusType]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[EventStatusType](
//	[EventStatusTypeID] [tinyint] NOT NULL,
//	[Name] [nvarchar](20) NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[EventStatusTypeID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
//UNIQUE NONCLUSTERED
//(
//	[Name] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY]
//GO
///****** Object:  Table [dbo].[Permission]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[Permission](
//	[PermissionID] [int] IDENTITY(1,1) NOT NULL,
//	[Name] [nvarchar](100) NOT NULL,
//	[Description] [nvarchar](255) NULL,
//PRIMARY KEY CLUSTERED
//(
//	[PermissionID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
//UNIQUE NONCLUSTERED
//(
//	[Name] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY]
//GO
///****** Object:  Table [dbo].[Presenter]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[Presenter](
//	[PresenterID] [int] IDENTITY(1,1) NOT NULL,
//	[FirstName] [nvarchar](100) NOT NULL,
//	[MiddleName] [nvarchar](100) NULL,
//	[LastName] [nvarchar](100) NOT NULL,
//	[Bio] [nvarchar](max) NULL,
//	[Email] [nvarchar](255) NULL,
//	[PhotoUrl] [nvarchar](500) NULL,
//	[ContactInfo] [nvarchar](255) NULL,
//	[CreatedByUserID] [int] NOT NULL,
//	[CreatedAt] [datetime2](7) NOT NULL,
//	[UpdatedAt] [datetime2](7) NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[PresenterID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
//UNIQUE NONCLUSTERED
//(
//	[Email] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
//GO
///****** Object:  Table [dbo].[Registration]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[Registration](
//	[RegistrationID] [int] IDENTITY(1,1) NOT NULL,
//	[AttendeeID] [int] NOT NULL,
//	[EventID] [int] NOT NULL,
//	[RegistrationStatusTypeID] [tinyint] NOT NULL,
//	[CreatedAt] [datetime2](7) NOT NULL,
//	[UpdatedAt] [datetime2](7) NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[RegistrationID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY]
//GO
///****** Object:  Table [dbo].[RegistrationStatusType]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[RegistrationStatusType](
//	[RegistrationStatusTypeID] [tinyint] NOT NULL,
//	[Name] [nvarchar](20) NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[RegistrationStatusTypeID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
//UNIQUE NONCLUSTERED
//(
//	[Name] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY]
//GO
///****** Object:  Table [dbo].[RolePermission]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[RolePermission](
//	[RoleTypeID] [tinyint] NOT NULL,
//	[PermissionID] [int] NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[RoleTypeID] ASC,
//	[PermissionID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY]
//GO
///****** Object:  Table [dbo].[RoleType]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[RoleType](
//	[RoleTypeID] [tinyint] NOT NULL,
//	[Name] [nvarchar](20) NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[RoleTypeID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
//UNIQUE NONCLUSTERED
//(
//	[Name] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY]
//GO
///****** Object:  Table [dbo].[Room]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[Room](
//	[RoomID] [int] IDENTITY(1,1) NOT NULL,
//	[VenueID] [int] NOT NULL,
//	[Name] [nvarchar](100) NOT NULL,
//	[Capacity] [int] NULL,
//	[CreatedAt] [datetime2](7) NOT NULL,
//	[UpdatedAt] [datetime2](7) NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[RoomID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY]
//GO
///****** Object:  Table [dbo].[SessionM]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[SessionM](
//	[SessionID] [int] IDENTITY(1,1) NOT NULL,
//	[EventID] [int] NOT NULL,
//	[CreatedByUserID] [int] NOT NULL,
//	[Track] [nvarchar](100) NOT NULL,
//	[Role] [nvarchar](50) NULL,
//	[Title] [nvarchar](255) NOT NULL,
//	[Abstract] [nvarchar](max) NULL,
//	[StartAt] [datetime2](7) NOT NULL,
//	[EndAt] [datetime2](7) NOT NULL,
//	[RoomID] [int] NULL,
//	[StatusTypeID] [tinyint] NOT NULL,
//	[CreatedAt] [datetime2](7) NOT NULL,
//	[UpdatedAt] [datetime2](7) NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[SessionID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
//GO
///****** Object:  Table [dbo].[SessionPresenter]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[SessionPresenter](
//	[SessionID] [int] NOT NULL,
//	[PresenterID] [int] NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[SessionID] ASC,
//	[PresenterID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY]
//GO
///****** Object:  Table [dbo].[SessionStatusType]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[SessionStatusType](
//	[SessionStatusTypeID] [tinyint] NOT NULL,
//	[Name] [nvarchar](20) NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[SessionStatusTypeID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
//UNIQUE NONCLUSTERED
//(
//	[Name] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY]
//GO
///****** Object:  Table [dbo].[StatusType]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[StatusType](
//	[StatusTypeID] [tinyint] NOT NULL,
//	[Name] [nvarchar](20) NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[StatusTypeID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
//UNIQUE NONCLUSTERED
//(
//	[Name] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY]
//GO
///****** Object:  Table [dbo].[Venue]    Script Date: 8/2/2025 1:45:58 AM ******/
//SET ANSI_NULLS ON
//GO
//SET QUOTED_IDENTIFIER ON
//GO
//CREATE TABLE [dbo].[Venue](
//	[VenueID] [int] IDENTITY(1,1) NOT NULL,
//	[Name] [nvarchar](255) NOT NULL,
//	[Address] [nvarchar](500) NULL,
//	[Capacity] [int] NULL,
//	[Rooms] [int] NULL,
//	[MapLink] [nvarchar](500) NULL,
//	[CreatedAt] [datetime2](7) NOT NULL,
//	[UpdatedAt] [datetime2](7) NOT NULL,
//PRIMARY KEY CLUSTERED
//(
//	[VenueID] ASC
//)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
//) ON [PRIMARY]
//GO
//ALTER TABLE [dbo].[Attendee] ADD  DEFAULT ((1)) FOR [StatusTypeID]
//GO
//ALTER TABLE [dbo].[Attendee] ADD  DEFAULT ((0)) FOR [PeriodCanLoginInMinutes]
//GO
//ALTER TABLE [dbo].[Attendee] ADD  DEFAULT (sysutcdatetime()) FOR [CreatedAt]
//GO
//ALTER TABLE [dbo].[Attendee] ADD  DEFAULT (sysutcdatetime()) FOR [UpdatedAt]
//GO
//ALTER TABLE [dbo].[Attendee] ADD  DEFAULT ((0)) FOR [UserID]
//GO
//ALTER TABLE [dbo].[EventM] ADD  DEFAULT ((1)) FOR [StatusTypeID]
//GO
//ALTER TABLE [dbo].[EventM] ADD  DEFAULT (sysutcdatetime()) FOR [CreatedAt]
//GO
//ALTER TABLE [dbo].[EventM] ADD  DEFAULT (sysutcdatetime()) FOR [UpdatedAt]
//GO
//ALTER TABLE [dbo].[Presenter] ADD  DEFAULT (sysutcdatetime()) FOR [CreatedAt]
//GO
//ALTER TABLE [dbo].[Presenter] ADD  DEFAULT (sysutcdatetime()) FOR [UpdatedAt]
//GO
//ALTER TABLE [dbo].[Registration] ADD  DEFAULT ((1)) FOR [RegistrationStatusTypeID]
//GO
//ALTER TABLE [dbo].[Registration] ADD  DEFAULT (sysutcdatetime()) FOR [CreatedAt]
//GO
//ALTER TABLE [dbo].[Registration] ADD  DEFAULT (sysutcdatetime()) FOR [UpdatedAt]
//GO
//ALTER TABLE [dbo].[Room] ADD  DEFAULT (sysutcdatetime()) FOR [CreatedAt]
//GO
//ALTER TABLE [dbo].[Room] ADD  DEFAULT (sysutcdatetime()) FOR [UpdatedAt]
//GO
//ALTER TABLE [dbo].[SessionM] ADD  DEFAULT ((1)) FOR [StatusTypeID]
//GO
//ALTER TABLE [dbo].[SessionM] ADD  DEFAULT (sysutcdatetime()) FOR [CreatedAt]
//GO
//ALTER TABLE [dbo].[SessionM] ADD  DEFAULT (sysutcdatetime()) FOR [UpdatedAt]
//GO
//ALTER TABLE [dbo].[UserM] ADD  DEFAULT ((2)) FOR [RoleTypeID]
//GO
//ALTER TABLE [dbo].[UserM] ADD  DEFAULT ((1)) FOR [StatusTypeID]
//GO
//ALTER TABLE [dbo].[UserM] ADD  DEFAULT ((0)) FOR [PeriodCanLoginInMinutes]
//GO
//ALTER TABLE [dbo].[UserM] ADD  DEFAULT (sysutcdatetime()) FOR [CreatedAt]
//GO
//ALTER TABLE [dbo].[UserM] ADD  DEFAULT (sysutcdatetime()) FOR [UpdatedAt]
//GO
//ALTER TABLE [dbo].[Venue] ADD  DEFAULT (sysutcdatetime()) FOR [CreatedAt]
//GO
//ALTER TABLE [dbo].[Venue] ADD  DEFAULT (sysutcdatetime()) FOR [UpdatedAt]
//GO
//ALTER TABLE [dbo].[Attendee]  WITH CHECK ADD  CONSTRAINT [FK_Attendee_StatusType] FOREIGN KEY([StatusTypeID])
//REFERENCES [dbo].[StatusType] ([StatusTypeID])
//ON UPDATE CASCADE
//GO
//ALTER TABLE [dbo].[Attendee] CHECK CONSTRAINT [FK_Attendee_StatusType]
//GO
//ALTER TABLE [dbo].[Attendee]  WITH CHECK ADD  CONSTRAINT [FK_Attendee_UserM] FOREIGN KEY([UserID])
//REFERENCES [dbo].[UserM] ([UserID])
//GO
//ALTER TABLE [dbo].[Attendee] CHECK CONSTRAINT [FK_Attendee_UserM]
//GO
//ALTER TABLE [dbo].[EventM]  WITH CHECK ADD  CONSTRAINT [FK_EventM_CreatedBy] FOREIGN KEY([CreatedByUserID])
//REFERENCES [dbo].[UserM] ([UserID])
//GO
//ALTER TABLE [dbo].[EventM] CHECK CONSTRAINT [FK_EventM_CreatedBy]
//GO
//ALTER TABLE [dbo].[EventM]  WITH CHECK ADD  CONSTRAINT [FK_EventM_StatusType] FOREIGN KEY([StatusTypeID])
//REFERENCES [dbo].[EventStatusType] ([EventStatusTypeID])
//ON UPDATE CASCADE
//GO
//ALTER TABLE [dbo].[EventM] CHECK CONSTRAINT [FK_EventM_StatusType]
//GO
//ALTER TABLE [dbo].[EventM]  WITH CHECK ADD  CONSTRAINT [FK_EventM_Venue] FOREIGN KEY([VenueID])
//REFERENCES [dbo].[Venue] ([VenueID])
//GO
//ALTER TABLE [dbo].[EventM] CHECK CONSTRAINT [FK_EventM_Venue]
//GO
//ALTER TABLE [dbo].[Presenter]  WITH CHECK ADD  CONSTRAINT [FK_Presenter_CreatedBy] FOREIGN KEY([CreatedByUserID])
//REFERENCES [dbo].[UserM] ([UserID])
//GO
//ALTER TABLE [dbo].[Presenter] CHECK CONSTRAINT [FK_Presenter_CreatedBy]
//GO
//ALTER TABLE [dbo].[Registration]  WITH CHECK ADD  CONSTRAINT [FK_Reg_Attendee] FOREIGN KEY([AttendeeID])
//REFERENCES [dbo].[Attendee] ([AttendeeID])
//ON DELETE CASCADE
//GO
//ALTER TABLE [dbo].[Registration] CHECK CONSTRAINT [FK_Reg_Attendee]
//GO
//ALTER TABLE [dbo].[Registration]  WITH CHECK ADD  CONSTRAINT [FK_Reg_Event] FOREIGN KEY([EventID])
//REFERENCES [dbo].[EventM] ([EventID])
//ON DELETE CASCADE
//GO
//ALTER TABLE [dbo].[Registration] CHECK CONSTRAINT [FK_Reg_Event]
//GO
//ALTER TABLE [dbo].[Registration]  WITH CHECK ADD  CONSTRAINT [FK_Reg_StatusType] FOREIGN KEY([RegistrationStatusTypeID])
//REFERENCES [dbo].[RegistrationStatusType] ([RegistrationStatusTypeID])
//ON UPDATE CASCADE
//GO
//ALTER TABLE [dbo].[Registration] CHECK CONSTRAINT [FK_Reg_StatusType]
//GO
//ALTER TABLE [dbo].[RolePermission]  WITH CHECK ADD  CONSTRAINT [FK_RP_Permission] FOREIGN KEY([PermissionID])
//REFERENCES [dbo].[Permission] ([PermissionID])
//ON UPDATE CASCADE
//ON DELETE CASCADE
//GO
//ALTER TABLE [dbo].[RolePermission] CHECK CONSTRAINT [FK_RP_Permission]
//GO
//ALTER TABLE [dbo].[RolePermission]  WITH CHECK ADD  CONSTRAINT [FK_RP_RoleType] FOREIGN KEY([RoleTypeID])
//REFERENCES [dbo].[RoleType] ([RoleTypeID])
//ON UPDATE CASCADE
//ON DELETE CASCADE
//GO
//ALTER TABLE [dbo].[RolePermission] CHECK CONSTRAINT [FK_RP_RoleType]
//GO
//ALTER TABLE [dbo].[Room]  WITH CHECK ADD  CONSTRAINT [FK_Room_Venue] FOREIGN KEY([VenueID])
//REFERENCES [dbo].[Venue] ([VenueID])
//ON DELETE CASCADE
//GO
//ALTER TABLE [dbo].[Room] CHECK CONSTRAINT [FK_Room_Venue]
//GO
//ALTER TABLE [dbo].[SessionM]  WITH CHECK ADD  CONSTRAINT [FK_SessionM_CreatedBy] FOREIGN KEY([CreatedByUserID])
//REFERENCES [dbo].[UserM] ([UserID])
//GO
//ALTER TABLE [dbo].[SessionM] CHECK CONSTRAINT [FK_SessionM_CreatedBy]
//GO
//ALTER TABLE [dbo].[SessionM]  WITH CHECK ADD  CONSTRAINT [FK_SessionM_Event] FOREIGN KEY([EventID])
//REFERENCES [dbo].[EventM] ([EventID])
//ON DELETE CASCADE
//GO
//ALTER TABLE [dbo].[SessionM] CHECK CONSTRAINT [FK_SessionM_Event]
//GO
//ALTER TABLE [dbo].[SessionM]  WITH CHECK ADD  CONSTRAINT [FK_SessionM_Room] FOREIGN KEY([RoomID])
//REFERENCES [dbo].[Room] ([RoomID])
//ON DELETE SET NULL
//GO
//ALTER TABLE [dbo].[SessionM] CHECK CONSTRAINT [FK_SessionM_Room]
//GO
//ALTER TABLE [dbo].[SessionM]  WITH CHECK ADD  CONSTRAINT [FK_SessionM_StatusType] FOREIGN KEY([StatusTypeID])
//REFERENCES [dbo].[SessionStatusType] ([SessionStatusTypeID])
//ON UPDATE CASCADE
//GO
//ALTER TABLE [dbo].[SessionM] CHECK CONSTRAINT [FK_SessionM_StatusType]
//GO
//ALTER TABLE [dbo].[SessionPresenter]  WITH CHECK ADD  CONSTRAINT [FK_SP_Presenter] FOREIGN KEY([PresenterID])
//REFERENCES [dbo].[Presenter] ([PresenterID])
//ON DELETE CASCADE
//GO
//ALTER TABLE [dbo].[SessionPresenter] CHECK CONSTRAINT [FK_SP_Presenter]
//GO
//ALTER TABLE [dbo].[SessionPresenter]  WITH CHECK ADD  CONSTRAINT [FK_SP_Session] FOREIGN KEY([SessionID])
//REFERENCES [dbo].[SessionM] ([SessionID])
//ON DELETE CASCADE
//GO
//ALTER TABLE [dbo].[SessionPresenter] CHECK CONSTRAINT [FK_SP_Session]
//GO
//ALTER TABLE [dbo].[UserM]  WITH CHECK ADD  CONSTRAINT [FK_UserM_RoleType] FOREIGN KEY([RoleTypeID])
//REFERENCES [dbo].[RoleType] ([RoleTypeID])
//ON UPDATE CASCADE
//GO
//ALTER TABLE [dbo].[UserM] CHECK CONSTRAINT [FK_UserM_RoleType]
//GO
//ALTER TABLE [dbo].[UserM]  WITH CHECK ADD  CONSTRAINT [FK_UserM_StatusType] FOREIGN KEY([StatusTypeID])
//REFERENCES [dbo].[StatusType] ([StatusTypeID])
//ON UPDATE CASCADE
//GO
//ALTER TABLE [dbo].[UserM] CHECK CONSTRAINT [FK_UserM_StatusType]
//GO
//ALTER TABLE [dbo].[EventM]  WITH CHECK ADD  CONSTRAINT [CK_EventM_Dates] CHECK  (([EndDate]>=[StartDate]))
//GO
//ALTER TABLE [dbo].[EventM] CHECK CONSTRAINT [CK_EventM_Dates]
//GO
//ALTER TABLE [dbo].[SessionM]  WITH CHECK ADD  CONSTRAINT [CK_SessionM_Times] CHECK  (([EndAt]>[StartAt]))
//GO
//ALTER TABLE [dbo].[SessionM] CHECK CONSTRAINT [CK_SessionM_Times]
//GO
//ALTER DATABASE [EventPlannerDB] SET  READ_WRITE
//GO
