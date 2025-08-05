# Profile Features Implementation - editb Branch

## Overview
This branch implements comprehensive user profile functionality for the Eventra application, including profile picture management, user information display, and profile editing capabilities.

## New Features

### 1. Dashboard Profile Picture
- **Location**: Top-right corner of the Dashboard
- **Functionality**: 
  - Displays user's profile picture (if set) or default logo placeholder
  - Clickable to navigate to User Info page
  - Shows logo on the left side of the top bar

### 2. User Info Page
- **Navigation**: Click on profile picture in Dashboard
- **Features**:
  - Large profile picture display (120x120px)
  - User information display (Username, Email, Full Name)
  - Clickable profile picture to change/upload new image
  - Edit Profile button
  - Logout button
  - Back to Dashboard button

### 3. Profile Picture Management
- **Upload**: Click on profile picture to open file chooser
- **Supported Formats**: PNG, JPG, JPEG, GIF, BMP
- **Storage**: File path stored in database (ProfilePicUrl field)
- **Fallback**: Default logo image if no profile picture is set

### 4. Edit Profile Page
- **Navigation**: Click "Edit Profile" button in User Info page
- **Editable Fields**:
  - Username
  - First Name
  - Middle Name
  - Last Name
  - Email
- **Validation**:
  - Required field validation
  - Username uniqueness check
  - Email uniqueness check
- **Actions**:
  - Save Changes (updates database and session)
  - Cancel (returns to User Info without saving)
  - Back to Profile (returns to User Info)

## Technical Implementation

### New Files Created:
1. `src/main/resources/fxml/UserInfo.fxml` - User info page layout
2. `src/main/java/com/eventra/controller/UserInfoController.java` - User info page controller
3. `src/main/resources/fxml/EditProfile.fxml` - Edit profile page layout
4. `src/main/java/com/eventra/controller/EditProfileController.java` - Edit profile controller

### Modified Files:
1. `src/main/resources/fxml/Dashboard.fxml` - Added top bar with logo and profile picture
2. `src/main/java/com/eventra/controller/DashboardController.java` - Added profile picture functionality
3. `src/main/resources/css/styles.css` - Added styles for new components

### Database Integration:
- Uses existing `ProfilePicUrl` field in `UserM` table
- Profile picture paths stored as absolute file paths
- User information updates handled through existing `UserDAO.updateUser()` method

## User Experience Flow

1. **Login** → User logs into the application
2. **Dashboard** → User sees logo on left, profile picture on right
3. **Profile Picture Click** → Navigates to User Info page
4. **User Info Page** → View profile information and options
5. **Edit Profile** → Click to modify user information
6. **Save Changes** → Updates database and returns to User Info
7. **Logout** → Available from User Info page

## Styling Features

### Visual Design:
- Gradient backgrounds for pages
- Rounded profile pictures with borders
- Hover effects and animations
- Consistent button styling
- Modern card-based layouts

### Responsive Elements:
- Profile pictures scale on hover
- Smooth transitions
- Drop shadows for depth
- Consistent spacing and typography

## Security Considerations

- File upload validation (image formats only)
- User authentication required for all profile operations
- Session management for user state
- Input validation and sanitization
- Database transaction safety

## Future Enhancements

Potential improvements for future iterations:
- Image compression and resizing
- Cloud storage for profile pictures
- Profile picture cropping functionality
- Additional profile fields (bio, location, etc.)
- Profile picture URL validation
- Batch profile picture processing

## Testing

To test the new features:
1. Run the application: `mvn javafx:run`
2. Login with any user account
3. Click on the profile picture in the top-right corner
4. Test profile picture upload functionality
5. Test profile information editing
6. Verify logout functionality

## Branch Information

- **Branch Name**: editb
- **Created From**: Main branch
- **Purpose**: Profile management features implementation
- **Status**: Complete and tested 