# Assignment 1 - Project Plan and Requirements

**Team:** [Your Team Number/Names]
**Date:** January 20, 2026
**Project:** RepX Mobile - Fitness Tracking Mobile Application

---

## 1 Project Vision

### 1.5 RepX Mobile - Your Workout Companion

**Note:** This document extends the HBV501G vision to focus on mobile-specific aspects.

RepX Mobile is a native mobile fitness tracking application that empowers users to log workouts, track body metrics, and analyze their fitness progress directly from their smartphones. Building on the robust RepX REST API backend developed in HBV501G, this mobile application brings workout logging to users' pockets, enabling real-time tracking at the gym, offline capability, and mobile-native features like camera integration, push notifications, and location services.

**Target Users:**
- Gym-goers who want to log workouts in real-time
- Fitness enthusiasts tracking body composition changes
- Athletes monitoring their personal records and progress
- Users needing offline workout logging when gym WiFi is unavailable

**Key Value Propositions:**
- **Mobile-First Experience:** Quick, intuitive workout logging optimized for one-handed use
- **Offline Support:** Log workouts without internet connection, sync when online
- **Camera Integration:** Take progress photos alongside body metrics
- **Real-time Notifications:** Get reminders for workout days and celebrate PRs
- **Location Awareness:** Tag workouts by gym location
- **Mobile Analytics:** Visual charts and progress tracking optimized for mobile viewing

### 1.6 Mobile Platform Strategy

**Primary Platform:** Android (Kotlin/Jetpack Compose)
**Reasoning:**
- Team expertise in Kotlin from HBV501G backend development
- Android Studio seamless integration with existing Kotlin codebase
- Jetpack Compose for modern, declarative UI development
- Larger market share for fitness apps in target demographic

**Future Considerations:**
- iOS version using Swift/SwiftUI (future sprint)
- Kotlin Multiplatform Mobile (KMM) for code sharing

### 2.2 Mobile Architecture

**Architecture Pattern:** MVVM (Model-View-ViewModel)

**Key Components:**
- **Presentation Layer:** Jetpack Compose UI components
- **ViewModel Layer:** Business logic and state management
- **Repository Layer:** Data source abstraction (API + Local DB)
- **Network Layer:** Retrofit for REST API communication
- **Local Storage:** Room database for offline support
- **Image Storage:** Local file system + cloud sync

**Technology Stack:**
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM + Clean Architecture
- **DI:** Hilt (Dagger)
- **Networking:** Retrofit + OkHttp
- **Local DB:** Room
- **Image Loading:** Coil
- **Navigation:** Compose Navigation
- **State Management:** StateFlow/ViewModel
- **Testing:** JUnit, Espresso, Compose UI Testing

### 2.3 Mobile-Specific Features

**Camera Integration:**
- Take progress photos directly in-app
- Attach photos to body metric records
- Before/after photo comparisons
- Camera permission handling

**Location Services:**
- Tag workouts with gym location
- Workout location history
- Find nearby gyms
- Location permission handling

**Push Notifications:**
- Workout reminders
- Rest day notifications
- PR achievement celebrations
- Weekly progress summaries

**Offline Functionality:**
- Local workout logging when offline
- Background sync when connection restored
- Conflict resolution for concurrent edits
- Offline indicator UI

**Mobile UI/UX:**
- Bottom navigation for main features
- Swipe gestures for quick actions
- Pull-to-refresh for data updates
- Floating action button for quick workout start
- One-handed operation optimized
- Dark mode support

### 2.4 Integration with Existing Backend

The mobile app will consume the RepX REST API developed in HBV501G:

**Existing Endpoints Used:**
- `POST /users` - User registration
- `POST /workouts` - Create workout
- `GET /workouts` - List workouts
- `GET /workouts/users/{userId}/history` - Workout history
- `GET /workouts/users/{userId}/prs` - Personal records
- `POST /body-metrics` - Log body metrics
- `GET /body-metrics` - View metrics history
- `GET /favorites` - User favorites
- `POST /favorites` - Add to favorites

**New Backend Requirements:**
- User authentication endpoint (login/JWT tokens)
- Image upload endpoint for progress photos
- Push notification registration endpoint

---

## 2 Product Backlog

**Priority System:** Lower number = higher priority (P1 > P2 > P3)

### High Priority (P1) - Core Mobile Functionality

**[US1; P1] User Authentication**
As a user, I want to log in to my account from the mobile app so that I can access my workout data securely.

**Acceptance Criteria:**
- Login screen with email/password fields
- JWT token storage in secure storage
- Session persistence across app restarts
- Logout functionality
- Error handling for invalid credentials

---

**[US2; P1] Create Workout (Mobile)**
As a gym-goer, I want to quickly start a new workout from my phone so that I can begin logging exercises immediately.

**Acceptance Criteria:**
- Floating action button for quick workout start
- Workout title input
- Exercise selection from favorites or search
- Set logging (reps, weight, RIR)
- Timer for rest periods
- Save workout to local DB and sync to API

**Mobile-Specific:**
- One-handed operation optimized
- Quick-add buttons for common rep ranges (8, 10, 12)
- Swipe to delete sets

---

**[US3; P1] Offline Workout Logging**
As a user working out in a gym with poor WiFi, I want to log my workout offline so that I don't lose data due to connectivity issues.

**Acceptance Criteria:**
- Workouts saved to local Room database
- Background sync when internet restored
- Visual indicator showing offline status
- Conflict resolution if data changed on server
- Queue of pending syncs visible to user

**Mobile-Specific:**
- Offline mode detection using ConnectivityManager
- Background WorkManager for sync
- Local database as single source of truth

---

**[US4; P1] View Workout History**
As a user, I want to view my past workouts on my phone so that I can see what exercises I did and track my consistency.

**Acceptance Criteria:**
- Scrollable list of past workouts
- Filter by date range
- Workout detail view showing exercises and sets
- Pull-to-refresh to update list
- Pagination for large workout history

---

**[US5; P1] Log Body Metrics**
As a fitness enthusiast, I want to log my weight and measurements from my phone so that I can track my body composition changes.

**Acceptance Criteria:**
- Form for entering weight, body fat %, chest, waist, hips
- Date/time picker
- Save to API
- Validation for reasonable values
- View history of past metrics

---

### Medium Priority (P2) - Enhanced Mobile Features

**[US6; P2] Take Progress Photos**
As a user, I want to take progress photos with my phone's camera so that I can visually track my fitness transformation.

**Acceptance Criteria:**
- Camera permission request
- Open device camera from app
- Capture and preview photo
- Attach photo to body metric record
- Upload photo to server
- Gallery view of progress photos

**Mobile-Specific:**
- Uses Android Camera API or CameraX
- Handles camera permission denial gracefully
- Image compression before upload
- Front/back camera switching

---

**[US7; P2] View Personal Records**
As an athlete, I want to see my PRs (personal records) on my phone so that I can track my strength gains and stay motivated.

**Acceptance Criteria:**
- List of PRs by exercise
- Show heaviest weight, best reps, best volume
- Visual indicators for recent PRs
- Filter by exercise or muscle group

---

**[US8; P2] Workout Reminders (Push Notifications)**
As a user, I want to receive workout reminders on my phone so that I stay consistent with my training schedule.

**Acceptance Criteria:**
- Enable/disable notifications in settings
- Schedule workout reminders for specific days/times
- Push notification delivered at scheduled time
- Tap notification opens app to workout creation
- PR celebration notifications

**Mobile-Specific:**
- Uses Firebase Cloud Messaging (FCM)
- Background notification handling
- Notification permission request (Android 13+)
- Notification channels for different types

---

**[US9; P2] Tag Workout Location**
As a user, I want to tag my workout with the gym location so that I can track which gym I worked out at.

**Acceptance Criteria:**
- Location permission request
- Automatically detect current location
- Attach GPS coordinates to workout
- Display gym name/address in workout details
- Map view of workout locations

**Mobile-Specific:**
- Uses Android Location Services
- Handles location permission denial
- Background location tracking during workout
- Reverse geocoding for address display

---

**[US10; P2] Search Exercises**
As a user, I want to search for exercises by name or muscle group so that I can quickly find and add them to my workout.

**Acceptance Criteria:**
- Search bar with real-time filtering
- Filter by muscle group, equipment, difficulty
- Favorite exercises shown first
- Tap to add exercise to current workout

---

### Lower Priority (P3) - Nice-to-Have Features

**[US11; P3] Dark Mode**
As a user, I want a dark mode option so that I can use the app comfortably in low-light gym environments.

**Acceptance Criteria:**
- Dark theme following Material Design guidelines
- Toggle in app settings
- System theme setting (auto dark/light)

---

**[US12; P3] Weekly Volume Charts**
As a user, I want to see visual charts of my weekly training volume so that I can ensure progressive overload.

**Acceptance Criteria:**
- Line chart showing volume by week
- Bar chart showing sets per muscle group
- Filter by date range

---

**[US13; P3] Copy Previous Workout**
As a user, I want to quickly duplicate my last workout so that I can follow the same routine with updated weights.

**Acceptance Criteria:**
- "Copy last workout" button
- Pre-fill exercises and sets from previous session
- Allow editing before saving

---

**[US14; P3] Exercise Video Demos**
As a beginner, I want to watch exercise demonstration videos so that I can learn proper form.

**Acceptance Criteria:**
- Video player embedded in exercise detail
- Videos from YouTube or hosted
- Play/pause controls

---

**[US15; P3] Share Workout**
As a user, I want to share my workout summary on social media so that I can celebrate my progress with friends.

**Acceptance Criteria:**
- "Share" button on workout detail
- Generate shareable image with workout stats
- Share via Android Share Sheet

---

## 3 User Story Estimates

**Estimation Method:** Three-point estimation using PERT formula
**Formula:** Expected = (Best + 4×Most Likely + Worst) / 6
**Units:** Person-hours

| User Story | Best Case | Most Likely | Worst Case | PERT Estimate |
|------------|-----------|-------------|------------|---------------|
| US1 - Authentication | 6 | 10 | 16 | 10.33 |
| US2 - Create Workout | 10 | 16 | 24 | 16.33 |
| US3 - Offline Logging | 12 | 20 | 32 | 20.67 |
| US4 - View History | 6 | 10 | 16 | 10.33 |
| US5 - Body Metrics | 6 | 10 | 14 | 10.00 |
| US6 - Progress Photos | 10 | 16 | 24 | 16.33 |
| US7 - View PRs | 4 | 8 | 12 | 8.00 |
| US8 - Push Notifications | 12 | 18 | 28 | 18.67 |
| US9 - Workout Location | 8 | 14 | 22 | 14.33 |
| US10 - Search Exercises | 6 | 10 | 16 | 10.33 |
| US11 - Dark Mode | 4 | 6 | 10 | 6.33 |
| US12 - Volume Charts | 8 | 14 | 20 | 14.00 |
| US13 - Copy Workout | 4 | 6 | 10 | 6.33 |
| US14 - Exercise Videos | 6 | 10 | 16 | 10.33 |
| US15 - Share Workout | 6 | 10 | 16 | 10.33 |

**Total Estimated Hours:** ~172 hours

---

## 4 Project Schedule

**Project Duration:** 10 weeks (Feb 3 - Apr 13, 2026)
**Sprint Structure:** 4 sprints (aligned with assignments)
- Sprint 1: 2 weeks
- Sprint 2: 3 weeks
- Sprint 3: 3 weeks
- Sprint 4 (Transition): 2 weeks

**Team Size:** 4 members
**Available Hours per Sprint:**
- Sprint 1: ~80 hours (4 people × 20 hours/person)
- Sprint 2 & 3: ~120 hours each (4 people × 30 hours/person)
- Sprint 4: ~80 hours

| Week | Sprint | User Stories | Expected Hours | P.O. (Initials) | Consultation | Milestone |
|------|--------|--------------|----------------|-----------------|--------------|-----------|
| 1 | 1 | None (setup) | 40 | [AB] | 1 | **A1 Presentation** - Project kickoff, architecture setup |
| 2 | 1 | US1, US4 | 40 | [AB] | 1 | Sprint 1 Demo - Authentication & workout viewing |
| 3 | 2 | US2, US5 | 50 | [CD] | 2 | Sprint 2 begins - Core logging features |
| 4 | 2 | US10, US7 | 40 | [CD] | 2 | Mid-sprint checkpoint |
| 5 | 2 | US3 | 30 | [CD] | 2 | **A2 Presentation** - Offline support complete |
| 6 | 3 | US6, US8 | 40 | [EF] | 3 | Sprint 3 begins - Mobile features |
| 7 | 3 | US9, US12 | 40 | [EF] | 3 | Mid-sprint checkpoint |
| 8 | 3 | US11, US13 | 40 | [EF] | 3 | **A3 Presentation** - Feature complete |
| 9 | 4 | Bug fixes, polish | 40 | [GH] | 4 | Sprint 4 - Testing & refinement |
| 10 | 4 | Final testing, docs | 40 | [GH] | 4 | **Final Presentation** - Production ready |

**Product Owner Rotation:**
- **Sprint 1:** [Name] - Responsible for authentication & viewing features
- **Sprint 2:** [Name] - Responsible for core logging & offline
- **Sprint 3:** [Name] - Responsible for mobile-native features
- **Sprint 4:** [Name] - Responsible for polish & deployment

**Key Milestones:**
1. **Week 2:** Basic app with authentication and workout viewing
2. **Week 5:** Core workout logging with offline support
3. **Week 8:** All major features implemented
4. **Week 10:** Production-ready mobile app

**Sprint Goals:**
- **Sprint 1 Goal:** Users can log in and view their workout history from the mobile app
- **Sprint 2 Goal:** Users can create and log workouts with full offline support
- **Sprint 3 Goal:** Mobile-native features (camera, location, notifications) working
- **Sprint 4 Goal:** Polished, tested, production-ready application

---

## 5 Definition of Done

A user story is considered "Done" when:
1. Code implemented and pushed to repository
2. Unit tests written with >80% coverage
3. UI/Espresso tests for critical paths
4. Code reviewed by at least one team member
5. Tested on physical Android device (not just emulator)
6. Integrated with backend API (not mocked)
7. Works offline (if applicable)
8. Handles edge cases (no internet, permissions denied, etc.)
9. UI follows Material Design guidelines
10. Documentation updated (README, API docs)
11. Accepted by Product Owner during sprint review

---

## 6 Risk Assessment

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| Camera API complexity | Medium | High | Start US6 early, use CameraX library |
| Offline sync conflicts | High | High | Implement last-write-wins strategy, test thoroughly |
| Backend auth not ready | Low | High | Implement mock auth initially, coordinate with backend team |
| Android permission denial | Medium | Medium | Graceful degradation, clear user messaging |
| Push notification reliability | Medium | Medium | Use FCM, implement retry logic |
| Team member availability | Medium | Medium | Cross-training, pair programming |

---

## 7 Success Criteria

The project will be considered successful if:
1. All P1 user stories (US1-US5) are fully implemented and tested
2. At least 3 P2 user stories (US6-US10) are completed
3. App works reliably offline with sync
4. Camera integration functional for progress photos
5. Authentication secure (JWT tokens, encrypted storage)
6. App runs smoothly on Android 10+ devices
7. Unit test coverage >70%
8. No critical bugs in production
9. Positive feedback from user testing sessions
10. Code quality maintained (clean architecture, linting passes)

---

**Document Version:** 1.0
**Last Updated:** January 20, 2026
**Status:** Draft - Pending team review
