# Attendee Navigation Flow Guide

## Login Flow
When a user logs in as an attendee (RoleTypeId = 4), they are automatically redirected to the **User Home Page** (`AttendeeEvents.fxml`) instead of the Dashboard.

### Demo Credentials
- **Admin Demo**: `demo@eventra.com` / `demo123` → Redirects to Dashboard
- **Attendee Demo**: `attendee@eventra.com` / `attendee123` → Redirects to AttendeeEvents

## Navigation Between Pages

### 1. User Home Page (AttendeeEvents.fxml)
- **Location**: Main attendee landing page after login
- **Features**: 
  - Featured organizers (Admin users)
  - Coming soon events
  - All events with search and date filtering
  - Event cards that can be clicked to view details

### 2. Event Details Page (EventDetails.fxml)
- **Access**: Click on any event card from User Home Page
- **Features**:
  - Event description and details
  - Speaker spotlight (presenter info)
  - Session schedule
  - Registration button
  - Organizer information

### 3. My Schedule Page (MySchedule.fxml)
- **Access**: Click "My Schedule" in header navigation
- **Features**:
  - Shows events the user has registered for
  - Split into upcoming and past events
  - Search functionality
  - Empty state when no events are registered

### 4. Profile Page (AttendeeProfile.fxml)
- **Access**: Click "Profile" in header navigation
- **Features**:
  - Display user information
  - Editable username and email
  - Save/cancel functionality
  - Account statistics

## Header Navigation
All attendee pages have a consistent header with:
- Logo with hover effects
- Navigation buttons (Browse Events, My Schedule, Profile)
- Search functionality
- Active page highlighting

## Session Management
- User information is stored in `SessionManager` and persists across all pages
- No need to refetch user data on each page
- Selected events are stored temporarily for navigation to details page

## Database Integration
- All pages fetch data from the database using DAO classes
- Sample data is provided in `init-db.sql` for testing
- Real authentication works when database is available

## Testing the Flow
1. Start the application
2. Login with `attendee@eventra.com` / `attendee123`
3. You'll be redirected to the User Home Page
4. Navigate between pages using the header buttons
5. Click on events to view details and register
6. Check your schedule and profile

The navigation is fully integrated and ready to use! 