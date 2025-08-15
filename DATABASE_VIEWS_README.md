# Eventra Database Views

This document describes the comprehensive set of database views created for the Eventra event management system. These views provide easy access to commonly needed data combinations and simplify complex queries.

## Quick Start

To create all views, run the `database_views.sql` file in your Azure SQL database:

```sql
-- Execute the entire file
-- Make sure you're connected to EventPlannerDB
```

## View Categories

### 1. User Management Views

#### `vw_UserComplete`
Complete user information with roles and status.
```sql
SELECT * FROM vw_UserComplete WHERE RoleName = 'Admin';
```

#### `vw_AttendeeComplete`
Attendee information with user details and status.
```sql
SELECT * FROM vw_AttendeeComplete WHERE StatusName = 'Active';
```

#### `vw_UserPermissions`
User permissions based on their role.
```sql
SELECT * FROM vw_UserPermissions WHERE Username = 'admin@eventra.com';
```

### 2. Event Management Views

#### `vw_EventComplete`
Complete event information with venue and creator details.
```sql
SELECT * FROM vw_EventComplete WHERE EventStatus = 'Published';
```

#### `vw_EventRegistrationStats`
Events with registration statistics.
```sql
SELECT * FROM vw_EventRegistrationStats 
WHERE TotalRegistrations > 0 
ORDER BY TotalRegistrations DESC;
```

#### `vw_UpcomingEvents`
Future events with days until event.
```sql
SELECT * FROM vw_UpcomingEvents 
WHERE DaysUntilEvent <= 30 
ORDER BY StartDate;
```

### 3. Session Management Views

#### `vw_SessionComplete`
Complete session information with event and room details.
```sql
SELECT * FROM vw_SessionComplete 
WHERE EventTitle = 'Tech Conference 2024';
```

#### `vw_SessionWithPresenters`
Sessions with presenter information and counts.
```sql
SELECT * FROM vw_SessionWithPresenters 
WHERE PresenterCount > 0;
```

#### `vw_PresenterComplete`
Presenter information with session counts.
```sql
SELECT * FROM vw_PresenterComplete 
WHERE SessionCount > 0 
ORDER BY SessionCount DESC;
```

### 4. Registration Views

#### `vw_RegistrationComplete`
Complete registration information with attendee and event details.
```sql
SELECT * FROM vw_RegistrationComplete 
WHERE RegistrationStatus = 'Confirmed';
```

#### `vw_AttendeeRegistrations`
All registrations for a specific attendee.
```sql
SELECT * FROM vw_AttendeeRegistrations 
WHERE AttendeeID = 123;
```

### 5. Venue and Room Views

#### `vw_VenueWithRooms`
Venue information with room counts and capacities.
```sql
SELECT * FROM vw_VenueWithRooms 
WHERE ActualRooms > 0;
```

#### `vw_RoomAvailability`
Room information with session counts.
```sql
SELECT * FROM vw_RoomAvailability 
WHERE ScheduledSessions = 0; -- Available rooms
```

### 6. Dashboard Views

#### `vw_DashboardStats`
Key statistics for dashboard display.
```sql
SELECT * FROM vw_DashboardStats;
```

#### `vw_RecentActivity`
Recent system activity (last 30 days).
```sql
SELECT TOP 20 * FROM vw_RecentActivity;
```

### 7. Reporting Views

#### `vw_EventAttendanceReport`
Event attendance statistics and percentages.
```sql
SELECT * FROM vw_EventAttendanceReport 
WHERE AttendancePercentage > 80;
```

#### `vw_PresenterSessionReport`
Presenter activity and session statistics.
```sql
SELECT * FROM vw_PresenterSessionReport 
WHERE TotalSessions > 0;
```

### 8. Search and Filter Views

#### `vw_EventSearch`
Events with search-friendly information.
```sql
SELECT * FROM vw_EventSearch 
WHERE Title LIKE '%conference%' 
   OR Description LIKE '%technology%';
```

#### `vw_AttendeeSearch`
Attendees with registration counts.
```sql
SELECT * FROM vw_AttendeeSearch 
WHERE FirstName LIKE '%John%' 
   OR Organization LIKE '%Tech%';
```

## Common Use Cases

### 1. Event Dashboard
```sql
-- Get upcoming events with registration stats
SELECT 
    e.EventID,
    e.Title,
    e.StartDate,
    e.DaysUntilEvent,
    ers.TotalRegistrations,
    ers.ConfirmedRegistrations
FROM vw_UpcomingEvents e
INNER JOIN vw_EventRegistrationStats ers ON e.EventID = ers.EventID
WHERE e.DaysUntilEvent <= 30
ORDER BY e.StartDate;
```

### 2. Attendee Management
```sql
-- Get all attendees with their event registrations
SELECT 
    ac.FirstName,
    ac.LastName,
    ac.Email,
    ac.Organization,
    ar.EventTitle,
    ar.RegistrationStatus
FROM vw_AttendeeComplete ac
LEFT JOIN vw_AttendeeRegistrations ar ON ac.AttendeeID = ar.AttendeeID
WHERE ac.StatusName = 'Active'
ORDER BY ac.LastName, ar.EventTitle;
```

### 3. Session Planning
```sql
-- Get sessions with presenters for an event
SELECT 
    swp.SessionTitle,
    swp.Track,
    swp.StartAt,
    swp.EndAt,
    swp.RoomName,
    swp.Presenters
FROM vw_SessionWithPresenters swp
WHERE swp.EventID = 1
ORDER BY swp.StartAt;
```

### 4. Venue Management
```sql
-- Check venue capacity utilization
SELECT 
    vwr.VenueName,
    vwr.VenueCapacity,
    vwr.ActualRooms,
    vwr.TotalRoomCapacity
FROM vw_VenueWithRooms vwr
WHERE vwr.ActualRooms > 0;
```

### 5. User Permissions Check
```sql
-- Check what permissions a user has
SELECT 
    up.PermissionName,
    up.PermissionDescription
FROM vw_UserPermissions up
WHERE up.Username = 'admin@eventra.com';
```

## Performance Considerations

1. **Indexing**: These views rely on proper indexing of the underlying tables. Ensure you have indexes on:
   - Foreign key columns
   - Frequently searched columns (Email, Username, Title)
   - Date columns used in filtering

2. **Materialized Views**: For frequently accessed views with complex aggregations, consider creating indexed views:
   ```sql
   CREATE UNIQUE CLUSTERED INDEX IX_vw_EventRegistrationStats 
   ON vw_EventRegistrationStats (EventID);
   ```

3. **Filtering**: Always add WHERE clauses to limit the data returned, especially for large datasets.

## Maintenance

### Adding New Views
When adding new views, follow the naming convention:
- `vw_[Category][Purpose]` (e.g., `vw_EventComplete`)
- Use descriptive names that indicate the view's purpose
- Include comments explaining the view's purpose

### Updating Views
Use `CREATE OR ALTER VIEW` to update existing views:
```sql
CREATE OR ALTER VIEW vw_ExampleView AS
SELECT ...
```

### View Dependencies
Some views depend on others. When modifying views, check for dependencies:
```sql
SELECT 
    OBJECT_NAME(referencing_id) AS ViewName,
    OBJECT_NAME(referenced_id) AS ReferencedObject
FROM sys.sql_expression_dependencies
WHERE OBJECT_NAME(referencing_id) LIKE 'vw_%';
```

## Troubleshooting

### Common Issues

1. **Permission Errors**: Ensure the database user has SELECT permissions on all underlying tables.

2. **Performance Issues**: 
   - Check execution plans for complex views
   - Consider adding indexes on frequently filtered columns
   - Use `SET STATISTICS IO ON` to monitor query performance

3. **Data Consistency**: Views reflect the current state of underlying tables. Ensure data integrity constraints are properly set.

### Debugging Views
To debug a view, you can:
```sql
-- Check view definition
SELECT OBJECT_DEFINITION(OBJECT_ID('vw_EventComplete'));

-- Test view with sample data
SELECT TOP 10 * FROM vw_EventComplete;
```

## Best Practices

1. **Use Views for Common Queries**: Instead of writing complex JOINs repeatedly, use these views.

2. **Document Changes**: When modifying views, update this documentation.

3. **Test Thoroughly**: Always test view modifications with sample data.

4. **Monitor Performance**: Regularly check view performance, especially for frequently used views.

5. **Security**: Consider row-level security for sensitive data in views.

## Support

For questions about these views or to request additional views, refer to the database schema documentation and the ERD diagram in `Eventra_ERD.md`.
